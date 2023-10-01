// класс перечисляет возможные причины для товара, каждый имеет возвраемый номер для формирования тега в xml
public enum TypeWithdrawalEnum {
    выборочный_контроль (6),
    таможенный_контроль (7),
    федеральный_надзор (8),
    в_целяк_клинических_исследований (9),
    в_целях_фармацевтической_экспертизы (10),
    недостача (11),
    отбор_демонстрационных_образцов (12),
    списание_без_уничтожения (13),
//    вывод_из_оборота_КИЗ_накопленных_в_рамках_эксперимента(14),
    производственный_брак (15),
    списание_разукомплектованной_потребительской_упаковки (16),
    производство_медицинский_изделий (17),
    производство_медицинских_препаратов (18),
//    ОТБОР_КОНТРОЛЬНЫХ_ОБРАЗЦОВ_В_РАМКАХ_ПРОЦЕССА_КОНТРОЛЯ_КАЧЕСТВА (19),
    отбор_архивных_образцов (20),
    хищение (21),
    автоматическое_списание_по_истечению_срока_годности (22),
//    СПИСАНИЕ_ЛЕКАРСТВЕННЫХ_ПРЕПАРАТОВ_ПРИ_ОТСУТСТВИИ_ИНФОРМАЦИИ_СОГЛАСНО_БП (23),
    экспорт_вне_россии (24),
    предоставлекние_гуманитарной_помощи (26);
    private final int variable;
    TypeWithdrawalEnum(int variable) {
        this.variable = variable;
    }
    public int getVariable() {
        return variable;
    }
}