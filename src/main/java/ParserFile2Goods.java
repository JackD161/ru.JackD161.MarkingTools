import javax.swing.*;
import java.util.*;

public class ParserFile2Goods {
    private final ExcelReader reader;
    private final ArrayList<Goods> goods;
    private int lengthData;
    private final JTextArea log;
    private HashMap<Integer, List<Object>> map;

    public ParserFile2Goods(JTextArea log) {
        this.log = log;
        lengthData = 0;
        reader = new ExcelReader();
        goods = new ArrayList<>();
        map = new HashMap<>();
    }
    public ArrayList<Goods> read(String file) throws ExceptionParseFile, ExceptiionReadExcellFile {
        reader.read(file);
        map = reader.getData();
        lengthData = map.get(0).size();
        extractData();
        return goods;
    }
    public void clear() {
        goods.clear();
        map.clear();
    }
    private void extractData() throws ExceptionParseFile {
        if (lengthData > 4) {
            log.append("\nВ файле больше данных чем нужно, но файл будет обработан, проверьте корректность вывода данных");
        }
        for (Map.Entry<Integer, List<Object>> pair : map.entrySet()) {
            String sgtin = "";
            switch (lengthData) {
                case 0 -> {
                    log.append("\nВ файле нет данных для обработки");
                    throw new ExceptionParseFile("В файле нет данных для обработки");
                }
                case 1 -> {
                    sgtin = String.valueOf(pair.getValue().get(0));
                    if (sgtin.startsWith("01"))
                        sgtin = ParserData.ejectSgtin(sgtin);
                    if (checkDublicateSgtin(sgtin))
                        goods.add(new Goods(sgtin));

                }
                case 2 -> {
                    sgtin = String.valueOf(pair.getValue().get(0));
                    if (sgtin.startsWith("01"))
                        sgtin = ParserData.ejectSgtin(sgtin);
                    if (checkDublicateSgtin(sgtin))
                        goods.add(new Goods(sgtin, String.valueOf(pair.getValue().get(1))));
                }
                case 3 -> {
                    sgtin = String.valueOf(pair.getValue().get(0));
                    if (sgtin.startsWith("01"))
                        sgtin = ParserData.ejectSgtin(sgtin);
                    if (checkDublicateSgtin(sgtin))
                        goods.add(new Goods(sgtin, String.valueOf(pair.getValue().get(1)), String.valueOf(pair.getValue().get(2))));
                }
                default -> {
                    sgtin = String.valueOf(pair.getValue().get(0));
                    if (sgtin.startsWith("01"))
                        sgtin = ParserData.ejectSgtin(sgtin);
                    if (checkDublicateSgtin(sgtin))
                        goods.add(new Goods(sgtin, String.valueOf(pair.getValue().get(1)), String.valueOf(pair.getValue().get(2)), String.valueOf(pair.getValue().get(3))));
                }
            }
        }
    }
    private boolean checkDublicateSgtin(String sgtin) {
        int cnt = 0;
        for (Goods goodsCh : goods) {
            if (goodsCh.getSgtin().equals(sgtin))
                cnt++;
        }
        return cnt == 0;
    }
}
