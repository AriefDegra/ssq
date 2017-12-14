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
public class Sim {

    public static double clock,
            meanInterArrivalTime,
            meanServiceTime,
            SIGMA,
            lastEventTime,
            totalBusy,
            maxQueueLength,
            sumResponseTime;
    public static long numberOfCustomers,
            queueLength,
            numberInService,
            totalCustomers,
            numberOfDepartures,
            longService;
    public final static int arrival = 1;
    public final static int departure = 2;
    public static EventList futureEventList;
    public static Queue customers;
    public static Random stream;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Mean Inter-arrival Time: ");
        meanInterArrivalTime = sc.nextDouble();
        System.out.print("Mean Service Time: ");
        meanServiceTime = sc.nextDouble();
        System.out.print("Standard Deviation: ");
        SIGMA = sc.nextDouble();
        System.out.print("Total Number Of Customers: ");
        totalCustomers = sc.nextInt();

        stream = new Random(seed);
        futureEventList = new EventList();
        customers = new Queue();

        initialization();

        while (numberOfDepartures < totalCustomers) {
            Event evt = (Event) futureEventList.getMin();
            futureEventList.dequeue();
            clock = evt.getTime();
            if (evt.getType() == arrival) {
                processArrival(evt);
            } else {
                processDeparture(evt);
            }
        }
        reportGeneration();
    }

    public static void initialization() {

        clock = 0.0;
        queueLength = 0;
        numberInService = 0;
        lastEventTime = 0.0;
        totalBusy = 0;
        maxQueueLength = 0;
        sumResponseTime = 0;
        numberOfDepartures = 0;
        longService = 0;

        Event evt = new Event(arrival, exponential(stream, meanInterArrivalTime));
        futureEventList.enqueue(evt);
    }

    public static void processArrival(Event evt) {

        Event nextArrival = new Event(arrival, (clock + exponential(stream, meanInterArrivalTime)));
        futureEventList.enqueue(nextArrival);

        customers.enqueue(evt);
        queueLength++;
        if (numberInService == 0) {
            scheduleDeparture();
        } else {
            totalBusy += (clock - lastEventTime);
        }
        if (maxQueueLength < queueLength) {
            maxQueueLength = queueLength;
        }

        lastEventTime = clock;
    }

    public static void scheduleDeparture() {
        double serviceTime;
        while ((serviceTime = normal(stream, meanServiceTime, SIGMA)) < 0);
        Event depart = new Event(departure, clock + serviceTime);
        futureEventList.enqueue(depart);
        numberInService = 1;
        queueLength--;

    }

    public static void processDeparture(Event e) {
        Event finished = (Event) customers.dequeue();
        if (queueLength > 0) {
            scheduleDeparture();
        } else {
            numberInService = 0;
        }
        double response = (clock - finished.getTime());
        sumResponseTime += response;
        if (response > 4.0) {
            longService++;
        }
        totalBusy += (clock - lastEventTime);
        numberOfDepartures++;
        lastEventTime = clock;
    }

    public static void reportGeneration() {
        double RHO = totalBusy / clock;
        double AVGR = sumResponseTime / totalCustomers;
        double PC4 = ((double) longService) / totalCustomers;

        System.out.println();

        System.out.println("                          ***REPORT***");

        System.out.println("#SINGLE SERVER QUEUE SIMULATION - GROCERY STORE CHECKOUT COUNTER#");
        System.out.println("\tMEAN INTER-ARRIVAL TIME                :        " + meanInterArrivalTime);
        System.out.println("\tMEAN SERVICE TIME                      :        " + meanServiceTime);
        System.out.println("\tSTANDARD DEVIATION OF SERVICE TIMES    :        " + SIGMA);
        System.out.println("\tNUMBER OF CUSTOMERS SERVED             :        " + totalCustomers);
        System.out.println();
        System.out.println("\tSERVER UTILIZATION                     :        " + RHO);
        System.out.println("\tMAXIMUM LINE LENGTH                    :        " + maxQueueLength);
        System.out.println("\tAVERAGE RESPONSE TIME                  :        " + AVGR + " MINUTES");
        System.out.println("\tPROPORTION WHO SPEND FOUR ");
        System.out.println("\t\tMINUTES OR MORE IN SYSTEM      :        " + PC4);
        System.out.println("\tSIMULATION RUNLENGTH                   :        " + clock + " MINUTES");
        System.out.println("\tNUMBER OF DEPARTURES                   :        " + totalCustomers);
    }

    public static double exponential(Random rng, double mean) {
        return -mean * Math.log(rng.nextDouble());
    }
    public static double saveNormal;
    public static int numNormals = 0;
    public static final double PI = 3.1415927;

    public static double normal(Random rng, double mean, double sigma) {
        double returnNormal;
        if (numNormals == 0) {
            double r1 = rng.nextDouble();
            double r2 = rng.nextDouble();
            returnNormal = Math.sqrt(-2 * Math.log(r1)) * Math.cos(2 * PI * r2);
            saveNormal = Math.sqrt(-2 * Math.log(r1)) * Math.sin(2 * PI * r2);
            numNormals = 1;
        } else {
            numNormals = 0;
            returnNormal = saveNormal;
        }
        return returnNormal * sigma + mean;

    }
}