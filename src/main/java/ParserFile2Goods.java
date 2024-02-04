import javax.swing.*;
import java.util.*;

public class ParserFile2Goods {
    private final ExcelReader reader;
    private final List<Goods> goods;
    private int lengthData;
    private final JTextArea log;
    private final String file;
    private HashMap<Integer, List<Object>> map;

    public ParserFile2Goods(String file, JTextArea log) throws ExceptionParseFile2Goods {
        this.file = file;
        this.log = log;
        lengthData = 0;
        reader = new ExcelReader();
        goods = new ArrayList<>();
        read();
    }
    private void read() throws ExceptionParseFile2Goods {
        try {
            reader.read(file);
            map = reader.getData();
            lengthData = map.get(0).size();
            extractData();
        }
        catch (ExceptiionReadExcellFile exceptiionReadExcellFile) {
            log.append("Ошибка чтения файла с данными");
        }
    }
    private void extractData() throws ExceptionParseFile2Goods {
        if (lengthData > 4) {
            log.append("В файле больше данных чем нужно, но файл будет обработан, проверьте корректность вывода данных");
        }
        for (Map.Entry<Integer, List<Object>> pair : map.entrySet()) {
            switch (lengthData) {
                case 0 -> {
                    log.append("В файле нет данных для обработки");
                    throw new ExceptionParseFile2Goods("В файле нет данных для обработки");
                }
                case 1 -> goods.add(new Goods(String.valueOf(pair.getValue().get(0))));
                case 2 -> goods.add(new Goods(String.valueOf(pair.getValue().get(0)), String.valueOf(pair.getValue().get(1))));
                case 3 -> goods.add(new Goods(String.valueOf(pair.getValue().get(0)), String.valueOf(pair.getValue().get(1)), String.valueOf(pair.getValue().get(2))));
                default -> goods.add(new Goods(String.valueOf(pair.getValue().get(0)), String.valueOf(pair.getValue().get(1)), String.valueOf(pair.getValue().get(2)), String.valueOf(pair.getValue().get(3))));
            }
        }
    }
    public List<Goods> getGoods() {
        return goods;
    }
}
