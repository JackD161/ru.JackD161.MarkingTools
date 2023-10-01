import java.io.IOException;

public class ExceptionReadExcell extends IOException{
    ExceptionReadExcell() {
        System.err.println("Ошибка чтения Excell файла");
    }
}
