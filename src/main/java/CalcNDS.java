import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcNDS {
    public static String Calculate(String nds, String coast) {
        return switch (nds) {
            case "10" -> String.valueOf(BigDecimal.valueOf(Double.parseDouble(coast) / 110 * 10).setScale(2, RoundingMode.DOWN));
            case "20" -> String.valueOf(BigDecimal.valueOf(Double.parseDouble(coast) / 120 * 20).setScale(2, RoundingMode.DOWN));
            default -> "0";
        };
    }
}
