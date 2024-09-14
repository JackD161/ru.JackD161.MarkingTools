import javax.swing.*;
import java.util.List;

public class Generate552xml {
    private final StringBuilder xml;

    public Generate552xml(String senderMD, String dateOperate, String docNum, String docDate, String typeWithdrawal, String countryCode, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        xml.append("<documents version=\"1.38\">\n");
        xml.append("  <withdrawal action_id=\"552\">\n");
        xml.append("    <subject_id>" + senderMD + "</subject_id>\n");
        xml.append("    <operation_date>" + dateOperate + "</operation_date>\n");
        xml.append("    <doc_num>" + docNum + "</doc_num>\n");
        xml.append("    <doc_date>" + docDate + "</doc_date>\n");
        xml.append("    <withdrawal_type>" + typeWithdrawal + "</withdrawal_type>\n");
        if (!countryCode.isBlank()) {
            xml.append("    <export_country_code>" + countryCode + "</export_country_code>\n");
        }
        xml.append("    <order_details>\n");
        for (Goods item : goods) {
            xml.append("      <sgtin>").append(item.getSgtin()).append("</sgtin>\n");
            if (!item.getName().equals("no name")) {
                log.append("\nОбработан " + item.getName());
            }
        }
        xml.append("    </order_details>\n");
        xml.append("  </withdrawal>\n");
        xml.append("</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
