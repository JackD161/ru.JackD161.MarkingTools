// класс перечисляет типы отгрузки товара со склада
public enum TurnoverTypeEnum {
    ПРОДАЖА (1),
    ВОЗВРАТ (2);
    private final int variable;
    TurnoverTypeEnum(int variable) {
        this.variable = variable;
    }
    public int getVariable() {
        return variable;
    }
}
