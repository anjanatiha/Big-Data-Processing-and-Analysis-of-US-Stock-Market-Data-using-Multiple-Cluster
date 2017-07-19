package Misc;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static Misc.Debug.debug;
import static Misc.Print.print;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

public class FileClass {
    private static String inputFileName;

    private int count;

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


//    public static void printFile(int start, int end) throws IOException {
//        File inputFile = new File(inputFileName);
//        byte[] inputBuffer = new byte[end + 1];
//        InputStream inputStream = new FileInputStream(inputFile);
//        int bytesRead = inputStream.read(inputBuffer);
//        String str = "";
//        for (int i = start; i <= end; i++) {
//            System.out.println(inputBuffer[i]);
//        }
//        System.out.println(str);
//
//    }

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


    public static void deleteFileOrFolder(final Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                    throws IOException {
                Files.delete(file);
                return CONTINUE;
            }

            @Override public FileVisitResult visitFileFailed(final Path file, final IOException e) {
                return handleException(e);
            }

            private FileVisitResult handleException(final IOException e) {
                e.printStackTrace();
                return TERMINATE;
            }

            @Override public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
                    throws IOException {
                if(e!=null)return handleException(e);
                Files.delete(dir);
                return CONTINUE;
            }
        });
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
    public static String getFirstLine(String inputFileName){
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
    public static String getYear(String inputZipFileName){
        String year = "";
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
                    String line = br.readLine();
                    year = line.substring(2,10);
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
        print(year);
        return year;

    }
}
