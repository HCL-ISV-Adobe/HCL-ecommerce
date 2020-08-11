package com.hcl.ecomm.core.servlets;


import com.hcl.ecomm.core.services.impl.ShippingInfoServiceImpl;
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
    private ShippingInfoServiceImpl shippingInfoService;

    @BeforeEach
    protected  void setUp()  {
        countryStateServlet = spy (new CountryStateServlet());
    }

    @Test
    void doGet() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        JSONArray jsonArray = new JSONArray(getSubmitResponse());
        doReturn(jsonArray).when(countryStateServlet).getStateCountryList();
        countryStateServlet.doGet(request,response);
        verify(countryStateServlet, times(1)).doGet(request, response);

    }
    private String getSubmitResponse() {
        String submitResponse =  "[{\"capital\":\"Kabul\",\"name\":\"Afghanistan\",\"currency\":\"AFN\",\"iso2\":\"AF\",\"iso3\":\"AFG\",\"phone_code\":\"93\",\"states\":[{\"name\":\"Badakhshan\",\"id\":3901,\"state_code\":\"BDS\"},{\"name\":\"Badghis\",\"id\":3871,\"state_code\":\"BDG\"},{\"name\":\"Baghlan\",\"id\":3875,\"state_code\":\"BGL\"},{\"name\":\"Balkh\",\"id\":3884,\"state_code\":\"BAL\"},{\"name\":\"Bamyan\",\"id\":3872,\"state_code\":\"BAM\"},{\"name\":\"Daykundi\",\"id\":3892,\"state_code\":\"DAY\"},{\"name\":\"Farah\",\"id\":3899,\"state_code\":\"FRA\"},{\"name\":\"Faryab\",\"id\":3889,\"state_code\":\"FYB\"},{\"name\":\"Ghazni\",\"id\":3870,\"state_code\":\"GHA\"},{\"name\":\"Gh?r\",\"id\":3888,\"state_code\":\"GHO\"},{\"name\":\"Helmand\",\"id\":3873,\"state_code\":\"HEL\"},{\"name\":\"Herat\",\"id\":3887,\"state_code\":\"HER\"},{\"name\":\"Jowzjan\",\"id\":3886,\"state_code\":\"JOW\"},{\"name\":\"Kabul\",\"id\":3902,\"state_code\":\"KAB\"},{\"name\":\"Kandahar\",\"id\":3890,\"state_code\":\"KAN\"},{\"name\":\"Kapisa\",\"id\":3879,\"state_code\":\"KAP\"},{\"name\":\"Khost\",\"id\":3878,\"state_code\":\"KHO\"},{\"name\":\"Kunar\",\"id\":3876,\"state_code\":\"KNR\"},{\"name\":\"Kunduz Province\",\"id\":3900,\"state_code\":\"KDZ\"},{\"name\":\"Laghman\",\"id\":3891,\"state_code\":\"LAG\"},{\"name\":\"Logar\",\"id\":3897,\"state_code\":\"LOG\"},{\"name\":\"Nangarhar\",\"id\":3882,\"state_code\":\"NAN\"},{\"name\":\"Nimruz\",\"id\":3896,\"state_code\":\"NIM\"},{\"name\":\"Nuristan\",\"id\":3880,\"state_code\":\"NUR\"},{\"name\":\"Paktia\",\"id\":3894,\"state_code\":\"PIA\"},{\"name\":\"Paktika\",\"id\":3877,\"state_code\":\"PKA\"},{\"name\":\"Panjshir\",\"id\":3881,\"state_code\":\"PAN\"},{\"name\":\"Parwan\",\"id\":3895,\"state_code\":\"PAR\"},{\"name\":\"Samangan\",\"id\":3883,\"state_code\":\"SAM\"},{\"name\":\"Sar-e Pol\",\"id\":3885,\"state_code\":\"SAR\"},{\"name\":\"Takhar\",\"id\":3893,\"state_code\":\"TAK\"},{\"name\":\"Urozgan\",\"id\":3898,\"state_code\":\"URU\"},{\"name\":\"Zabul\",\"id\":3874,\"state_code\":\"ZAB\"}]}]";
        return submitResponse;
    }
}