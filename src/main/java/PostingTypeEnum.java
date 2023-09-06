// класс перечисляет типы прихода товара на склада по схеме 702
public enum PostingTypeEnum {
    ПОСТУПЛЕНИЕ (1),
    ВОЗВРАТ_ОТ_ПОКУПАТЕЛЯ (2),
    ПЕРЕСОРТ (3);
    private final int variable;
    PostingTypeEnum(int variable) {
        this.variable = variable;
    }
    public int getVariable() {
        return variable;
    }
}
