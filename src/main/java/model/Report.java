package model;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.Properties;

public class Report {
    //example
    public static String getReportById(int id) {
       String callStatement = "{? = call get_report_by_id( ? ) }";
       JSONObject json = new JSONObject();
       JSONArray jsonArray = new JSONArray();
       JSONObject inputObject = new JSONObject();
       inputObject.put("type",Types.INTEGER);
       inputObject.put("value",id);
       jsonArray.add(inputObject);
       json.put("out_type",Types.OTHER);
       json.put("input_array",jsonArray);
       json.put("call_statement",callStatement);
       return json.toString();
    }

    public static String deleteReportById(int id){
        String callStatement = "{? = call delete_report( ? ) }";
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject inputID = new JSONObject();
        inputID.put("type",Types.INTEGER);
        inputID.put("value",id);
        jsonArray.add(inputID);
        json.put("call_statement",callStatement);
        json.put("out_type",Types.INTEGER);
        json.put("input_array",jsonArray);
        return json.toString();
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

    public static String getReportsPaginated(int pageSize, int pageNumber){
        String callStatement = "{? = call list_reports( ? , ? ) }";
        JSONArray  jsonArray = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject inputSize = new JSONObject();
        JSONObject inputNumber = new JSONObject();
        inputSize.put("type",Types.INTEGER);
        inputSize.put("value",pageSize);
        inputNumber.put("type",Types.INTEGER);
        inputNumber.put("value",pageNumber);
        jsonArray.add(inputSize);
        jsonArray.add(inputNumber);
        json.put("out_type",Types.OTHER);
        json.put("call_statement",callStatement);
        json.put("input_array",jsonArray);
        return json.toString();
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
