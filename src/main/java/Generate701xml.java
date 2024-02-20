import javax.swing.*;
import java.util.List;
// класс генерирует xml для подтверждения отгрузки/приемки товара
public class Generate701xml {
    private final StringBuilder xml;

    public Generate701xml(String senderMD, String receiverMD, String dateOperate, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<documents xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.36\">\n" +
                "  <accept action_id=\"701\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <counterparty_id>" + receiverMD + "</counterparty_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <order_details>\n");
        for (Goods item : goods) {
            xml.append("      <sgtin>").append(item.getSgtin()).append("</sgtin>\n");
            log.append("\nОбработано " + item.getName());
        }
        xml.append("    </order_details>\n" +
                "  </accept>\n" +
                "</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
