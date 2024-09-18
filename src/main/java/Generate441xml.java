import javax.swing.*;
import java.util.List;

public class Generate441xml {
    private final StringBuilder xml;
    public Generate441xml(String senderMD, String inn, String kpp, String dateOperate, String docNum, String docDate, String contractType, List<Goods> goods, JTextArea log) throws ExceptiionContractType441 {
        this.xml = new StringBuilder();
        if (Integer.parseInt(contractType) > 4) {
            throw new ExceptiionContractType441();
        }
        else {
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
            xml.append("<documents version=\"1.38\">\n");
            xml.append("  <move_unregistered_order action_id=\"441\">\n");
            xml.append("    <subject_id>" + senderMD + "</subject_id>\n");
            xml.append("    <operation_date>" + dateOperate + "</operation_date>\n");
            xml.append("    <receiver_info>\n");
            xml.append("      <receiver_inn>\n");
            xml.append("        <ul>\n");
            xml.append("          <inn>" + inn + "</inn>\n");
            xml.append("          <kpp>" + kpp + "</kpp>\n");
            xml.append("        </ul>\n");
            xml.append("      </receiver_inn>\n");
            xml.append("    </receiver_info>\n");
            xml.append("    <contract_type>" + contractType + "</contract_type>\n");
            xml.append("    <doc_num>" + docNum + "</doc_num>\n");
            xml.append("    <doc_date>" + docDate + "</doc_date>\n");
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
            xml.append("  </move_unregistered_order>\n");
            xml.append("</documents>");
        }
    }
    public String getXML() {
        return xml.toString();
    }
}
