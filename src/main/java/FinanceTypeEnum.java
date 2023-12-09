// класс перечисляет возможные типы финансирования для отгрузки товара, каждый имеет возвраемый номер для формирования тега в xml
public enum FinanceTypeEnum {
    РЕГИОНАЛЬНОЕ (3),
    ФЕДЕРАЛЬНОЕ (2),
    СРЕДСТВА_БЮДЖЕТОВ_ВНЕБЮДЖЕТНЫХ_ФОНДОВ (4),
    СМЕШАННОЕ (5),
    СОБСТВЕННЫЕ_СРЕДСТВА (1);
    private final int variable;
    FinanceTypeEnum(int variable) {
        this.variable = variable;
    }
    public int getVariable() {
        return variable;
    }
}
