import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class GeneratorGUI {
    private JFrame window;
    private JTextField srcFile;
    private JTextField outFile;
    private JTextField senderMD;
    private JTextField receiverMD;
    private JTextField dateOperate;
    private JTextArea outputField;
    private final JButton xml415;
    private final JButton xml701;
    private final JButton xml251;
    private final JButton xml431;
    private final JButton dateOperateNow;
    private final JButton docDateNow;
    private final JButton gosDateNow;
    private int xmlNumber;
    private JTextField gosNum;
    private  JTextField gosDate;
    private  JTextField docNum;
    private  JTextField docDate;
    private final JPanel center;
    private JTextField reasonRecall;
    private HashMap<String, JTextField> fieldsMap;
    private HashMap<String, JLabel> labelsMap;
    private JComboBox<ContractTypeEnum> contractTypeBox;
    private JComboBox<FinanceTypeEnum> financeTypeBox;
    private JComboBox<TurnoverTypeEnum> turnoverTypeBox;
    private final String[] schema251 = {"srcFile", "outFile", "senderMD", "receiverMD", "dateOperate", "reasonRecall"};
    private final String[] schema701 = {"srcFile", "outFile", "senderMD", "receiverMD", "dateOperate"};
    private final String[] schema431 = {"srcFile", "outFile", "senderMD", "receiverMD", "dateOperate", "docNum", "docDate"};
    private final String[] schema415 = {"srcFile", "outFile", "senderMD", "receiverMD", "dateOperate", "docNum", "docDate", "gosNum", "gosDate"};
    private ZonedDateTime time;

    public GeneratorGUI() {
        initFrame();
        window.setBounds(200, 300, 770, 700);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(outputField);
        JButton reset = new JButton("Сброс");
        JButton confirm = new JButton("Генерация");
        JButton clipboard = new JButton("В буффер");
        xml415 = new JButton("415");
        xml701 = new JButton("701");
        xml251 = new JButton("251");
        xml431 = new JButton("431");
        dateOperateNow = new JButton("Сейчас");
        docDateNow = new JButton("Сегодня");
        gosDateNow = new JButton("Сегодня");
        xml415.setBackground(Color.WHITE);
        xml701.setBackground(Color.WHITE);
        xml251.setBackground(Color.WHITE);
        xml431.setBackground(Color.WHITE);
        confirm.setBackground(Color.GREEN);
        clipboard.setBackground(Color.MAGENTA);
        reset.setBackground(Color.RED);
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
        center = new JPanel();
        JPanel right = new JPanel();
        JPanel left = new JPanel();

        left.add(xml415);
        left.add(xml701);
        left.add(xml251);
        left.add(xml431);

        right.add(scrollPane);

        footer.add(confirm);
        footer.add(reset);
        footer.add(clipboard);
        footer.setLayout(new FlowLayout());
        left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
        window.getContentPane().add(BorderLayout.PAGE_START, bar);
        window.getContentPane().add(BorderLayout.PAGE_END, footer);
        window.getContentPane().add(BorderLayout.CENTER, center);
        window.getContentPane().add(BorderLayout.LINE_END, right);
        window.getContentPane().add(BorderLayout.LINE_START, left);

        repaint();

        xml415.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 415;
            bleachingButtons();
            xml415.setBackground(Color.ORANGE);
            generateForm(schema415);
            initComboBoxFor415();
        });
        xml701.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 701;
            bleachingButtons();
            xml701.setBackground(Color.ORANGE);
            generateForm(schema701);
        });
        xml251.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 251;
            bleachingButtons();
            xml251.setBackground(Color.ORANGE);
            generateForm(schema251);
        });
        xml431.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 431;
            bleachingButtons();
            xml431.setBackground(Color.ORANGE);
            generateForm(schema431);
        });
        reset.addActionListener(e -> {
            clearFields();
            bleachingFields();
            bleachingButtons();
            center.removeAll();
            xmlNumber = 0;
            repaint();
        });
        dateOperateNow.addActionListener(e -> {
            ZonedDateTime timeNow = ZonedDateTime.now(ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateOperate.setText(timeNow.format(formatter));
        });
        docDateNow.addActionListener(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            docDate.setText(time.format(formatter));
        });
        gosDateNow.addActionListener(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            gosDate.setText(time.format(formatter));
        });
        aboutIt.addActionListener(e -> JOptionPane.showMessageDialog(window, "Версия генератора МДЛП документов 1.0 alpha"));
        exit.addActionListener(e -> System.exit(0));
        saveAs.addActionListener(e -> {
            new SaveXMLfile(outFile.getText(), "Document" + xmlNumber + ".xml", outputField.getText());
            JOptionPane.showMessageDialog(window,"Файл сохранен");
        });
        clipboard.addActionListener(e -> {
            String inputScanField = outputField.getText();
            StringSelection stringSelection = new StringSelection(inputScanField);
            Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            systemClipboard.setContents(stringSelection, null);
        });
        confirm.addActionListener(e -> {
            switch (xmlNumber) {
                case 0 -> JOptionPane.showMessageDialog(window, "Не выбрана схема документа");
                case 415 -> {
                    JOptionPane.showMessageDialog(window, "Выбрана схема документа 415");
                    ExcelReader reader = new ExcelReader();
                    reader.read(srcFile.getText());
                    String contractTypeTeg = (String.valueOf(((ContractTypeEnum) Objects.requireNonNull(contractTypeBox.getSelectedItem())).getVariable()));
                    String financeTypeTeg = (String.valueOf(((FinanceTypeEnum) Objects.requireNonNull(financeTypeBox.getSelectedItem())).getVariable()));
                    String turnoverTypeTeg = (String.valueOf(((TurnoverTypeEnum) Objects.requireNonNull(turnoverTypeBox.getSelectedItem())).getVariable()));
                    outputField.setText(String.valueOf(new Generate415xml(senderMD.getText(), receiverMD.getText(), dateOperate.getText(), docNum.getText(), docDate.getText(), gosNum.getText(), gosDate.getText(), contractTypeTeg, financeTypeTeg, turnoverTypeTeg, reader.getData()).getXML()));
                }
                case 701 -> {
                    JOptionPane.showMessageDialog(window, "Выбрана схема документа 701");
                    ExcelReader reader = new ExcelReader();
                    reader.read(srcFile.getText());
                    outputField.setText(String.valueOf(new Generate701xml(senderMD.getText(), receiverMD.getText(), dateOperate.getText(), reader.getData()).getXML()));
                }
                case 251 -> {
                    JOptionPane.showMessageDialog(window, "Выбрана схема документа 251");
                    ExcelReader reader = new ExcelReader();
                    reader.read(srcFile.getText());
                    outputField.setText(String.valueOf(new Generate251xml(senderMD.getText(), receiverMD.getText(), dateOperate.getText(), reasonRecall.getText(), reader.getData()).getXML()));
                }
                case 431 -> {
                    JOptionPane.showMessageDialog(window, "Выбрана схема документа 431");
                    ExcelReader reader = new ExcelReader();
                    reader.read(srcFile.getText());
                    outputField.setText(String.valueOf(new Generate431xml(senderMD.getText(), receiverMD.getText(), dateOperate.getText(), docNum.getText(), docDate.getText(), reader.getData()).getXML()));
                }
                default -> JOptionPane.showMessageDialog(window, "Выбрана неизвестная схема документа");
            }
        });
    }
    private void initLabels() {
        JLabel srcFileLabel = new JLabel("Файл источник SGTIN и цен");
        JLabel outFileLAbel = new JLabel("Путь куда сохранить");
        JLabel senderMDLabel = new JLabel("МД отправителя");
        JLabel receiverMDLabel = new JLabel("МД получателя");
        JLabel dateOperateLabel = new JLabel("Дата совершения операции");
        JLabel gosNumLabel = new JLabel("Номер госконтракта");
        JLabel gosDateLabel = new JLabel("Дата госконтракта");
        JLabel docNumLabel = new JLabel("Номер документа");
        JLabel docDateLabel = new JLabel("Дата документа");
        JLabel reasonRecallLabel = new JLabel("Причина отзыва");
        JLabel contractTypeLabel = new JLabel("Тип контракта");
        JLabel financeTypeLabel = new JLabel("Тип финансирования");
        JLabel turnoverTypeLabel = new JLabel("Действие");
        labelsMap.put("srcFile", srcFileLabel);
        labelsMap.put("outFile", outFileLAbel);
        labelsMap.put("senderMD", senderMDLabel);
        labelsMap.put("receiverMD", receiverMDLabel);
        labelsMap.put("dateOperate", dateOperateLabel);
        labelsMap.put("gosNum", gosNumLabel);
        labelsMap.put("gosDate", gosDateLabel);
        labelsMap.put("docNum", docNumLabel);
        labelsMap.put("docDate", docDateLabel);
        labelsMap.put("reasonRecall", reasonRecallLabel);
        labelsMap.put("turnoverType", turnoverTypeLabel);
        labelsMap.put("financeType", financeTypeLabel);
        labelsMap.put("contractType", contractTypeLabel);
    }
    private void initTextFields() {
        srcFile = new JTextField(30);
        outFile = new JTextField(30);
        senderMD = new JTextField(30);
        receiverMD = new JTextField(30);
        dateOperate = new JTextField(22);
        outputField = new JTextArea(30, 30);
        gosNum = new JTextField(30);
        gosDate = new JTextField(22);
        docNum = new JTextField(30);
        docDate = new JTextField(23);
        reasonRecall = new JTextField(30);
        docDate.setToolTipText("ДД.ММ.ГГГГ");
        gosDate.setToolTipText("ДД.ММ.ГГГГ");
        fieldsMap.put("srcFile", srcFile);
        fieldsMap.put("outFile", outFile);
        fieldsMap.put("senderMD", senderMD);
        fieldsMap.put("receiverMD", receiverMD);
        fieldsMap.put("dateOperate", dateOperate);
        fieldsMap.put("gosNum", gosNum);
        fieldsMap.put("gosDate", gosDate);
        fieldsMap.put("docNum", docNum);
        fieldsMap.put("docDate", docDate);
        fieldsMap.put("reasonRecall", reasonRecall);
    }
    private void initComboBoxFor415() {
        contractTypeBox = new JComboBox<>(ContractTypeEnum.values());
        financeTypeBox = new JComboBox<>(FinanceTypeEnum.values());
        turnoverTypeBox = new JComboBox<>(TurnoverTypeEnum.values());
        center.add(labelsMap.get("contractType"));
        center.add(contractTypeBox);
        center.add(labelsMap.get("financeType"));
        center.add(financeTypeBox);
        center.add(labelsMap.get("turnoverType"));
        center.add(turnoverTypeBox);
        repaint();
    }
    private void bleachingFields() {
        srcFile.setBackground(Color.WHITE);
        outFile.setBackground(Color.WHITE);
        senderMD.setBackground(Color.WHITE);
        receiverMD.setBackground(Color.WHITE);
        dateOperate.setBackground(Color.WHITE);
        gosNum.setBackground(Color.WHITE);
        gosDate.setBackground(Color.WHITE);
        docNum.setBackground(Color.WHITE);
        docDate.setBackground(Color.WHITE);
    }
    private void bleachingButtons() {
        xml415.setBackground(Color.white);
        xml701.setBackground(Color.white);
        xml251.setBackground(Color.white);
        xml431.setBackground(Color.white);
    }
    private void clearFields() {
        srcFile.setText("");
        outFile.setText("");
        senderMD.setText("");
        receiverMD.setText("");
        dateOperate.setText("");
        gosNum.setText("");
        gosDate.setText("");
        docNum.setText("");
        docDate.setText("");
        outputField.setText("");
    }
    private void generateForm(String[] list) {
        for (String field : list) {
            center.add(labelsMap.get(field));
            center.add(fieldsMap.get(field));
            if (field.equals("dateOperate")) {
                center.add(dateOperateNow);
            }
            if (field.equals("docDate")) {
                center.add(docDateNow);
            }
            if (field.equals("gosDate")) {
                center.add(gosDateNow);
            }
        }
        repaint();
    }
    private void repaint() {
        window.setVisible(true);
        window.repaint();
    }

    private void initFrame() {
        window = new JFrame("Генератор МДЛП документа");
        time = ZonedDateTime.now(ZoneOffset.UTC);
        fieldsMap = new HashMap<>();
        labelsMap = new HashMap<>();
        initLabels();
        initTextFields();
        xmlNumber = 0;
    }

    public static void main(String[] args) {
        new GeneratorGUI();
    }
}
