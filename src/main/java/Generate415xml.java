import java.util.HashMap;
import java.util.List;
import java.util.Map;
// класс генерирует xml для отгрузки товара со склада
public class Generate415xml {
    private final String senderMD;
    private final String receiverMD;
    private final String dateOperate;
    private final HashMap<Integer, List<Object>> mapSgtin;
    private final StringBuilder xml;
    private final String gosNum;
    private final String gosDate;
    private final String docNum;
    private final String docDate;
    private final String contractType;
    private final String financeType;
    private final String turnoverType;

    public Generate415xml(String senderMD, String receiverMD, String dateOperate, String docNum, String docDate, String gosNum, String gosDate, String contractType, String financeType, String turnoverType, HashMap<Integer, List<Object>> mapSgtin) {
        this.senderMD = senderMD;
        this.receiverMD = receiverMD;
        this.dateOperate = dateOperate;
        this.mapSgtin = mapSgtin;
        this.xml = new StringBuilder();
        this.gosNum = gosNum;
        this.gosDate = gosDate;
        this.docNum = docNum;
        this.docDate = docDate;
        this.contractType = contractType;
        this.financeType = financeType;
        this.turnoverType = turnoverType;
        generate();
    }
    public void generate() {
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<documents version=\"1.38\">\n" +
                "  <move_order action_id=\"415\">\n" +
                "    <subject_id>"+ senderMD + "</subject_id>\n" +
                "    <receiver_id>" + receiverMD + "</receiver_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <doc_num>" + docNum + "</doc_num>\n" +
                "    <doc_date>" + docDate + "</doc_date>\n" +
                "    <turnover_type>" + turnoverType + "</turnover_type>\n" +
                "    <source>" + financeType + "</source>\n" +
                "    <contract_type>" + contractType + "</contract_type>\n");
        if (!gosDate.isBlank() || !gosNum.isBlank()) {
            xml.append("    <contract_gos_num>" + gosNum + "</contract_gos_num>\n" +
                    "    <contract_gos_date>" + gosDate + "</contract_gos_date>\n");
        }
        xml.append("    <order_details>\n");
        for (Map.Entry<Integer, List<Object>> pair : mapSgtin.entrySet()) {
            xml.append("      <union>\n" +
                    "        <sgtin>" + pair.getValue().get(0) + "</sgtin>\n" +
                    "        <cost>" + pair.getValue().get(1) + "</cost>\n" +
                    "        <vat_value>" + CalcNDS.Calculate((String) pair.getValue().get(2), (String) pair.getValue().get(1)) + "</vat_value>\n" +
                    "      </union>\n");
        }
        xml.append("    </order_details>\n" +
                "  </move_order>\n" +
                "</documents>");
    }

    public String getXML() {
        return xml.toString();
    }
}
