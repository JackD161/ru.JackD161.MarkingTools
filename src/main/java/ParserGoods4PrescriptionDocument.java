import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserGoods4PrescriptionDocument {
    private final HashMap<String, PrescriptionDocument> prescriptionDocumentsMap;

    public ParserGoods4PrescriptionDocument(String fileName, JTextArea log) {
        this.prescriptionDocumentsMap = new HashMap<>();
        parseFile(fileName, log);
    }

    private void parseFile(String fileName, JTextArea log) {
        ExcelReader reader = new ExcelReader();
        HashMap<Integer, List<Object>> map;
        try {
            reader.read(fileName);
            map = reader.getData();
            for (Map.Entry<Integer, List<Object>> pair : map.entrySet()) {
                PrescriptionGoods goods = getPrescriptionGoodsFromRow(pair.getValue());
                if (checkSgtin(prescriptionDocumentsMap, goods.getSgtin())) {
                    if (prescriptionDocumentsMap.containsKey(goods.getPrescriptionName())) {
                        prescriptionDocumentsMap.get(goods.getPrescriptionName()).addGoods(goods);
                    } else {
                        prescriptionDocumentsMap.put(goods.getPrescriptionName(), new PrescriptionDocument(goods));
                    }
                }
            }
        }
        catch (ExceptiionReadExcellFile e) {
            log.append(e.toString());
        }
    }

    private PrescriptionGoods getPrescriptionGoodsFromRow(List<Object> list) {
        int length = list.size();
        return switch (length) {
            case 4 -> new PrescriptionGoods(list.get(0).toString(), list.get(1).toString(), list.get(2).toString(), list.get(3).toString());
            default -> new PrescriptionGoods(list.get(0).toString(), list.get(1).toString(), list.get(2).toString(), list.get(3).toString(), list.get(4).toString(), list.get(5).toString(), list.get(6).toString());
            };
        }
    private boolean checkSgtin(HashMap<String, PrescriptionDocument> map, String sgtin) {
        int cnt = 0;
        for (Map.Entry<String, PrescriptionDocument> pair : map.entrySet()) {
            List<PrescriptionGoods> goodsList = pair.getValue().getGoodsList();
            for (PrescriptionGoods goods : goodsList) {
                if (goods.getSgtin().equals(sgtin))
                    cnt++;
            }
        }
        return cnt == 0;
    }
    public HashMap<String, PrescriptionDocument> getPrescriptingMap() {
        return prescriptionDocumentsMap;
    }
}
