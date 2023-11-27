import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// класс генерирует xml для перемещения товара между местами деятельности внутри организации
public class Generate512xml {
    private final String senderMD;
    private final String docDate;
    private final String dateOperate;
    private final HashMap<Integer, List<Object>> mapSgtin;
    private final StringBuilder xml;

    public Generate512xml(String senderMD, String dateOperate, String docDate, HashMap<Integer, List<Object>> mapSgtin) {
        this.senderMD = senderMD;
        this.docDate = docDate;
        this.dateOperate = dateOperate;
        this.mapSgtin = mapSgtin;
        this.xml = new StringBuilder();
        generate();
    }
    private void generate() {
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<documents version=\"1.38\">\n" +
                "  <withdrawal_without_kkt action_id=\"512\">\n" +
                "    <subject_id>" + senderMD + "</subject_id>\n" +
                "    <operation_date>" + dateOperate + "</operation_date>\n" +
                "    <sales>\n");
        for (Map.Entry<Integer, List<Object>> pair : mapSgtin.entrySet()) {
            xml.append(pregnantCertificate(pair.getValue()));
        }
        xml.append("    </sales>\n" +
                "  </withdrawal_without_kkt>\n" +
                "</documents>");
    }

    private String pregnantCertificate(List<Object> data) {
        String sgtin = String.valueOf(data.get(3));
        if (sgtin.startsWith("01")) {
            sgtin = ParserData.ejectSgtin(sgtin);
        }
        return "      <union>\n" +
                "        <doc_number>" + data.get(1) + "</doc_number>\n" +
                "        <doc_date>"+ docDate +"</doc_date>\n" +
                "        <prescription>\n" +
                "          <prescription_num>" + data.get(1) + "</prescription_num>\n" +
                "          <prescription_date>" + data.get(2) + "</prescription_date>\n" +
                "          <prescription_series>" + data.get(0) + "</prescription_series>\n" +
                "        </prescription>\n" +
                "        <detail>\n" +
                "          <sgtin>" + sgtin + "</sgtin>\n" +
                "          <cost>" + data.get(4) + "</cost>\n" +
                "          <vat_value>" + CalcNDS.Calculate(String.valueOf(data.get(5)) , String.valueOf(data.get(4))) + "</vat_value>\n" +
                "        </detail>\n" +
                "      </union>\n";
    }
    public String getXML() {
        return xml.toString();
    }
}
