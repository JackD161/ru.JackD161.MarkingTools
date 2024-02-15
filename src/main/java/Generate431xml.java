import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// класс генерирует xml для перемещения товара между местами деятельности внутри организации
public class Generate431xml {
    private final StringBuilder xml;
    public Generate431xml(String senderMD, String receiverMD, String dateOperate, String docNum, String docDate, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<documents version=\"1.38\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <move_place action_id=\"431\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <receiver_id>" + receiverMD + "</receiver_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <doc_num>" + docNum + "</doc_num>\n" +
                "    <doc_date>" + docDate + "</doc_date>\n" +
                "    <order_details>\n");
        for (Goods item : goods) {
            xml.append("      <sgtin>").append(item.getSgtin()).append("</sgtin>\n");
            log.append("\nОбработано " + item.getName());
        }
        xml.append("    </order_details>\n" +
                "  </move_place>\n" +
                "</documents>");
    }
    private void generate() {

    }
    public String getXML() {
        return xml.toString();
    }
}
