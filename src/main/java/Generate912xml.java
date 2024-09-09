import javax.swing.*;
import java.util.*;

// класс генерирует документ xml для вывода из оборота товара по льготному рецепту
public class Generate912xml {
    private final StringBuilder xml;

    public Generate912xml(String senderMD, String dateOperate, HashMap<Integer, List<Object>> mapSSCC, JTextArea log) throws ExceptionParseFile {
        xml = new StringBuilder();
        HashSet<String> set = collectSSCC(mapSSCC);
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<documents version=\"1.38\">\n" +
                "  <unit_unpack action_id=\"912\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n");
        for (String sscc : set) {
            xml.append("    <sscc>" + sscc + "</sscc>\n");
            log.append("Обработано " + sscc);
        }
                xml.append("  </unit_unpack>\n" +
                "</documents>");
    }
    private HashSet<String> collectSSCC(HashMap<Integer, List<Object>> mapSSCC) throws ExceptionParseFile {
        HashSet<String> set = new HashSet<>();
        for (Map.Entry<Integer, List<Object>> pair : mapSSCC.entrySet()) {
            String sscc = pair.getValue().toString();
            if (sscc.startsWith("00")) {
                set.add(pair.getValue().toString());
            }
            else {
                System.err.println("Код не соответствует транспортной упаковке");
                throw new ExceptionParseFile("Код не соответствует транспортной упаковке");
            }
        }
        return set;
    }
    public String getXML() {
        return xml.toString();
    }
}
