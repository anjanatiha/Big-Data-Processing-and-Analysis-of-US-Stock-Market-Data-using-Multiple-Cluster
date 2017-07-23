package Misc;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static Misc.Debug.debug;
import static Misc.Print.print;

public class FileClass {

    public static void printFileLine(String inputFileName, int lineCount){
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(inputFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            int k = 0;
            StringBuilder str = new StringBuilder();
            while ((strLine = br.readLine()) != null) {
                if (k >= 0) {
                    System.out.println(strLine);
                    str.append(strLine + "\n");
                    lineCount--;
                    if (lineCount < 0)
                        break;
                }
                k++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File[] readDirectory(String directory){

        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        return listOfFiles;
    }
    public static void deleteFileOrDir(String path){
        File file = new File(path);
        file.delete();
    }
    public static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        System.out.println("deleted directory");
        return(directory.delete());
    }

    public static String getFirstLineUnzipped(String inputFileName){
        FileInputStream fstream = null;
        String firstLine="";
        try {
            fstream = new FileInputStream(inputFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            firstLine = br.readLine();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return firstLine;
    }
    public static String getFirstLineZip(String inputZipFileName){
        String line = "";
        BufferedReader br = null;
        try {
            ZipFile zf = new ZipFile(inputZipFileName);
            Enumeration entries = zf.entries();

            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                long size = ze.getSize();
                if (size > 0) {
                    System.out.println("Length is " + size);
                    br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    line = br.readLine();

                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            if(br!=null)
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

        }
        return line;

    }

    public static void FileSampleWrite(String inputFileName, String count) {
        FileInputStream inputStream;
        String outputFileName;
        File outputFile;
        PrintStream outputStream;
        BufferedReader br;
        try {
            inputStream = new FileInputStream(inputFileName);
            outputFileName = inputFileName + "_sample";
            outputFile = new File(outputFileName);
            outputStream = new PrintStream(outputFile);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            int lastindex = Integer.parseInt(count);
            int k = 0;
            StringBuilder stringBuilder = new StringBuilder();
            int bufferSize = 100000;
            while ((strLine = br.readLine()) != null) {

                if (k > lastindex)
                    break;
                else {
                    stringBuilder.append(strLine);
                    if (k < lastindex)
                        stringBuilder.append('\n');
                    k++;
                    if (k > bufferSize) {
                        outputStream.print(stringBuilder);
                        outputStream.flush();
                        stringBuilder.setLength(0);
                        if (k%bufferSize==0)
                            print(k);
                    }
                }
            }
            outputStream.print(stringBuilder);
            outputStream.flush();
            stringBuilder.setLength(0);
            inputStream.close();
            br.close();
            outputStream.close();
            debug("end");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getFileType(String directory){

        if ((directory.substring(directory.length()-5, directory.length()-1))=="NBBO"){
            return "nbbo";
        }

        else if ((directory.substring(directory.length()-6, directory.length()))=="Quote"){
            return "quote";
        }

        else if ((directory.substring(directory.length()-6, directory.length()))=="Trade"){
            return "trade";
        }
        else return "None";
    }

    public static void mkdir(String dirName)
    {
        File dir = new File(dirName);
        boolean successful = dir.mkdir();
        if (successful)
        {
            System.out.println("directory was created successfully");
        }
        else
        {
            System.out.println("failed trying to create the directory");
        }
    }
    public static void unZip(String zipFile, String outputFileName){
        try {
            ZipFile zf=new ZipFile(zipFile);
            File outputFile = new File(outputFileName);
            PrintWriter outputStream = new PrintWriter(outputFile);
            StringBuilder strBuilder = new StringBuilder();
            Enumeration entries = zf.entries();
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                long size = ze.getSize();
                if (size > 0) {
                    System.out.println("Length is " + size);
                    BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line;
                    int k = 0;
                    line = br.readLine();
                    line = br.readLine();
                    print("Extracting zip file...");
                    while (line != null) {
                        strBuilder.append(line);
                        strBuilder.append("\r\n");
                        if (k % 10000000==0) {
                            outputStream.print(strBuilder);
                            outputStream.flush();
                            strBuilder.setLength(0);
                            print("On Line: "+k);
                        }
                        k++;
                        line = br.readLine();
                    }
                    outputStream.print(strBuilder);
                    outputStream.flush();
                    strBuilder.setLength(0);
                    print("ends: line: "+k);
                    br.close();
                    outputStream.close();

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        deleteFileOrDir("/home/anjana/Downloads/DATA/a");
    }
}
