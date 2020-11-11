import java.util.Comparator;
public class StucoEventSorter implements Comparator<StucoEvent> {
    public int compare(StucoEvent event1, StucoEvent event2){
        int nP1 = event1.getNumberOfParticipants();
        int nP2 = event2.getNumberOfParticipants();
        if (nP1 == nP2){
            return 0;
        }
        else if (nP1 < nP2){
            return -1;
        }
        else{
            return 1;
        }
    }
}

//^ Checks the number of participants and compares between two events for the purposes of sorting