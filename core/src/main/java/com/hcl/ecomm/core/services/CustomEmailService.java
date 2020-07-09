package com.hcl.ecomm.core.services;

import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;
import java.util.List;
import java.util.Map;

public interface CustomEmailService {


    public List<InternetAddress> sendEmail(final String templatePath, Map<String, String> params,final InternetAddress... recipients);

    public List<InternetAddress> sendEmail(String templatePath, Map<String, String> emailParams, Map<String, DataSource> attachments, InternetAddress... recipients);

    public List<String> sendEmail(String templatePath, Map<String, String> emailParams, Map<String, DataSource> attachments, String... recipients);

}
