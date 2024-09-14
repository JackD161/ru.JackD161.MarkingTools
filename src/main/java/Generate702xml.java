import javax.swing.*;
import java.util.List;

public class Generate702xml {
    private final StringBuilder xml;

    public Generate702xml(String senderMD, String receiverMD, String inn, String kpp, String dateOperate, String docNum, String docDate, String gosNum, String gosDate, String contractType, String financeType, String turnoverType, List<Goods> goods, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        xml.append("<documents version=\"1.38\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
        xml.append("  <posting action_id=\"702\">\n");
        xml.append("    <subject_id>" + senderMD + "</subject_id>\n");
        xml.append("    <shipper_info>\n");
        xml.append("      <inn>" + inn + "</inn>\n");
        if (!kpp.isBlank()) {
            xml.append("      <kpp>" + kpp + "</kpp>\n");
        }
        xml.append("    </shipper_info>\n");
        if (!receiverMD.isBlank()) {
            xml.append("    <shipper_id>" + receiverMD + "</shipper_id>\n");
        }
        xml.append("    <operation_date>" + dateOperate + "</operation_date>\n");
        xml.append("    <doc_num>" + docNum + "</doc_num>\n");
        xml.append("    <doc_date>" + docDate + "</doc_date>\n");
        xml.append("    <receive_type>" + turnoverType + "</receive_type>\n");
        xml.append("    <contract_type>" + contractType + "</contract_type>\n");
        xml.append("    <source>" + financeType + "</source>\n");
        if (!gosDate.isBlank() || !gosNum.isBlank()) {
            xml.append("    <contract_gos_num>" + gosNum + "</contract_gos_num>\n");
            xml.append("    <contract_gos_date>" + gosDate + "</contract_gos_date>\n");
        }
        xml.append("    <order_details>\n");
        for (Goods goodItem : goods) {
            xml.append("      <union>\n");
            xml.append("        <sgtin>" + goodItem.getSgtin() + "</sgtin>\n");
            xml.append("        <cost>" + goodItem.getCost() + "</cost>\n");
            xml.append("        <vat_value>" + goodItem.getVatValue() + "</vat_value>\n");
            xml.append("      </union>\n");
            log.append("\nОбработано " + goodItem.getName());
        }
        xml.append("    </order_details>\n");
        xml.append("  </posting>\n");
        xml.append("</documents>");
    }

    public String getXML() {
        return xml.toString();
    }
}
