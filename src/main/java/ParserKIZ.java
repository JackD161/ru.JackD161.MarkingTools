import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

public class ParserKIZ {
    public static void main(String[] args) {
        JFrame window = new JFrame("Конвертер КИЗ в SGTIN");
        window.setBounds(200, 300, 730, 550);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        JLabel sourceFile = new JLabel("Файл источник");
        JLabel destinationFile = new JLabel("Файл куда записать");
        JLabel scan = new JLabel("Сканировать КИЗ");
        JLabel sgtinLabel = new JLabel("SGTIN");
        JTextField inputFile = new JTextField(30);
        JTextField outputFile = new JTextField(30);
        JTextField scanField = new JTextField(30);
        JTextField sgtin = new JTextField(30);
        JTextArea outputField = new JTextArea(25, 30);
        JScrollPane scrollPane = new JScrollPane(outputField);
        JButton reset = new JButton("Сброс");
        reset.setBackground(Color.RED);
        JButton confirm = new JButton("Конвертация");
        JButton ejectSgtin = new JButton("Извлечь SGTIN");
        JButton clipboard = new JButton("В буффер");
        JCheckBox fromFile = new JCheckBox("Из файла");
        fromFile.setBackground(Color.ORANGE);
        clipboard.setBackground(Color.MAGENTA);
        ejectSgtin.setBackground(Color.ORANGE);
        confirm.setBackground(Color.GREEN);
        window.setVisible(true);
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Файл");
        JMenu help = new JMenu("Помощь");
        JMenuItem saveAs = new JMenuItem("Сохранить результат как...");
        JMenuItem exit = new JMenuItem("Выход");
        JMenuItem aboutIt = new JMenuItem("О программе");
        file.add(saveAs);
        file.add(exit);
        help.add(aboutIt);
        bar.add(file);
        bar.add(help);
        JPanel footer = new JPanel();
        JPanel center = new JPanel();
        JPanel right = new JPanel();
        center.add(sourceFile);
        center.add(inputFile);
        center.add(destinationFile);
        center.add(outputFile);
        center.add(scan);
        center.add(scanField);
        center.add(sgtinLabel);
        center.add(sgtin);
        right.add(scrollPane);
        footer.add(confirm);
        footer.add(reset);
        footer.add(ejectSgtin);
        footer.add(fromFile);
        footer.add(clipboard);
        footer.setLayout(new FlowLayout());
        window.getContentPane().add(BorderLayout.NORTH, bar);
        window.getContentPane().add(BorderLayout.SOUTH, footer);
        window.getContentPane().add(BorderLayout.CENTER, center);
        window.getContentPane().add(BorderLayout.EAST, right);
        window.setVisible(true);
        reset.addActionListener(e -> {
            inputFile.setText("");
            outputFile.setText("");
            scanField.setText("");
            sgtin.setText("");
            inputFile.setBackground(Color.WHITE);
            outputFile.setBackground(Color.WHITE);
            scanField.setBackground(Color.WHITE);
            outputField.setText("");
            fromFile.setSelected(false);
        });
        aboutIt.addActionListener(e -> JOptionPane.showMessageDialog(window, "Версия конвертора 1.0 alpha"));
        exit.addActionListener(e -> System.exit(0));
        saveAs.addActionListener(e -> JOptionPane.showMessageDialog(window,"Ну допустим сохранили какой-то файл, реализую функционал потом"));
        confirm.addActionListener(e -> {
            boolean outFileNotEmpty;
            boolean originFileNotEmpty;
            String originFile= inputFile.getText();
            String outFile = outputFile.getText();

            if (originFile.isEmpty()) {
                inputFile.setBackground(Color.PINK);
                originFileNotEmpty = false;
            }
            else {
                originFileNotEmpty = true;
            }
            if (outFile.isEmpty()) {
                outputFile.setBackground(Color.PINK);
                outFileNotEmpty = false;
            }
            else {
                outFileNotEmpty = true;
            }
            if (originFileNotEmpty && outFileNotEmpty) {
                new FileWorker(originFile).parseKIZ(outFile);
            }
        });
        ejectSgtin.addActionListener(e -> {
            boolean scanFieldNotEmpty;
            boolean originFileNotEmpty;
            String originFile= inputFile.getText();
            boolean fromFileCheckBox = fromFile.isSelected();
            String inputScanField;
            if (fromFileCheckBox) {
                if (originFile.isEmpty()) {
                    inputFile.setBackground(Color.PINK);
                    originFileNotEmpty = false;
                }
                else {
                    originFileNotEmpty = true;
                    outputFile.setBackground(Color.WHITE);
                }
                if (originFileNotEmpty) {
                    ArrayList<String> kizs = new FileWorker(originFile).getReadStrings();
                    outputField.removeAll();
                    for (String kiz : kizs) {
                        outputField.append(ParserData.ejectSgtin(kiz));
                        outputField.append("\n");
                    }
                }
            }
            else {
                inputScanField = scanField.getText();
                if (inputScanField.isEmpty()) {
                    scanField.setBackground(Color.PINK);
                    scanFieldNotEmpty = false;
                } else {
                    scanFieldNotEmpty = true;
                    scanField.setBackground(Color.WHITE);
                }
                if (scanFieldNotEmpty) {
                    sgtin.setText(ParserData.ejectSgtin(inputScanField));
                }
            }
        });
        clipboard.addActionListener(e -> {
            String inputScanField = outputField.getText();
            StringSelection stringSelection = new StringSelection(inputScanField);
            Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            systemClipboard.setContents(stringSelection, null);
        });
    }
}
