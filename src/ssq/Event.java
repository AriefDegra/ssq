/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssq;

/**
 *
 * @author SMJPX
 */
class Event {

    private double time;
    private int type;

    public Event(int type, double time) {
        this.type = type;
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public double getTime() {
        return time;
    }
}
