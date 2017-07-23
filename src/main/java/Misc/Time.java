package Misc;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anjana on 7/16/17.
 */
public class Time {
    public static void printTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");
        String formattedDate = dateFormat.format(new Date()).toString();
        System.out.println(formattedDate);
    }

    public static void printElapsedTime(long startTime, long endTime) {
        long totalTime = endTime - startTime;
        int seconds = (int) ((totalTime / 1000) % 60);
        int minutes = (int) ((totalTime / 1000) / 60);
        System.out.println("\n\nTotal Time: " + minutes + ":" + seconds);
    }
    public static String printElapsedTime(long startTime, long endTime, String str) {
        long totalTime = endTime - startTime;
        int seconds = (int) ((totalTime / 1000) % 60);
        int minutes = (int) ((totalTime / 1000) / 60);
        String min = String.valueOf(minutes);
        String sec = String.valueOf(seconds);
        if(sec.length()==1)
            sec = "0"+sec;
        String strTime = minutes + ":" + seconds;
        System.out.println("\n\nTotal Time for "+str+ " : " + minutes + ":" + seconds);
        return strTime;
    }
    public static void decipherTime(String time){
        String year=time.substring(time.length()-4, time.length());
        String month=time.substring(time.length()-6, time.length()-4);

    }
}
