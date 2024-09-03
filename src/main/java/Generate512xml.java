import java.util.HashMap;
import java.util.Map;

// класс генерирует xml для перемещения товара между местами деятельности внутри организации
public class Generate512xml {
    private final StringBuilder xml;

    public Generate512xml(String senderMD, String dateOperate, HashMap<String, PrescriptionDocument> mapPrescriptionDocuments) {
        this.xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<documents version=\"1.38\">\n" +
                "  <withdrawal_without_kkt action_id=\"512\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <sales>\n");
        for (Map.Entry<String, PrescriptionDocument> pair : mapPrescriptionDocuments.entrySet()) {
            xml.append(prescriptionDocument(pair.getValue()));
        }
        xml.append("    </sales>\n" +
                "  </withdrawal_without_kkt>\n" +
                "</documents>");
    }

    private String prescriptionDocument(PrescriptionDocument doc) {
        StringBuilder document = new StringBuilder();
        document.append("      <union>\n" +
                "        <doc_number>" + doc.getPrescription_num() + "</doc_number>\n" +
                "        <doc_date>" + doc.getPrescription_date() + "</doc_date>\n" +
                "        <prescription>\n" +
                "          <prescription_num>" + doc.getPrescription_num() + "</prescription_num>\n" +
                "          <prescription_date>" + doc.getPrescription_date() + "</prescription_date>\n");
        if (!doc.getPrescription_series().equals("0"))
            document.append("          <prescription_series>" + doc.getPrescription_series() + "</prescription_series>\n");
        document.append("        </prescription>\n");
        for (PrescriptionGoods goods : doc.getGoodsList()) {
                document.append("        <detail>\n" +
                "          <sgtin>" + goods.getSgtin() + "</sgtin>\n" +
                "          <cost>" + goods.getCost() + "</cost>\n" +
                "          <vat_value>" + goods.getVat_value() + "</vat_value>\n" +
                "        </detail>\n");
        }
        document.append("      </union>\n");
        return document.toString();
    }
    public String getXML() {
        return xml.toString();
    }
}
