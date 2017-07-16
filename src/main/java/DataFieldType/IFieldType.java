package DataFieldType;

import java.io.Serializable;

/**
 * Created by Anjana on 5/29/2017.
 */
public interface IFieldType extends Serializable {
    public String convertFromBinary(byte[] lineBuffer, int offset);
    public String convertFromBinary(String line, int offset);
    public int getLength();
}
