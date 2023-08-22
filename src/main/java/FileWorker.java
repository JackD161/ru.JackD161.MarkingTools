import java.io.*;
import java.util.ArrayList;
// класс читает и пишет текстовые файлы
public class FileWorker {
    private final ArrayList<String> readStrings = new ArrayList<>();
    public void parseKIZ(String outputFile) {
        saveParserDataToFile(outputFile, ParserData.parseArrayKIZ(readStrings));
    }
    public FileWorker(String inputFile) {
        loadFile(inputFile);
    }
    private void loadFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while(reader.ready()) {
                readStrings.add(reader.readLine());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveParserDataToFile(String fileName, ArrayList<String> result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String string : result) {
                writer.write(string + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getReadStrings() {
        return readStrings;
    }
}
