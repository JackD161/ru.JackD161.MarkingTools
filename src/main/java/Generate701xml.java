import javax.swing.*;
import java.util.List;

public class Generate701xml {
    private final StringBuilder xml;

    public Generate701xml(String senderMD, String receiverMD, String dateOperate, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        xml.append("<documents xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.36\">\n");
        xml.append("  <accept action_id=\"701\">\n");
        xml.append("    <subject_id>" + senderMD + "</subject_id>\n");
        xml.append("    <counterparty_id>" + receiverMD + "</counterparty_id>\n");
        xml.append("    <operation_date>" + dateOperate + "</operation_date>\n");
        xml.append("    <order_details>\n");
        for (Goods item : goods) {
            xml.append("      <sgtin>").append(item.getSgtin()).append("</sgtin>\n");
            if (!item.getName().equals("no name")) {
                log.append("\nОбработан " + item.getName());
            }
        }
        xml.append("    </order_details>\n");
        xml.append("  </accept>\n");
        xml.append("</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
