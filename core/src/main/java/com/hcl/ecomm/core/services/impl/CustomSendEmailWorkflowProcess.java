package com.hcl.ecomm.core.services.impl;

import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.hcl.ecomm.core.services.CustomEmailService;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.*;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.activation.DataSource;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Session;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component(service = WorkflowProcess.class, property = {"process.label = Hclecomm Custom EMail Workflow"})

public class CustomSendEmailWorkflowProcess implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Reference
    private MessageGatewayService messageGatewayService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private CustomEmailService emailService;

    private Session session;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOG.info("Starting Custom Email Workflow");

        WorkflowData workflowData = workItem.getWorkflowData(); //gain access to the payload data
        String path = workflowData.getPayload().toString();

        try {
            Map<String, Object> authInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, (Object) "userName");
            ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(authInfo);
            session = resourceResolver.adaptTo(Session.class);
            Node item = session.getNode(path);
            String recipients = item.getProperty("receipent").getString();

            Node parentNode = item.getParent();
            String formPath = parentNode.getProperty("formPath").getString();
            Node formPropertyNode = session.getNode(formPath);
            String attachmentPath = formPropertyNode.getProperty("attachment").getString();
            String templatePath = formPropertyNode.getProperty("templatePath").getString();


            Resource resource = resourceResolver.getResource(attachmentPath);
            Asset asset = resource.adaptTo(Asset.class);
            Rendition rendition = asset.getOriginal();
            InputStream inputStream = rendition.adaptTo(InputStream.class);
            String inputString = inputStream.toString();
            ByteArrayDataSource assetDataDource = new ByteArrayDataSource(inputStream, "application/pdf");
            Map<String, DataSource> attachments = new HashMap<>();
            attachments.put("Newsletter.pdf", assetDataDource );

            List<String> failureList = new ArrayList<String>();

            if (recipients == null || recipients.length() <= 0) {
                throw new IllegalArgumentException("Invalid Recepient");
            }

            List<InternetAddress> addresses = new ArrayList<InternetAddress>(recipients.length());
            try {
                addresses.add(new InternetAddress(recipients));
            } catch (AddressException e) {
                LOG.error("Invalid email address {} passed to sendEmail(). Skipping.", recipients);
            }

            Map<String, String> params = new HashMap<>();
            InternetAddress[] iAddressRecipients = addresses.toArray(new InternetAddress[addresses.size()]);
            List<InternetAddress> failureInternetAddresses = emailService.sendEmail(templatePath, params, attachments, iAddressRecipients);


        } catch (Exception e) {
            LOG.error("Exception while sending email : ", e.getMessage());
        }
    }


    public List<InternetAddress> sendEmail(final String templatePath, Map<String, String> params,
                                           final InternetAddress... recipients) {

        List<InternetAddress> failureList = new ArrayList<InternetAddress>();

        if (recipients == null || recipients.length <= 0) {
            throw new IllegalArgumentException("Invalid Receipents");
        }
        LOG.info("Inside Send Email Method : ");
        final MailTemplate mailTemplate = this.getMailTemplate(templatePath);
        final Class<? extends Email> mailType = this.getMailType(templatePath);
        final MessageGateway<Email> messageGateway = messageGatewayService.getGateway(mailType);

        for (final InternetAddress address : recipients) {
            try {
                LOG.info("Sending Email to :" ,address);
                final Email email = getEmail(mailTemplate, mailType, params);
                email.setTo(Collections.singleton(address));

                messageGateway.send(email);

            } catch (Exception e) {
                failureList.add(address);
                LOG.error("Error sending email to [ " + address + " ]", e);
            }
        }
        return failureList;
    }



    private Email getEmail(final MailTemplate mailTemplate,
                           final Class<? extends Email> mailType,
                           final Map<String, String> params) throws EmailException, MessagingException, IOException {

        final Email email = mailTemplate.getEmail(StrLookup.mapLookup(params), mailType);

        if (params.containsKey("senderName")) {
            email.setFrom(params.get("senderName"));
        }

        if (params.containsKey("subject")) {
            email.setSubject(params.get("subject"));
        }
        return email;
    }

    private Class<? extends Email> getMailType(String templatePath) {
        return templatePath.endsWith(".html") ? HtmlEmail.class : SimpleEmail.class;
    }

    private MailTemplate getMailTemplate(String templatePath) throws IllegalArgumentException {
        MailTemplate mailTemplate = null;
        Map<String, Object> authInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, (Object) "userName");
        try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(authInfo)) {
            mailTemplate = MailTemplate.create(templatePath, resourceResolver.adaptTo(Session.class));

            if (mailTemplate == null) {
                throw new IllegalArgumentException("Mail template path [ "
                        + templatePath + " ] could not resolve to a valid template");
            }
        } catch (LoginException e) {
            LOG.error("Unable to obtain an administrative resource resolver to get the Mail Template at [ "
                    + templatePath + " ]", e);
        }

        return mailTemplate;
    }
}
