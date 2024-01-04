import javax.swing.*;
import java.util.*;

public class ParserFile2Goods {
    private ExcelReader reader;
    private List<Goods> goods;
    private int schema;
    private JTextArea log;
    private String file;
    private HashMap<Integer, List<Object>> map;

    public ParserFile2Goods(String file, int schema, JTextArea log) {
        this.file = file;
        this.log = log;
        this.schema = schema;
        reader = new ExcelReader();
        goods = new ArrayList<>();
        read();
    }
    private void read() {
        try {
            reader.read(file);
            map = reader.getData();
        }
        catch (ExceptiionReadExcellFile exceptiionReadExcellFile) {
            log.append("Ошибка чтения файла с данными");
        }
    }
    private void extractData(int schema) {

    }
}
