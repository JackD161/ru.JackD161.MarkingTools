import javax.swing.*;
import java.util.List;
// класс генерирует xml для отгрузки товара со склада
public class Generate415xml {
    private final StringBuilder xml;
    public Generate415xml(String senderMD, String receiverMD, String dateOperate, String docNum, String docDate, String gosNum, String gosDate, String contractType, String financeType, String turnoverType, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        xml.append("<documents version=\"1.38\">\n");
        xml.append("  <move_order action_id=\"415\">\n");
        xml.append("    <subject_id>"+ senderMD + "</subject_id>\n");
        xml.append("    <receiver_id>" + receiverMD + "</receiver_id>\n");
        xml.append("    <operation_date>" + dateOperate + "</operation_date>\n");
        xml.append("    <doc_num>" + docNum + "</doc_num>\n");
        xml.append("    <doc_date>" + docDate + "</doc_date>\n");
        xml.append("    <turnover_type>" + turnoverType + "</turnover_type>\n");
        xml.append("    <source>" + financeType + "</source>\n");
        xml.append("    <contract_type>" + contractType + "</contract_type>\n");
        if (!gosDate.isBlank() || !gosNum.isBlank()) {
            xml.append("    <contract_gos_num>" + gosNum + "</contract_gos_num>\n");
            xml.append("    <contract_gos_date>" + gosDate + "</contract_gos_date>\n");
        }
        xml.append("    <order_details>\n");
        for (Goods item : goods) {
            xml.append("      <union>\n");
            xml.append("        <sgtin>" + item.getSgtin() + "</sgtin>\n");
            xml.append("        <cost>" + item.getCost() + "</cost>\n");
            xml.append("        <vat_value>" + item.getVatValue() + "</vat_value>\n");
            xml.append("      </union>\n");
            if (!item.getName().equals("no name")) {
                log.append("\nОбработан " + item.getName());
            }
        }
        xml.append("    </order_details>\n");
        xml.append("  </move_order>\n");
        xml.append("</documents>");
    }

    public String getXML() {
        return xml.toString();
    }
}
