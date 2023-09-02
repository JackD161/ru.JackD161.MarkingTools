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
    private final String version = "1.2 alpha\n" +
            "Разработчик: Холопкин Юрий (JackD161)\n" +
            "e-mail: holopkin_yurik@mail.ru";
    private final String instruct = "Инструменты для работы с маркировкой ЛС\n" +
            "\n" +
            "Позволяет генерировать xml документы наиболее популярных схем для отправки в личном кабинете МДЛП.\n" +
            "Генератор работает с Excell файлами, из которых читает данные. Файл должен содержать информацию по колонкам:\n" +
            "SGTIN | Цена отгрузки включая налог | НДС 10%/20%\n" +
            "Цена отгрузки и НДС используются только для формирования 415 и 702 документов,\n" +
            "величина НДС рассчитывается от цены отгрузки, для остальных документов используется только первая колонка.\n";
    private final String descriptionSchemas = "251 - Отзыв части товара отправителем\n" +
            "415 - Отгрузка ЛП со склада\n" +
            "417 - Возврат приостановленных лекарственных препаратов\n" +
            "431 - Перемещение\n" +
            "701 - Подтверждение отгрузки / приемки\n" +
            "702 - Оприходование\n";
    private JFrame window;
    private JLabel srcFileLabel;
    private JTextField srcFile;
    private JLabel outFileLAbel;
    private JTextField outFile;
    private JLabel senderMDLabel;
    private JTextField senderMD;
    private JLabel receiverMDLabel;
    private JTextField receiverMD;
    private JLabel dateOperateLabel;
    private JTextField dateOperate;
    private JLabel innLabel;
    private JTextField inn;
    private JLabel kppLabel;
    private JTextField kpp;
    private JTextArea outputField;
    private JButton xml415;
    private JButton xml701;
    private JButton xml251;
    private JButton xml431;
    private JButton xml702;
    private JButton xml417;
    private JButton dateOperateNow;
    private JButton docDateNow;
    private JButton gosDateNow;
    private JButton reset;
    private JButton confirm;
    private JButton clipboard;
    private int xmlNumber;
    private JLabel gosNumLabel;
    private JTextField gosNum;
    private JLabel gosDateLabel;
    private  JTextField gosDate;
    private JLabel docNumLabel;
    private  JTextField docNum;
    private JLabel docDateLabel;
    private  JTextField docDate;
    private final JPanel center;
    private JLabel reasonRecallLabel;
    private JTextField reasonRecall;
    private HashMap<String, JTextField> fieldsMap;
    private HashMap<String, JLabel> labelsMap;
    private JLabel contractTypeLabel;
    private JComboBox<ContractTypeEnum> contractTypeBox;
    private JLabel financeTypeLabel;
    private JComboBox<FinanceTypeEnum> financeTypeBox;
    private JLabel turnoverTypeLabel;
    private JComboBox<TurnoverTypeEnum> turnoverTypeBox;
    private final String[] schema251 = {"srcFile", "outFile", "senderMD", "receiverMD", "dateOperate", "reasonRecall"};
    private final String[] schema701 = {"srcFile", "outFile", "senderMD", "receiverMD", "dateOperate"};
    private final String[] schema431 = {"srcFile", "outFile", "senderMD", "receiverMD", "dateOperate", "docNum", "docDate"};
    private final String[] schema415 = {"srcFile", "outFile", "senderMD", "receiverMD", "dateOperate", "docNum", "docDate", "gosNum", "gosDate"};
    private final String[] schema702 = {"srcFile", "outFile", "senderMD", "inn", "kpp", "receiverMD", "dateOperate", "docNum", "docDate", "gosNum", "gosDate"};
    private final String[] schema417 = {"srcFile", "outFile", "senderMD", "receiverMD", "dateOperate", "docNum", "docDate"};
    private ZonedDateTime time;

    public GeneratorGUI() {
        initFrame();
        window.setBounds(300, 100, 900, 800);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(outputField);

        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Файл");
        JMenu help = new JMenu("Помощь");
        JMenuItem saveAs = new JMenuItem("Сохранить результат как...");
        JMenuItem exit = new JMenuItem("Выход");
        JMenuItem aboutIt = new JMenuItem("О программе");
        JMenuItem instruction = new JMenuItem("Описание работы");
        JMenuItem opisanie = new JMenuItem("Описание схем документов");
        file.add(saveAs);
        file.add(exit);
        help.add(aboutIt);
        help.add(instruction);
        help.add(opisanie);
        bar.add(file);
        bar.add(help);

        JPanel footer = new JPanel();
        center = new JPanel();
        JPanel right = new JPanel();
        JPanel left = new JPanel();

        left.add(xml251);
        left.add(xml415);
        left.add(xml417);
        left.add(xml431);
        left.add(xml701);
        left.add(xml702);

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
            schema415LabelsNaming();
            generateForm(schema415);
        });
        xml701.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 701;
            bleachingButtons();
            xml701.setBackground(Color.ORANGE);
            schema701LabelsNaming();
            generateForm(schema701);
        });
        xml251.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 251;
            bleachingButtons();
            xml251.setBackground(Color.ORANGE);
            schema251LabelsNaming();
            generateForm(schema251);
        });
        xml431.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 431;
            bleachingButtons();
            xml431.setBackground(Color.ORANGE);
            schema431LabelsNaming();
            generateForm(schema431);
        });
        xml417.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 417;
            bleachingButtons();
            xml417.setBackground(Color.ORANGE);
            schema417LabelsNaming();
            generateForm(schema417);
        });
        xml702.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 702;
            bleachingButtons();
            xml702.setBackground(Color.ORANGE);
            schema702LabelsNaming();
            generateForm(schema702);
        });
        reset.addActionListener(e -> {
            clearLabelsNaming();
            clearFields();
            bleachingFields();
            bleachingButtons();
            defaultLabelsColor();
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
        aboutIt.addActionListener(e -> JOptionPane.showMessageDialog(window,"Версия генератора МДЛП документов " + version));
        instruction.addActionListener(e -> {
            JOptionPane.showMessageDialog(window,instruct);
                });
        opisanie.addActionListener(e -> {
            JOptionPane.showMessageDialog(window,descriptionSchemas);
        });
        exit.addActionListener(e -> System.exit(0));
        saveAs.addActionListener(e -> {
            if (!outFile.getText().isBlank()) {
                new SaveXMLfile(outFile.getText(), "Document" + xmlNumber + ".xml", outputField.getText());
                JOptionPane.showMessageDialog(window, "Файл сохранен");
            }
            else {
                JOptionPane.showMessageDialog(window, "Не задан путь сохранения файла");
            }
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
                case 702 -> {
                    JOptionPane.showMessageDialog(window, "Выбрана схема документа 702");
                    ExcelReader reader = new ExcelReader();
                    reader.read(srcFile.getText());
                    String contractTypeTeg = (String.valueOf(((ContractTypeEnum) Objects.requireNonNull(contractTypeBox.getSelectedItem())).getVariable()));
                    String financeTypeTeg = (String.valueOf(((FinanceTypeEnum) Objects.requireNonNull(financeTypeBox.getSelectedItem())).getVariable()));
                    String turnoverTypeTeg = (String.valueOf(((TurnoverTypeEnum) Objects.requireNonNull(turnoverTypeBox.getSelectedItem())).getVariable()));
                    outputField.setText(String.valueOf(new Generate702xml(senderMD.getText(), receiverMD.getText(),inn.getText(), kpp.getText(), dateOperate.getText(), docNum.getText(), docDate.getText(), gosNum.getText(), gosDate.getText(), contractTypeTeg, financeTypeTeg, turnoverTypeTeg, reader.getData()).getXML()));
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
                case 417 -> {
                    JOptionPane.showMessageDialog(window, "Выбрана схема документа 417");
                    ExcelReader reader = new ExcelReader();
                    reader.read(srcFile.getText());
                    outputField.setText(String.valueOf(new Generate417xml(senderMD.getText(), receiverMD.getText(), dateOperate.getText(), docNum.getText(), docDate.getText(), reader.getData()).getXML()));
                }
                default -> JOptionPane.showMessageDialog(window, "Выбрана неизвестная схема документа");
            }
        });
    }
    private void clearLabelsNaming() {
        srcFileLabel.setText("");
        outFileLAbel.setText("");
        senderMDLabel.setText("");
        receiverMDLabel.setText("");
        dateOperateLabel.setText("");
        gosNumLabel.setText("");
        gosDateLabel.setText("");
        docNumLabel.setText("");
        docDateLabel.setText("");
        contractTypeLabel.setText("");
        financeTypeLabel.setText("");
        turnoverTypeLabel.setText("");
        innLabel.setText("");
        kppLabel.setText("");
        window.setTitle("Генератор МДЛП документа");
    }
    private void schema415LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN и отгрузочными ценами");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        receiverMDLabel.setText("Идентификатор организации-получателя");
        dateOperateLabel.setText("Дата совершения отгрузки");
        gosNumLabel.setText("Номер государственного контракта");
        gosDateLabel.setText("Дата государственного контракта");
        docNumLabel.setText("Реквизиты документа отгрузки: номер документа");
        docDateLabel.setText("Реквизиты документа отгрузки: дата документа");
        contractTypeLabel.setText("Тип договора");
        financeTypeLabel.setText("Источник финансирования");
        turnoverTypeLabel.setText("Тип операции отгрузки со склада");
        window.setTitle("Отгрузка ЛП со склада");
    }
    private void schema702LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN и приходными ценами");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        innLabel.setText("ИНН организации-грузоотправителя");
        kppLabel.setText("КПП организации-грузоотправителя");
        receiverMDLabel.setText("Идентификатор грузоотправителя");
        dateOperateLabel.setText("Дата совершения операции");
        gosNumLabel.setText("Номер государственного контракта");
        gosDateLabel.setText("Дата государственного контракта");
        docNumLabel.setText("Реквизиты документа основания: номер документа");
        docDateLabel.setText("Реквизиты документа основания: дата документа");
        contractTypeLabel.setText("Тип договора");
        financeTypeLabel.setText("Источник финансирования");
        turnoverTypeLabel.setText("Тип операции оприходования");
        window.setTitle("Оприходование");
    }
    private void schema251LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        receiverMDLabel.setText("Идентификатор организации-получателя");
        dateOperateLabel.setText("Дата совершения операции");
        reasonRecallLabel.setText("Причина отзыва");
        window.setTitle("Отзыв части товара отправителем");
    }
    private void schema431LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        receiverMDLabel.setText("Идентификатор организации-получателя");
        dateOperateLabel.setText("Дата совершения операции");
        docNumLabel.setText("Реквизиты документа перемещения: номер документа");
        docDateLabel.setText("Реквизиты документа перемещения: дата документа");
        window.setTitle("Перемещение");
    }
    private void schema417LabelsNaming() {
        schema431LabelsNaming();
        window.setTitle("Возврат приостановленных лекарственных препаратов");
    }
    private void schema701LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        receiverMDLabel.setText("Идентификатор контрагента");
        dateOperateLabel.setText("Дата совершения операции");
        window.setTitle("Подтверждение отгрузки / приемки");
    }
    private void initLabels() {
        srcFileLabel = new JLabel();
        outFileLAbel = new JLabel();
        senderMDLabel = new JLabel();
        receiverMDLabel = new JLabel();
        dateOperateLabel = new JLabel();
        gosNumLabel = new JLabel();
        gosDateLabel = new JLabel();
        docNumLabel = new JLabel();
        docDateLabel = new JLabel();
        reasonRecallLabel = new JLabel();
        contractTypeLabel = new JLabel();
        financeTypeLabel = new JLabel();
        turnoverTypeLabel = new JLabel();
        innLabel = new JLabel();
        kppLabel = new JLabel();
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
        labelsMap.put("inn", innLabel);
        labelsMap.put("kpp", kppLabel);
    }
    private void initTextFields() {
        srcFile = new JTextField(30);
        outFile = new JTextField(30);
        senderMD = new JTextField(30);
        receiverMD = new JTextField(30);
        dateOperate = new JTextField(23);
        outputField = new JTextArea(40, 40);
        gosNum = new JTextField(30);
        gosDate = new JTextField(22);
        docNum = new JTextField(30);
        docDate = new JTextField(23);
        reasonRecall = new JTextField(30);
        inn = new JTextField(30);
        kpp = new JTextField(30);
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
        fieldsMap.put("inn", inn);
        fieldsMap.put("kpp", kpp);
    }
    private void initComboBox() {
        contractTypeBox = new JComboBox<>(ContractTypeEnum.values());
        financeTypeBox = new JComboBox<>(FinanceTypeEnum.values());
        turnoverTypeBox = new JComboBox<>(TurnoverTypeEnum.values());
        center.add(labelsMap.get("contractType"));
        center.add(contractTypeBox);
        center.add(labelsMap.get("financeType"));
        center.add(financeTypeBox);
        center.add(labelsMap.get("turnoverType"));
        center.add(turnoverTypeBox);
    }
    private void initButtons() {
        reset = new JButton("Сброс");
        confirm = new JButton("Генерация");
        clipboard = new JButton("В буффер");
        xml415 = new JButton("415");
        xml701 = new JButton("701");
        xml251 = new JButton("251");
        xml431 = new JButton("431");
        xml702 = new JButton("702");
        xml417 = new JButton("417");
        dateOperateNow = new JButton("Сейчас");
        docDateNow = new JButton("Сегодня");
        gosDateNow = new JButton("Сегодня");
        xml415.setBackground(Color.WHITE);
        xml701.setBackground(Color.WHITE);
        xml251.setBackground(Color.WHITE);
        xml431.setBackground(Color.WHITE);
        xml702.setBackground(Color.WHITE);
        xml417.setBackground(Color.WHITE);
        confirm.setBackground(Color.GREEN);
        clipboard.setBackground(Color.MAGENTA);
        reset.setBackground(Color.RED);
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
        inn.setBackground(Color.WHITE);
        kpp.setBackground(Color.WHITE);
    }
    private void defaultLabelsColor() {
        srcFileLabel.setForeground(Color.BLACK);
        outFileLAbel.setForeground(Color.BLACK);
        senderMDLabel.setForeground(Color.BLACK);
        receiverMDLabel.setForeground(Color.BLACK);
        dateOperateLabel.setForeground(Color.BLACK);
        gosNumLabel.setForeground(Color.BLACK);
        gosDateLabel.setForeground(Color.BLACK);
        docNumLabel.setForeground(Color.BLACK);
        docDateLabel.setForeground(Color.BLACK);
        reasonRecallLabel.setForeground(Color.BLACK);
        contractTypeLabel.setForeground(Color.BLACK);
        financeTypeLabel.setForeground(Color.BLACK);
        turnoverTypeLabel.setForeground(Color.BLACK);
        innLabel.setForeground(Color.BLACK);
        kppLabel.setForeground(Color.BLACK);
    }
    private void optionFields251() {
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void optionFields701() {
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void optionFields415() {
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
        gosDateLabel.setForeground(Color.GRAY);
        gosDateLabel.setToolTipText("Не обязательное поле");
        gosNumLabel.setForeground(Color.GRAY);
        gosNumLabel.setToolTipText("Не обязательное поле");
    }
    private void optionFields702() {
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
        gosDateLabel.setForeground(Color.GRAY);
        gosDateLabel.setToolTipText("Не обязательное поле");
        gosNumLabel.setForeground(Color.GRAY);
        gosNumLabel.setToolTipText("Не обязательное поле");
        kppLabel.setForeground(Color.GRAY);
        kppLabel.setToolTipText("Не обязательное поле");
        receiverMDLabel.setForeground(Color.GRAY);
        receiverMDLabel.setToolTipText("Не обязательное поле");

    }
    private void optionFields431() {
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void optionFields417() {
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void bleachingButtons() {
        xml415.setBackground(Color.white);
        xml417.setBackground(Color.white);
        xml701.setBackground(Color.white);
        xml251.setBackground(Color.white);
        xml431.setBackground(Color.white);
        xml702.setBackground(Color.white);
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
        reasonRecall.setText("");
        inn.setText("");
        kpp.setText("");
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
        if (xmlNumber == 415 || xmlNumber == 702)
            initComboBox();
        switch (xmlNumber) {
            case 251 -> optionFields251();
            case 701 -> optionFields701();
            case 415 -> optionFields415();
            case 431 -> optionFields431();
            case 702 -> optionFields702();
            case 417 -> optionFields417();
            default -> defaultLabelsColor();
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
        initButtons();
        xmlNumber = 0;
    }

    public static void main(String[] args) {
        new GeneratorGUI();
    }
}
