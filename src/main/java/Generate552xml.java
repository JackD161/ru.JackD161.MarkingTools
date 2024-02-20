import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// класс генерирует документ xml для вывода из оборота товара отправителем
public class Generate552xml {
    private final StringBuilder xml;

    public Generate552xml(String senderMD, String dateOperate, String docNum, String docDate, String typeWithdrawal, String countryCode, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
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
        for (Goods item : goods) {
            xml.append("      <sgtin>").append(item.getSgtin()).append("</sgtin>\n");
            log.append("\nОбработано " + item.getName());
        }
        xml.append("    </order_details>\n" +
                "  </withdrawal>\n" +
                "</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
