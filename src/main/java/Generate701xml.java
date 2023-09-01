import java.util.HashMap;
import java.util.List;
import java.util.Map;
// класс генерирует xml для подтверждения отгрузки/приемки товара
public class Generate701xml {
    private final String senderMD;
    private final String receiverMD;
    private final String dateOperate;
    private final HashMap<Integer, List<Object>> mapSgtin;
    private final StringBuilder xml;

    public Generate701xml(String senderMD, String receiverMD, String dateOperate, HashMap<Integer, List<Object>> mapSgtin) {
        this.senderMD = senderMD;
        this.receiverMD = receiverMD;
        this.dateOperate = dateOperate;
        this.mapSgtin = mapSgtin;
        this.xml = new StringBuilder();
        generate();
    }
    private void generate() {
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<documents xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.36\">\n" +
                "  <accept action_id=\"701\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <counterparty_id>" + receiverMD + "</counterparty_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <order_details>\n");
        for (Map.Entry<Integer, List<Object>> pair : mapSgtin.entrySet()) {
            xml.append("      <sgtin>").append(pair.getValue().get(0)).append("</sgtin>\n");
        }
        xml.append("    </order_details>\n" +
                "  </accept>\n" +
                "</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
