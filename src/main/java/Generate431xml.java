import javax.swing.*;
import java.util.List;
// класс генерирует xml для перемещения товара между местами деятельности внутри организации
public class Generate431xml {
    private final StringBuilder xml;
    public Generate431xml(String senderMD, String receiverMD, String dateOperate, String docNum, String docDate, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<documents version=\"1.38\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
        xml.append("  <move_place action_id=\"431\">\n");
        xml.append("    <subject_id>" + senderMD + "</subject_id>\n");
        xml.append("    <receiver_id>" + receiverMD + "</receiver_id>\n");
        xml.append("    <operation_date>" + dateOperate + "</operation_date>\n");
        xml.append("    <doc_num>" + docNum + "</doc_num>\n");
        xml.append("    <doc_date>" + docDate + "</doc_date>\n");
        xml.append("    <order_details>\n");
        for (Goods item : goods) {
            xml.append("      <sgtin>").append(item.getSgtin()).append("</sgtin>\n");
            if (!item.getName().equals("no name")) {
                log.append("\nОбработан " + item.getName());
            }
        }
        xml.append("    </order_details>\n");
        xml.append("  </move_place>\n");
        xml.append("</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
