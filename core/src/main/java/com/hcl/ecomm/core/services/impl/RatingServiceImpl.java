package com.hcl.ecomm.core.services.impl;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.hcl.ecomm.core.pojo.Ratings;
import com.hcl.ecomm.core.services.RatingService;
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
    String query = "";
    PreparedStatement pstmt = null;
    PreparedStatement ps = null;


    @Reference
    private DataSourcePool source;

    @Override
    public JSONObject saveRating(JSONObject ratItem) {
        boolean rowInserted = false;
        JSONObject rateItemRes = new JSONObject();
        Connection connection = getConnection();
         try {

            query = "INSERT INTO hclecomm.product_ratings(sku,name,rating) VALUES (?,?,?)";


            pstmt = connection.prepareStatement(query);
            pstmt.setString(1,ratItem.getString("sku"));
            pstmt.setString(2,ratItem.getString("name"));
            pstmt.setFloat(3, (float) ratItem.getDouble("rating"));
            rowInserted = pstmt.executeUpdate() > 0;
            if(rowInserted){
                rateItemRes.put("message",rowInserted);
            }
            else{
                rateItemRes.put("message",rowInserted);
                log.error("Error while saving  the data : ");
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
        log.info("inserted data is {}",rowInserted);
         return rateItemRes;
    }




    @Override
    public List<Ratings> getRatingDataSQL(String sku)  {
        List<Ratings> ratingsList = new ArrayList<Ratings>();
        Connection  connection = getConnection();
        try{
                query = "SELECT sku,name,avg(rating) from hclecomm.product_ratings where sku=?";

                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, sku);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {

                    Ratings ratings = new Ratings();
                    ratings.setSku(rs.getString("sku"));
                    ratings.setName(rs.getString("name"));
                    ratings.setRating(rs.getFloat("avg(rating)"));
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
        log.info("Rating List is {}",ratingsList.toString());
        return ratingsList;

    }


    private Connection getConnection() {

        DataSource dataSource = null;
        Connection con = null;
        try{
            log.info("GET CONNECTION");
            dataSource = (DataSource) source.getDataSource("hclecomm");
            con = dataSource.getConnection();
            log.info("connection is returned");

        }
        catch (Exception e)
        {
            log.error("Error while Setting Connection : " + e);
        }

        return con;
    }

}
