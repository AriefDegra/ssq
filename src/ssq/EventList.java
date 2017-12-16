/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssq;

import java.util.*;

/**
 *
 * @author SMJPX
 */
class EventList extends LinkedList {


    public EventList() {
        super();
    }

    public Object getMin() {
        sort();
        return getFirst();
    }

    public void enqueue(Object _o) {
        add(_o);
    }

    public void dequeue() {
        removeFirst();
    }


    public void sort(){
        Collections.sort(this,  new Comparator<Event>() {
            @Override public int compare(Event e1, Event e2) {
                return e1.getTime() > e2.getTime() ? 1 : (e1.getTime() < e2.getTime() ? -1 : 0);
            }

        });
    }
}
