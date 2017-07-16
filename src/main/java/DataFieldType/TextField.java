package DataFieldType;

import java.io.Serializable;

/**
 * Created by Anjana on 5/29/2017.
 */
public class TextField implements IFieldType, Serializable {
    private int length;
    TextField(int length){
        this.length = length;
    }
    public int getLength(){
        return length;
    }

    public String convertFromBinary(byte[] buffer, int offset){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=offset;i<offset+length;i++){
            stringBuilder.append((char) (buffer[i] & 0x00ff));
        }
//        System.out.println("text ->" +stringBuilder.toString().trim() + " offset " + offset);

        return stringBuilder.toString().trim();
    }
    public String convertFromBinary(String line, int offset){
        char[] buffer = line. toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=offset;i<offset+length;i++){
            stringBuilder.append((char) (buffer[i] & 0x00ff));
        }
//        System.out.println("text ->" + stringBuilder.toString().trim() + " offset " + offset);
        return stringBuilder.toString().trim();
    }
}
