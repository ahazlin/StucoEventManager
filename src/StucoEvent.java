import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class StucoEvent {
    private String name;
    private LocalDate date;
    private String location;
    private LocalDateTime time;
    private int numberOfParticipants;
    private ArrayList<String> inviteList;

    public StucoEvent(String name, LocalDate date, String location, ArrayList<String> inviteList) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.inviteList = inviteList;
    }

    public String toString() {
        String res = "Event Information: \n";
        res = res + "Name: " + this.name + "\n";
        res = res + "Date: " + this.date + "\n";
        res = res + "Location: " + this.location + "\n";
        res = res + "Time" + this.time + "\n";
        res = res + "Number of Participants: " + this.numberOfParticipants + "\n";
        res = res + "Invite List: ";

        for (int i = 0; i<this.inviteList.size(); i = i+1){
            res = res + this.inviteList.get(i);

            if (i< this.inviteList.size() - 1){
                res = res + ", ";
            }
        }
        res = res + "\n\n";
        return res;
    }
    public int getNumberOfParticipants(){
        return this.numberOfParticipants;
    }

    public String getLocation(){ return this.location;}

    public String getName(){return this.name;}

    public LocalDateTime gettime(){return this.time;}

    public LocalDate getdate(){
        return this.date;
    }



}