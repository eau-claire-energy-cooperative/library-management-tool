package Functions;

import Objects.ComparableFile;
import Objects.Spreadsheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

public class GenerateSpreadsheet {

    private final Spreadsheet spreadsheet;
    private final Workbook workbook;

    public GenerateSpreadsheet(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
        this.workbook = this.spreadsheet.getWorkbook();
    }

    /**
     * Creates and fills spreadsheet with the contents of an ArrayList.
     * @param fileContents The contents with which to populate the spreadsheet.
     * @param sheetName The name of the sheet being created and populated.
     */
    public void fillSpreadsheetWithList(ArrayList<File> fileContents, String sheetName) {

        Sheet sheet = this.workbook.createSheet(sheetName);
        Row row;
        CellStyle dateStyle = this.spreadsheet.getDateStyle();

        System.out.println("Adding files to \"" + sheetName + "\"");

        Date date;
        double totalSpace = 0;

        for(int i = 0; i <= fileContents.size() - 1; i++) {

            row = sheet.createRow(i);

            row.createCell(0);
            row.getCell(0).setCellValue(fileContents.get(i).getName());
            row.createCell(1);
            row.getCell(1).setCellValue(fileContents.get(i).getParent() + "\\");
            row.createCell(2);
            row.getCell(2).setCellValue((fileContents.get(i).length() / 1000.0));
            totalSpace = fileContents.get(i).length() + totalSpace;
            row.createCell(3);
            date = new Date(fileContents.get(i).lastModified());
            row.getCell(3).setCellValue(date);
            row.getCell(3).setCellStyle(dateStyle);

        }

        //Sum total
        row = sheet.createRow(fileContents.size() + 1);
        row.createCell(0).setCellValue(fileContents.size() + " files");
        row.createCell(2).setCellValue((totalSpace / 1000000000) + " GB");

        sheet.setColumnWidth(0, 12000);
        sheet.setColumnWidth(1, 20000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 4000);

        System.out.println("Successfully added files to \"" + sheetName + "\"");

    }

    /**
     * Creates and fills spreadsheet with the contents of an ArrayList<ArrayList>.
     * @param identicalSets The contents with which to populate the spreadsheet.
     * @param name The name of the sheet being created and populated.
     */
    public void fillSpreadsheetWithSets(ArrayList<ArrayList<ComparableFile>> identicalSets, String name) {

        int count = 0;
        double totalSpace = 0;
        int maxNumDuplicates = 0;

        //Create cell style for "original" files column
        //"Original" files are the first iterations of a file with duplicates that the program discovered
        CellStyle originalCellStyle = this.workbook.createCellStyle();
        originalCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        originalCellStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
        Font originalCellStyleFont = this.workbook.createFont();
        originalCellStyleFont.setColor(IndexedColors.VIOLET.index);
        originalCellStyle.setFont(originalCellStyleFont);

        //Create cell style for "duplicate" files columns
        //"Duplicate" files are the subsequent iterations of a file with duplicates
        CellStyle duplicateCellStyle = this.workbook.createCellStyle();
        duplicateCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        duplicateCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
        Font duplicateCellStyleFont = this.workbook.createFont();
        duplicateCellStyleFont.setColor(IndexedColors.BLACK.index);
        duplicateCellStyleFont.setBold(true);
        duplicateCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        duplicateCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        duplicateCellStyle.setFont(duplicateCellStyleFont);

        //Create sheet
        Sheet sheet = this.workbook.createSheet(name);
        Row row;

        System.out.println("Adding files to \"" + name + "\"");

        //Populate sheet cells with values from identical duplicate sets and set cell styles
        for(int i = 0; i <= identicalSets.size() - 1; i++) {

            row = sheet.createRow(i);
            if(identicalSets.get(i).size() > maxNumDuplicates) {
                maxNumDuplicates = identicalSets.get(i).size();
            }

            for(int j = 0; j <= identicalSets.get(i).size() - 1; j++) {

                count++;
                row.createCell(j);
                row.getCell(j).setCellValue(identicalSets.get(i).get(j).getFile().getAbsolutePath());
                if(j == 0) {
                    row.getCell(j).setCellStyle(originalCellStyle);
                } else {
                    row.getCell(j).setCellStyle(duplicateCellStyle);
                    totalSpace = totalSpace + identicalSets.get(i).get(j).getFile().length();
                }

            }

        }

        //Add sum total cells
        row = sheet.createRow(identicalSets.size() + 1);
        row.createCell(0).setCellValue(count + " total files");
        row.createCell(1).setCellValue(count - identicalSets.size() + " purgeable files");
        row.createCell(2).setCellValue((totalSpace / 1000000000) + " GB");

        for(int i = 0; i <= maxNumDuplicates - 1; i++) {
            sheet.setColumnWidth(i, 10000);
        }

        System.out.println("Successfully added files to \"" + name + "\"");

    }

