import javax.swing.JTextArea;
import java.util.List;
// класс генерирует документ xml для отзыва части товара отправителем
public class Generate251xml {
    private final StringBuilder xml;
    public Generate251xml(String senderMD, String receiverMD, String dateOperate, String reasonRecall, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<documents version=\"1.38\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <refusal_sender action_id=\"251\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <receiver_id>" + receiverMD + "</receiver_id>\n" +
                "    <reason>" + reasonRecall + "</reason>\n" +
                "    <order_details>\n");
        for (Goods item : goods) {
            xml.append("      <sgtin>").append(item.getSgtin()).append("</sgtin>\n");
            if (!item.getName().equals("no name")) {
                log.append("\nОбработан " + item.getName());
            }
        }
        xml.append("    </order_details>\n" +
                "  </refusal_sender>\n" +
                "</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
