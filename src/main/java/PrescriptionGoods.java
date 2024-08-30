public class PrescriptionGoods {
    private final String prescriptionSeria;
    private final String prescriptionNumber;
    private final String prescriptionDate;
    private final String sgtin;
    private final String cost;
    private final String vat_value;
    private final String nameGoods;
    private final String prescriptionName;

    public PrescriptionGoods(String prescriptionSeria, String prescriptionNumber, String prescriptionDate, String sgtin, String cost, String nds, String nameGoods) {
        this.prescriptionSeria = prescriptionSeria;
        this.prescriptionNumber = prescriptionNumber;
        this.prescriptionDate = prescriptionDate;
        this.sgtin = sgtin;
        this.cost = cost;
        this.vat_value = CalcNDS.Calculate(nds, cost);
        this.nameGoods = nameGoods;
        this.prescriptionName = prescriptionSeria + prescriptionNumber;
    }
    public PrescriptionGoods(String prescriptionSeria, String prescriptionNumber, String prescriptionDate, String sgtin) {
        this.prescriptionSeria = prescriptionSeria;
        this.prescriptionNumber = prescriptionNumber;
        this.prescriptionDate = prescriptionDate;
        this.sgtin = sgtin;
        this.cost = "0";
        this.vat_value = "0";
        this.nameGoods = "noname";
        this.prescriptionName = prescriptionSeria + prescriptionNumber;
    }

    public String getPrescriptionSeria() {
        return prescriptionSeria;
    }

    public String getPrescriptionNumber() {
        return prescriptionNumber;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public String getSgtin() {
        return sgtin;
    }

    public String getCost() {
        return cost;
    }

    public String getVat_value() {
        return vat_value;
    }

    public String getNameGoods() {
        return nameGoods;
    }

    public String getPrescriptionName() {
        return prescriptionName;
    }
}
