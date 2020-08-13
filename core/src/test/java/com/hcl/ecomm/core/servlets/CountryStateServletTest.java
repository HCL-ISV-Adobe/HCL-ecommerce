package com.hcl.ecomm.core.servlets;


import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.servlet.ServletException;

import java.io.IOException;


import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CountryStateServletTest {
    private CountryStateServlet countryStateServlet;


    @BeforeEach
    protected void setUp() {
        countryStateServlet = spy(new CountryStateServlet());
    }

    @Test
    void doGet() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getHeader("CustomerToken")).thenReturn("ae5koz3io5tvhbgy7p2gsxla2fqxiu98");

        JSONArray jsonArray = new JSONArray(getSubmitResponse());
        doReturn(jsonArray).when(countryStateServlet).getStateCountryList();
        countryStateServlet.doGet(request, response);
        verify(countryStateServlet, times(1)).doGet(request, response);

    }


    @Test
    void doGet_else() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getHeader("CustomerToken")).thenReturn("ae5koz3io5tvhbgy7p2gsxla2fqxiu98");

        JSONArray jsonArray = new JSONArray(getSubmitResponse_else());
        doReturn(jsonArray).when(countryStateServlet).getStateCountryList();
        countryStateServlet.doGet(request, response);
        verify(countryStateServlet, times(1)).doGet(request, response);

    }

    private String getSubmitResponse() {
        String submitResponse = "[{\"capital\":\"Kabul\",\"name\":\"Afghanistan\"," +
                "\"currency\":\"AFN\",\"iso2\":\"AF\",\"iso3\":\"AFG\",\"phone_code\":" +
                "\"93\",\"states\":[{\"name\":\"Badakhshan\",\"id\":3901,\"state_code\":" +
                "\"BDS\"},{\"name\":\"Badghis\",\"id\":3871,\"state_code\":\"BDG\"}," +
                "{\"name\":\"Baghlan\",\"id\":3875,\"state_code\":\"BGL\"},{" +
                "\"name\":\"Zabul\",\"id\":3874,\"state_code\":\"ZAB\"}]}]";
        return submitResponse;
    }

    private String getSubmitResponse_else() {
        String submitResponse_else =
                "[{\"capital\":\"Kabul\",\"name\":\"Afghanistan\"," +
                        "\"currency\":\"AFN\",\"iso2\":\"AF\",\"iso3\":\"AFG\",\"phone_code\":" +
                        "\"93\",\"states\":[]}]";

        return submitResponse_else;

    }

}
