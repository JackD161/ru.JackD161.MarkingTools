
public class ExceptiionContractType441 extends Throwable {
    ExceptiionContractType441() {
        System.err.println("Для 441 схемы возможно указание только следующих типов договора (contract_type):\n" +
                "1 - купля продажи\n" +
                "2 - комиссия\n" +
                "3 - агентский договор\n" +
                "4 - передача на безвозмездной основе");
    }
}
