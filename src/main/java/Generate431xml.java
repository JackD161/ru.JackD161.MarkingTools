import java.util.HashMap;
import java.util.List;
import java.util.Map;
// класс генерирует xml для перемещения товара между местами деятельности внутри организации
public class Generate431xml {
    private final String senderMD;
    private final String receiverMD;
    private final String dateOperate;
    private final String docNum;
    private final String docDate;
    private final HashMap<Integer, List<Object>> mapSgtin;
    private final StringBuilder xml;

    public Generate431xml(String senderMD, String receiverMD, String dateOperate, String docNum, String docDate, HashMap<Integer, List<Object>> mapSgtin) {
        this.senderMD = senderMD;
        this.receiverMD = receiverMD;
        this.dateOperate = dateOperate;
        this.mapSgtin = mapSgtin;
        this.xml = new StringBuilder();
        this.docNum = docNum;
        this.docDate = docDate;
        generate();
    }
    private void generate() {
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<documents version=\"1.38\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <move_place action_id=\"431\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <receiver_id>" + receiverMD + "</receiver_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <doc_num>" + docNum + "</doc_num>\n" +
                "    <doc_date>" + docDate + "</doc_date>\n" +
                "    <order_details>\n");
        for (Map.Entry<Integer, List<Object>> pair : mapSgtin.entrySet()) {
            xml.append("      <sgtin>").append(pair.getValue().get(0)).append("</sgtin>\n");
        }
        xml.append("    </order_details>\n" +
                "  </move_place>\n" +
                "</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
