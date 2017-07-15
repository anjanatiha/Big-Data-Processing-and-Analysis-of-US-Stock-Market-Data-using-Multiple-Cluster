import java.io.Serializable;

/**
 * Created by Anjana on 5/29/2017.
 */
public class NumericField implements IFieldType,Serializable {
    private int length;
    private int numberOfDecimalDigits=0;
    private char type;

    public int getLength(){
        return length;
    }
    NumericField(int length){
        this.length=length;
    }

    NumericField(int length, int numberOfDecimalDigits){
        this.length=length;
        this.numberOfDecimalDigits=numberOfDecimalDigits;
    }
    public String convertFromBinary(byte[] buffer, int offset){
        long number = 0;
        if(type=='p'){
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=offset;i<offset+length;i++){
                stringBuilder.append((char) (buffer[i] & 0x00ff));
            }
            return stringBuilder.toString();
        }
        else if(type=='b') {
            return "";
        }
        else{
            for (int i = offset; i < offset + length; i++) {
                number = 10 * number + (buffer[i] & 0x00ff) - '0';
            }
            if (numberOfDecimalDigits == 0)
                return String.valueOf(number);
            double temp = (double) number / Math.pow(10, numberOfDecimalDigits);
//            System.out.println("number blank -> " + String.valueOf(temp) + " offset " + offset);

            return String.valueOf(temp);
        }

    }
    public String convertFromBinary(String line, int offset){
        char[] buffer = line. toCharArray();
        long number = 0;
        if(type!='p') {
            for (int i = offset; i < offset + length; i++) {
                number = 10 * number + (buffer[i] & 0x00ff) - '0';
            }
            if (numberOfDecimalDigits == 0)
                return String.valueOf(number);
            double temp = (double) number / Math.pow(10, numberOfDecimalDigits);
//            System.out.println("number ->" + String.valueOf(temp) + " offset " + offset);

            return String.valueOf(temp);
        }
        else{
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=offset;i<offset+length;i++){
                stringBuilder.append((char) (buffer[i] & 0x00ff));
            }
//            System.out.println("number ->" + stringBuilder.toString() + " offset " + offset);
            return stringBuilder.toString();
        }
    }

    NumericField(int length, char type){
        this.length=length;
        this.type=type;
    }
}