    /**
     * Sets conditional formatting on the "Last Modified" column of the sheet.
     * @param bottomThreshold The threshold beneath which files are highlighted red.
     * @param middleThreshold The threshold beneath (and above the bottom threshold) which files are highlighted orange,
     *                        and above (and below the top threshold) which files are highlighted yellow.
     * @param topThreshold The threshold above which files are highlighted green.
     */
    public void setConditionalFormatting(int sheetIndex, Date bottomThreshold, Date middleThreshold, Date topThreshold) {

        Sheet sheet = this.workbook.getSheetAt(sheetIndex);

        if(sheet.getRow(0) != null) {

            //Declare sheet conditional formatting
            SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

            //Set conditional formatting rule 1
            ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.LT, String.valueOf(DateUtil.getExcelDate(bottomThreshold)));
            PatternFormatting patternFmt1 = rule1.createPatternFormatting();
            patternFmt1.setFillBackgroundColor(IndexedColors.RED.index);

            //Set conditional formatting rule 2
            ConditionalFormattingRule rule2 = sheetCF.createConditionalFormattingRule(ComparisonOperator.BETWEEN, String.valueOf(DateUtil.getExcelDate(bottomThreshold)), String.valueOf(DateUtil.getExcelDate(middleThreshold)));
            PatternFormatting patternFmt2 = rule2.createPatternFormatting();
            patternFmt2.setFillBackgroundColor(IndexedColors.ORANGE.index);

            //Set conditional formatting rule 3
            ConditionalFormattingRule rule3 = sheetCF.createConditionalFormattingRule(ComparisonOperator.BETWEEN, String.valueOf(DateUtil.getExcelDate(middleThreshold)), String.valueOf(DateUtil.getExcelDate(topThreshold)));
            PatternFormatting patternFmt3 = rule3.createPatternFormatting();
            patternFmt3.setFillBackgroundColor(IndexedColors.YELLOW.index);

            //Set conditional formatting rule 4
            ConditionalFormattingRule rule4 = sheetCF.createConditionalFormattingRule(ComparisonOperator.GT, String.valueOf(DateUtil.getExcelDate(topThreshold)));
            PatternFormatting patternFmt4 = rule4.createPatternFormatting();
            patternFmt4.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.index);

            //Add rules to proper cells
            ConditionalFormattingRule[] cfRules = {rule1, rule2, rule3, rule4};
            CellRangeAddress[] regions = {CellRangeAddress.valueOf("D1:D" + (sheet.getLastRowNum() - 1))};
            sheetCF.addConditionalFormatting(regions, cfRules);

        }

    }

    /**
     * Writes the Workbook to a .xlsx file of a specified name and path.
     * @param spreadsheetPath The file path and name of the .xlsx file being written.
     */
    public void saveSpreadsheet(String spreadsheetPath) {

        try(OutputStream outputStream = new FileOutputStream(spreadsheetPath)) {

            System.out.println("Attempting to write Excel spreadsheet");
            this.workbook.write(outputStream);
            System.out.println("Successfully wrote to Excel spreadsheet");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
