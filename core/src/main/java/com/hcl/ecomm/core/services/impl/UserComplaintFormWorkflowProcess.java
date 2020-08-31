package com.hcl.ecomm.core.services.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.hcl.ecomm.core.services.CustomEmailService;
import com.hcl.ecomm.core.services.MySqlService;



@Component(property = {"service.description=HCLecomm User Complaint form workflow", "service.vendor=HCLecomm", "process.label=HCLecomm User Complaint"})
public class UserComplaintFormWorkflowProcess implements WorkflowProcess {
	  private static final Logger LOG = LoggerFactory.getLogger(UserComplaintFormWorkflowProcess.class);
	  
		@Reference
	    CustomEmailService customEmailService;
		
		@Reference
		MySqlService mySqlService;
	  
	  public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
	    LOG.debug("UserComplaintFormWorkflowProcess execute start" + ((String)arg2.get("PROCESS_ARGS", "string")).toString());
	    String params = ((String)arg2.get("PROCESS_ARGS", "string")).toString();
	    String[] parameters = params.split(",");
	    	String nodeName = parameters[0];
	 	  
	 	    String payloadPath = workItem.getWorkflowData().getPayload().toString(); 
	 	    String dataFilePath = payloadPath + "/Data.xml/jcr:content";
	 	    Session session = (Session)workflowSession.adaptTo(Session.class);
	 	    DocumentBuilderFactory factory = null;
	 	    DocumentBuilder builder = null;
	 	    Document xmlDocument = null;
	 	    Node xmlDataNode = null;
	 	    XPath xPath= null;
	 	    try {
	 	      xmlDataNode = session.getNode(dataFilePath);
	 	      InputStream xmlDataStream = xmlDataNode.getProperty("jcr:data").getBinary().getStream();
	 	      xPath = XPathFactory.newInstance().newXPath();
	 	      factory = DocumentBuilderFactory.newInstance();
	 	      builder = factory.newDocumentBuilder();
	 	      xmlDocument = builder.parse(xmlDataStream);
	 	      if (parameters.length==3) {
	 	    	 String compIdNode = parameters[2];
	 	    	  String value = parameters[1];
		 	      org.w3c.dom.Node node = (org.w3c.dom.Node)xPath.compile(nodeName).evaluate(xmlDocument, XPathConstants.NODE);
		 	      org.w3c.dom.Node complaintIdNode = (org.w3c.dom.Node)xPath.compile(compIdNode).evaluate(xmlDocument, XPathConstants.NODE);
		 	      
		 	      String complaintId=persistComplaintIndb(xmlDocument, StringUtils.EMPTY);
		 	      node.setTextContent(value);
		 	      complaintIdNode.setTextContent(complaintId);
		 	      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		 	      DOMSource source = new DOMSource(xmlDocument);
		 	      StreamResult outputTarget = new StreamResult(outputStream);
		 	      TransformerFactory.newInstance().newTransformer().transform(source, outputTarget);
		 	      InputStream is1 = new ByteArrayInputStream(outputStream.toByteArray());
		 	      Binary binary = session.getValueFactory().createBinary(is1);
		 	      xmlDataNode.setProperty("jcr:data", binary);
		 	      session.save();
			} else {
				persistComplaintIndb(xmlDocument, "Closed");
			}

	 	    } catch (PathNotFoundException e) {
		 	     LOG.error("PathNotFoundException while getting the Data.xml node. Error={}",e);
	 	    } catch (RepositoryException e) {
	 	    	 LOG.error("RepositoryException while getting the Data.xml node. Error={}",e);
	 	    } catch (ParserConfigurationException e) {
	 	    	LOG.error("ParserConfigurationException while getting the Data.xml node. Error={}",e);			
	 	    } catch (SAXException e) {
				LOG.error("SAXException while getting the Data.xml node. Error={}",e);
			} catch (IOException e) {
				LOG.error("IOException while getting the Data.xml node. Error={}",e);
			} catch (XPathExpressionException e) {
				LOG.error("XPathExpressionException while getting the Data.xml node. Error={}",e);
			} catch (TransformerConfigurationException e) {
				LOG.error("TransformerConfigurationException while getting the Data.xml node. Error={}",e);
			} catch (TransformerException e) {
				LOG.error("TransformerException while getting the Data.xml node. Error={}",e);
			} catch (TransformerFactoryConfigurationError e) {
				LOG.error("TransformerFactoryConfigurationError while getting the Data.xml node. Error={}",e);
			} 
	 	    	LOG.debug("UserComplaintFormWorkflowProcess execute end");
	  }
	  
	  private String persistComplaintIndb(Document xmlDocument, String status) {
		  LOG.debug("UserComplaintFormWorkflowProcess persistComplaintIndb start");
		  String dbcode = StringUtils.EMPTY;
		  NodeList nodeList = xmlDocument.getElementsByTagName("afUnboundData");
		  for (int itr = 0; itr < nodeList.getLength(); itr++){  
			  org.w3c.dom.Node node = nodeList.item(itr);  
			  if(node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE){  
				  org.w3c.dom.Element eElement = (org.w3c.dom.Element) node;
				  String complaintId = eElement.getElementsByTagName("complaint_id").item(0).getTextContent();
				  LOG.info("complaintId={}",complaintId);
				  String first_Name = eElement.getElementsByTagName("first_name").item(0).getTextContent();
				  String last_name = eElement.getElementsByTagName("last_name").item(0).getTextContent();
				  String email = eElement.getElementsByTagName("email").item(0).getTextContent();
				  String subject = eElement.getElementsByTagName("subject").item(0).getTextContent();
				  String complaint = eElement.getElementsByTagName("complaint").item(0).getTextContent();
				  if (StringUtils.isEmpty(status)) {
					  dbcode = mySqlService.userComplaintSubmission(first_Name, last_name, email, subject, complaint);
				} else {
					String closingComment=  eElement.getElementsByTagName("complaint_closing_comment").item(0).getTextContent();
					dbcode = mySqlService.userComplaintUpdate( first_Name,  last_name,  email,  subject,  complaint,  closingComment,  status,complaintId);
				}
			  }  
		  }
		  LOG.debug("UserComplaintFormWorkflowProcess persistComplaintIndb end");
		  return dbcode;
	  }
	  
	}
