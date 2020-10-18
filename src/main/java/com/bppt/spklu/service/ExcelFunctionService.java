package com.bppt.spklu.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExcelFunctionService {

    /**
     *
     * A1 start at rowNum 1 colNum 1
     * @param sheet
     * @param rowNum line y | 1, 2, 3, 4
     * @param colNum line x -- A, B, C, D
     * @param mergeCell
     * @return
     */
    public Cell createCell(Sheet sheet, int rowNum, int colNum, ExcelService.MergeRowCol mergeCell, CellStyle cellStyle, Integer size) {
        if (mergeCell != null) {
            CellRangeAddress cellRange = getCellRangeAddressByNumber(rowNum, mergeCell.getLastRow(), colNum, mergeCell.getLastCol());
//                    new CellRangeAddress(rowNum - 1, mergeCell.getLastRow() - 1,
//                            colNum - 1, mergeCell.getLastCol() - 1);
            sheet.addMergedRegion(cellRange);
        }

        Row row = sheet.getRow(rowNum - 1);
        if (row == null) row = sheet.createRow(rowNum - 1);

        Cell cell = row.getCell(colNum - 1);
        if (cell == null) cell = row.createCell(colNum - 1);
        if (cellStyle != null) cell.setCellStyle(cellStyle);
        if (size != null) sheet.setColumnWidth(colNum - 1, size);
        return cell; // new SheetCell(sheet, cell);
    }

    public CellRangeAddress getCellRangeAddressByNumber(int firstRow, int lastRow, int firstCol, int lastCol) {
        return new CellRangeAddress(firstRow - 1, lastRow - 1,
                firstCol - 1, lastCol - 1);
    }

    public CellStyle cellStyle(Workbook workbook, boolean center, boolean bold, Short indexedCellColors, Short indexedFontColors) {
        CellStyle cellStyle = workbook.createCellStyle();
        if (indexedCellColors != null) {
            cellStyle.setFillForegroundColor(indexedCellColors);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        if (center) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 16);
        if (indexedFontColors != null) font.setColor(indexedFontColors);
        if (bold) font.setBold(true);
        cellStyle.setFont(font);

        return cellStyle;
    }

    private static final List<String> colIdx = IntStream.rangeClosed('A', 'Z')
            .mapToObj(c -> "" + (char) c).collect(Collectors.toList());
    private static final List<String> colIdxA = IntStream.rangeClosed('A', 'Z')
            .mapToObj(c -> "A" + (char) c).collect(Collectors.toList());

    public String getIndexCol(int idx) {
        return colIdx.get(idx - 1);
    }

}
