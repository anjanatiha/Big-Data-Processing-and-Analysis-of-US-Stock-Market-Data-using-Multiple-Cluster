package Misc;

import static Misc.FileClass.printFileLine;
import static Misc.FileClass.readDirectory;

/**
 * Created by anjana on 7/16/17.
 */
public class Test {
    public static void main(String[] args){
        readDirectory("/home/anjana/Downloads/");
        printFileLine(args[0], Integer.parseInt(args[1]));

    }

}
