package com.hcl.ecomm.core.servlets;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PickupStoreServletTest {

    private PickupStoreServlet pickupStoreServlet;

    @BeforeEach
    void setUp(){
        pickupStoreServlet = spy(new PickupStoreServlet());
    }

    @Test
    void doGet() throws ServletException, IOException, JSONException {

        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);

        JSONObject jsonObject = new JSONObject(getPickupResponse());

        doReturn(jsonObject).when(pickupStoreServlet).getPickupStoreList();
        pickupStoreServlet.doGet(request,response);
        verify(pickupStoreServlet, times(1)).doGet(request, response);
    }

    private String getPickupResponse(){
        String pickupResponse = "{\n" +
                "\t\"items\": [{\n" +
                "\t\t\"pickup_location_code\": \"txspeqa\",\n" +
                "\t\t\"name\": \"Society delivery Pt. (Alpine eco Residents only)\",\n" +
                "\t\t\"email\": \"sales@company.com\",\n" +
                "\t\t\"contact_name\": \"Ethan Carter\",\n" +
                "\t\t\"description\": \"Sport Equipment Store description\",\n" +
                "\t\t\"latitude\": 29.7543,\n" +
                "\t\t\"longitude\": -95.3609,\n" +
                "\t\t\"country_id\": \"IN\",\n" +
                "\t\t\"region_id\": 57,\n" +
                "\t\t\"region\": \"Karnataka\",\n" +
                "\t\t\"city\": \"Bengaluru\",\n" +
                "\t\t\"street\": \"Chinnapanahalli Main Rd, Dodda Nekkundi Extension,Marathahalli\",\n" +
                "\t\t\"postcode\": \"560037\",\n" +
                "\t\t\"phone\": \"(91)9876543214\"\n" +
                "\t}, {\n" +
                "\t\t\"pickup_location_code\": \"txspeqs\",\n" +
                "\t\t\"name\": \"Sanjivi Store\",\n" +
                "\t\t\"email\": \"sales@company.com\",\n" +
                "\t\t\"contact_name\": \"Ethan Carter\",\n" +
                "\t\t\"description\": \"Sport Equipment Store description\",\n" +
                "\t\t\"latitude\": 29.7543,\n" +
                "\t\t\"longitude\": -95.3609,\n" +
                "\t\t\"country_id\": \"IN\",\n" +
                "\t\t\"region_id\": 57,\n" +
                "\t\t\"region\": \"Maharashtra\",\n" +
                "\t\t\"city\": \"Pune\",\n" +
                "\t\t\"street\": \"No. 13 Hobli Village, Doddanekundi, Krishnarajapura\",\n" +
                "\t\t\"postcode\": \"560103\",\n" +
                "\t\t\"phone\": \"(91)9876543214\"\n" +
                "\t}],\n" +
                "\t\"search_request\": {\n" +
                "\t\t\"area\": {\n" +
                "\t\t\t\"radius\": 1500,\n" +
                "\t\t\t\"search_term\": \"Austin\"\n" +
                "\t\t},\n" +
                "\t\t\"current_page\": 1,\n" +
                "\t\t\"scope_type\": \"website\",\n" +
                "\t\t\"scope_code\": \"base\",\n" +
                "\t\t\"extension_attributes\": {\n" +
                "\t\t\t\"products_info\": [{\n" +
                "\t\t\t\t\"sku\": \"SKU1\"\n" +
                "\t\t\t}]\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"total_count\": 2\n" +
                "}";
        return pickupResponse;
    }
}
