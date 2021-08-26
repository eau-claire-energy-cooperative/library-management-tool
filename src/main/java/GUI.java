import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GUI implements ActionListener {

    String osSeparator;
    final Taskbar taskbar;

    SimpleDateFormat simpleDateFormat;
    LibraryManagementTool libraryManagementTool;

    ArrayList<JFileChooser> fileChoosers;
    ArrayList<JTextField> textFields;
    ArrayList<JCheckBox> checkBoxes;
    ArrayList<JLabel> resultLabels;

    public GUI(LibraryManagementTool libraryManagementTool) {

        this.osSeparator = getOperatingSystemSeparator();
        this.taskbar = Taskbar.getTaskbar();

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch(Exception exception) {
            System.out.println("Failed to initialize LaF");
        }
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);

        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.libraryManagementTool = libraryManagementTool;

        this.fileChoosers = new ArrayList<>();
        this.textFields = new ArrayList<>();
        this.checkBoxes = new ArrayList<>();
        this.resultLabels = new ArrayList<>();

        JFrame frame = new JFrame("Library Management Tool");
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel gen_arch_Panel = new JPanel();
        JPanel arch_files_Panel = new JPanel();
        JPanel gen_ss_Panel = new JPanel();
        JPanel del_empt_dirs_Panel = new JPanel();
        JPanel del_file_by_name_Panel = new JPanel();

        setTabbedPane(tabbedPane, gen_arch_Panel, arch_files_Panel, gen_ss_Panel, del_empt_dirs_Panel, del_file_by_name_Panel);
        setFrame(frame);

        frame.add(tabbedPane);

        gen_arch_Panel.setLayout(new GridBagLayout());
        GridBagConstraints gapCons = new GridBagConstraints();
        arch_files_Panel.setLayout(new GridBagLayout());
        GridBagConstraints afpCons = new GridBagConstraints();
        gen_ss_Panel.setLayout(new GridBagLayout());
        GridBagConstraints gsspCons = new GridBagConstraints();
        del_empt_dirs_Panel.setLayout(new GridBagLayout());
        GridBagConstraints dedpCons = new GridBagConstraints();
        del_file_by_name_Panel.setLayout(new GridBagLayout());
        GridBagConstraints dfbnpCons = new GridBagConstraints();

        setGen_Arch_Panel(gen_arch_Panel, gapCons);
        setArch_Files_Panel(arch_files_Panel, afpCons);
        setGen_SS_Panel(gen_ss_Panel, gsspCons);
        setDel_Empt_Dirs(del_empt_dirs_Panel, dedpCons);
        setDel_File_By_Name(del_file_by_name_Panel, dfbnpCons);

    }

    public void setFrame(JFrame frame) {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setPreferredSize(new Dimension(800, 800));

        java.net.URL imgURL = getClass().getResource("resources" + this.osSeparator + "Library-Management-Tool-logo.jpg");
        assert imgURL != null;
        ImageIcon imageIcon = new ImageIcon(imgURL);
        Image iconImage = imageIcon.getImage();
        frame.setIconImage(iconImage);
        if(System.getProperty("os.name").charAt(0) == 'M') {
            taskbar.setIconImage(iconImage);
        }

        frame.pack();
        frame.setVisible(true);

    }

    public void setTabbedPane(JTabbedPane tabbedPane, JPanel gen_arch_Panel, JPanel arch_files_Panel, JPanel gen_ss_Panel, JPanel del_empt_dirs_Panel, JPanel del_file_by_name_Panel) {

        tabbedPane.addTab("Generate Archive", gen_arch_Panel);
        tabbedPane.addTab("Archive Files", arch_files_Panel);
        tabbedPane.addTab("Generate Spreadsheet", gen_ss_Panel);
        tabbedPane.addTab("Delete Empty Directories", del_empt_dirs_Panel);
        tabbedPane.addTab("Delete Files By Name", del_file_by_name_Panel);

    }

    public void setGen_Arch_Panel(JPanel gen_arch_Panel, GridBagConstraints gapCons) {

        gapCons.gridx = 0;
        gapCons.gridy = 0;

        JFileChooser fileChooser = new JFileChooser();
        this.fileChoosers.add(fileChooser);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        gen_arch_Panel.add(fileChooser, gapCons);
        gapCons.gridy++;

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints fieldsCons = new GridBagConstraints();
        fieldsCons.gridx = 0;
        fieldsCons.gridy = 0;

        Dimension textBoxDimension = new Dimension(300, 26);

        JTextField sourceLocation = new JTextField();
        this.textFields.add(sourceLocation);
        sourceLocation.setPreferredSize(textBoxDimension);
        fieldsPanel.add(sourceLocation, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JButton sourceLocationButton = new JButton("Set source location");
        fieldsPanel.add(sourceLocationButton, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField destLocation = new JTextField();
        this.textFields.add(destLocation);
        destLocation.setPreferredSize(textBoxDimension);
        fieldsPanel.add(destLocation, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(20), fieldsCons);
        fieldsCons.gridx++;
        JButton destLocationButton = new JButton("Set destination location");
        fieldsPanel.add(destLocationButton, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(20), fieldsCons);
        fieldsCons.gridy++;

        JLabel resultLabel = new JLabel("Result");
        this.resultLabels.add(resultLabel);
        fieldsPanel.add(resultLabel, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(20), fieldsCons);
        fieldsCons.gridx++;
        JButton submitButton = new JButton("Submit");
        fieldsPanel.add(submitButton, fieldsCons);

        gen_arch_Panel.add(Box.createVerticalStrut(20), gapCons);
        gapCons.gridy++;
        gen_arch_Panel.add(fieldsPanel, gapCons);

        sourceLocationButton.addActionListener(this);
        sourceLocationButton.setActionCommand("0_0_Gen_Arch");
        destLocationButton.addActionListener(this);
        destLocationButton.setActionCommand("0_1_Gen_Arch");
        submitButton.addActionListener(this);
        submitButton.setActionCommand("Gen_Arch_submit");

    }

    public void setArch_Files_Panel(JPanel arch_files_Panel, GridBagConstraints afpCons) {

        afpCons.gridx = 0;
        afpCons.gridy = 0;

        JFileChooser fileChooser = new JFileChooser();
        this.fileChoosers.add(fileChooser);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        arch_files_Panel.add(fileChooser, afpCons);
        afpCons.gridy++;

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints fieldsCons = new GridBagConstraints();
        fieldsCons.gridx = 0;
        fieldsCons.gridy = 0;

        Dimension textBoxDimension = new Dimension(300, 26);

        JTextField sourceLocation = new JTextField();
        this.textFields.add(sourceLocation);
        sourceLocation.setPreferredSize(textBoxDimension);
        fieldsPanel.add(sourceLocation, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JButton sourceLocationButton = new JButton("Set source location");
        fieldsPanel.add(sourceLocationButton, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField destLocation = new JTextField();
        this.textFields.add(destLocation);
        destLocation.setPreferredSize(textBoxDimension);
        fieldsPanel.add(destLocation, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JButton destLocationButton = new JButton("Set destination location");
        fieldsPanel.add(destLocationButton, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField dateThreshold = new JTextField();
        this.textFields.add(dateThreshold);
        dateThreshold.setPreferredSize(textBoxDimension);
        fieldsPanel.add(dateThreshold, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JLabel dateThresholdLabel = new JLabel("\"Old\" date threshold (YYYY-MM-DD)");
        fieldsPanel.add(dateThresholdLabel, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JLabel resultLabel = new JLabel("Result");
        this.resultLabels.add(resultLabel);
        fieldsPanel.add(resultLabel, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(20), fieldsCons);
        fieldsCons.gridx++;
        JButton submitButton = new JButton("Submit");
        fieldsPanel.add(submitButton, fieldsCons);

        arch_files_Panel.add(Box.createVerticalStrut(20), afpCons);
        afpCons.gridy++;
        arch_files_Panel.add(fieldsPanel, afpCons);

        sourceLocationButton.addActionListener(this);
        sourceLocationButton.setActionCommand("1_2_Arch_Files");
        destLocationButton.addActionListener(this);
        destLocationButton.setActionCommand("1_3_Arch_Files");
        submitButton.addActionListener(this);
        submitButton.setActionCommand("Arch_Files_submit");

    }

    public void setGen_SS_Panel(JPanel gen_ss_Panel, GridBagConstraints gsspCons) {

        gsspCons.gridx = 0;
        gsspCons.gridy = 0;

        JFileChooser fileChooser = new JFileChooser();
        this.fileChoosers.add(fileChooser);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        gen_ss_Panel.add(fileChooser, gsspCons);
        gsspCons.gridy++;

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints fieldsCons = new GridBagConstraints();
        fieldsCons.gridx = 0;
        fieldsCons.gridy = 0;

        Dimension textBoxDimension = new Dimension(300, 26);

        JTextField sourceLocation = new JTextField();
        this.textFields.add(sourceLocation);
        sourceLocation.setPreferredSize(textBoxDimension);
        fieldsPanel.add(sourceLocation, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JButton sourceLocationButton = new JButton("Set source location");
        fieldsPanel.add(sourceLocationButton, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField destLocation = new JTextField();
        this.textFields.add(destLocation);
        destLocation.setPreferredSize(textBoxDimension);
        fieldsPanel.add(destLocation, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JButton destLocationButton = new JButton("Set destination .xlsx file");
        fieldsPanel.add(destLocationButton, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField oldDateThreshold = new JTextField();
        this.textFields.add(oldDateThreshold);
        oldDateThreshold.setPreferredSize(textBoxDimension);
        fieldsPanel.add(oldDateThreshold, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JLabel oldDateThresholdLabel = new JLabel("\"Old\" date threshold (YYYY-MM-DD)");
        fieldsPanel.add(oldDateThresholdLabel, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField numThreads = new JTextField();
        this.textFields.add(numThreads);
        numThreads.setPreferredSize(textBoxDimension);
        fieldsPanel.add(numThreads, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JLabel numThreadsLabel = new JLabel("Number of threads to use");
        fieldsPanel.add(numThreadsLabel, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField bottomDateThreshold = new JTextField();
        this.textFields.add(bottomDateThreshold);
        bottomDateThreshold.setPreferredSize(textBoxDimension);
        fieldsPanel.add(bottomDateThreshold, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JLabel bottomDateThresholdLabel = new JLabel("Bottom date threshold (YYYY-MM-DD)");
        fieldsPanel.add(bottomDateThresholdLabel, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField middleDateThreshold = new JTextField();
        this.textFields.add(middleDateThreshold);
        middleDateThreshold.setPreferredSize(textBoxDimension);
        fieldsPanel.add(middleDateThreshold, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JLabel middleDateThresholdLabel = new JLabel("Middle date threshold (YYYY-MM-DD)");
        fieldsPanel.add(middleDateThresholdLabel, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField topDateThreshold = new JTextField();
        this.textFields.add(topDateThreshold);
        topDateThreshold.setPreferredSize(textBoxDimension);
        fieldsPanel.add(topDateThreshold, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JLabel topDateThresholdLabel = new JLabel("Top date threshold (YYYY-MM-DD)");
        fieldsPanel.add(topDateThresholdLabel, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JCheckBox compareNamesOnly = new JCheckBox();
        this.checkBoxes.add(compareNamesOnly);
        fieldsPanel.add(compareNamesOnly, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JLabel compareNamesOnlyLabel = new JLabel("Compare file names only?");
        fieldsPanel.add(compareNamesOnlyLabel, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JLabel resultLabel = new JLabel("Result");
        this.resultLabels.add(resultLabel);
        fieldsPanel.add(resultLabel, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(20), fieldsCons);
        fieldsCons.gridx++;
        JButton submitButton = new JButton("Submit");
        fieldsPanel.add(submitButton, fieldsCons);

        gen_ss_Panel.add(Box.createVerticalStrut(20), gsspCons);
        gsspCons.gridy++;
        gen_ss_Panel.add(fieldsPanel, gsspCons);

        sourceLocationButton.addActionListener(this);
        sourceLocationButton.setActionCommand("2_5_Gen_SS_Files");
        destLocationButton.addActionListener(this);
        destLocationButton.setActionCommand("2_6_Gen_SS_Files");
        submitButton.addActionListener(this);
        submitButton.setActionCommand("Gen_SS_submit");

    }

    public void setDel_Empt_Dirs(JPanel del_empt_dirs_Panel, GridBagConstraints dedpCons) {

        dedpCons.gridx = 0;
        dedpCons.gridy = 0;

        JFileChooser fileChooser = new JFileChooser();
        this.fileChoosers.add(fileChooser);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        del_empt_dirs_Panel.add(fileChooser, dedpCons);
        dedpCons.gridy++;

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints fieldsCons = new GridBagConstraints();
        fieldsCons.gridx = 0;
        fieldsCons.gridy = 0;

        Dimension textBoxDimension = new Dimension(300, 26);

        JTextField sourceLocation = new JTextField();
        this.textFields.add(sourceLocation);
        sourceLocation.setPreferredSize(textBoxDimension);
        fieldsPanel.add(sourceLocation, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JButton sourceLocationButton = new JButton("Set source location");
        fieldsPanel.add(sourceLocationButton, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JLabel resultLabel = new JLabel("Result");
        this.resultLabels.add(resultLabel);
        fieldsPanel.add(resultLabel, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(20), fieldsCons);
        fieldsCons.gridx++;
        JButton submitButton = new JButton("Submit");
        fieldsPanel.add(submitButton, fieldsCons);

        del_empt_dirs_Panel.add(Box.createVerticalStrut(20), dedpCons);
        dedpCons.gridy++;
        del_empt_dirs_Panel.add(fieldsPanel, dedpCons);

        sourceLocationButton.addActionListener(this);
        sourceLocationButton.setActionCommand("3_12_Del_Empt_Dirs");
        submitButton.addActionListener(this);
        submitButton.setActionCommand("Del_Empt_Dirs_submit");

    }

    public void setDel_File_By_Name(JPanel del_file_by_name_Panel, GridBagConstraints dfbnpCons) {

        dfbnpCons.gridx = 0;
        dfbnpCons.gridy = 0;

        JFileChooser fileChooser = new JFileChooser();
        this.fileChoosers.add(fileChooser);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        del_file_by_name_Panel.add(fileChooser, dfbnpCons);
        dfbnpCons.gridy++;

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints fieldsCons = new GridBagConstraints();
        fieldsCons.gridx = 0;
        fieldsCons.gridy = 0;

        Dimension textBoxDimension = new Dimension(300, 26);

        JTextField sourceLocation = new JTextField();
        this.textFields.add(sourceLocation);
        sourceLocation.setPreferredSize(textBoxDimension);
        fieldsPanel.add(sourceLocation, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JButton sourceLocationButton = new JButton("Set source location");
        fieldsPanel.add(sourceLocationButton, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JTextField fileName = new JTextField();
        this.textFields.add(fileName);
        fileName.setPreferredSize(textBoxDimension);
        fieldsPanel.add(fileName, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(10), fieldsCons);
        fieldsCons.gridx++;
        JButton fileNameButton = new JButton("Set file to delete");
        fieldsPanel.add(fileNameButton, fieldsCons);
        fieldsCons.gridx = 0;
        fieldsCons.gridy++;
        fieldsPanel.add(Box.createVerticalStrut(10), fieldsCons);
        fieldsCons.gridy++;

        JLabel resultLabel = new JLabel("Result");
        this.resultLabels.add(resultLabel);
        fieldsPanel.add(resultLabel, fieldsCons);
        fieldsCons.gridx++;
        fieldsPanel.add(Box.createHorizontalStrut(20), fieldsCons);
        fieldsCons.gridx++;
        JButton submitButton = new JButton("Submit");
        fieldsPanel.add(submitButton, fieldsCons);

        del_file_by_name_Panel.add(Box.createVerticalStrut(20), dfbnpCons);
        dfbnpCons.gridy++;
        del_file_by_name_Panel.add(fieldsPanel, dfbnpCons);

        sourceLocationButton.addActionListener(this);
        sourceLocationButton.setActionCommand("4_13_Del_File_By_Name");
        submitButton.addActionListener(this);
        submitButton.setActionCommand("Del_File_By_Name_submit");

    }

    public String getOperatingSystemSeparator() {

        if(System.getProperty("os.name").charAt(0) == 'W') {
            return "\\";
        } else {
            return "/";
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {

            case "0_0_Gen_Arch":
            case "0_1_Gen_Arch":
            case "1_2_Arch_Files":
            case "1_3_Arch_Files":
            case "2_5_Gen_SS_Files":
            case "2_6_Gen_SS_Files":
            case "3_12_Del_Empt_Dirs":
            case "4_13_Del_File_By_Name":
                if(this.fileChoosers.get(Integer.parseInt(e.getActionCommand().split("_")[0])).getSelectedFile() != null) {
                    this.textFields.get(Integer.parseInt(e.getActionCommand().split("_")[1])).setText(
                            this.fileChoosers.get(Integer.parseInt(e.getActionCommand().split("_")[0])).getSelectedFile().getAbsolutePath()
                    );
                }
                break;

            case "Gen_Arch_submit":
                System.out.println("Source: " + this.textFields.get(0).getText() + this.osSeparator
                + "\nDestination: " + this.textFields.get(1).getText() + this.osSeparator);
                try {
                    this.libraryManagementTool.generateArchive(this.textFields.get(0).getText() + this.osSeparator, this.textFields.get(1).getText() + this.osSeparator);
                    this.resultLabels.get(0).setText("Success!");
                } catch (IOException ex) {
                    this.resultLabels.get(0).setText("Failed...");
                    ex.printStackTrace();
                }
                break;

            case "Arch_Files_submit":
                System.out.println("Source: " + this.textFields.get(2).getText() + this.osSeparator
                + "\nDestination: " + this.textFields.get(3).getText() + this.osSeparator
                + "\nDate Threshold: " + this.textFields.get(4).getText());
                try {
                    this.libraryManagementTool.archiveFiles(this.textFields.get(2).getText() + this.osSeparator, this.textFields.get(3).getText() + this.osSeparator, this.simpleDateFormat.parse(this.textFields.get(4).getText()));
                    this.resultLabels.get(1).setText("Success!");
                } catch (IOException | ParseException ex) {
                    this.resultLabels.get(0).setText("Failed...");
                    ex.printStackTrace();
                }
                break;

            case "Gen_SS_submit":
                System.out.println("Source: " + this.textFields.get(5).getText() + this.osSeparator
                + "\nDestination file: " + this.textFields.get(6).getText()
                + "\nOld Date Threshold: " + this.textFields.get(7).getText()
                + "\nNumber of Threads: " + this.textFields.get(8).getText()
                + "\nBottom Date Threshold: " + this.textFields.get(9).getText()
                + "\nMiddle Date Threshold: " + this.textFields.get(10).getText()
                + "\nTop Date Threshold: " + this.textFields.get(11).getText());
                try {
                    this.libraryManagementTool.generateSpreadsheet(this.textFields.get(5).getText() + this.osSeparator, this.textFields.get(6).getText(), this.simpleDateFormat.parse(this.textFields.get(7).getText()), Integer.parseInt(this.textFields.get(8).getText()), this.simpleDateFormat.parse(this.textFields.get(9).getText()), this.simpleDateFormat.parse(this.textFields.get(10).getText()), this.simpleDateFormat.parse(this.textFields.get(11).getText()), this.checkBoxes.get(0).isSelected());
                    this.resultLabels.get(2).setText("Success!");
                } catch (ParseException ex) {
                    this.resultLabels.get(0).setText("Failed...");
                    ex.printStackTrace();
                }
                break;

            case "Del_Empt_Dirs_submit":
                System.out.println("Source: " + this.textFields.get(12).getText() + this.osSeparator);
                try {
                    this.libraryManagementTool.deleteEmptyDirectories(this.textFields.get(12).getText() + this.osSeparator);
                    this.resultLabels.get(3).setText("Success!");
                } catch (IOException ex) {
                    this.resultLabels.get(0).setText("Failed...");
                    ex.printStackTrace();
                }
                break;

            case "Del_File_By_Name_submit":
                System.out.println("Source: " + this.textFields.get(13).getText() + this.osSeparator
                + "File to delete: " + this.textFields.get(14).getText());
                try {
                    this.libraryManagementTool.deleteFileByName(this.textFields.get(13).getText() + this.osSeparator, this.textFields.get(14).getText());
                    this.resultLabels.get(4).setText("Success!");
                } catch (IOException ex) {
                    this.resultLabels.get(0).setText("Failed...");
                    ex.printStackTrace();
                }
                break;

        }

    }

}
