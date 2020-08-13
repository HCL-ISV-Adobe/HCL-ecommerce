<%@include file="/libs/fd/af/components/guidesglobal.jsp" %>
<%@include file="/libs/foundation/global.jsp"%>
<%@page import="com.day.cq.wcm.foundation.forms.FormsHelper,
             org.apache.sling.api.resource.ResourceUtil,
             org.apache.sling.api.resource.ValueMap" %>
<%@taglib prefix="sling"
                uri="http://sling.apache.org/taglibs/sling/1.0" %>
<%@taglib prefix="cq"
                uri="http://www.day.com/taglibs/cq/1.0"
%>
<cq:defineObjects/>
<sling:defineObjects/>
<%
  
    String first_Name = request.getParameter("first_name");
    String last_Name = request.getParameter("last_name");
    String email = request.getParameter("email");
    String subject = request.getParameter("subject");
	String complaint = request.getParameter("complaint");

 

    com.hcl.ecomm.core.services.MySqlService mysqlService = sling.getService(com.hcl.ecomm.core.services.MySqlService.class);
    mysqlService.userComplaintSubmission(first_Name,last_Name,email,subject,complaint);

  
%>