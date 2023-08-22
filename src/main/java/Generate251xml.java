import java.util.HashMap;
import java.util.List;
import java.util.Map;
// класс генерирует документ xml для отзыва части товара отправителем
public class Generate251xml {
    private final String senderMD;
    private final String receiverMD;
    private final String dateOperate;
    private final String reasonRecall;
    private final HashMap<Integer, List<Object>> mapSgtin;
    private final StringBuilder xml;

    public Generate251xml(String senderMD, String receiverMD, String dateOperate, String reasonRecall, HashMap<Integer, List<Object>> mapSgtin) {
        this.senderMD = senderMD;
        this.receiverMD = receiverMD;
        this.dateOperate = dateOperate;
        this.reasonRecall = reasonRecall;
        this.mapSgtin = mapSgtin;
        xml = new StringBuilder();
        generate();
    }
    private void generate() {
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<documents version=\"1.38\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <refusal_sender action_id=\"251\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <receiver_id>" + receiverMD + "</receiver_id>\n" +
                "    <reason>" + reasonRecall + "</reason>\n" +
                "    <order_details>\n");
        for (Map.Entry<Integer, List<Object>> pair : mapSgtin.entrySet()) {
            xml.append("      <sgtin>").append(pair.getValue().get(0)).append("</sgtin>\n");
        }
        xml.append("    </order_details>\n" +
                "  </refusal_sender>\n" +
                "</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
