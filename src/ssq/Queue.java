package ssq;
import java.util.*;

class Queue extends LinkedList {

    public void enqueue(Event event) {
        add(event);
    }

    public Object dequeue() {
        return removeFirst();
    }
}
