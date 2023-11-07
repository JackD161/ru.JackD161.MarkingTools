// перечисление всех известных адресов мест деятельности Фармации и Фармации Дона
public enum AddressesMDEnum {
    Аптека_1 ("00000000518234", "FD"),
    Аптека_2 ("00000000492594", "FD"),
    Аптека_3 ("00000000492593", "FD"),
    Аптека_4 ("00000000492592", "FD"),
    Аптека_5 ("00000000496122", "FD"),
    Аптечный_пункт_6 ("00000000510776", "FD"),
    Аптека_7 ("00000000495188", "FD"),
    Аптечный_пункт_8 ("00000000509782", "FD"),
    Аптека_11 ("00000000498942", "FD"),
    Аптека_12 ("00000000499920", "FD"),
    Аптека_13 ("00000000509978", "FD"),
    Аптечный_пункт_14 ("00000000509370", "FD"),
    Аптека_15 ("00000000501763", "FD"),
    Аптека_16 ("00000000508603", "FD"),
    Аптечный_пункт_17 ("00000000508757", "FD"),
    Аптека_18 ("00000000511413", "FD"),
    Аптечный_пункт_20 ("00000000511414", "FD"),
    Аптека_21 ("00000000513160", "FD"),
    Аптека_25 ("00000000517118", "FD"),
    Аптека_70 ("00000000519248", "FD"),
    Аптечный_пункт_1_Аптеки_70 ("00000000154088", "F"),
    Аптечный_пункт_2_Аптеки_74 ("00000000154089", "F"),
    Аптека_92 ("00000000142322", "F"),
    Аптека_99 ("00000000224937", "F"),
    Аптека_222 ("00000000276434", "F"),
    Склад_Фармация_Дона ("00000000520889", "FD"),
    Склад_Фармация ("00000000109665", "F");
    private final String md;
    private final String org;
    AddressesMDEnum(String md, String org) {
        this.md = md;
        this.org = org;
    }

    public String getMd() {
        return md;
    }

    public String getOrg() {
        return org;
    }
}