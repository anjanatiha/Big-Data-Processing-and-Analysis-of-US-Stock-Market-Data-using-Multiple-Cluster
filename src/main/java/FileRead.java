import java.io.*;

public class FileRead {
    private String inputFileName;
    private String outputFileName;
    private File outputFile;
    private PrintStream outputStream;
    private int count;
    FileRead(String inputFileName, String count){
        this.inputFileName = inputFileName;
        String outputFileName = inputFileName+"_sample"+".txt";
        outputFile = new File(outputFileName);
        try {
            outputStream = new PrintStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            printFileLine(Integer.parseInt(count));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void printFile(int start, int end) throws IOException {
        File inputFile = new File(inputFileName);
        byte[] inputBuffer = new byte[end+1];
        InputStream inputStream = new FileInputStream(inputFile);
        int bytesRead = inputStream.read(inputBuffer);
        String str = "";
        for(int i=start;i<=end;i++){
            System.out.println(inputBuffer[i]);
        }
        System.out.println(str);

    }
    public void printFileLine(int lineCount) throws IOException {
        FileInputStream fstream = new FileInputStream(inputFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        int k=0;
        StringBuilder str = new StringBuilder();
        while ((strLine = br.readLine()) != null) {
            if(k>0) {
                System.out.println(strLine);
                str.append(strLine + "\n");
                lineCount--;
                if (lineCount < 0)
                    break;
            }
            k++;
        }

        br.close();
        outputStream.append(str);
        outputStream.close();
    }

}
