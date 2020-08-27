package com.hcl.ecomm.core.servlets;
import com.hcl.ecomm.core.services.PincodeService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.Servlet;

@Component(service= Servlet.class,property ={"sling.servlet.paths=/bin/hclecomm/locationpincode",
"sling.servlet.methods="+ HttpConstants.METHOD_GET,"sling.servlet.extensions=json"})

public class PincodeLocationServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 4016057296495129474L;
    private static final Logger LOG = LoggerFactory.getLogger(PincodeLocationServlet.class);
    private static int flag=0;

    @Reference
    PincodeService pincodeService;

    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res)
    {
        LOG.info("Inside PincodeLocationServlet doGet method");
        try{
            JSONArray responseStream =getPincodeLocation();
            LOG.info("responseStream for getPincodeLocation() {}", responseStream.toString());
            JSONArray pinres=new JSONArray();
            JSONObject pincontent=new JSONObject();
            String pincode=req.getParameter("Pincode");
            LOG.info("pincode entered by user: ",pincode);
            for (int i = 0; i < responseStream.length(); i++)
            {
                if(pincode.equals(responseStream.getJSONObject(i).get("Pincode")))
                {
                pincontent.put("Pincode",responseStream.getJSONObject(i).get("Pincode"));
                pincontent.put("City",responseStream.getJSONObject(i).get("City"));
                pincontent.put("District",responseStream.getJSONObject(i).get("District"));
                pincontent.put("State",responseStream.getJSONObject(i).get("State"));
                pinres.put(pincontent);
                flag++;
                }
            }
            LOG.info("value of flag : ",flag);
            if (flag==0)
            {   pincontent.put("Invalid Pincode","invalid Pincode");
                pinres.put(pincontent);
            }
            res.setContentType("application/json");
            res.getWriter().write(pinres.toString());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JSONArray getPincodeLocation() throws JSONException
    {
        return pincodeService.getPincodeLocation();
    }
}
