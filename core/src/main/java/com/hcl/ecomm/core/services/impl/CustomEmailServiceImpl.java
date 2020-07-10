package com.hcl.ecomm.core.services.impl;

import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.hcl.ecomm.core.services.CustomEmailService;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataSource;
import javax.jcr.Session;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component(
        immediate = true,
        enabled = true,
        service = CustomEmailService.class)
public class CustomEmailServiceImpl implements CustomEmailService {
    private static final Logger LOG = LoggerFactory.getLogger(CustomEmailServiceImpl.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private Session session;

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
                LOG.debug("Sending Email to :" ,address);
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

    /* Sends Email with Attachment*/
    public List<InternetAddress> sendEmail(String templatePath, Map<String, String> emailParams, Map<String, DataSource> attachments, InternetAddress... recipients) {

        List<InternetAddress> failureList = new ArrayList<InternetAddress>();

        if (recipients == null || recipients.length <= 0) {
            throw new IllegalArgumentException("Invalid Recepient");
        }

        final MailTemplate mailTemplate = this.getMailTemplate(templatePath);
        final Class<? extends Email> mailType;
        if (attachments != null && attachments.size() > 0) {
            mailType = HtmlEmail.class;
        } else {
            mailType = this.getMailType(templatePath);
        }
        final MessageGateway<Email> messageGateway = messageGatewayService.getGateway(mailType);

        for (final InternetAddress address : recipients) {
            try {
                // Get a new email per recipient to avoid duplicate attachments
                Email email = getEmail(mailTemplate, mailType, emailParams);
                email.setTo(Collections.singleton(address));

                if (attachments != null && attachments.size() > 0) {
                    for (Map.Entry<String, DataSource> entry : attachments.entrySet()) {
                        ((HtmlEmail) email).attach(entry.getValue(), entry.getKey(), null);
                    }
                }

                messageGateway.send(email);
            } catch (Exception e) {
                failureList.add(address);
                LOG.error("Error sending email to [ " + address + " ]", e);
            }
        }

        return failureList;
    }

    /* Send Email with attachment method */
    @Override
    public List<String> sendEmail(String templatePath, Map<String, String> emailParams, Map<String, DataSource> attachments, String... recipients) {
        List<String> failureList = new ArrayList<String>();

        if (recipients == null || recipients.length <= 0) {
            throw new IllegalArgumentException("Invalid recepients");
        }

        List<InternetAddress> addresses = new ArrayList<InternetAddress>(recipients.length);
        for (String recipient : recipients) {
            try {
                addresses.add(new InternetAddress(recipient));
            } catch (AddressException e) {
                LOG.warn("Invalid email address {} passed to sendEmail(). Skipping.", recipient);
            }
        }
        InternetAddress[] iAddressRecipients = addresses.toArray(new InternetAddress[addresses.size()]);
        List<InternetAddress> failureInternetAddresses = sendEmail(templatePath, emailParams, attachments, iAddressRecipients);

        for (InternetAddress address : failureInternetAddresses) {
            failureList.add(address.toString());
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

