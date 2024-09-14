import javax.swing.*;

public class Generate521xml {
    private final StringBuilder xml;

    public Generate521xml(String senderMD, String dateOperate, PrescriptionDocument doc, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        xml.append("<documents version=\"1.38\">\n");
        xml.append("  <recipe action_id=\"521\">\n");
        xml.append("    <subject_id>" + senderMD + "</subject_id>\n");
        xml.append("    <operation_date>" + dateOperate + "</operation_date>\n");
        xml.append("    <doc_date>" + doc.getPrescription_date() + "</doc_date>\n");
        xml.append("    <doc_series>" + doc.getPrescription_series() + "</doc_series>\n");
        xml.append("    <doc_num>" + doc.getPrescription_num() + "</doc_num>\n");
        xml.append("    <order_details>");
        for (PrescriptionGoods goods : doc.getGoodsList()) {
            xml.append("      <union>\n");
            xml.append("        <sgtin>" + goods.getSgtin() + "</sgtin>\n");
            xml.append("      </union>\n");
        }
        xml.append("    </order_details>\n");
        xml.append("  </recipe>\n");
        xml.append("</documents>");
        log.append("\nРецепт номер " + doc.getPrescription_num() + " обработан");
    }
    public String getXML() {
        return xml.toString();
    }
}
