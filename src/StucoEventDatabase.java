import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;
import java.util.Date;

public class Database {
    private Connection conn;
    public Database(String databaseName) throws SQLException {
          this.conn = create_connection(databaseName);
    }

    public static Connection create_connection(String databaseName) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/"+databaseName+"?serverTimezone=UTC";
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

    public static void newPerson(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        savePerson(conn,firstName,lastName,email);
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

    public static void newGroup(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Group Name: ");
        String groupName = sc.nextLine();
        saveGroups(conn,groupName);
    }


    public static void saveGroups(Connection conn, String groupName) throws SQLException{
        String groupSave = "INSERT INTO MailingGroup (GroupName) VALUES (?)";
        PreparedStatement stmt = conn.prepareStatement(groupSave);
        stmt.setString(1,groupName);
        System.out.println(stmt);
        stmt.executeUpdate();
        System.out.println("New Group saved: " + groupName);
    }

    public static int retrieveID(Connection conn, String tableName, String columns, String condition) throws SQLException{
        Statement stmt = conn.createStatement();
        String retrieve = "SELECT "+columns+" FROM "+tableName+" WHERE "+condition;
        ResultSet resID = stmt.executeQuery(retrieve);
        if(!resID.next()){
            printTable(conn,tableName);
            throw new SQLException("SQL query returned no results: "+retrieve);
        }
        int id = resID.getInt(1);
        return id;
    }

    public static boolean addPersontoGroup(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Group Name: ");
        String groupName = sc.nextLine();
        return addPersontoGroup(conn, groupName, email);
    }

    public static boolean addPersontoGroup(Connection conn,String groupName,String email) throws SQLException{
        int groupID = retrieveID(conn,"MailingGroup","GroupID","GroupName=\""+ groupName+ "\"");

        int personID = retrieveID(conn,"Persons","PersonID","email=\""+email+"\"");

        String addMember = "INSERT INTO GroupMembers (MembersGroupID,MembersPersonID) VALUES (?,?)";
        PreparedStatement stmt = conn.prepareStatement(addMember);
        stmt.setInt(1,groupID);
        stmt.setInt(2,personID);
        stmt.executeUpdate();
        System.out.println("Member " +email+ " was added to " +groupName);
        return true;
    }

    public static void printTable(Connection conn, String tableName) throws SQLException {
        String selectTable = "SELECT * FROM "+tableName;
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(selectTable);
        ResultSetMetaData rsmd = res.getMetaData();
        while (res.next()) {
            String rowValues = "";
            for (int i = 1; i <= rsmd.getColumnCount(); i = i + 1) {
                rowValues = rowValues + " " + res.getString(i);
            }
            System.out.println(rowValues);
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        try{
            conn = create_connection();
            if (conn != null) {
                System.out.println("Connected to the database StucoEventDB");
                System.out.println (conn.isValid(2));

                //newStucoEvents(conn);
                //saveStucoEvents(conn,"Basketball","2020-12-20","Courts","17:00:00");

                //newGroup(conn);
                //newPerson(conn);
                //addPersontoGroup(conn,"Alex.Hazlin@isbasel.ch","Grade 10");
                addPersontoGroup(conn);
                printTable(conn,"StucoEvents");
            }

            //(System.out.println("There was an error with the user input: "+email);

            //System.out.println("There was an error with the user input: "+groupName);

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
