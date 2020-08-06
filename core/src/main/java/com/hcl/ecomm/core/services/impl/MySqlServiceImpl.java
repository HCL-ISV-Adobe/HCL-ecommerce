package com.hcl.ecomm.core.services.impl;

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
	public void userComplaintSubmission(String first_Name, String last_Name, String email, String subject,
			String complaint) {

		LOG.info("userComplaintSubmission method start. email="+email);

		Connection connection = null;

		try {
			connection = getConnection();
			PreparedStatement ps = null;
			String insert = "INSERT INTO complaints(first_name, last_name, email, subject, complaint, status) VALUES(?,?,?,?,?,?);";
			ps = connection.prepareStatement(insert);
			ps.setString(1, first_Name);
			ps.setString(2, last_Name);
			ps.setString(3, email);
			ps.setString(4, subject);
			ps.setString(5, complaint);
			ps.setString(6, "Open");
			int responseCode=ps.executeUpdate();
			if (responseCode>0) {
				triggerNotifyMail(first_Name, email);
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

	}

	private void triggerNotifyMail(String first_Name, String email) {
		Map emailParams = new HashMap<>();
		LOG.info("Email =="+email);
		String templatePath = "/etc/notification/email/hclecomm/user-complaint-email-template.html";
		emailParams.put("receiveremail", email);
		emailParams.put("firstname", first_Name);
		customEmailService.sendEmail(templatePath, emailParams, email);
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

}