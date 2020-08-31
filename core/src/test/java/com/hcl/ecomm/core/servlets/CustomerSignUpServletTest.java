package com.hcl.ecomm.core.servlets;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerSignUpServletTest {
    private CustomerSignUpServlet customerSignUpServlet;

    @BeforeEach
    void setUp() {
        customerSignUpServlet = spy(new CustomerSignUpServlet());
    }

    @Test
    void doPost() throws JSONException, UnsupportedEncodingException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
        doReturn(getCustomerSigUpResponse()).when(customerSignUpServlet).getCustomerSignUp(any());
        doReturn(getMailChimpCustomerSigUpResponse()).when(customerSignUpServlet).getMailChimpSignup(any());

        customerSignUpServlet.doPost(request, response);
        verify(customerSignUpServlet, times(1)).doPost(request, response);
    }

    private String getPayload() {
        return "{\n" +
                "\t\"firstname\":\"Jane\",\n" +
                "\t\"email\":\"jdoe@example.com\",\n" +
                "\t\"phone\":\"1234567890\",\n" +
                "\t\"password\":\"Password123\",\n" +
                "\t\"lastname\":\"Doe\"\n" +
                "}";
    }

    private JSONObject getCustomerSigUpResponse() throws JSONException {
        String response = "{\n" +
                "  \"message\": {\n" +
                "    \"customerToken\": \"oq191aqcp7wb2ues48cuuukga1ig37g1\"\n" +
                "  },\n" +
                "  \"statusCode\": 200\n" +
                "}";
        return new JSONObject(response);
    }

    private JSONObject getMailChimpCustomerSigUpResponse() throws JSONException {
        String response = "{\n" +
                "\t\"message\":{\n" +
                "\t\t\"id\": \"2f07f7da993e8f37b71d530afa053914\",\n" +
                "\t\t\"email_address\": \"rishabh1.b@hcl.com\",\n" +
                "\t\t\"unique_email_id\": \"48c9488d6b\",\n" +
                "\t\t\"web_id\": 348800093,\n" +
                "\t\t\"email_type\": \"html\",\n" +
                "\t\t\"status\": \"subscribed\",\n" +
                "\t\t\"merge_fields\": {\n" +
                "\t\t\t\"FNAME\": \"adfsdf\",\n" +
                "\t\t\t\"LNAME\": \"dsfsdf\",\n" +
                "\t\t\t\"ADDRESS\": \"\",\n" +
                "\t\t\t\"PHONE\": \"1234567890\"\n" +
                "\t\t},\n" +
                "\t\t\"stats\": {\n" +
                "\t\t\t\"avg_open_rate\": 0,\n" +
                "\t\t\t\"avg_click_rate\": 0\n" +
                "\t\t},\n" +
                "\t\t\"ip_signup\": \"\",\n" +
                "\t\t\"timestamp_signup\": \"\",\n" +
                "\t\t\"ip_opt\": \"103.211.17.156\",\n" +
                "\t\t\"timestamp_opt\": \"2020-08-27T13:25:12+00:00\",\n" +
                "\t\t\"member_rating\": 2,\n" +
                "\t\t\"last_changed\": \"2020-08-27T13:25:12+00:00\",\n" +
                "\t\t\"language\": \"\",\n" +
                "\t\t\"vip\": false,\n" +
                "\t\t\"email_client\": \"\",\n" +
                "\t\t\"location\": {\n" +
                "\t\t\t\"latitude\": 0,\n" +
                "\t\t\t\"longitude\": 0,\n" +
                "\t\t\t\"gmtoff\": 0,\n" +
                "\t\t\t\"dstoff\": 0,\n" +
                "\t\t\t\"country_code\": \"\",\n" +
                "\t\t\t\"timezone\": \"\"\n" +
                "\t\t},\n" +
                "\t\t\"source\": \"API - Generic\",\n" +
                "\t\t\"tags_count\": 0,\n" +
                "\t\t\"tags\": [],\n" +
                "\t\t\"list_id\": \"6bcf7b7a39\",\n" +
                "\t\t\"_links\": [{\n" +
                "\t\t\t\"rel\": \"self\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members/2f07f7da993e8f37b71d530afa053914\",\n" +
                "\t\t\t\"method\": \"GET\",\n" +
                "\t\t\t\"targetSchema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Response.json\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"rel\": \"parent\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members\",\n" +
                "\t\t\t\"method\": \"GET\",\n" +
                "\t\t\t\"targetSchema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/CollectionResponse.json\",\n" +
                "\t\t\t\"schema\": \"https://us17.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Members.json\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"rel\": \"update\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members/2f07f7da993e8f37b71d530afa053914\",\n" +
                "\t\t\t\"method\": \"PATCH\",\n" +
                "\t\t\t\"targetSchema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Response.json\",\n" +
                "\t\t\t\"schema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/PATCH.json\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"rel\": \"upsert\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members/2f07f7da993e8f37b71d530afa053914\",\n" +
                "\t\t\t\"method\": \"PUT\",\n" +
                "\t\t\t\"targetSchema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Response.json\",\n" +
                "\t\t\t\"schema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/PUT.json\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"rel\": \"delete\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members/2f07f7da993e8f37b71d530afa053914\",\n" +
                "\t\t\t\"method\": \"DELETE\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"rel\": \"activity\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members/2f07f7da993e8f37b71d530afa053914/activity\",\n" +
                "\t\t\t\"method\": \"GET\",\n" +
                "\t\t\t\"targetSchema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Activity/Response.json\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"rel\": \"goals\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members/2f07f7da993e8f37b71d530afa053914/goals\",\n" +
                "\t\t\t\"method\": \"GET\",\n" +
                "\t\t\t\"targetSchema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Goals/Response.json\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"rel\": \"notes\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members/2f07f7da993e8f37b71d530afa053914/notes\",\n" +
                "\t\t\t\"method\": \"GET\",\n" +
                "\t\t\t\"targetSchema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Notes/CollectionResponse.json\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"rel\": \"events\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members/2f07f7da993e8f37b71d530afa053914/events\",\n" +
                "\t\t\t\"method\": \"POST\",\n" +
                "\t\t\t\"targetSchema\": \"https://us17.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Events/POST.json\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"rel\": \"delete_permanent\",\n" +
                "\t\t\t\"href\": \"https://us17.api.mailchimp.com/3.0/lists/6bcf7b7a39/members/2f07f7da993e8f37b71d530afa053914/actions/delete-permanent\",\n" +
                "\t\t\t\"method\": \"POST\"\n" +
                "\t\t}]\n" +
                "\t},\n" +
                "\t\"statusCode\":200\n" +
                "}";
        return new JSONObject(response);
    }
}