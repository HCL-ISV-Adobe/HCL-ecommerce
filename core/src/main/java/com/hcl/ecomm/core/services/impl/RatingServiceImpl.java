package com.hcl.ecomm.core.services.impl;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.hcl.ecomm.core.pojo.Ratings;
import com.hcl.ecomm.core.services.RatingService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component(
        immediate = true,
        enabled = true,
        service = RatingService.class)
public class RatingServiceImpl implements RatingService {

    private static final Logger log = LoggerFactory.getLogger(RatingServiceImpl.class);
    DataSource dataSource = null;
    //String query = "";
    PreparedStatement pstmt = null;
    PreparedStatement ps = null;


    @Reference
    private DataSourcePool source;

    @Override
    public JSONObject saveRating(JSONObject ratItem,String email,String sku) {
        boolean custRowInserted = false;
        JSONObject rateItemRes = new JSONObject();
        Connection connection = getConnection();
         try {
            String custQuery="SELECT email from hclecomm.customer where email=?";
            pstmt = connection.prepareStatement(custQuery);
             pstmt.setString(1, email);
             ResultSet rs = pstmt.executeQuery();
             String reviewQuery="SELECT title,description from hclecomm.product_reviews  where sku =? and email=?";
             pstmt = connection.prepareStatement(reviewQuery);
             pstmt.setString(1, sku);
             pstmt.setString(2, email);
             ResultSet rs2 = pstmt.executeQuery();
             if(!rs.next()) {
                 String insertIntoCust = "INSERT INTO hclecomm.customer(name,email) VALUES (?,?)";
                 pstmt = connection.prepareStatement(insertIntoCust);
                 pstmt.setString(1, ratItem.getString("name"));
                 pstmt.setString(2, ratItem.getString("email"));
                 custRowInserted = pstmt.executeUpdate() > 0;
                 if (custRowInserted) {
                     rateItemRes.put("message", custRowInserted);
                 } else {
                     rateItemRes.put("message", custRowInserted);
                     log.error("Error while Inserting  the data : ");
                 }
                 String insertIntoReview = "INSERT INTO hclecomm.product_reviews(sku,title,description,customer,email,name,rating) VALUES (?,?,?,?,?,?,?)";
                 pstmt = connection.prepareStatement(insertIntoReview);
                 pstmt.setString(1,ratItem.getString("sku"));
                 pstmt.setString(2,ratItem.getString("title"));
                 pstmt.setString(3,ratItem.getString("description"));
                 pstmt.setString(4,ratItem.getString("customer"));
                 pstmt.setString(5,ratItem.getString("email"));
                 pstmt.setString(6,ratItem.getString("name"));
                 pstmt.setFloat(7, (float) ratItem.getDouble("rating"));
                 boolean reviewRowInserted = pstmt.executeUpdate() > 0;
                 if(reviewRowInserted){
                     rateItemRes.put("message",reviewRowInserted);
                 }
                 else{
                     rateItemRes.put("message",reviewRowInserted);
                     log.error("Error while Inserting  the data : ");
                 }
             }
             else if (!rs2.next()){
                 String insertIntoReview = "INSERT INTO hclecomm.product_reviews(sku,title,description,customer,email,name,rating) VALUES (?,?,?,?,?,?,?)";
                 pstmt = connection.prepareStatement(insertIntoReview);
                 pstmt.setString(1,ratItem.getString("sku"));
                 pstmt.setString(2,ratItem.getString("title"));
                 pstmt.setString(3,ratItem.getString("description"));
                 pstmt.setString(4,ratItem.getString("customer"));
                 pstmt.setString(5,ratItem.getString("email"));
                 pstmt.setString(6,ratItem.getString("name"));
                 pstmt.setFloat(7, (float) ratItem.getDouble("rating"));
                 boolean reviewRowInserted = pstmt.executeUpdate() > 0;
                 if(reviewRowInserted){
                     rateItemRes.put("message",reviewRowInserted);
                 }
                 else{
                     rateItemRes.put("message",reviewRowInserted);
                     log.error("Error while Inserting  the data : ");
                 }
                 log.debug("inserted data is {}",reviewRowInserted);
             }
             else{
                 String updateReview = "UPDATE hclecomm.product_reviews set title=?,description=?,rating=? where sku =? and email=?";
                 pstmt = connection.prepareStatement(updateReview);
                 pstmt.setString(1,ratItem.getString("title"));
                 pstmt.setString(2,ratItem.getString("description"));
                 pstmt.setFloat(3, (float) ratItem.getDouble("rating"));
                 pstmt.setString(4,ratItem.getString("sku"));
                 pstmt.setString(5,ratItem.getString("email"));
                 boolean ReviewUpdated = pstmt.executeUpdate() > 0;
                 if(ReviewUpdated){
                     rateItemRes.put("message",ReviewUpdated);
                 }
                 else{
                     rateItemRes.put("message",ReviewUpdated);
                     log.error("Error while Updating  the data : ");
                 }
                 log.debug("inserted data is {}",ReviewUpdated);

             }
         } catch (Exception e) {
             log.error("Error while saving  the data : " + e);
        }
         finally {
             try {
                 if(null != connection && !connection.isClosed())
                     connection.close();
             }
             catch (SQLException e) {
                 log.error("Error while trying to close connection. SQLException = {}", e);
             }
         }
        log.debug("inserted data is {}",custRowInserted);

         return rateItemRes;
    }

    @Override
    public List<Ratings> getRatingDataSQL(String sku)  {
        List<Ratings> ratingsList = new ArrayList<Ratings>();
        Connection  connection = getConnection();
        JSONArray getRatingRes= new JSONArray();

        try{
            String avgRating = "SELECT avg(rating) FROM hclecomm.product_reviews where sku=?";
            pstmt = connection.prepareStatement(avgRating);
            pstmt.setString(1, sku);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Ratings ratings = new Ratings();
                ratings.setRating(rs.getFloat("avg(rating)"));
                ratingsList.add(ratings);
            }

            String reviewComments = "SELECT ps.sku, ps.title,ps.description,ps.customer,ps.rating FROM hclecomm.product_reviews ps INNER JOIN hclecomm.customer c ON c.email = ps.email WHERE  ps.sku=?";

                pstmt = connection.prepareStatement(reviewComments);
                pstmt.setString(1, sku);
                ResultSet rs2 = pstmt.executeQuery();

                while (rs2.next()) {

                    Ratings ratings = new Ratings();
                    ratings.setSku(rs2.getString("sku"));
                    ratings.setTitle(rs2.getString("title"));
                    ratings.setDescription(rs2.getString("description"));
                    ratings.setCustomer(rs2.getString("customer"));
                    ratings.setRating(rs2.getFloat("rating"));
                    ratingsList.add(ratings);

            }

        } catch (Exception e) {
            log.error("Error while getting the data : " + e);
        }
        finally {
            try {
                if(null != connection && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e) {
                log.error("Error while trying to close connection. SQLException = {}", e);
            }
        }
        log.debug("Rating List is {}",ratingsList.toString());
        return ratingsList;

    }


    private Connection getConnection() {

        DataSource dataSource = null;
        Connection con = null;
        try{
            log.debug("GET CONNECTION");
            dataSource = (DataSource) source.getDataSource("hclecomm");
            con = dataSource.getConnection();
            log.debug("connection is returned");

        }
        catch (Exception e)
        {
            log.error("Error while Setting Connection : " + e);
        }

        return con;
    }

}
