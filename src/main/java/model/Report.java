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
        props.setProperty("user", "nagaty");
        props.setProperty("password", "61900");
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
        props.setProperty("user", "nagaty");
        props.setProperty("password", "61900");
        Connection conn = null;
        int rowsDeleted = 0;
        try {
            conn = DriverManager.getConnection(url, props);
            CallableStatement upperProc = conn.prepareCall("{? =call delete_report( ? ) }");
            upperProc.registerOutParameter(1,Types.INTEGER);
            upperProc.setInt(2,id);
            upperProc.execute();
            rowsDeleted = upperProc.getInt(1);
            upperProc.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsDeleted + " rows deleted";
    }

    public static String createReport(String submitterid, String againstid, String status) {
        String url = "jdbc:postgresql://localhost/scalable";
        Properties props = new Properties();
        props.setProperty("user", "nagaty");
        props.setProperty("password", "61900");
        Connection conn = null;
        String rowsInserted ="";
        try {
            conn = DriverManager.getConnection(url, props);
            CallableStatement upperProc = conn.prepareCall("{ call report_user_by_id( ?, ?, ? ) }");
            upperProc.setInt(1, Integer.parseInt(submitterid));
            upperProc.setInt(2,Integer.parseInt(againstid));
            upperProc.setString(3,status);

            rowsInserted = "rows inserted " + upperProc.executeUpdate();
            //Set SQL Function to return 1 if successful insert
            upperProc.close();
        } catch (SQLException e) {
            System.out.println("SQL Error State: " + e.getSQLState());
                e.printStackTrace();
            }

        return rowsInserted;
    }

    public static String updateReportStatus(int id, String password) {
        String url = "jdbc:postgresql://localhost/scalable";
        Properties props = new Properties();
        props.setProperty("user", "nagaty");
        props.setProperty("password", "61900");
        Connection conn = null;
        int rowsAffected = 0;
        try {
            conn = DriverManager.getConnection(url, props);
            CallableStatement upperProc = conn.prepareCall("{ ? = call update_report_by_id( ?, ? ) }");
            upperProc.registerOutParameter(1,Types.INTEGER);
            upperProc.setInt(2,id);
            upperProc.setString(3,password);
            upperProc.execute();
            rowsAffected = upperProc.getInt(1);
            upperProc.close();

        } catch (SQLException e) {
            System.out.println("SQL Error State: " + e.getSQLState());
            e.printStackTrace();
        }
        return "Rows Affected: " + rowsAffected;
    }
}
