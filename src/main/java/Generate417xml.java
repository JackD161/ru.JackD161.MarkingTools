import javax.swing.JTextArea;
import java.util.List;
// класс генерирует xml для возврата приостановленного товара поставщику
public class Generate417xml {
    private final StringBuilder xml;
    public Generate417xml(String senderMD, String receiverMD, String dateOperate, String docNum, String docDate, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<documents version=\"1.38\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <move_return action_id=\"417\">\n" +
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
                "  </move_return>\n" +
                "</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
