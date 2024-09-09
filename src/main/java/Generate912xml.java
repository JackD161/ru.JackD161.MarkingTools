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
            log.append("\nОбработано " + sscc);
        }
                xml.append("  </unit_unpack>\n" +
                "</documents>");
    }
    private HashSet<String> collectSSCC(HashMap<Integer, List<Object>> mapSSCC) throws ExceptionParseFile {
        HashSet<String> set = new HashSet<>();
        for (Map.Entry<Integer, List<Object>> pair : mapSSCC.entrySet()) {
            if ((pair.getValue()).get(0).toString().length() == 18) {
                set.add((pair.getValue()).get(0).toString());
            }
            else {
                throw new ExceptionParseFile("Код не соответствует транспортной упаковке '[0-9]{18}");
            }
        }
        return set;
    }
    public String getXML() {
        return xml.toString();
    }
}
