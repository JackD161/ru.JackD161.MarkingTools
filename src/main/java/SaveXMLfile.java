import java.io.FileWriter;
import java.io.IOException;
// класс сохраняет сформированный документ в xml файл
public class SaveXMLfile {
    private final String path;
    private final String fileName;
    private final String data;

    public SaveXMLfile(String path, String fileName, String data) {
        this.path = path;
        this.fileName = fileName;
        this.data = data;
        saveFile();
    }

    private void saveFile() {
        try (FileWriter writer = new FileWriter(path + "\\" + fileName, false)) {
            writer.write(data);
            writer.flush();
        }
        catch (IOException e) {
            System.err.println("Ошибка записи файла");
        }
    }
}
