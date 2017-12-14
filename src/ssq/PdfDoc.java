package ssq;

import java.io.*;
import java.text.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PdfDoc {

    public static float posX = 318;
    public static float posY = 523;
    public static PDPageContentStream contentStream;

    public static void reportGeneration() {

        double RHO = Sim.totalBusy / Sim.clock;
        double AVGR = Sim.sumResponseTime / Sim.totalCustomers;
        double PC4 = ((double) Sim.longService) / Sim.totalCustomers;

        //Saving the document
        try {

            //Loading an existing document
            File file = new File("A:\\Git\\ssq\\demo.pdf");
            PDDocument document = PDDocument.load(file);

            System.out.println("PDF loaded");


            //Retrieving the pages of the document
            PDPage page = document.getPage(0);
            contentStream = new PDPageContentStream(document, page, true, true);

            //Setting the font to the Content stream
            contentStream.setFont(PDType1Font.TIMES_BOLD, 16);
            contentStream.setNonStrokingColor(0,0,0);


            Date now = new Date();
            DateFormat df1 = DateFormat.getDateInstance(DateFormat.SHORT);
            String s1 = df1.format(now);
            //Begin the Content stream
            contentStream.beginText();
            //Setting the position for the line
            contentStream.newLineAtOffset(125, 697);
            //Adding text in the form of string
            contentStream.showText(s1);
            //Ending the content stream
            contentStream.endText();

            // MEAN INTER-ARRIVAL TIME
            writeText(Double.toString( Sim.meanInterArrivalTime));
            // MEAN SERVICE TIME
            writeText(Double.toString(Sim.meanServiceTime));
            // STANDARD DEVIATION OF SERVICE TIMES
            writeText(Double.toString(Sim.SIGMA));
            // NUMBER OF CUSTOMERS SERVED
            writeText(Double.toString((int) Sim.totalCustomers));
            // SERVER UTILIZATION
            writeText(Double.toString(RHO));
            // MAXIMUM LINE LENGTH
            writeText(Double.toString(Sim.maxQueueLength));
            // AVERAGE RESPONSE TIME
            writeText(Double.toString(AVGR));
            // PROPORTION WHO SPEND FOUR MINUTES OR MORE IN SYSTEM
            writeText(Double.toString(PC4));
            // SIMULATION RUN LENGTH
            writeText(Double.toString(Sim.clock));
            // NUMBER OF DEPARTURES
            writeText(Double.toString((int) Sim.numberOfDepartures));



            //Begin the Content stream
            contentStream.beginText();
            //Setting the position for the line
            contentStream.newLineAtOffset(359, 104);
            //Adding text in the form of string
            contentStream.showText(Double.toString(Sim.runtime));
            //Ending the content stream
            contentStream.endText();


            //Ending the content stream
            contentStream.close();
            //Saving the document
            document.save("A:\\Git\\ssq\\Report-1.pdf");

            //Closing the document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println();

        System.out.println("                          ***REPORT***");

        System.out.println("#SINGLE SERVER QUEUE SIMULATION - GROCERY STORE CHECKOUT COUNTER#");
        System.out.println("\tMEAN INTER-ARRIVAL TIME                :        " + Sim.meanInterArrivalTime);
        System.out.println("\tMEAN SERVICE TIME                      :        " + Sim.meanServiceTime);
        System.out.println("\tSTANDARD DEVIATION OF SERVICE TIMES    :        " + Sim.SIGMA);
        System.out.println("\tNUMBER OF CUSTOMERS SERVED             :        " + Sim.totalCustomers);
        System.out.println();
        System.out.println("\tSERVER UTILIZATION                     :        " + RHO);
        System.out.println("\tMAXIMUM LINE LENGTH                    :        " + Sim.maxQueueLength);
        System.out.println("\tAVERAGE RESPONSE TIME                  :        " + AVGR + " MINUTES");
        System.out.println("\tPROPORTION WHO SPEND FOUR ");
        System.out.println("\t\tMINUTES OR MORE IN SYSTEM            :        " + PC4);
        System.out.println("\tSIMULATION RUNLENGTH                   :        " + Sim.clock + " MINUTES");
        System.out.println("\tNUMBER OF DEPARTURES                   :        " + Sim.totalCustomers);

    }

    static void writeText(String str) throws IOException {
        posY -= 36.5;

        //Begin the Content stream
        contentStream.beginText();
        //Setting the position for the line
        contentStream.newLineAtOffset(posX, posY);
        //Adding text in the form of string
        contentStream.showText(str);
        //Ending the content stream
        contentStream.endText();
    }

}
