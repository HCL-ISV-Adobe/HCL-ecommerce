package com.hcl.ecomm.core.services;

public interface MySqlService {
    
    
    public String userComplaintSubmission(String first_Name, String last_Name, String email, String subject, String complaint); 
    
    public String userComplaintUpdate(String first_Name, String last_name, String email, String subject, String complaint,String  closingComment,String  status, String complaintId); 
 
    public String userComplaintFeedback( String Status, String complaintId); 
    
}



