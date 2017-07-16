package Misc;

/**
 * Created by anjana on 7/16/17.
 */
public class Debug {
    public static void debug(){
        System.out.println("\n\n\n->debug<-\n\n\n");
    }
    public static void debug(int n){
        for (int i =0;i<n;i++){
            System.out.println("\n");
        }
        for (int i =0;i<n;i++){
            System.out.println("->debug-<");
        }
        for (int i =0;i<n;i++){
            System.out.println("\n");
        }
    }
    public static void debug(String str, int n){
        for (int i =0;i<n;i++){
            System.out.println("\n");
        }
        for (int i =0;i<n;i++){
            System.out.println(str);
        }

        for (int i =0;i<n;i++){
            System.out.println("\n");
        }
    }
    public static void debug(String str){
        System.out.println("\n\n\ndebug->"+str+"\n\n\n");
    }

}
