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
    private final String version = """
            1.2 alpha
            Разработчик: Холопкин Юрий (JackD161)
            e-mail: holopkin_yurik@mail.ru
            tel: +7-951-827-85-67
            """;
    private final String specification = """
            Инструменты для работы с маркировкой ЛС

            Позволяет генерировать xml документы наиболее популярных схем для отправки в личном кабинете МДЛП.
            Генератор работает с Excell файлами, из которых читает данные. Файл должен содержать информацию по колонкам:
            SGTIN | Цена отгрузки включая налог | НДС 10%/20%
            Цена отгрузки и НДС используются только для формирования 415 и 702 документов,
            величина НДС рассчитывается от цены отгрузки, для остальных документов используется только первая колонка.
            """;
    private final String descriptionSchemas = """
            251 - Отзыв части товара отправителем
            415 - Отгрузка ЛП со склада
            417 - Возврат приостановленных ЛП
            431 - Перемещение
            552 - Вывод из оборота
            701 - Подтверждение отгрузки / приемки
            702 - Оприходование
            """;
    private final String errorEqMD = """
            Места деятельности отправителя и получателя
            принадлежат одному юридическому лицу.
            Это недопустимо для выбранной схемы документа""";
    private final String errorNotEqMD = """
            Места деятельности отправителя и получателя
            не принадлежат одному юридическому лицу.
            Это недопустимо для выбранной схемы документа""";
    private final String errRqFields = "Не заполнены обязательные поля документа";
    private final String errReadExcellFile = "Ошибка чтения Excell файла";
    private JFrame window;
    private JLabel srcFileLabel;
    private JTextField srcFile;
    private JLabel outFileLAbel;
    private JTextField outFile;
    private JLabel senderMDLabel;
    private JComboBox<AddressesMDEnum> senderMDBox;
    private JLabel receiverMDLabel;
    private JComboBox<AddressesMDEnum> receiverMDBox;
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
    private JButton xml552;
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
    private JLabel countryCodeLabel;
    private JTextField countryCode;
    private HashMap<String, JTextField> fieldsMap;
    private HashMap<String, JLabel> labelsMap;
    private HashMap<String, JComboBox> comboBoxMap;
    private JLabel contractTypeLabel;
    private JComboBox<ContractTypeEnum> contractTypeBox;
    private JLabel financeTypeLabel;
    private JComboBox<FinanceTypeEnum> financeTypeBox;
    private JLabel turnoverTypeLabel;
    private JComboBox<TurnoverTypeEnum> turnoverTypeBox;
    private JLabel typeWithdrawalLabel;
    private JComboBox<TypeWithdrawalEnum> typeWithdrawalBox;
    private JLabel postingTypeLabel;
    private JComboBox<PostingTypeEnum> postingTypeBox;
    private final String[] schema251 = {"srcFile", "outFile", "senderMDBox", "receiverMDBox", "dateOperate", "reasonRecall"};
    private final String[] schema701 = {"srcFile", "outFile", "senderMDBox", "receiverMDBox", "dateOperate"};
    private final String[] schema431 = {"srcFile", "outFile", "senderMDBox", "receiverMDBox", "dateOperate", "docNum", "docDate"};
    private final String[] schema415 = {"srcFile", "outFile", "senderMDBox", "receiverMDBox", "dateOperate", "docNum", "docDate", "gosNum", "gosDate", "contractTypeBox", "financeTypeBox", "turnoverTypeBox"};
    private final String[] schema702 = {"srcFile", "outFile", "senderMDBox", "inn", "kpp", "receiverMDBox", "dateOperate", "docNum", "docDate", "gosNum", "gosDate", "contractTypeBox", "financeTypeBox", "postingTypeBox"};
    private final String[] schema417 = {"srcFile", "outFile", "senderMDBox", "receiverMDBox", "dateOperate", "docNum", "docDate"};
    private final String[] schema552 = {"srcFile", "outFile", "senderMDBox", "dateOperate", "docNum", "docDate", "countryCode", "typeWithdrawalBox"};
    private ZonedDateTime time;

    public GeneratorGUI() {
        initFrame();
        window.setBounds(300, 100, 930, 800);
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
        JMenuItem definition = new JMenuItem("Описание схем документов");
        file.add(saveAs);
        file.add(exit);
        help.add(aboutIt);
        help.add(instruction);
        help.add(definition);
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
        left.add(xml552);
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
            xml415.setBackground(Color.ORANGE);
            schema415LabelsNaming();
            generateForm(schema415);
        });
        xml552.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 552;
            xml552.setBackground(Color.ORANGE);
            schema552LabelsNaming();
            generateForm(schema552);
        });
        xml701.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 701;
            xml701.setBackground(Color.ORANGE);
            schema701LabelsNaming();
            generateForm(schema701);
        });
        xml251.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 251;
            xml251.setBackground(Color.ORANGE);
            schema251LabelsNaming();
            generateForm(schema251);
        });
        xml431.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 431;
            xml431.setBackground(Color.ORANGE);
            schema431LabelsNaming();
            generateForm(schema431);
        });
        xml417.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 417;
            xml417.setBackground(Color.ORANGE);
            schema417LabelsNaming();
            generateForm(schema417);
        });
        xml702.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 702;
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
            setDefaultComboBoxValues();
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
        instruction.addActionListener(e -> JOptionPane.showMessageDialog(window, specification));
        definition.addActionListener(e -> JOptionPane.showMessageDialog(window,descriptionSchemas));
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
                case 552 -> {
                    if (checkRequiredField(xmlNumber)) {
                        JOptionPane.showMessageDialog(window, "Выбрана схема документа 552");
                        ExcelReader reader = new ExcelReader();
                        try {
                        reader.read(srcFile.getText());
                            String typeWithdrawal = (String.valueOf(((TypeWithdrawalEnum) Objects.requireNonNull(typeWithdrawalBox.getSelectedItem())).getVariable()));
                            String senderMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getMd()));
                            outputField.setText(String.valueOf(new Generate552xml(senderMD, dateOperate.getText(), docNum.getText(), docDate.getText(), typeWithdrawal, countryCode.getText(), reader.getData()).getXML()));
                        }
                        catch (ExceptionReadExcell exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile);
                        }
                    }
                    else JOptionPane.showMessageDialog(window, errRqFields);
                }
                case 415 -> {
                    if (checkRequiredField(xmlNumber)) {
                        ExcelReader reader = new ExcelReader();
                        try {
                            reader.read(srcFile.getText());
                            String contractTypeTeg = (String.valueOf(((ContractTypeEnum) Objects.requireNonNull(contractTypeBox.getSelectedItem())).getVariable()));
                            String financeTypeTeg = (String.valueOf(((FinanceTypeEnum) Objects.requireNonNull(financeTypeBox.getSelectedItem())).getVariable()));
                            String turnoverTypeTeg = (String.valueOf(((TurnoverTypeEnum) Objects.requireNonNull(turnoverTypeBox.getSelectedItem())).getVariable()));
                            String senderMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getMd()));
                            String receiverMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getMd()));
                            String receiverOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getOrg()));
                            String senderOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getOrg()));
                            if (senderOrg.equals(receiverOrg)) {
                                JOptionPane.showMessageDialog(window, errorEqMD);
                            } else {
                                JOptionPane.showMessageDialog(window, "Выбрана схема документа 415");
                                outputField.setText(String.valueOf(new Generate415xml(senderMD, receiverMD, dateOperate.getText(), docNum.getText(), docDate.getText(), gosNum.getText(), gosDate.getText(), contractTypeTeg, financeTypeTeg, turnoverTypeTeg, reader.getData()).getXML()));
                            }
                        }
                        catch (ExceptionReadExcell exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile);
                        }
                    }
                    else JOptionPane.showMessageDialog(window, errRqFields);
                }
                case 702 -> {
                    if (checkRequiredField(xmlNumber)) {
                        ExcelReader reader = new ExcelReader();
                        try {
                        reader.read(srcFile.getText());
                        String contractTypeTeg = (String.valueOf(((ContractTypeEnum) Objects.requireNonNull(contractTypeBox.getSelectedItem())).getVariable()));
                        String financeTypeTeg = (String.valueOf(((FinanceTypeEnum) Objects.requireNonNull(financeTypeBox.getSelectedItem())).getVariable()));
                        String postingTypeTeg = (String.valueOf(((PostingTypeEnum) Objects.requireNonNull(postingTypeBox.getSelectedItem())).getVariable()));
                        String senderMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getMd()));
                        String receiverMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getMd()));
                        String receiverOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getOrg()));
                        String senderOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getOrg()));
                        if (senderOrg.equals(receiverOrg)) {
                            JOptionPane.showMessageDialog(window, errorEqMD);
                        } else {
                            JOptionPane.showMessageDialog(window, "Выбрана схема документа 702");
                            outputField.setText(String.valueOf(new Generate702xml(senderMD, receiverMD, inn.getText(), kpp.getText(), dateOperate.getText(), docNum.getText(), docDate.getText(), gosNum.getText(), gosDate.getText(), contractTypeTeg, financeTypeTeg, postingTypeTeg, reader.getData()).getXML()));
                        }
                        }
                        catch (ExceptionReadExcell exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile);
                        }
                    }
                    else JOptionPane.showMessageDialog(window, errRqFields);
                }
                case 701 -> {
                    if (checkRequiredField(xmlNumber)) {
                        ExcelReader reader = new ExcelReader();
                        try {
                        reader.read(srcFile.getText());
                        String senderMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getMd()));
                        String receiverMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getMd()));
                        String receiverOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getOrg()));
                        String senderOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getOrg()));
                        if (senderOrg.equals(receiverOrg)) {
                            JOptionPane.showMessageDialog(window, errorEqMD);
                        } else {
                            JOptionPane.showMessageDialog(window, "Выбрана схема документа 701");
                            outputField.setText(String.valueOf(new Generate701xml(senderMD, receiverMD, dateOperate.getText(), reader.getData()).getXML()));
                        }
                        }
                        catch (ExceptionReadExcell exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile);
                        }
                    }
                    else JOptionPane.showMessageDialog(window, errRqFields);
                }
                case 251 -> {
                    if (checkRequiredField(xmlNumber)) {
                        ExcelReader reader = new ExcelReader();
                        try {
                        reader.read(srcFile.getText());
                        String senderMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getMd()));
                        String receiverMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getMd()));
                        String receiverOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getOrg()));
                        String senderOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getOrg()));
                        if (senderOrg.equals(receiverOrg)) {
                            JOptionPane.showMessageDialog(window, errorEqMD);
                        } else {
                            JOptionPane.showMessageDialog(window, "Выбрана схема документа 251");
                            outputField.setText(String.valueOf(new Generate251xml(senderMD, receiverMD, dateOperate.getText(), reasonRecall.getText(), reader.getData()).getXML()));
                        }
                        }
                        catch (ExceptionReadExcell exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile);
                        }
                    }
                    else JOptionPane.showMessageDialog(window, errRqFields);
                }
                case 431 -> {
                    if (checkRequiredField(xmlNumber)) {
                        ExcelReader reader = new ExcelReader();
                        try {
                        reader.read(srcFile.getText());
                        String senderMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getMd()));
                        String receiverMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getMd()));
                        String receiverOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getOrg()));
                        String senderOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getOrg()));
                        if (senderOrg.equals(receiverOrg)) {
                            JOptionPane.showMessageDialog(window, "Выбрана схема документа 431");
                            outputField.setText(String.valueOf(new Generate431xml(senderMD, receiverMD, dateOperate.getText(), docNum.getText(), docDate.getText(), reader.getData()).getXML()));
                        } else {
                            JOptionPane.showMessageDialog(window, errorNotEqMD);
                        }
                        }
                        catch (ExceptionReadExcell exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile);
                        }
                    }
                    else JOptionPane.showMessageDialog(window, errRqFields);
                }
                case 417 -> {
                    if (checkRequiredField(xmlNumber)) {
                        ExcelReader reader = new ExcelReader();
                        try {
                        reader.read(srcFile.getText());
                        String senderMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getMd()));
                        String receiverMD = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getMd()));
                        String receiverOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getOrg()));
                        String senderOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getOrg()));
                        if (senderOrg.equals(receiverOrg)) {
                            JOptionPane.showMessageDialog(window, errorEqMD);
                        } else {
                            JOptionPane.showMessageDialog(window, "Выбрана схема документа 417");
                            outputField.setText(String.valueOf(new Generate417xml(senderMD, receiverMD, dateOperate.getText(), docNum.getText(), docDate.getText(), reader.getData()).getXML()));
                        }
                        }
                        catch (ExceptionReadExcell exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile);
                        }
                    }
                    else JOptionPane.showMessageDialog(window, errRqFields);
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
        typeWithdrawalLabel.setText("");
        innLabel.setText("");
        kppLabel.setText("");
        countryCode.setText("");
        postingTypeLabel.setText("");
        window.setTitle("Генератор МДЛП документа");
    }
    private void schema415LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN и отгрузочными ценами");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        receiverMDLabel.setText("Идентификатор организации-получателя");
        dateOperateLabel.setText("Дата совершения отгрузки/отправки");
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
        dateOperateLabel.setText("Дата совершения операции/отправки");
        gosNumLabel.setText("Номер государственного контракта");
        gosDateLabel.setText("Дата государственного контракта");
        docNumLabel.setText("Реквизиты документа основания: номер документа");
        docDateLabel.setText("Реквизиты документа основания: дата документа");
        contractTypeLabel.setText("Тип договора");
        financeTypeLabel.setText("Источник финансирования");
        postingTypeLabel.setText("Тип операции оприходования");
        window.setTitle("Оприходование");
    }
    private void schema251LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        receiverMDLabel.setText("Идентификатор организации-получателя");
        dateOperateLabel.setText("Дата совершения операции/отправки");
        reasonRecallLabel.setText("Причина отзыва");
        window.setTitle("Отзыв части товара отправителем");
    }
    private void schema552LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        dateOperateLabel.setText("Дата совершения операции/отправки");
        docNumLabel.setText("Реквизиты документа основания: номер документа");
        docDateLabel.setText("Реквизиты документа основания: дата документа");
        typeWithdrawalLabel.setText("Тип вывода из оборота");
        countryCodeLabel.setText("Код страны экспорта");
        window.setTitle("Вывод из оборота");
    }
    private void schema431LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        receiverMDLabel.setText("Идентификатор организации-получателя");
        dateOperateLabel.setText("Дата совершения операции/отправки");
        docNumLabel.setText("Регистрационный номер документа");
        docDateLabel.setText("Дата регистрации документа");
        typeWithdrawalLabel.setText("Тип вывода из оборота");
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
        receiverMDLabel.setText("Идентификатор контрагента получателя");
        dateOperateLabel.setText("Дата совершения операции/отправки");
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
        typeWithdrawalLabel = new JLabel();
        countryCodeLabel = new JLabel();
        postingTypeLabel = new JLabel();
        labelsMap.put("srcFile", srcFileLabel);
        labelsMap.put("outFile", outFileLAbel);
        labelsMap.put("senderMDBox", senderMDLabel);
        labelsMap.put("receiverMDBox", receiverMDLabel);
        labelsMap.put("dateOperate", dateOperateLabel);
        labelsMap.put("gosNum", gosNumLabel);
        labelsMap.put("gosDate", gosDateLabel);
        labelsMap.put("docNum", docNumLabel);
        labelsMap.put("docDate", docDateLabel);
        labelsMap.put("reasonRecall", reasonRecallLabel);
        labelsMap.put("turnoverTypeBox", turnoverTypeLabel);
        labelsMap.put("financeTypeBox", financeTypeLabel);
        labelsMap.put("contractTypeBox", contractTypeLabel);
        labelsMap.put("inn", innLabel);
        labelsMap.put("kpp", kppLabel);
        labelsMap.put("countryCode", countryCodeLabel);
        labelsMap.put("typeWithdrawalBox", typeWithdrawalLabel);
        labelsMap.put("postingTypeBox", postingTypeLabel);
    }
    private void initTextFields() {
        srcFile = new JTextField(30);
        outFile = new JTextField(30);
        dateOperate = new JTextField(23);
        outputField = new JTextArea(40, 40);
        gosNum = new JTextField(30);
        gosDate = new JTextField(22);
        docNum = new JTextField(30);
        docDate = new JTextField(23);
        reasonRecall = new JTextField(30);
        inn = new JTextField(30);
        kpp = new JTextField(30);
        countryCode = new JTextField(30);
        docDate.setToolTipText("ДД.ММ.ГГГГ");
        gosDate.setToolTipText("ДД.ММ.ГГГГ");
        fieldsMap.put("srcFile", srcFile);
        fieldsMap.put("outFile", outFile);
        fieldsMap.put("dateOperate", dateOperate);
        fieldsMap.put("gosNum", gosNum);
        fieldsMap.put("gosDate", gosDate);
        fieldsMap.put("docNum", docNum);
        fieldsMap.put("docDate", docDate);
        fieldsMap.put("reasonRecall", reasonRecall);
        fieldsMap.put("inn", inn);
        fieldsMap.put("kpp", kpp);
        fieldsMap.put("countryCode", countryCode);
    }
    private void initComboBox() {
        senderMDBox = new JComboBox<>(AddressesMDEnum.values());
        receiverMDBox = new JComboBox<>(AddressesMDEnum.values());
        contractTypeBox = new JComboBox<>(ContractTypeEnum.values());
        financeTypeBox = new JComboBox<>(FinanceTypeEnum.values());
        turnoverTypeBox = new JComboBox<>(TurnoverTypeEnum.values());
        typeWithdrawalBox = new JComboBox<>(TypeWithdrawalEnum.values());
        postingTypeBox = new JComboBox<>(PostingTypeEnum.values());
        comboBoxMap.put("senderMDBox", senderMDBox);
        comboBoxMap.put("receiverMDBox", receiverMDBox);
        comboBoxMap.put("contractTypeBox", contractTypeBox);
        comboBoxMap.put("financeTypeBox", financeTypeBox);
        comboBoxMap.put("turnoverTypeBox", turnoverTypeBox);
        comboBoxMap.put("typeWithdrawalBox", typeWithdrawalBox);
        comboBoxMap.put("postingTypeBox", postingTypeBox);
    }
    private void setDefaultComboBoxValues() {
        senderMDBox.setSelectedItem(AddressesMDEnum.Склад_Фармация);
        receiverMDBox.setSelectedItem(AddressesMDEnum.Склад_Фармация_Дона);
        contractTypeBox.setSelectedItem(ContractTypeEnum.КУПЛЯ_ПРОДАЖА);
        financeTypeBox.setSelectedItem(FinanceTypeEnum.СОБСТВЕННЫЕ_СРЕДСТВА);
        turnoverTypeBox.setSelectedItem(TurnoverTypeEnum.ПРОДАЖА);
        typeWithdrawalBox.setSelectedItem(TypeWithdrawalEnum.списание_без_уничтожения);
        postingTypeBox.setSelectedItem(PostingTypeEnum.ПОСТУПЛЕНИЕ);
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
        xml552 = new JButton("552");
        dateOperateNow = new JButton("Сейчас");
        docDateNow = new JButton("Сегодня");
        gosDateNow = new JButton("Сегодня");
        xml415.setBackground(Color.WHITE);
        xml701.setBackground(Color.WHITE);
        xml251.setBackground(Color.WHITE);
        xml431.setBackground(Color.WHITE);
        xml702.setBackground(Color.WHITE);
        xml417.setBackground(Color.WHITE);
        xml552.setBackground(Color.WHITE);
        confirm.setBackground(Color.GREEN);
        clipboard.setBackground(Color.MAGENTA);
        reset.setBackground(Color.RED);
    }
    private void bleachingFields() {
        srcFile.setBackground(Color.WHITE);
        outFile.setBackground(Color.WHITE);
        senderMDBox.setBackground(Color.WHITE);
        receiverMDBox.setBackground(Color.WHITE);
        dateOperate.setBackground(Color.WHITE);
        gosNum.setBackground(Color.WHITE);
        gosDate.setBackground(Color.WHITE);
        docNum.setBackground(Color.WHITE);
        docDate.setBackground(Color.WHITE);
        inn.setBackground(Color.WHITE);
        kpp.setBackground(Color.WHITE);
        countryCode.setBackground(Color.WHITE);
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
        countryCodeLabel.setForeground(Color.BLACK);
        typeWithdrawalLabel.setForeground(Color.BLACK);
        postingTypeLabel.setForeground(Color.BLACK);
    }
    private void optionFields251() {
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void optionFields701() {
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void optionFields552() {
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
        countryCodeLabel.setForeground(Color.GRAY);
        countryCodeLabel.setToolTipText("Не обязательное поле, формат A-Z 2 символа (RU)");
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
        xml552.setBackground(Color.white);
    }
    private void clearFields() {
        srcFile.setText("");
        outFile.setText("");
        dateOperate.setText("");
        gosNum.setText("");
        gosDate.setText("");
        docNum.setText("");
        docDate.setText("");
        outputField.setText("");
        reasonRecall.setText("");
        inn.setText("");
        kpp.setText("");
        countryCode.setText("");
    }
    private void generateForm(String[] list) {
        for (String field : list) {
            center.add(labelsMap.get(field));
            if (field.endsWith("Box")) {
                center.add(comboBoxMap.get(field));
            }
            else {
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
        }
        switch (xmlNumber) {
            case 251 -> optionFields251();
            case 701 -> optionFields701();
            case 415 -> optionFields415();
            case 431 -> optionFields431();
            case 702 -> optionFields702();
            case 417 -> optionFields417();
            case 552 -> optionFields552();
            default -> defaultLabelsColor();
        }
        repaint();
    }
    private boolean checkRequiredField(int schema) {
        boolean srcFileFlag = !srcFile.getText().isBlank();
        boolean dateOperateFlag = !dateOperate.getText().isBlank();
        boolean docNumFlag = !docNum.getText().isBlank();
        boolean docDateFlag = !docDate.getText().isBlank();
        boolean reasonRecallFlag = !reasonRecall.getText().isBlank();
        boolean innFlag = !inn.getText().isBlank();
        if (!srcFileFlag) srcFile.setBackground(Color.RED);
        else srcFile.setBackground(Color.WHITE);
        if (!dateOperateFlag) dateOperate.setBackground(Color.RED);
        else dateOperate.setBackground(Color.WHITE);
        if (!docNumFlag) docNum.setBackground(Color.RED);
        else docNum.setBackground(Color.WHITE);
        if (!docDateFlag) docDate.setBackground(Color.RED);
        else docDate.setBackground(Color.WHITE);
        if (!reasonRecallFlag) reasonRecall.setBackground(Color.RED);
        else reasonRecall.setBackground(Color.WHITE);
        if (!innFlag) inn.setBackground(Color.RED);
        else inn.setBackground(Color.WHITE);
        return switch (schema) {
            case 415, 431, 417, 552 -> srcFileFlag && dateOperateFlag && docNumFlag && docDateFlag;
            case 251 -> srcFileFlag && dateOperateFlag && reasonRecallFlag;
            case 701 -> srcFileFlag && dateOperateFlag;
            case 702 -> srcFileFlag && dateOperateFlag && docNumFlag && docDateFlag && innFlag;

            default -> throw new IllegalStateException("Unexpected value: " + schema);
        };
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
        comboBoxMap = new HashMap<>();
        initLabels();
        initTextFields();
        initButtons();
        initComboBox();
        xmlNumber = 0;
    }

    public static void main(String[] args) {
        new GeneratorGUI();
    }
}
