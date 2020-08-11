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
        String submitResponse =  "[{\n" +
                "        \"country_name\": \"Afghanistan\",\n" +
                "        \"country_id\": \"AF\",\n" +
                "        \"phone_code\": \"93\",\n" +
                "        \"states\": [\n" +
                "            {\n" +
                "                \"state_name\": \"Badakhshan\",\n" +
                "                \"region_id\": 3901,\n" +
                "                \"region_code\": \"BDS\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Badghis\",\n" +
                "                \"region_id\": 3871,\n" +
                "                \"region_code\": \"BDG\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Baghlan\",\n" +
                "                \"region_id\": 3875,\n" +
                "                \"region_code\": \"BGL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Balkh\",\n" +
                "                \"region_id\": 3884,\n" +
                "                \"region_code\": \"BAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Bamyan\",\n" +
                "                \"region_id\": 3872,\n" +
                "                \"region_code\": \"BAM\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Daykundi\",\n" +
                "                \"region_id\": 3892,\n" +
                "                \"region_code\": \"DAY\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Farah\",\n" +
                "                \"region_id\": 3899,\n" +
                "                \"region_code\": \"FRA\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Faryab\",\n" +
                "                \"region_id\": 3889,\n" +
                "                \"region_code\": \"FYB\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Ghazni\",\n" +
                "                \"region_id\": 3870,\n" +
                "                \"region_code\": \"GHA\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Ghor\",\n" +
                "                \"region_id\": 3888,\n" +
                "                \"region_code\": \"GHO\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Helmand\",\n" +
                "                \"region_id\": 3873,\n" +
                "                \"region_code\": \"HEL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Herat\",\n" +
                "                \"region_id\": 3887,\n" +
                "                \"region_code\": \"HER\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Jowzjan\",\n" +
                "                \"region_id\": 3886,\n" +
                "                \"region_code\": \"JOW\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Kabul\",\n" +
                "                \"region_id\": 3902,\n" +
                "                \"region_code\": \"KAB\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Kandahar\",\n" +
                "                \"region_id\": 3890,\n" +
                "                \"region_code\": \"KAN\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Kapisa\",\n" +
                "                \"region_id\": 3879,\n" +
                "                \"region_code\": \"KAP\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Khost\",\n" +
                "                \"region_id\": 3878,\n" +
                "                \"region_code\": \"KHO\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Kunar\",\n" +
                "                \"region_id\": 3876,\n" +
                "                \"region_code\": \"KNR\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Kunduz Province\",\n" +
                "                \"region_id\": 3900,\n" +
                "                \"region_code\": \"KDZ\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Laghman\",\n" +
                "                \"region_id\": 3891,\n" +
                "                \"region_code\": \"LAG\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Logar\",\n" +
                "                \"region_id\": 3897,\n" +
                "                \"region_code\": \"LOG\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Nangarhar\",\n" +
                "                \"region_id\": 3882,\n" +
                "                \"region_code\": \"NAN\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Nimruz\",\n" +
                "                \"region_id\": 3896,\n" +
                "                \"region_code\": \"NIM\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Nuristan\",\n" +
                "                \"region_id\": 3880,\n" +
                "                \"region_code\": \"NUR\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Paktia\",\n" +
                "                \"region_id\": 3894,\n" +
                "                \"region_code\": \"PIA\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Paktika\",\n" +
                "                \"region_id\": 3877,\n" +
                "                \"region_code\": \"PKA\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Panjshir\",\n" +
                "                \"region_id\": 3881,\n" +
                "                \"region_code\": \"PAN\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Parwan\",\n" +
                "                \"region_id\": 3895,\n" +
                "                \"region_code\": \"PAR\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Samangan\",\n" +
                "                \"region_id\": 3883,\n" +
                "                \"region_code\": \"SAM\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Sar-e Pol\",\n" +
                "                \"region_id\": 3885,\n" +
                "                \"region_code\": \"SAR\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Takhar\",\n" +
                "                \"region_id\": 3893,\n" +
                "                \"region_code\": \"TAK\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Urozgan\",\n" +
                "                \"region_id\": 3898,\n" +
                "                \"region_code\": \"URU\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"state_name\": \"Zabul\",\n" +
                "                \"region_id\": 3874,\n" +
                "                \"region_code\": \"ZAB\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"country_name\": \"Aland Islands\",\n" +
                "        \"country_id\": \"AX\",\n" +
                "        \"phone_code\": \"+358-18\",\n" +
                "        \"states\": [\n" +
                "            {\n" +
                "                \"state_name\": \"Aland Islands\",\n" +
                "                \"region_id\": 0,\n" +
                "                \"region_code\": \"AX\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }]";
        return submitResponse;
    }
}