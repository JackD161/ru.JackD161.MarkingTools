public class Goods {
    private final String name;
    private final String sgtin;
    private final String cost;
    private final String vat_value;

    public Goods (String sgtin, String cost, String nds, String name) {
        this.name = name;
        this.sgtin = sgtin;
        this.cost = cost;
        this.vat_value = CalcNDS.Calculate(nds, cost);
    }
    public Goods (String sgtin, String cost, String nds) {
        this.name = "no name";
        this.sgtin = sgtin;
        this.cost = cost;
        this.vat_value = CalcNDS.Calculate(nds, cost);
    }
    public Goods (String sgtin, String name) {
        this.name = name;
        this.sgtin = sgtin;
        this.cost = "0";
        this.vat_value = "0";
    }
    public Goods (String sgtin) {
        this.name = "no name";
        this.sgtin = sgtin;
        this.cost = "0";
        this.vat_value = "0";
    }

    public String getName() {
        return name;
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
}
