import javax.swing.*;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
public class Database {
    public static Connection create_connection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/Practice?serverTimezone=UTC";
        String user = "root";
        String password = "Gu254at!$";
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
    public static void login (Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please input e-mail: ");
        String email = sc.nextLine();
        System.out.print("\n Please input password: ");
        String password = sc.nextLine();
        System.out.println(" ");
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery("Select * From Users where Email=\"" + email + "\" and Password=\"" + password +"\"");
        if(res.next() == true){
            System.out.println("User logged in");
        }
        else {
            System.out.println("Email or Password is incorrect");
        }
    }
    // Function used to create a new User + fields
    public static void newUser(Connection conn) throws SQLException{
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter New FirstName: ");
        String FirstName = sc.nextLine();
        System.out.print("Enter New LastName: ");
        String LastName = sc.nextLine();
        System.out.print("Enter New Email: ");
        String Email = sc.nextLine();
        System.out.print("Enter New Password: ");
        String Password = sc.nextLine();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Users (FirstName,LastName,Email,Password) VALUES ('"+FirstName+"','"+LastName+"','"+Email+"','"+Password+"')");
        System.out.print ("New User added: " + FirstName);
    }

    public static void changePassword(Connection conn) throws SQLException{
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter User ID: ");
        String UserID = sc.nextLine();
        System.out.print("Enter Old Password: ");
        String PasswordOld = sc.nextLine();
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery("Select * From Users where idUsers=" + UserID + " and Password=\"" + PasswordOld +"\"");
        if(res.next() == true) {
            System.out.print("Enter New Password: ");
            String PasswordNew = sc.nextLine();
            stmt.executeUpdate("UPDATE Users SET Password = '"+PasswordNew+"' WHERE idUsers=" + UserID + ";");
            System.out.print("Password Updated");
        }
        else{
            System.out.print("User ID or Password is incorrect");
        }
    }
    // connects to my sql database
    public static void main(String[] args) {
        Connection conn1 = null;
        try{
            conn1 = create_connection();
            if (conn1 != null) {
                System.out.println("Connected to the database Practice");
                System.out.println (conn1.isValid(2));
                Statement stmt = conn1.createStatement();
                ResultSet res = stmt.executeQuery("Select * From Users");
                while(res.next()){
                    String val = "";
                    for(int i=1; i<6; i= i+1){
                        val = val + " " + res.getString(i);
                    }
                    System.out.println(val);
                }
                //   login(conn1);
                //    newUser(conn1);
                // changePassword(conn1);
                //add a method called printUsers
            }
        }catch(SQLException err){
            System.out.println("An exception occurred in the database: ");
            err.printStackTrace();
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();

                }
            }
        }
    }
}
