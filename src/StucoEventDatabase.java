import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class StucoEventDatabase {
    private Connection conn;

    /**
     * Finds the SQL database matching the parameters
     * @param databaseName Name of the database on the MySQL server
     * @throws SQLException
     */
    public StucoEventDatabase(String databaseName) throws SQLException {
        this.conn = create_connection(databaseName);
    }

    /**
     * Connects to the SQL database
     * @param databaseName Name of the database on the MySQL server
     * @return
     * @throws SQLException
     */
    public static Connection create_connection(String databaseName) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/" + databaseName + "?serverTimezone=UTC";
        String user = "root";
        String password = "Gu254at!$";
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    /**
     * Scanner to input data and create a new event
     * @throws SQLException
     */
    public void newStucoEvents() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter New Event Name: ");
        String eventName = sc.nextLine();
        System.out.print("Enter Date (yyyy-MM-dd): ");
        String date = sc.nextLine();
        System.out.print("Enter Location: ");
        String location = sc.nextLine();
        System.out.print("Enter Time (hh:mm:ss): ");
        String time = sc.nextLine();
        saveStucoEvents(eventName, date, location, time);
    }

    /**
     * Inserts a new event with the provided parameters into the database
     * @param eventName Name of the event
     * @param date Date of the event in yyyy-MM-dd format
     * @param location Location of the event
     * @param time Time of the event in hh:mm:ss format
     * @throws SQLException
     */
    public void saveStucoEvents(String eventName, String date, String location, String time) throws SQLException {
        String eventInsert = "INSERT INTO StucoEvents (Name,Date,Location,Time) VALUES (?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(eventInsert);
        stmt.setString(1, eventName);
        stmt.setString(2, date);
        stmt.setString(3, location);
        stmt.setString(4, time);
        stmt.executeUpdate();
        System.out.println("New Stuco Event saved: " + eventName);
    }

    /**
     * Scanner for savePerson
     * @throws SQLException
     */
    public void newPerson() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        savePerson(firstName, lastName, email);
    }

    /**
     * Saves a new person in the database using the inputted parameters
     * @param firstName First Name of the person
     * @param lastName Last Name of the person
     * @param email E-mail address of the person
     * @throws SQLException
     */
    public void savePerson(String firstName, String lastName, String email) throws SQLException {
        String personInsert = "INSERT INTO Persons (FirstName,LastName,Email) VALUES (?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(personInsert);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, email);
        stmt.executeUpdate();
        System.out.println("New Person saved: " + firstName + " " + lastName);
    }

    /**
     * Scanner for saveGroups
     * @throws SQLException
     */
    public void newGroup() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Group Name: ");
        String groupName = sc.nextLine();
        saveGroups(groupName);
    }

    /**
     * Saves a new Mailing Group in the database
     * @param groupName Name of the Mailing group
     * @throws SQLException
     */
    public void saveGroups(String groupName) throws SQLException {
        String groupSave = "INSERT INTO MailingGroup (GroupName) VALUES (?)";
        PreparedStatement stmt = conn.prepareStatement(groupSave);
        stmt.setString(1, groupName);
        System.out.println(stmt);
        stmt.executeUpdate();
        System.out.println("New Group saved: " + groupName);
    }

    /**
     * Retrieves the ID column for a given table and condition
     * @param tableName Name of the table in the SQL database
     * @param columns ID column within the given table
     * @param condition Condition within the column
     * @return ID
     * @throws SQLException
     */
    public int retrieveID(String tableName, String columns, String condition) throws SQLException {
        Statement stmt = conn.createStatement();
        String retrieve = "SELECT " + columns + " FROM " + tableName + " WHERE " + condition;
        ResultSet resID = stmt.executeQuery(retrieve);
        if (!resID.next()) {
            printTable(tableName);
            throw new SQLException("SQL query returned no results: " + retrieve);
        }
        int id = resID.getInt(1);
        return id;
    }

    /**
     * Scanner for addPersontoMailingGroup
     * @return
     * @throws SQLException
     */
    public boolean addPersontoMailingGroup() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Group Name: ");
        String groupName = sc.nextLine();
        return addPersontoMailingGroup(groupName, email);
    }

    /**
     * Retrieves person ID from the persons table in the database for a provided email
     * @param email E-mail address of the person
     * @return Person ID
     * @throws SQLException
     */
    public int getPersonID(String email) throws SQLException{
        int getPersonID = retrieveID("Persons","PersonID","email=\"" + email +"\"");
        return getPersonID;
    }

    /**
     * Adds a member's email address into a mailing group
     * @param groupName Name of the mailing group
     * @param email email address of the person being added
     * @return
     * @throws SQLException
     */
    public boolean addPersontoMailingGroup(String groupName, String email) throws SQLException {
        int groupID = getGroupID(groupName);

        int personID = getPersonID(email);

        String addMember = "INSERT INTO GroupMembers (MembersGroupID,MembersPersonID) VALUES (?,?)";
        PreparedStatement stmt = conn.prepareStatement(addMember);
        stmt.setInt(1, groupID);
        stmt.setInt(2, personID);
        stmt.executeUpdate();
        System.out.println("Member " + email + " was added to " + groupName);
        return true;
    }

    /**
     * Scanner for addPersontoInvitees
     * @return
     * @throws SQLException
     */
    public boolean addPersontoInvitees() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter date: ");
        String date = sc.nextLine();
        return addPersontoInvitees(email, name, date);
    }

    /**
     * Adds a person individually to an invitee list for a given event
     * @param email The Persons e-mail
     * @param name The Name of the event
     * @param date The Date of the event (to avoid confusion with an event of the same name)
     * @return
     * @throws SQLException
     */
    public boolean addPersontoInvitees(String email, String name, String date) throws SQLException {
        int inviteeEventID = getEventID(name, date);

        int inviteePersonID =  getPersonID(email);

        String invitePerson = "INSERT INTO Invitees (inviteeEventID,inviteePersonID) VALUES (?,?)";
        PreparedStatement stmt = conn.prepareStatement(invitePerson);
        stmt.setInt(1, inviteeEventID);
        stmt.setInt(2, inviteePersonID);
        stmt.executeUpdate();
        System.out.println("Member " + email + " was invited to " + name + " on " + date);
        return true;
    }

    /**
     * Scanner for addGroupstoEvents
     * @return Parameters for addGroupstoEvents
     * @throws SQLException
     */
    public boolean addGroupstoEvents() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Event Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Event Date: ");
        String date = sc.nextLine();
        System.out.print("Enter Group Name: ");
        String groupName = sc.nextLine();
        return addGroupstoEvents(name, date, groupName);
    }

    /**
     * Adds an entire mailing group to InvitedGroups of a given event
     * @param name Name of event
     * @param date date of event
     * @param groupName name of mailing group
     * @return
     * @throws SQLException
     */
    public boolean addGroupstoEvents(String name, String date, String groupName) throws SQLException {

        int InvitedGroupID = getGroupID(groupName);

        int InvitedEventID = getEventID(name, date);

        String addMember = "INSERT INTO InvitedGroups (InvitedGroupID,InvitedEventID) VALUES (?,?)";
        PreparedStatement stmt = conn.prepareStatement(addMember);
        stmt.setInt(1, InvitedGroupID);
        stmt.setInt(2, InvitedEventID);
        stmt.executeUpdate();
        System.out.println("Groups " + groupName + " was added to event: " + name + " " + date);
        return true;
    }

    /**
     * Prints given table from SQL database
     * @param tableName Name of the given table
     * @throws SQLException
     */
    public void printTable(String tableName) throws SQLException {
        String selectTable = "SELECT * FROM " + tableName;
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

    /**
     * Retrieves the ID of an event matching the parameters in the database
     * @param name Name of event
     * @param date Date of event
     * @return Event ID
     * @throws SQLException
     */
    public int getEventID(String name, String date) throws SQLException {
        int eventID = retrieveID("StucoEvents", "EventID", "Name=\"" + name + "\" AND Date=\"" + date + "\"");
        return eventID;
    }

    /**
     * Retrieves the ID of a mailing group mathcing the parameters in the database
     * @param groupName Name of a mailing group
     * @return Group ID
     * @throws SQLException
     */
    public int getGroupID(String groupName) throws SQLException {
        int groupID = retrieveID("MailingGroup", "GroupID", "GroupName=\"" + groupName + "\"");
        return groupID;
    }

    /**
     * Makes an array list of the person IDs of all the members in a mailing group that matches the parameters in the database
     * @param groupName Name of a mailing group
     * @return The Person IDs of the members in the group
     * @throws SQLException
     */
    public ArrayList<Integer> getGroupMemberIDs(String groupName) throws SQLException {
        int groupID = getGroupID(groupName);
        String selectPersons = "SELECT MembersPersonID FROM GroupMembers WHERE MembersGroupID=?";
        PreparedStatement stmt = conn.prepareStatement(selectPersons);
        stmt.setInt(1, groupID);
        ResultSet res = stmt.executeQuery();
        ArrayList<Integer> groupMemberIDs = new ArrayList<>();
        while (res.next()) {
            int memberID = res.getInt(1);
            groupMemberIDs.add(memberID);
        }
        return groupMemberIDs;
    }

    /**
     * Retrieves the names of the mailing groups that have been invited to an event
     * @param eventID ID of the event
     * @return array list with names of groups
     * @throws SQLException
     */
    public ArrayList<String> getInvitedGroupNames(int eventID) throws SQLException {
        String selectInvitedGroupNames = "SELECT MailingGroup.GroupName FROM MailingGroup INNER JOIN InvitedGroups ON " +
                "MailingGroup.GroupID=InvitedGroups.InvitedGroupID WHERE InvitedGroups.InvitedEventID=?";
        PreparedStatement stmt = conn.prepareStatement(selectInvitedGroupNames);
        stmt.setInt(1, eventID);
        ResultSet res = stmt.executeQuery();
        ArrayList<String> groupNames = new ArrayList<>();
        while (res.next()) {
            String groupName = res.getString(1);
            groupNames.add(groupName);
        }
        return groupNames;
    }


    public ArrayList<String> getInvitedGroupNames(String name, String date) throws SQLException {
        int eventID = getEventID(name, date);
        return getInvitedGroupNames(eventID);
    }


    public void getEventInvitees(String name, String date) throws SQLException {
        int eventID = getEventID(name, date);
        String getInviteePersonIDs = "SELECT InviteePersonID FROM Invitees WHERE InviteeEventID=?";
        PreparedStatement stmt = conn.prepareStatement(getInviteePersonIDs);
        stmt.setInt(1, eventID);
        ResultSet res = stmt.executeQuery();
        ArrayList<Integer> inviteePersonIDs = new ArrayList<>();
        while (res.next()) {
            int inviteeID = res.getInt(1);
            inviteePersonIDs.add(inviteeID);
        }

        ArrayList<String> invitedGroupNames = getInvitedGroupNames(eventID);
        for (String invitedGroupName : invitedGroupNames) {
            ArrayList<Integer> groupMemberIDs = getGroupMemberIDs(invitedGroupName);
            inviteePersonIDs.addAll(groupMemberIDs);
        }

        System.out.println("People Invited to Event" +name+" on "+date+":");
        System.out.println(getPersonsInfos(inviteePersonIDs));
    }

    /**
     *
     * @param personIDs
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getPersonsInfos(ArrayList<Integer> personIDs) throws SQLException{
        String ids = "";
        for(int i=0; i<personIDs.size(); i++){
            ids = ids+personIDs.get(i);
            if(i<personIDs.size()-1){
                ids = ids + ", ";
            }
        }

        String getPersonInfo = "SELECT FirstName, LastName, Email FROM Persons WHERE PersonID IN ("+ids+")";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(getPersonInfo);
        ArrayList<String> personInfos = new ArrayList<>();
        while(res.next()){
            String personInfo = res.getString(1) + " " + res.getString(2) + " " + res.getString(3);
            personInfos.add(personInfo);
        }
        return personInfos;
    }


    public void closeConnection() throws SQLException {
        conn.close();
    }


    public static void main(String[] args) {
        StucoEventDatabase db = null;
        try {
            db = new StucoEventDatabase("StucoEventDB");
            System.out.println("Connected to the database StucoEventDB");
            //db.printTable("StucoEvents");
            //db.addPersontoMailingGroup();
            //db.addPersontoInvitees("ajhazlin@gmail.com","Football","2020-12-18");
            //db.addGroupstoEvents();

            //ArrayList<Integer> groupMemberIDs = db.getGroupMemberIDs("Grade 10");
            //System.out.println(groupMemberIDs);

            //ArrayList<String> invitedGroupNames = db.getInvitedGroupNames("Basketball","2020-12-20");
            //System.out.println(invitedGroupNames);

            //db.getEventInvitees("Basketball","2020-12-20");




            //db.addPersontoInvitees();
            //db.getEventInvitees("Football","2020-12-18");


        } catch (SQLException err) {
            System.out.println("An exception occurred in the database: ");
            err.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.closeConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

