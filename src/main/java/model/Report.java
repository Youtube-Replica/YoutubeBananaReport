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
            conn.setAutoCommit(false);
            CallableStatement upperProc = conn.prepareCall("{? = call get_report_by_id( ? ) }");
            upperProc.registerOutParameter(1,Types.OTHER);
            upperProc.setInt(2,id);
            upperProc.execute();
            ResultSet rs = (ResultSet) upperProc.getObject(1);
            while (rs.next()) {
                reportObject.put("submitterid",rs.getString(2));
                reportObject.put("againstid",rs.getString(3));
                reportObject.put("content",rs.getString(4));
                reportObject.put("status",rs.getString(5));
            }
            rs.close();
            upperProc.close();
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
        try {
            conn = DriverManager.getConnection(url, props);
            CallableStatement upperProc = conn.prepareCall("{ call delete_report( ? ) }");
            upperProc.setInt(1,id);
            upperProc.execute();
            upperProc.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "rows deleted";
    }
}
