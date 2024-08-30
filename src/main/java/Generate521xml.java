import javax.swing.*;

// класс генерирует документ xml для вывода из оборота товара по льготному рецепту
public class Generate521xml {
    private final StringBuilder xml;

    public Generate521xml(String senderMD, String dateOperate, PrescriptionDocument doc, JTextArea log) {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<documents version=\"1.38\">\n" +
                "  <recipe action_id=\"521\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <doc_date>" + doc.getPrescription_date() + "</doc_date>\n" +
                "    <doc_series>" + doc.getPrescription_series() + "</doc_series>\n" +
                "    <doc_num>" + doc.getPrescription_num() + "</doc_num>\n" +
                "    <order_details>");
        for (PrescriptionGoods goods : doc.getGoodsList()) {
            xml.append("      <union>\n" +
                    "        <sgtin>" + goods.getSgtin() + "</sgtin>\n" +
                    "      </union>\n");
        }
                xml.append("    </order_details>\n" +
                "  </recipe>\n" +
                "</documents>");
    }
    public String getXML() {
        return xml.toString();
    }
}
