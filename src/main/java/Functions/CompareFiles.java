package Functions;

import Objects.ComparableFile;
import net.ricecode.similarity.StringSimilarityService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.IOException;

public record CompareFiles(StringSimilarityService service, ComparableFile file1,
                           ComparableFile file2, boolean doCompareNames, boolean doCompareBytes,
                           boolean doCompareContents) implements Runnable {

    public void run() {

        if (this.doCompareContents && this.doCompareNames) {
            if (compareFileExtensions()) {
                compareBytes();
                if (!this.file1.getFlag() && !this.file2.getFlag()) {
                    if (this.file1.getFile().getName().split("\\.")[this.file1.getFile().getName().split("\\.").length - 1].equals("docx")) {
                        compareDocx();
                    } else if (this.file1.getFile().getName().split("\\.")[this.file1.getFile().getName().split("\\.").length - 1].equals("xlsx")) {
                        compareXLSX();
                    } else {
                        compareNames();
                    }
                }
            } else {
                compareNames();
            }
        } else if (this.doCompareBytes && this.doCompareNames) {
            if (compareFileExtensions()) {
                compareBytes();
            }
            if (!this.file1.getFlag() && !this.file2.getFlag()) {
                compareNames();
            }
        } else if (this.doCompareContents) {
            if (compareFileExtensions()) {
                compareBytes();
                if (!this.file1.getFlag() && !this.file2.getFlag()) {
                    if (this.file1.getFile().getName().split("\\.")[this.file1.getFile().getName().split("\\.").length - 1].equals("docx")) {
                        compareDocx();
                    } else if (this.file1.getFile().getName().split("\\.")[this.file1.getFile().getName().split("\\.").length - 1].equals("xlsx")) {
                        compareXLSX();
                    }
                }
            }
        } else if (this.doCompareBytes) {
            if (compareFileExtensions()) {
                compareBytes();
            }
        } else if (this.doCompareNames) {
            compareNames();
        }

    }

    public boolean compareFileExtensions() {

        String[] file1Split = this.file1.getFile().getName().split("\\.");
        String[] file2Split = this.file2.getFile().getName().split("\\.");

        return file1Split[file1Split.length - 1].equals(file2Split[file2Split.length - 1]);

    }

    public void compareBytes() {

        try {
            if (FileUtils.contentEquals(this.file1.getFile(), this.file2.getFile())) {

                //Set flags to true to indicate files have been analyzed
                this.file1.setFlag(true);
                this.file2.setFlag(true);
                this.file2.setIdenticalPointer(this.file1.getIndex());

                System.out.println(this.file1.getFile().getName() + "  -=->  " + this.file2.getFile().getName());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void compareNames() {

        if (this.service.score(this.file1.getFile().getName(), this.file2.getFile().getName()) >= 0.8) {

            this.file1.setFlag(true);
            this.file2.setFlag(true);
            this.file2.setNameSimilarPointer(this.file1.getIndex());

            System.out.println(this.file1.getFile().getName() + "  -/->  " + this.file2.getFile().getName());

        }

    }

    public void compareDocx() {

        String file1Text = null;
        String file2Text = null;

        try (FileInputStream input = new FileInputStream(this.file1.getFile().getAbsolutePath())) {

            XWPFDocument file1Docx = new XWPFDocument(OPCPackage.open(input));
            XWPFWordExtractor extractor = new XWPFWordExtractor(file1Docx);
            file1Text = extractor.getText();

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        try (FileInputStream input = new FileInputStream(this.file2.getFile().getAbsolutePath())) {

            XWPFDocument file2Docx = new XWPFDocument(OPCPackage.open(input));
            XWPFWordExtractor extractor = new XWPFWordExtractor(file2Docx);
            file2Text = extractor.getText();

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        if (file1Text != null && file2Text != null && this.service.score(file1Text, file2Text) >= 0.75) {

            this.file1.setFlag(true);
            this.file2.setFlag(true);
            this.file2.setContentsSimilarPointer(this.file1.getIndex());

            System.out.println(this.file1.getFile().getName() + "  -+->  " + this.file2.getFile().getName());

        }

    }

    public void compareXLSX() {

        String file1Text = null;
        String file2Text = null;

        try (FileInputStream input = new FileInputStream(this.file1.getFile().getAbsolutePath())) {

            StringBuilder stringBuilder = new StringBuilder();
            Workbook workbook = WorkbookFactory.create(input);

            if (workbook.getNumberOfSheets() > 0) {
                for (int i = 0; i <= workbook.getNumberOfSheets() - 1; i++) {

                    for (int j = 0; j <= workbook.getSheetAt(i).getLastRowNum() - 1; j++) {

                        if (workbook.getSheetAt(i).getRow(j) != null) {
                            for (int k = 0; k <= workbook.getSheetAt(i).getRow(j).getLastCellNum() - 1; k++) {

                                if (workbook.getSheetAt(i).getRow(j).getCell(k) != null) {
                                    stringBuilder.append(workbook.getSheetAt(i).getRow(j).getCell(k).toString()).append(",");
                                }

                            }
                        }

                    }

                }
            }

            file1Text = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream input = new FileInputStream(this.file2.getFile().getAbsolutePath())) {

            StringBuilder stringBuilder = new StringBuilder();
            Workbook workbook = WorkbookFactory.create(input);

            if (workbook.getNumberOfSheets() > 0) {
                for (int i = 0; i <= workbook.getNumberOfSheets() - 1; i++) {

                    for (int j = 0; j <= workbook.getSheetAt(i).getLastRowNum() - 1; j++) {

                        if (workbook.getSheetAt(i).getRow(j) != null) {
                            for (int k = 0; k <= workbook.getSheetAt(i).getRow(j).getLastCellNum() - 1; k++) {

                                if (workbook.getSheetAt(i).getRow(j).getCell(k) != null) {
                                    stringBuilder.append(workbook.getSheetAt(i).getRow(j).getCell(k).toString()).append(",");
                                }

                            }
                        }

                    }

                }
            }

            file2Text = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file1Text != null && file2Text != null && this.service.score(file1Text, file2Text) >= 0.75) {

            this.file1.setFlag(true);
            this.file2.setFlag(true);
            this.file2.setContentsSimilarPointer(this.file1.getIndex());

            System.out.println(this.file1.getFile().getName() + "  -+->  " + this.file2.getFile().getName());

        }

    }

}
