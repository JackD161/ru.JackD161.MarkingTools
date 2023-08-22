import java.util.ArrayList;
// статический класс разделяющий переданный КИЗ на составные части
public final class ParserData {
    private static String parseKIZ(String string) {
        if (string.startsWith("01")) {
            // печатает тип упаковки 2 символа
            return string.substring(0, 2) +
                    " " +
                    // печатаем GTIN 14 символов
                    string.substring(2, 16) +
                    " " +
                    // печатаем серийный номер 13 символов
                    string.substring(18, 31) +
                    " " +
                    // печатаем ключ проверки 4 символа
                    string.substring(33, 37) +
                    " " +
                    // печатаем криптохвост 44 символа
                    string.substring(39) +
                    " " + ejectSgtin(string);
        }
        else {
            return string;
        }
    }
    public static String ejectSgtin(String string) {
        if (string.startsWith("01")) {
            return string.substring(2, 16) +
                    string.substring(18, 31);
        }
        else return "Невозможно извлеч SGTIN из строки " + string;
    }

    public static ArrayList<String> parseArrayKIZ(ArrayList<String> strings) {
        ArrayList<String> result = new ArrayList<>();
        result.add(("Тип\tGTIN\tСерийный номер\tКод ответа\tКриптохвост\t\t\t\tSGTIN\n"));
        for (String string : strings) {
            result.add(parseKIZ(string));
        }
        return result;
    }


}
