package Misc;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static Misc.Print.print;

/**
 * Created by anjana on 7/17/17.
 */
public class UnZip
{
    List<String> fileList;
    private static final String INPUT_ZIP_FILE = "/home/anjana/Downloads/EQY_US_ALL_TRADE_20100506.zip";
    private static final String OUTPUT_FOLDER = "/home/anjana/Downloads/EQY_US_ALL_TRADE_20100506";

    public static void main( String[] args )
    {
        UnZip unZip = new UnZip();
        unZip.unZipIt(INPUT_ZIP_FILE,OUTPUT_FOLDER);
    }

//    public File unZipIt(String zipFile, String outputFolder){
//
//        byte[] buffer = new byte[1024];
//
//        try{
//            File folder = new File(OUTPUT_FOLDER);
//            if(!folder.exists()){
//                folder.mkdir();
//            }
//
//            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
//            ZipEntry ze = zis.getNextEntry();
//
//            while(ze!=null){
//
//                String fileName = ze.getName();
//                File newFile = new File(outputFolder + File.separator + fileName);
//
//                System.out.println("file unzip : "+ newFile.getAbsoluteFile());
//
//                new File(newFile.getParent()).mkdirs();
//
//                FileOutputStream fos = new FileOutputStream(newFile);
//
//                int len;
//                while ((len = zis.read(buffer)) > 0) {
//                    fos.write(buffer, 0, len);
//                }
//
//                fos.close();
//                ze = zis.getNextEntry();
//            }
//
//            zis.closeEntry();
//            zis.close();
//
//            System.out.println("Unzip Done");
//            return folder;
//
//        }catch(IOException ex){
//            ex.printStackTrace();
//        }
//        return null;
//    }
    public void unZipIt(String zipFile, String outputFileName){
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
                    while (line != null) {
                        strBuilder.append(line);
                        strBuilder.append("\r\n");
                        if (k % 10000000==0) {
                            outputStream.print(strBuilder);
                            outputStream.flush();
                            strBuilder.setLength(0);
                            print("Reached Line: "+k);
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


    public static void mkdir(String dirName)
    {
        File dir = new File(dirName);

        // attempt to create the directory here
        boolean successful = dir.mkdir();
        if (successful)
        {
            // creating the directory succeeded
            System.out.println("directory was created successfully");
        }
        else
        {
            // creating the directory failed
            System.out.println("failed trying to create the directory");
        }
    }

}
