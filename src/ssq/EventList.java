package ssq;
import java.util.*;

class EventList extends LinkedList {


    public EventList() {
        super();
    }

    public Object getMin() {
        Collections.sort(this, (Event Event1, Event Event2)-> Event1.getTime() > Event2.getTime() ? 1 : (Event1.getTime() < Event2.getTime() ? -1 : 0));
        return getFirst();
    }

    public void enqueue(Event event) {
        add(event);
    }

    public void dequeue() {
        removeFirst();
    }


}
