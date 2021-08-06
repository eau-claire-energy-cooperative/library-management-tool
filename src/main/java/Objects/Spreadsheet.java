package Objects;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Spreadsheet {

    private final Workbook workbook;
    private final CellStyle dateStyle;

    public Spreadsheet() {

        this.workbook = new XSSFWorkbook();

        DataFormat dateFormat = this.workbook.createDataFormat();
        this.dateStyle = this.workbook.createCellStyle();
        this.dateStyle.setDataFormat(dateFormat.getFormat("m/d/yy"));

    }

    public Workbook getWorkbook() { return workbook; }

    public CellStyle getDateStyle() { return dateStyle; }

}
