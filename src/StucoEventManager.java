import java.util.ArrayList;
import java.util.Collections;
public class StucoEventManager {
    private ArrayList<StucoEvent> eventlist;

    public StucoEventManager(){
        this.eventlist = new ArrayList<StucoEvent>();
    }

    public void addEvent(StucoEvent myevent){
        this.eventlist.add(myevent);
    }

    public void printEventNames(){
        System.out.print("The Event Manager Contains " + this.eventlist.size() + " event(s): ");
        for (int i=0; i< this.eventlist.size(); i=i+1){
            StucoEvent x = this.eventlist.get(i);
            System.out.print(x.getName());
            if(i < this.eventlist.size() - 1){
                System.out.print(", ");
            }
        }
        System.out.println();
    }
    public void sortEvents(){
        Collections.sort(this.eventlist, new StucoEventSorter().reversed());

    }
}
