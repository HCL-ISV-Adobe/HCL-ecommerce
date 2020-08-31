package com.hcl.ecomm.core.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.hcl.ecomm.core.services.CustomEmailService;
import com.hcl.ecomm.core.services.MySqlService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.SQLException;
import javax.sql.DataSource;

@Component
public class MySqlServiceImpl implements MySqlService {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Reference
	private DataSourcePool source;
	
	@Reference
    CustomEmailService customEmailService;

	@Override
	public String userComplaintSubmission(String first_Name, String last_Name, String email, String subject,
			String complaint) {

		LOG.debug("userComplaintSubmission method start. email="+email);
		String complaintId = StringUtils.EMPTY;
		Connection connection = null;

		try {
			connection = getConnection();
			PreparedStatement ps = null;
			String insert = "INSERT INTO complaints(first_name, last_name, email, subject, complaint, status) VALUES(?,?,?,?,?,?);";
			ps = connection.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, first_Name);
			ps.setString(2, last_Name);
			ps.setString(3, email);
			ps.setString(4, subject);
			ps.setString(5, complaint);
			ps.setString(6, "Open");
			int responseCode=ps.executeUpdate();
			if (responseCode>0) {
				ResultSet res = ps.getGeneratedKeys();
				if (!res.next()) {
					LOG.error("Compalint id is generated in db");
				}
				else {
					res.first();
					complaintId =res.getString(1);
					}
				triggerNotifyMail(first_Name, email,StringUtils.EMPTY,StringUtils.EMPTY);
			} else {
				LOG.error("Something went wrong while inserting in mysql db.");
			}

		} catch (Exception e) {
			LOG.error("Error while executing th sql query. Error = {}", e);
		} finally {
			try {
				if(null != connection && !connection.isClosed())
				connection.close();
			}
			catch (SQLException e) {
				LOG.error("Error while trying to close connection. SQLException = {}", e);
			}
		}
		LOG.debug("userComplaintSubmission method end.");
		return complaintId;
	}

	private void triggerNotifyMail(String first_Name, String email, String template, String complaintId) {
		Map emailParams = new HashMap<>();
		LOG.debug("triggerNotifyMail() methid start: Email="+email);
		String templatePath = StringUtils.EMPTY;
		if(template.equals("closingComplaint")) {
			 templatePath = "/etc/notification/email/hclecomm/user-complaint-resolved-email-template.html";	
			 emailParams.put("positveFeedbackUrl", "http://localhost:4502/content/hclecomm/us/en/complaint-feedback.html?wcmmode=disabled&satisfied=yes&complaintId="+complaintId);
			 emailParams.put("negativeFeedbackUrl", "http://localhost:4502/content/hclecomm/us/en/complaint-feedback.html?wcmmode=disabled&satisfied=no&complaintId="+complaintId);
			 emailParams.put("subject", "HCLecomm Complaint closed");
		} else {
			 templatePath = "/etc/notification/email/hclecomm/user-complaint-email-template.html";
		}
		emailParams.put("receiveremail", email);
		emailParams.put("firstname", first_Name);
		customEmailService.sendEmail(templatePath, emailParams, email);
		LOG.debug("triggerNotifyMail() method end:=");
	}
	
	private Connection getConnection() {
		DataSource dataSource = null;
		Connection con = null;
		try {
			dataSource = (DataSource) source.getDataSource("hclecomm");
			con = dataSource.getConnection();
			return con;

		} catch (Exception e) {
			LOG.error("Error while getConnection. Full error = {}", e);
		}
		return null;
	}
	
	@Override
	public String userComplaintUpdate(String first_Name, String last_Name, String email, String subject,
			String complaint,String closingComment, String status, String complaintId) {

		LOG.debug("userComplaintUpdate method start. email="+email);
		String responseCode = StringUtils.EMPTY;
		Connection connection = null;

		try {
			connection = getConnection();
			PreparedStatement ps = null;
			String update = "UPDATE complaints SET closing_comment = ? , status = ? WHERE id = ?;";
			ps = connection.prepareStatement(update);
			ps.setString(1, closingComment);
			ps.setString(2, status);

			ps.setString(3, complaintId);
			int code=ps.executeUpdate();
			if (code>0) {
				responseCode = String.valueOf(code);
				triggerNotifyMail(first_Name, email, "closingComplaint", complaintId);
			} else {
				LOG.error("Something went wrong while updating complaint form in mysql db.");
			}

		} catch (Exception e) {
			LOG.error("Error while executing th sql query. Error = {}", e);
		} finally {
			try {
				if(null != connection && !connection.isClosed())
				connection.close();
			}
			catch (SQLException e) {
				LOG.error("Error while trying to close connection. SQLException = {}", e);
			}
		}
		
		LOG.debug("userComplaintUpdate method end.");
		return responseCode;
	}

	@Override
	public String userComplaintFeedback(String Status, String complaintId) {
		LOG.debug("userComplaintFeedback method start. complaintId={} and Status={}",complaintId,Status);
		String responseCode = StringUtils.EMPTY;
		Connection connection = null;

		try {
			connection = getConnection();
			PreparedStatement ps = null;
			String update = "UPDATE complaints SET status = ? WHERE id = ?;";
			ps = connection.prepareStatement(update);
			ps.setString(1, Status);

			ps.setString(2, complaintId);
			int code=ps.executeUpdate();
			if (code>0) {
				responseCode = String.valueOf(code);
			} else {
				LOG.error("Something went wrong while userComplaintFeedback complaint form in mysql db.");
			}

		} catch (Exception e) {
			LOG.error("Error while executing the sql query in userComplaintFeedback. Error = {}", e);
		} finally {
			try {
				if(null != connection && !connection.isClosed())
				connection.close();
			}
			catch (SQLException e) {
				LOG.error("Error while trying to close connection in userComplaintFeedback. SQLException = {}", e);
			}
		}
		
		LOG.debug("userComplaintFeedback method end.");
		return responseCode;
	}

}