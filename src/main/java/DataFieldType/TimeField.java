package DataFieldType;

import java.io.Serializable;

/**
 * Created by Anjana on 5/29/2017.
 */
public class TimeField implements IFieldType, Serializable {
    private int length;

    TimeField(int length) {
        this.length = length;
    }

    public String convertFromBinary(byte[] buffer, int offset) {
        StringBuilder stringBuilder = new StringBuilder();
        char temp;
        for (int i = offset; i < offset + length; i++) {
            temp = (char) buffer[i];
            stringBuilder.append(temp);

        }
//        System.out.println("time ->" + stringBuilder.toString() + " offset " + offset);

        return stringBuilder.toString();

    }

    public String convertFromBinary(String line, int offset) {
        char[] buffer = line.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        char temp;
        for (int i = offset; i < offset + length; i++) {
            temp = (char) buffer[i];
            stringBuilder.append(temp);
        }
//        System.out.println("time ->" + stringBuilder.toString() + " offset " + offset);
        return stringBuilder.toString();
    }

    public int getLength() {
        return length;
    }
}
