package ssq;
import java.util.*;

public class Sim {
    public static long runtime;
    public static double Clock,
            MeanInterArrivalTime,
            MeanServiceTime,
            SIGMA,
            LastEventTime,
            TotalBusy,
            MaxQueueLength,
            SumResponseTime;
    public static long QueueLength,
            NumberInService,
            TotalCustomers,
            NumberOfDepartures,
            LongService;
    public final static int arrival = 1;
    public final static int departure = 2;
    public static EventList futureEventList;
    public static Queue customers;
    public static Random stream;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Mean Inter-arrival Time: ");
        MeanInterArrivalTime = sc.nextDouble();
        System.out.print("Mean Service Time: ");
        MeanServiceTime = sc.nextDouble();
        System.out.print("Standard Deviation: ");
        SIGMA = sc.nextDouble();
        System.out.print("Total Number Of Customers: ");
        TotalCustomers = sc.nextInt();

        Date start = new Date();

        long seed = 1000; //Long.parseLong(argv[0]);
        stream = new Random(seed);
        futureEventList = new EventList();
        customers = new Queue();

        initialization();

        while (NumberOfDepartures < TotalCustomers) {
            Event evt = (Event) futureEventList.getMin();
            futureEventList.dequeue();
            Clock = evt.getTime();
            if (evt.getType() == arrival) {
                processArrival(evt);
            } else {
                processDeparture(evt);
            }
        }
        Date finished = new Date();
        runtime = finished.getTime() - start.getTime();
        PdfDoc.reportGeneration();
    }

    public static void initialization() {

        Clock = 0.0;
        QueueLength = 0;
        NumberInService = 0;
        LastEventTime = 0.0;
        TotalBusy = 0;
        MaxQueueLength = 0;
        SumResponseTime = 0;
        NumberOfDepartures = 0;
        LongService = 0;

        Event evt = new Event(arrival, exponential(stream, MeanInterArrivalTime));
        futureEventList.enqueue(evt);
    }

    public static void processArrival(Event evt) {

        Event nextArrival = new Event(arrival, (Clock + exponential(stream, MeanInterArrivalTime)));
        futureEventList.enqueue(nextArrival);

        customers.enqueue(evt);
        QueueLength++;
        if (NumberInService == 0) {
            scheduleDeparture();
        } else {
            TotalBusy += (Clock - LastEventTime);
        }
        if (MaxQueueLength < QueueLength) {
            MaxQueueLength = QueueLength;
        }

        LastEventTime = Clock;
    }

    public static void scheduleDeparture() {
        double serviceTime;
        while ((serviceTime = normal(stream, MeanServiceTime, SIGMA)) < 0);
        Event depart = new Event(departure, Clock + serviceTime);
        futureEventList.enqueue(depart);
        NumberInService = 1;
        QueueLength--;

    }

    public static void processDeparture(Event e) {
        Event finished = (Event) customers.dequeue();
        if (QueueLength > 0) {
            scheduleDeparture();
        } else {
            NumberInService = 0;
        }
        double response = (Clock - finished.getTime());
        SumResponseTime += response;
        if (response > 4.0) {
            LongService++;
        }
        TotalBusy += (Clock - LastEventTime);
        NumberOfDepartures++;
        LastEventTime = Clock;
    }

    public static double exponential(Random rng, double mean) {
        return -mean * Math.log(rng.nextDouble());
    }

    public static double SaveNormal;
    public static int NumNormals = 0;
    public static final double PI = 3.1415927;

    public static double normal(Random rng, double Mean, double Sigma) {
        double ReturnNormal;
        if (NumNormals == 0) {
            double r1 = rng.nextDouble();
            double r2 = rng.nextDouble();
            ReturnNormal = Math.sqrt(-2 * Math.log(r1)) * Math.cos(2 * PI * r2);
            SaveNormal = Math.sqrt(-2 * Math.log(r1)) * Math.sin(2 * PI * r2);
            NumNormals = 1;
        } else {
            NumNormals = 0;
            ReturnNormal = SaveNormal;
        }
        return ReturnNormal * Sigma + Mean;

    }
}