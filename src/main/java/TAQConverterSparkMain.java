import java.io.IOException;
import java.io.Serializable;

public class TAQConverterSparkMain implements Serializable{
    public static void main(String[] args) throws IOException {
        TAQ2010Spec a = new TAQ2010Spec();
        IFieldType[] fieldtype= a.getTradeFields();
        TAQConverterSparkFN TAQConverterSparkObject = new TAQConverterSparkFN(args[0], fieldtype, 1);
        TAQConverterSparkObject.convertFile();

    }
}
