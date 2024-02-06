import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GeneratorGUI {
    private final String version = """
            1.5 alpha
            Разработчик: Холопкин Юрий (JackD161)
            e-mail: holopkin_yurik@mail.ru
            tel: +7-951-827-85-67
            """;
    private final String specification = """
            Инструменты для работы с маркировкой ЛС

            Позволяет генерировать xml документы наиболее популярных схем для отправки в личном кабинете МДЛП.
            Генератор работает с Excell файлами, из которых читает данные.
            Для схем 415 и 702 файл должен содержать информацию по колонкам:
            SGTIN | Цена отгрузки включая налог | НДС 10%/20%
            Для схемы 512 файл должен содержать информацию по колонкам:
            Серия документа | Номер документа | SGTIN или полный КИЗ | Отпускная цена | НДС
            Величина НДС рассчитывается от цены отгрузки.
            Для остальных схем используется только первая колонка файла с данными SGTIN
            """;
    private final String descriptionSchemas = """
            251 - Отзыв части товара отправителем
            415 - Отгрузка ЛП со склада
            417 - Возврат приостановленных ЛП
            431 - Перемещение
            512 - Вывод из оборота с причиной «Отпуск по документу»
            552 - Вывод из оборота
            701 - Подтверждение отгрузки / приемки
            702 - Оприходование
            """;
    private final String errReadExcellFile = "Ошибка чтения файла с SGTIN";
    private final String errParserFile = "Ошибка разбора файла с данными";
    private final String errRqFields = "Не заполнены обязательные поля для формирования документа";
    private final String selectedSchema = "Выбрана схема документа ";
    private JFrame window;
    private JLabel srcFileLabel;
    private JTextField srcFile;
    private JLabel outFileLAbel;
    private JTextField outFile;
    private JLabel senderMDLabel;
    private JTextField senderMD;
    private JComboBox<AddressesMDEnum> senderMDBox;
    private JLabel receiverMDLabel;
    private JTextField receiverMD;
    private JComboBox<AddressesMDEnum> receiverMDBox;
    private JLabel dateOperateLabel;
    private JTextField dateOperate;
    private JLabel innLabel;
    private JTextField inn;
    private JLabel kppLabel;
    private JTextField kpp;
    private JTextArea outputField;
    private JTextArea logField;
    private JButton xml415;
    private JButton xml701;
    private JButton xml251;
    private JButton xml431;
    private JButton xml702;
    private JButton xml417;
    private JButton xml552;
    private JButton xml512;
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
    private JLabel contractTypeLabelBox;
    private JComboBox<ContractTypeEnum> contractTypeBox;
    private JLabel financeTypeLabelBox;
    private JComboBox<FinanceTypeEnum> financeTypeBox;
    private JLabel turnoverTypeLabelBox;
    private JComboBox<TurnoverTypeEnum> turnoverTypeBox;
    private JLabel typeWithdrawalLabelBox;
    private JComboBox<TypeWithdrawalEnum> typeWithdrawalBox;
    private JLabel postingTypeLabelBox;
    private JComboBox<PostingTypeEnum> postingTypeBox;
    private final String[] schema251 = {"srcFile", "outFile", "sender", "receiver", "dateOperate", "reasonRecall"};
    private final String[] schema701 = {"srcFile", "outFile", "sender", "receiver", "dateOperate"};
    private final String[] schema431 = {"srcFile", "outFile", "sender", "receiver", "dateOperate", "docNum", "docDate"};
    private final String[] schema415 = {"srcFile", "outFile", "sender", "receiver", "dateOperate", "docNum", "docDate", "gosNum", "gosDate", "contractTypeBox", "financeTypeBox", "turnoverTypeBox"};
    private final String[] schema702 = {"srcFile", "outFile", "sender", "receiver", "inn", "kpp", "dateOperate", "docNum", "docDate", "gosNum", "gosDate", "contractTypeBox", "financeTypeBox", "postingTypeBox"};
    private final String[] schema417 = {"srcFile", "outFile", "sender", "receiver", "dateOperate", "docNum", "docDate"};
    private final String[] schema512 = {"srcFile", "outFile", "sender", "dateOperate", "docDate"};
    private final String[] schema552 = {"srcFile", "outFile", "sender", "dateOperate", "docNum", "docDate", "countryCode", "typeWithdrawalBox"};
    private ZonedDateTime time;
    public GeneratorGUI() {
        initFrame();
        window.setBounds(300, 100, 930, 820);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        JScrollPane scrollOutPane = new JScrollPane(outputField);
        JScrollPane scrollLogPane = new JScrollPane(logField);

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

        left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));

        left.add(xml251);
        left.add(xml415);
        left.add(xml417);
        left.add(xml431);
        left.add(xml512);
        left.add(xml552);
        left.add(xml701);
        left.add(xml702);

        right.add(new JLabel("Вывод"));
        right.add(scrollOutPane);
        right.add(new JLabel("Log"));
        right.add(scrollLogPane);

        footer.add(confirm);
        footer.add(reset);
        footer.add(clipboard);
        footer.setLayout(new FlowLayout());

        window.getContentPane().add(BorderLayout.PAGE_START, bar);
        window.getContentPane().add(BorderLayout.PAGE_END, footer);
        window.getContentPane().add(BorderLayout.CENTER, center);
        window.getContentPane().add(BorderLayout.LINE_END, right);
        window.getContentPane().add(BorderLayout.LINE_START, left);

        repaint();

        xml415.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 415;
            log(selectedSchema + xmlNumber);
            xml415.setBackground(Color.ORANGE);
            schema415LabelsNaming();
            generateForm(schema415);
        });
        xml552.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 552;
            log(selectedSchema + xmlNumber);
            xml552.setBackground(Color.ORANGE);
            schema552LabelsNaming();
            generateForm(schema552);
        });
        xml701.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 701;
            log(selectedSchema + xmlNumber);
            xml701.setBackground(Color.ORANGE);
            schema701LabelsNaming();
            generateForm(schema701);
        });
        xml251.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 251;
            log(selectedSchema + xmlNumber);
            xml251.setBackground(Color.ORANGE);
            schema251LabelsNaming();
            generateForm(schema251);
        });
        xml431.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 431;
            log(selectedSchema + xmlNumber);
            xml431.setBackground(Color.ORANGE);
            schema431LabelsNaming();
            generateForm(schema431);
        });
        xml417.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 417;
            log(selectedSchema + xmlNumber);
            xml417.setBackground(Color.ORANGE);
            schema417LabelsNaming();
            generateForm(schema417);
        });
        xml512.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 512;
            log(selectedSchema + xmlNumber);
            xml512.setBackground(Color.ORANGE);
            schema512LabelsNaming();
            generateForm(schema512);
        });
        xml702.addActionListener(e -> {
            reset.doClick();
            xmlNumber = 702;
            log(selectedSchema + xmlNumber);
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
            log("Сброс полей формы");
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
                log("Файл сохранен");
            }
            else {
                JOptionPane.showMessageDialog(window, "Не задан путь сохранения файла", "Ошибка", JOptionPane.WARNING_MESSAGE);
                log("Не задан путь сохранения файла");
            }
        });
        clipboard.addActionListener(e -> {
            String inputScanField = outputField.getText();
            StringSelection stringSelection = new StringSelection(inputScanField);
            Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            systemClipboard.setContents(stringSelection, null);
        });
        confirm.addActionListener(e -> {
            boolean dictMDsenser = false;
            boolean dictMDreceiver = false;
            String sender = senderMD.getText();
            if (sender.isBlank()) {
                dictMDsenser = true;
                sender = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getMd()));
            }
            String receiver = receiverMD.getText();
            if (receiver.isBlank()) {
                dictMDreceiver = true;
                receiver = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getMd()));
            }
            String contractTypeTeg = (String.valueOf(((ContractTypeEnum) Objects.requireNonNull(contractTypeBox.getSelectedItem())).getVariable()));
            String financeTypeTeg = (String.valueOf(((FinanceTypeEnum) Objects.requireNonNull(financeTypeBox.getSelectedItem())).getVariable()));
            String turnoverTypeTeg = (String.valueOf(((TurnoverTypeEnum) Objects.requireNonNull(turnoverTypeBox.getSelectedItem())).getVariable()));
            String postingTypeTeg = (String.valueOf(((PostingTypeEnum) Objects.requireNonNull(postingTypeBox.getSelectedItem())).getVariable()));
            String receiverOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(receiverMDBox.getSelectedItem())).getOrg()));
            String senderOrg = (String.valueOf(((AddressesMDEnum) Objects.requireNonNull(senderMDBox.getSelectedItem())).getOrg()));
            ExcelReader reader = new ExcelReader();
            ParserFile2Goods parser = new ParserFile2Goods(logField);
            switch (xmlNumber) {
                case 0 -> JOptionPane.showMessageDialog(window, "Не выбрана схема документа", "Ошибка", JOptionPane.ERROR_MESSAGE);
                case 552 -> {
                    if (checkRequiredField(xmlNumber)) {
                        try {
                            reader.clear();
                            reader.read(srcFile.getText());
                            log(selectedSchema + xmlNumber);
                            String typeWithdrawal = (String.valueOf(((TypeWithdrawalEnum) Objects.requireNonNull(typeWithdrawalBox.getSelectedItem())).getVariable()));
                            outputField.setText(String.valueOf(new Generate552xml(sender, dateOperate.getText(), docNum.getText(), docDate.getText(), typeWithdrawal, countryCode.getText(), reader.getData()).getXML()));
                        } catch (ExceptiionReadExcellFile exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile, "Ошибка", JOptionPane.ERROR_MESSAGE);
                            log(errReadExcellFile);
                        }
                    }
                    else log(errRqFields);
                }
                case 512 -> {
                    if (checkRequiredField(xmlNumber)) {
                        try {
                            reader.clear();
                            reader.read(srcFile.getText());
                            log(selectedSchema + xmlNumber);
                            outputField.setText(String.valueOf(new Generate512xml(sender, dateOperate.getText(), docDate.getText(), reader.getData()).getXML()));
                        } catch (ExceptiionReadExcellFile exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile, "Ошибка", JOptionPane.ERROR_MESSAGE);
                            log(errReadExcellFile);
                        }
                    }
                    else log(errRqFields);
                }
                case 415 -> {
                    if (checkRequiredField(xmlNumber)) {
                        if (dictMDreceiver && dictMDsenser) {
                            if (senderOrg.equals(receiverOrg)) {
                                JOptionPane.showMessageDialog(window, errorEqMD(0), "Ошибка", JOptionPane.ERROR_MESSAGE);
                                log(errorEqMD(0));
                                break;
                            }
                        }
                            try {
                                reader.clear();
                                reader.read(srcFile.getText());
                                log(selectedSchema + xmlNumber);
                                outputField.setText(String.valueOf(new Generate415xml(sender, receiver, dateOperate.getText(), docNum.getText(), docDate.getText(), gosNum.getText(), gosDate.getText(), contractTypeTeg, financeTypeTeg, turnoverTypeTeg, reader.getData()).getXML()));
                            } catch (ExceptiionReadExcellFile exception) {
                                JOptionPane.showMessageDialog(window, errReadExcellFile, "Ошибка", JOptionPane.ERROR_MESSAGE);
                                log(errReadExcellFile);
                            }
                    }
                    else log(errRqFields);
                }
                case 702 -> {
                    if (checkRequiredField(xmlNumber)) {
                        if (dictMDreceiver && dictMDsenser) {
                            if (senderOrg.equals(receiverOrg)) {
                                JOptionPane.showMessageDialog(window, errorEqMD(0), "Ошибка", JOptionPane.ERROR_MESSAGE);
                                log(errorEqMD(0));
                                break;
                            }
                        }
                        try {
                            reader.clear();
                            reader.read(srcFile.getText());
                            log(selectedSchema + xmlNumber);
                            outputField.setText(String.valueOf(new Generate702xml(sender, receiver, inn.getText(), kpp.getText(), dateOperate.getText(), docNum.getText(), docDate.getText(), gosNum.getText(), gosDate.getText(), contractTypeTeg, financeTypeTeg, postingTypeTeg, reader.getData()).getXML()));
                        } catch (ExceptiionReadExcellFile exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile, "Ошибка", JOptionPane.ERROR_MESSAGE);
                            log(errReadExcellFile);
                        }
                    }
                    else log(errRqFields);
                }
                case 701 -> {
                    if (checkRequiredField(xmlNumber)) {
                        if (dictMDreceiver && dictMDsenser) {
                            if (senderOrg.equals(receiverOrg)) {
                                JOptionPane.showMessageDialog(window, errorEqMD(0), "Ошибка", JOptionPane.ERROR_MESSAGE);
                                log(errorEqMD(0));
                                break;
                            }
                        }
                        try {
                            reader.clear();
                            reader.read(srcFile.getText());
                            log(selectedSchema + xmlNumber);
                            outputField.setText(String.valueOf(new Generate701xml(sender, receiver, dateOperate.getText(), reader.getData()).getXML()));
                        } catch (ExceptiionReadExcellFile exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile, "Ошибка", JOptionPane.ERROR_MESSAGE);
                            log(errReadExcellFile);
                        }
                    }
                    else log(errRqFields);
                }
                case 251 -> {
                    if (checkRequiredField(xmlNumber)) {
                        if (dictMDreceiver && dictMDsenser) {
                            if (senderOrg.equals(receiverOrg)) {
                                JOptionPane.showMessageDialog(window, errorEqMD(0), "Ошибка", JOptionPane.ERROR_MESSAGE);
                                log(errorEqMD(0));
                                break;
                            }
                        }
                        try {
                            ArrayList<Goods> goods = parser.read(srcFile.getText());
                            log(selectedSchema + xmlNumber);
                            outputField.setText(String.valueOf(new Generate251xml(sender, receiver, dateOperate.getText(), reasonRecall.getText(), goods, logField).getXML()));
                        } catch (ExceptiionReadExcellFile exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile, "Ошибка", JOptionPane.ERROR_MESSAGE);
                            log(errReadExcellFile);
                        }
                        catch (ExceptionParseFile2Goods exceptionParseFile2Goods) {
                            JOptionPane.showMessageDialog(window, errParserFile, "Ошибка", JOptionPane.ERROR_MESSAGE);
                            log(errParserFile);
                        }
                    }
                    else log(errRqFields);
                }
                case 431 -> {
                    if (checkRequiredField(xmlNumber)) {
                        if (dictMDreceiver && dictMDsenser) {
                            if (!senderOrg.equals(receiverOrg)) {
                                JOptionPane.showMessageDialog(window, errorEqMD(1), "Ошибка", JOptionPane.ERROR_MESSAGE);
                                log(errorEqMD(1));
                                break;
                            }
                        }
                        try {
                            reader.clear();
                            reader.read(srcFile.getText());
                            log(selectedSchema + xmlNumber);
                            outputField.setText(String.valueOf(new Generate431xml(sender, receiver, dateOperate.getText(), docNum.getText(), docDate.getText(), reader.getData()).getXML()));
                        } catch (ExceptiionReadExcellFile exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile, "Ошибка", JOptionPane.ERROR_MESSAGE);
                            log(errReadExcellFile);
                        }
                    }
                    else log(errRqFields);
            }
                case 417 -> {
                    if (checkRequiredField(xmlNumber)) {
                        if (dictMDreceiver && dictMDsenser) {
                            if (senderOrg.equals(receiverOrg)) {
                                JOptionPane.showMessageDialog(window, errorEqMD(0), "Ошибка", JOptionPane.ERROR_MESSAGE);
                                log(errorEqMD(0));
                                break;
                            }
                        }
                        try {
                            reader.clear();
                            reader.read(srcFile.getText());
                            outputField.setText(String.valueOf(new Generate417xml(sender, receiver, dateOperate.getText(), docNum.getText(), docDate.getText(), reader.getData()).getXML()));
                        } catch (ExceptiionReadExcellFile exception) {
                            JOptionPane.showMessageDialog(window, errReadExcellFile);
                            log(errReadExcellFile);
                        }
                    }
                    else log(errRqFields);
                }
                default -> JOptionPane.showMessageDialog(window, "Выбрана неизвестная схема документа", "Внимание", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    private void clearLabelsNaming() {
        for (Map.Entry<String, JLabel> pair : labelsMap.entrySet()) {
            pair.getValue().setText("");
        }
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
        contractTypeLabelBox.setText("Тип договора");
        financeTypeLabelBox.setText("Источник финансирования");
        turnoverTypeLabelBox.setText("Тип операции отгрузки со склада");
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
        contractTypeLabelBox.setText("Тип договора");
        financeTypeLabelBox.setText("Источник финансирования");
        postingTypeLabelBox.setText("Тип операции оприходования");
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
    private void schema552LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        dateOperateLabel.setText("Дата совершения операции");
        docNumLabel.setText("Реквизиты документа основания: номер документа");
        docDateLabel.setText("Реквизиты документа основания: дата документа");
        typeWithdrawalLabelBox.setText("Тип вывода из оборота");
        countryCodeLabel.setText("Код страны экспорта");
        window.setTitle("Вывод из оборота");
    }
    private void schema431LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        receiverMDLabel.setText("Идентификатор организации-получателя");
        dateOperateLabel.setText("Дата совершения операции");
        docNumLabel.setText("Регистрационный номер документа");
        docDateLabel.setText("Дата регистрации документа");
        typeWithdrawalLabelBox.setText("Тип вывода из оборота");
        window.setTitle("Перемещение");
    }
    private void schema417LabelsNaming() {
        schema431LabelsNaming();
        window.setTitle("Возврат приостановленных лекарственных препаратов");
    }
    private void schema512LabelsNaming() {
        srcFileLabel.setText("Файл Excell c даннми о отпускаемом товаре");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        dateOperateLabel.setText("Дата и время совершения операции");
        docDateLabel.setText("Дата регистрации документа отпуска");
        window.setTitle("Вывод из оборота с причиной «Отпуск по документу»");
    }
    private void schema701LabelsNaming() {
        srcFileLabel.setText("Файл Excell cо списком SGTIN");
        outFileLAbel.setText("Путь куда сохранить созданный файл");
        senderMDLabel.setText("Идентификатор организации-отправителя");
        receiverMDLabel.setText("Идентификатор организации-получателя");
        dateOperateLabel.setText("Дата совершения операции");
        window.setTitle("Подтверждение отгрузки / приемки");
    }
    private void initLabels() {
        labelsMap = new HashMap<>();
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
        contractTypeLabelBox = new JLabel();
        financeTypeLabelBox = new JLabel();
        turnoverTypeLabelBox = new JLabel();
        innLabel = new JLabel();
        kppLabel = new JLabel();
        typeWithdrawalLabelBox = new JLabel();
        countryCodeLabel = new JLabel();
        postingTypeLabelBox = new JLabel();
        labelsMap.put("srcFile", srcFileLabel);
        labelsMap.put("outFile", outFileLAbel);
        labelsMap.put("sender", senderMDLabel);
        labelsMap.put("receiver", receiverMDLabel);
        labelsMap.put("dateOperate", dateOperateLabel);
        labelsMap.put("gosNum", gosNumLabel);
        labelsMap.put("gosDate", gosDateLabel);
        labelsMap.put("docNum", docNumLabel);
        labelsMap.put("docDate", docDateLabel);
        labelsMap.put("reasonRecall", reasonRecallLabel);
        labelsMap.put("turnoverTypeBox", turnoverTypeLabelBox);
        labelsMap.put("financeTypeBox", financeTypeLabelBox);
        labelsMap.put("contractTypeBox", contractTypeLabelBox);
        labelsMap.put("inn", innLabel);
        labelsMap.put("kpp", kppLabel);
        labelsMap.put("countryCode", countryCodeLabel);
        labelsMap.put("typeWithdrawalBox", typeWithdrawalLabelBox);
        labelsMap.put("postingTypeBox", postingTypeLabelBox);
    }
    private void initTextFields() {
        fieldsMap = new HashMap<>();
        srcFile = new JTextField(30);
        outFile = new JTextField(30);
        senderMD = new JTextField(30);
        receiverMD = new JTextField(30);
        dateOperate = new JTextField(23);
        outputField = new JTextArea(35, 40);
        logField = new JTextArea(5, 40);
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
        fieldsMap.put("sender", senderMD);
        fieldsMap.put("receiver", receiverMD);
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
        comboBoxMap = new HashMap<>();
        senderMDBox = new JComboBox<>(AddressesMDEnum.values());
        receiverMDBox = new JComboBox<>(AddressesMDEnum.values());
        contractTypeBox = new JComboBox<>(ContractTypeEnum.values());
        financeTypeBox = new JComboBox<>(FinanceTypeEnum.values());
        turnoverTypeBox = new JComboBox<>(TurnoverTypeEnum.values());
        typeWithdrawalBox = new JComboBox<>(TypeWithdrawalEnum.values());
        postingTypeBox = new JComboBox<>(PostingTypeEnum.values());
        comboBoxMap.put("sender", senderMDBox);
        comboBoxMap.put("receiver", receiverMDBox);
        comboBoxMap.put("contractTypeBox", contractTypeBox);
        comboBoxMap.put("financeTypeBox", financeTypeBox);
        comboBoxMap.put("turnoverTypeBox", turnoverTypeBox);
        comboBoxMap.put("typeWithdrawalBox", typeWithdrawalBox);
        comboBoxMap.put("postingTypeBox", postingTypeBox);
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
        xml512 = new JButton("512");
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
        xml512.setBackground(Color.WHITE);
        confirm.setBackground(Color.GREEN);
        clipboard.setBackground(Color.MAGENTA);
        reset.setBackground(Color.RED);
    }
    private void bleachingFields() {
        for (Map.Entry<String, JTextField> pair : fieldsMap.entrySet()) {
            pair.getValue().setBackground(Color.WHITE);
        }
    }
    private void defaultLabelsColor() {
        for (Map.Entry<String, JLabel> pair : labelsMap.entrySet()) {
            pair.getValue().setForeground(Color.BLACK);
        }
    }
    private void clearFields() {
        for (Map.Entry<String, JTextField> pair : fieldsMap.entrySet()) {
            pair.getValue().setText("");
        }
        outputField.setText("");
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
    private void optionFields251() {
        senderMD.setForeground(Color.GRAY);
        senderMD.setToolTipText("Не обязательное поле");
        receiverMD.setForeground(Color.GRAY);
        receiverMD.setToolTipText("Не обязательное поле");
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void optionFields701() {
        senderMD.setForeground(Color.GRAY);
        senderMD.setToolTipText("Не обязательное поле");
        receiverMD.setForeground(Color.GRAY);
        receiverMD.setToolTipText("Не обязательное поле");
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void optionFields552() {
        senderMD.setForeground(Color.GRAY);
        senderMD.setToolTipText("Не обязательное поле");
        receiverMD.setForeground(Color.GRAY);
        receiverMD.setToolTipText("Не обязательное поле");
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
        countryCodeLabel.setForeground(Color.GRAY);
        countryCodeLabel.setToolTipText("Не обязательное поле, формат A-Z 2 символа (RU)");
    }
    private void optionFields415() {
        senderMD.setForeground(Color.GRAY);
        senderMD.setToolTipText("Не обязательное поле");
        receiverMD.setForeground(Color.GRAY);
        receiverMD.setToolTipText("Не обязательное поле");
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
        gosDateLabel.setForeground(Color.GRAY);
        gosDateLabel.setToolTipText("Не обязательное поле");
        gosNumLabel.setForeground(Color.GRAY);
        gosNumLabel.setToolTipText("Не обязательное поле");
    }
    private void optionFields702() {
        senderMD.setForeground(Color.GRAY);
        senderMD.setToolTipText("Не обязательное поле");
        receiverMD.setForeground(Color.GRAY);
        receiverMD.setToolTipText("Не обязательное поле");
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
        senderMD.setForeground(Color.GRAY);
        senderMD.setToolTipText("Не обязательное поле");
        receiverMD.setForeground(Color.GRAY);
        receiverMD.setToolTipText("Не обязательное поле");
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void optionFields512() {
        senderMD.setForeground(Color.GRAY);
        senderMD.setToolTipText("Не обязательное поле");
        outFileLAbel.setForeground(Color.GRAY);
        outFileLAbel.setToolTipText("Не обязательное поле");
    }
    private void optionFields417() {
        senderMD.setForeground(Color.GRAY);
        senderMD.setToolTipText("Не обязательное поле");
        receiverMD.setForeground(Color.GRAY);
        receiverMD.setToolTipText("Не обязательное поле");
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
        xml512.setBackground(Color.white);
    }
    private boolean checkRequiredField(int schema) {
        boolean srcFileFlag = !srcFile.getText().isBlank();
        boolean senderMdFlag = !senderMD.getText().isBlank();
        boolean receiverMdFlag = !receiverMD.getText().isBlank();
        boolean dateOperateFlag = !dateOperate.getText().isBlank();
        boolean docNumFlag = !docNum.getText().isBlank();
        boolean docDateFlag = !docDate.getText().isBlank();
        boolean reasonRecallFlag = !reasonRecall.getText().isBlank();
        boolean innFlag = !inn.getText().isBlank();
        if (!srcFileFlag) srcFile.setBackground(Color.RED);
        else srcFile.setBackground(Color.WHITE);
        if (!senderMdFlag) {
            senderMD.setBackground(Color.WHITE);
            log("МД отправителя взят из справочника");
        }
        else {
            senderMD.setBackground(Color.CYAN);
            log("МД отправителя введен вручную");
        }
        if (!receiverMdFlag) {
            receiverMD.setBackground(Color.WHITE);
            log("МД получателя взят из справочника");
        }
        else {
            receiverMD.setBackground(Color.CYAN);
            log("МД получателя введен вручную");
        }
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
            case 415, 431, 417, 552-> srcFileFlag && dateOperateFlag && docNumFlag && docDateFlag;
            case 251 -> srcFileFlag && dateOperateFlag && reasonRecallFlag;
            case 701 -> srcFileFlag && dateOperateFlag;
            case 512 -> srcFileFlag && dateOperateFlag && docDateFlag;
            case 702 -> srcFileFlag && dateOperateFlag && docNumFlag && docDateFlag && innFlag;
            default -> throw new IllegalStateException("Неизвестный тип схемы: " + schema);
        };
    }
    private void generateForm(String[] list) {
        for (String field : list) {
            center.add(labelsMap.get(field));
            if (field.equals("sender") || field.equals("receiver")) {
                center.add(comboBoxMap.get(field));
                center.add(fieldsMap.get(field));
            }
            if (field.endsWith("Box")) center.add(comboBoxMap.get(field));
            else center.add(fieldsMap.get(field));
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
        switch (xmlNumber) {
            case 251 -> optionFields251();
            case 701 -> optionFields701();
            case 415 -> optionFields415();
            case 431 -> optionFields431();
            case 702 -> optionFields702();
            case 417 -> optionFields417();
            case 552 -> optionFields552();
            case 512 -> optionFields512();
            default -> defaultLabelsColor();
        }
        repaint();
    }
    private void repaint() {
        window.setVisible(true);
        window.repaint();
    }
    private String errorEqMD (int i) {
        String not = "";
        if (i == 1)
            not = "не ";
        return "Места деятельности отправителя и получателя\n" + not + "принадлежат одному юридическому лицу.\nЭто недопустимо для выбранной схемы документа";
    }
    private void log(String message) {
        logField.append("\n" + message);
    }

    private void initFrame() {
        window = new JFrame("Генератор МДЛП документа");
        time = ZonedDateTime.now(ZoneOffset.UTC);
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
