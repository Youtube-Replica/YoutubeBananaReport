package model;


import org.json.simple.JSONObject;

import java.sql.*;
import java.util.Properties;

public class Report {
    //example
    public static String getReportById(int id) {
        String url = "jdbc:postgresql://localhost/scalable";
        System.out.println("ID is: "+id);
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "passw0rd");
        Connection conn = null;
        JSONObject reportObject = new JSONObject();
        try {
            conn = DriverManager.getConnection(url, props);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Reports WHERE reportid="+id);
            while (rs.next()) {
                reportObject.put("submitterid",rs.getString(2));
                reportObject.put("againstid",rs.getString(3));
                reportObject.put("content",rs.getString(4));
                reportObject.put("status",rs.getString(5));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return reportObject.toString();
    }

    public static String deleteReportById(int id){
        String url = "jdbc:postgresql://localhost/scalable";
        System.out.println("ID is: "+id);
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "passw0rd");
        Connection conn = null;
        int rowsDeleted =0;
        try {
            conn = DriverManager.getConnection(url, props);
            PreparedStatement st = conn.prepareStatement("DELETE FROM reports WHERE reportid="+id);
             rowsDeleted = st.executeUpdate();
            System.out.println(rowsDeleted + " rows deleted");
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsDeleted+" rows deleted";
    }
}
