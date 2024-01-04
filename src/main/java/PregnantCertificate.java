import java.util.ArrayList;
import java.util.List;

public class PregnantCertificate {
    String name;
    String prescription_num;
    String prescription_date;
    String prescription_series;
    List<Goods> goods;

    public PregnantCertificate (String prescription_series, String prescription_num, String prescription_date) {
        this.name = prescription_series + prescription_num;
        this.prescription_series = prescription_series;
        this.prescription_num = prescription_num;
        this.prescription_date = prescription_date;
        this.goods = new ArrayList<>();
    }
    public void addGoods (Goods goods) {
        this.goods.add(goods);
    }
    public String getName() {
        return name;
    }

    public String getPrescription_num() {
        return prescription_num;
    }

    public String getPrescription_date() {
        return prescription_date;
    }

    public String getPrescription_series() {
        return prescription_series;
    }

    public List<Goods> getGoods() {
        return goods;
    }
}
