import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;
import java.util.Date;
public class Database {
    public static Connection create_connection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/StucoEventDB?serverTimezone=UTC";
        String user = "root";
        String password = "Gu254at!$";
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static void newStucoEvents(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter New Event Name: ");
        String eventName = sc.nextLine();
        System.out.print("Enter Date (yyyy-MM-dd): ");
        String date = sc.nextLine();
        System.out.print("Enter Location: ");
        String location = sc.nextLine();
        System.out.print("Enter Time (hh:mm:ss): ");
        String time = sc.nextLine();
        saveStucoEvents(conn,eventName,date,location,time);
    }

    public static void saveStucoEvents(Connection conn, String eventName, String date, String location, String time) throws SQLException{
        String eventInsert = "INSERT INTO StucoEvents (Name,Date,Location,Time) VALUES (?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(eventInsert);
        stmt.setString(1,eventName);
        stmt.setString(2,date);
        stmt.setString(3,location);
        stmt.setString(4,time);
        stmt.executeUpdate();
        System.out.println ("New Stuco Event saved: " + eventName);
    }

    public static void savePerson(Connection conn, String firstName, String lastName, String email) throws SQLException{
        String personInsert = "INSERT INTO Persons (FirstName,LastName,Email) VALUES (?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(personInsert);
        stmt.setString(1,firstName);
        stmt.setString(2,lastName);
        stmt.setString(3,email);
        stmt.executeUpdate();
        System.out.println("New Person saved: " + firstName + " " + lastName);
    }

    public static boolean addPersontoGroup(Connection conn,String email,String groupName) throws SQLException{
        String groupIDselect = "SELECT GroupID FROM Groups WHERE GroupName=?";
        PreparedStatement stmt = conn.prepareStatement(groupIDselect);
        stmt.setString(1,groupName);
        ResultSet resGroupID = stmt.executeQuery();
        if(!resGroupID.next()){
            return false;
        }
        int groupID = resGroupID.getInt(1);
        int personID =

    }

    //public static void saveGroup(Connection conn, String)

    public static void main(String[] args) {
        Connection conn = null;
        try{
            conn = create_connection();
            if (conn != null) {
                System.out.println("Connected to the database StucoEventDB");
                System.out.println (conn.isValid(2));

                newStucoEvents(conn);
                //saveStucoEvents(conn,"Basketball","2020-12-20","Courts","17:00:00");


                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery("Select * From StucoEvents");
                while(res.next()){
                    String val = "";
                    for(int i=1; i<=5; i= i+1){
                        val = val + " " + res.getString(i);
                    }
                    System.out.println(val);
                }
            }
        } catch(SQLException err){
            System.out.println("An exception occurred in the database: ");
            err.printStackTrace();
        }
        finally{
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
