import java.util.ArrayList;
import java.util.List;

public class PrescriptionDocument {
    private final String prescription_num;
    private final String prescription_date;
    private final String prescription_series;
    private final List<PrescriptionGoods> goodsList;

    public PrescriptionDocument(String prescription_series, String prescription_num, String prescription_date, PrescriptionGoods goods) {
        this.prescription_series = prescription_series;
        this.prescription_num = prescription_num;
        this.prescription_date = prescription_date;
        this.goodsList = new ArrayList<>();
        this.addGoods(goods);
    }
    public PrescriptionDocument(PrescriptionGoods goods) {
        this.prescription_series = goods.getPrescriptionSeria();
        this.prescription_num = goods.getPrescriptionNumber();
        this.prescription_date = goods.getPrescriptionDate();
        this.goodsList = new ArrayList<>();
        this.addGoods(goods);
    }
    public void addGoods (PrescriptionGoods goods) {
        this.goodsList.add(goods);
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

    public List<PrescriptionGoods> getGoodsList() {
        return goodsList;
    }

}
