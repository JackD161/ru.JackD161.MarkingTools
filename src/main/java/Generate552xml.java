import java.util.HashMap;
import java.util.List;
import java.util.Map;

// класс генерирует документ xml для вывода из оборота товара отправителем
public class Generate552xml {
    private final String senderMD;
    private final String dateOperate;
    private final String docNum;
    private final String docDate;
    private final String typeWithdrawal;
    private final String countryCode;
    private final HashMap<Integer, List<Object>> mapSgtin;
    private final StringBuilder xml;

    public Generate552xml(String senderMD, String dateOperate, String docNum, String docDate, String typeWithdrawal, String countryCode, HashMap<Integer, List<Object>> mapSgtin) {
        this.senderMD = senderMD;
        this.docNum = docNum;
        this.dateOperate = dateOperate;
        this.docDate = docDate;
        this.countryCode = countryCode;
        this.mapSgtin = mapSgtin;
        this.typeWithdrawal = typeWithdrawal;
        xml = new StringBuilder();
        generate();
    }
    private void generate() {
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<documents version=\"1.38\">\n" +
                "  <withdrawal action_id=\"552\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <doc_num>" + docNum + "</doc_num>\n" +
                "    <doc_date>" + docDate + "</doc_date>\n" +
                "    <withdrawal_type>" + typeWithdrawal + "</withdrawal_type>\n");
        if (!countryCode.isBlank()) {
            xml.append("    <export_country_code>" + countryCode + "</export_country_code>\n");
        }
        xml.append("    <order_details>\n");
        for (Map.Entry<Integer, List<Object>> pair : mapSgtin.entrySet()) {
            xml.append("      <sgtin>").append(pair.getValue().get(0)).append("</sgtin>\n");
        }
        xml.append("    </order_details>\n" +
                "  </withdrawal>\n" +
                "</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
