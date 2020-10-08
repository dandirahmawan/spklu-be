package com.bppt.spklu.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ExcelService {

    public void generate(int a) throws IOException {
        //
        Workbook workbook = new XSSFWorkbook();

        // create sheet
        Sheet sheet = workbook.createSheet("Kalkulasi");

        // et size column => A, B, C, D ...
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 2000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 7000);

        // create row => 1, 2, 3, 4 ...
        Row header = sheet.createRow(0);

        // set cell style
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // set font style
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        // create cell on row 0, column 0 & 1 (header)
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Age");
        headerCell.setCellStyle(headerStyle);

        ///
//        CellStyle style = workbook.createCellStyle();
//        style.setWrapText(true);

        Row row = sheet.createRow(1);
        Cell cell = row.createCell(0);
        cell.setCellValue("John Smith");
//        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue(20);
//        cell.setCellStyle(style);

        ///
        File currDir = new File("/");
        String path = currDir.getAbsolutePath();
        String fileLocation = "/temp.xlsx"; //path.substring(0, path.length() - 1) + "temp.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }

    static final String[] param1 = {
            "Jumlah Kendaraan Full EV pada tahun baseline",
            "Jumlah Kendaraan Hybrid EV pada tahun baseline",
            "Frekuensi charging Full EV per hari",
            "Frekuensi charging Hybrid EV per hari",
            "Rasio SPKLU Banding BEV, 1:N",
            "Kapasitas pengisian 1 kendaraan listrik (kWh)",
            "Jumlah Konektor",
    };
    static final Integer[] value1 = {
            600,
            0,
            1,
            0,
            38,
            25,
            2,
    };

    static final String[] param2 = {
            "Pertumbuhan Tahunan Kendaraan EV",
            "Pertumbuhan Tahunan Kendaraan Hybrid EV",
            "Harga 1 SPKLU",
            "Subsidi energi Persen",
            "Rasio harga beli listrik PLN (Q, Rp 707 x Q)",
            "Rasio harga jual listrik SPKLU (N, Rp 1650 x N)",
            "Rugi-rugi dan kebutuhan daya pendukung",
            "Durasi penggunaan EVSE/hari (h)",
            "Biaya Sewa Lahan",
    };
    static final Double[] value2 = {
            0.23, // %
            0.0, // %
            800000000.0,
            0.0, // %
            1.2,
            1.5,
            0.1, // %
            20.0,
            0.0,
    };

    static final List<Integer> jumlahKonektor = Arrays.asList(50, 43);

    static final Integer startYear = 2020;
    static final Integer endYear = 2030;

    static final Double ieikAdd = 550000.0;
    static final Double ieikDiv = 1000.0;

    static final Double bo = 0.02;

    static final Double bp = 0.02;

    static final Double btt = -100000.0;

    static final Double keh = 1.0;

    static final Double heMtp = 0.707;
    static final Double heMin = 1.0;
    static final Double heMtpN = 0.035;

    static final Double bet = 365.0;

    static final Double js = 1.0;

    static final Double bos = 57600.0;

    static final Double pkl = 1.0;

    void genFormula() {
        FormulaService fs = new FormulaService();

        Double jkfev = 600.0; // Jumlah Kendaraan Full EV pada tahun baseline
        Double pkl = 1.0; // param ? Populasi Kendaraan Listrik
        Double ptkev = 0.23; // Pertumbuhan Tahunan Kendaraan EV
        int length = 10; // range year

        Double rsbb = 38.0; // Rasio SPKLU Banding BEV, 1:N

        Double hs = 800000000.0; // Harga 1 SPKLU
        Double ieikAdd = 550000.0; // param Infrastructure Expense (in kIDR)
        Double ieikDiv = 1000.0; // param Infrastructure Expense (in kIDR)

        Double bo = 0.02; // param Biaya Operasional *)
        Double bsl = 0.0; // Biaya Sewa Lahan

        Double bp = 0.02; // param Biaya Pemasaran

        Double btt = 100000.0; // param Biaya Tak Terduga

        Double kpkl = 25.0; // Kapasitas pengisian 1 kendaraan listrik (kWh)
        Double keh = 1.0; // param Kebutuhan Energi harian (KWH)
        Double rdkdp = 0.1; // Rugi-rugi dan kebutuhan daya pendukung

        Double heMtp = 0.707; // param ? Harga Energi (Rp/KwH)
        Double rhblp = 1.2; // Rasio harga beli listrik PLN (Q, Rp 707 x Q)
        Double heMin = 1.0; // param ? Harga Energi (Rp/KwH)
        Double sep = 0.0; // Subsidi energi Persen
        Double heMtpN = 0.035; // param ? Harga Energi (Rp/KwH)

        Double bet = 365.0; // param Biaya Energy Tahunan

        Double bos = 57600.0; // param Biaya Operasi SPKLU, Gaji Pegawai Pertahun

        Double jkhev = 0.0; // Jumlah Kendaraan Hybrid EV pada tahun baseline
        Double pkhg = 1.0; // param ? Populasi Kendaraan Hybrid (Gaikindo)
        Double ptkhev = 0.0; // Pertumbuhan Tahunan Kendaraan Hybrid EV

        Double bc = 1.65; // param ? Biaya Charging
        Double rhjls = 1.5; // Rasio harga jual listrik SPKLU (N, Rp 1650 x N)

        Double fcfeph = 1.0; // Frekuensi charging Full EV per hari

        Double fcheph = 0.0; // Frekuensi charging Hybrid EV per hari

        Double tei = 1.0; // param Total Expense (inflated)
        Integer stYear = 2020;
        Integer edYear = 2030;
        List<Double> yearRes = new ArrayList<>(); // tahun 2020, 2021, 2022, ...
        for (Integer i = stYear; i <= edYear; i+=1) {
            yearRes.add(Double.parseDouble(i + ""));
        }
        Double ir = 0.035; // Inflation Rate

        Double pl = 365.0; // param Pendapatan Layanan

        Double tax = 0.25; // Tax

        Double dcs = 1.0; // param Discounted Cash Flow
        Double dr = 0.13; // Discount Rate
        List<Double> yearTo = new ArrayList<>(); // tahun ke 0, 1, 2, ...
        Double yt = 0.0;
        for (Integer i = stYear; i <= edYear; i+=1) {
            yearTo.add(yt);
            yt += 1;
        }

        List<Double> pklRes = fs.pklRes(jkfev, pkl, ptkev, length);
        List<Double> jsRes = fs.jsRes(pkl, rsbb, pklRes);
        List<Double> sbRes = fs.sbRes(jsRes);
        List<Double> ieikRes = fs.ieikRes(sbRes, hs, ieikAdd, ieikDiv);

        List<Double> boRes = fs.boRes(bo, ieikRes, bsl, jsRes);
        List<Double> bpRes = fs.bpRes(bp, ieikRes);
        List<Double> bttRes = fs.bttRes(btt, length);
        List<Double> kehRes = fs.kehRes(jsRes, rsbb, kpkl, keh, rdkdp);
        List<Double> heRes = fs.heRes(heMtp, rhblp, heMin, sep, heMtpN, length);
        List<Double> betRes = fs.betRes(kehRes, heRes, bet);
        List<Double> bosRes = fs.bosRes(jsRes, bos);

        List<Double> pkhgRes = fs.pkhgRes(jkhev, pkhg, ptkhev, length);
        List<Double> bcRes = fs.bcRes(kpkl, bc, rhjls, length);
        List<Double> fcfehRes = fs.fcfehRes(fcfeph, length);
        List<Double> fchhRes = fs.fchhRes(fcheph, length);
        List<Double> teiRes = fs.teiRes(ieikRes, boRes, bpRes, bttRes, betRes, bosRes, tei, yearRes, ir);
        List<Double> plRes = fs.plRes(pklRes, bcRes, fcfehRes, pkhgRes, fchhRes, pl);
        List<Double> gpikRes = fs.gpikRes(teiRes, plRes);
        List<Double> taxRes = fs.taxRes(gpikRes, tax);
        List<Double> csRes = fs.csRes(gpikRes, taxRes);
        List<Double> dcsRes = fs.dcsRes(csRes, dcs, dr, yearTo);
        List<Double> cfRes = fs.cfRes(csRes, dcsRes);
        List<Double> ppParamRes = fs.ppParamRes(cfRes);

        double[] csResD= new double[csRes.size()];
        for (int i = 0; i < csRes.size(); i++) {
            csResD[i] = csRes.get(i);
        }

        double npv = FormulaService.npv(dr, csResD);
        log.info("npv={}", FormulaService.round(npv, 0));

        double irr = FormulaService.irr(csResD);
        log.info("irr={}", FormulaService.round(irr, 2));

        double mirr = FormulaService.mirr(csResD, 0, 0);
        log.info("mirr={}", FormulaService.round(mirr, 2));

        Double fbeRes = fs.fbeRes(cfRes);
        log.info("fbeRes={}", FormulaService.round(fbeRes, 3));

        Double llRes = fs.llRes(cfRes);
        log.info("llRes={}", FormulaService.round(llRes, 4));

        Double lyRes = fs.lyRes(ppParamRes);
        log.info("llRes={}", FormulaService.round(lyRes, 0));

        Double pprdRes = fs.pprdRes(fbeRes, llRes, lyRes);
        log.info("pprdRes={}", FormulaService.round(pprdRes, 2));
    }

    public void generate() throws IOException {
        genFormula();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Kalkulasi");

        ExcelFunctionService efs = new ExcelFunctionService();

        // header = input variable model
        CellStyle inputVariableModelCellStyle = efs.cellStyle(workbook, true, true,
                IndexedColors.YELLOW.getIndex(), null);
        Cell inputVariableModelCell = efs.createCell(sheet, 2, 2,
                new MergeRowCol(2, 7), inputVariableModelCellStyle, null);
        inputVariableModelCell.setCellValue("Input Variable Model");


        CellStyle column1CellStyle = efs.cellStyle(workbook, false, false,
                IndexedColors.LIGHT_YELLOW.getIndex(), null);

        // param 1
        int startRowParam = 3;
        for (String param : param1) {
            Cell column1Cell = efs.createCell(sheet, startRowParam, 2, null, column1CellStyle, 12000);
            column1Cell.setCellValue(param);
            startRowParam += 1;
        }
        startRowParam = 3;
        for (Integer param : value1) {
            Cell column1Cell = efs.createCell(sheet, startRowParam, 3, null, null, 3000);
            column1Cell.setCellValue(param);
            startRowParam += 1;
        }

        // param 2
        startRowParam = 3;
        for (String param : param2) {
            Cell column1Cell = efs.createCell(sheet, startRowParam, 4, new MergeRowCol(startRowParam, 6), column1CellStyle, null);
            column1Cell.setCellValue(param);
            startRowParam += 1;
        }
        startRowParam = 3;
        for (Double param : value2) {
            Cell column1Cell = efs.createCell(sheet, startRowParam, 7, null, null, 3000);
            column1Cell.setCellValue(param);
            startRowParam += 1;
        }

        // konektor
        int startKonektor = 10;
        int idxKonektor = 1;
        for (Integer konektor : jumlahKonektor) {
            Cell konektorParamCell = efs.createCell(sheet, startKonektor, 2, null, column1CellStyle, null);
            konektorParamCell.setCellValue(">> Spesifikasi daya luaran konektor " + idxKonektor + " (kW)");
            Cell konektorValueCell = efs.createCell(sheet, startKonektor, 3, null, null, null);
            konektorValueCell.setCellValue(konektor);
            startKonektor += 1;
            idxKonektor += 1;
        }

        int colCalculate = 3;
        int totalRowCalculate = 0;

        // tahun
        int startRowCalculate = 10 + jumlahKonektor.size() + 2;
        Cell tahunCell = efs.createCell(sheet, startRowCalculate, 2, null, null, null);
        tahunCell.setCellValue("Tahun");

        for (int i = startYear; i <= endYear; i += 1) {
            Cell cell = efs.createCell(sheet, startRowCalculate, colCalculate, null, null, 4000);
            cell.setCellValue(startYear + totalRowCalculate);
            colCalculate += 1;
            totalRowCalculate += 1;
        }

        // tahun ke
        int tahunKeRowNum = startRowCalculate + 1;
        Cell tahunKeCell = efs.createCell(sheet, tahunKeRowNum, 2, null, null, null);
        tahunKeCell.setCellValue("Tahun ke-");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, tahunKeRowNum, colCalculate, null, null, null);
            cell.setCellValue(i);
            colCalculate += 1;
        }

        // Expense (in kIDR)
        int eik = startRowCalculate + 2;
        Cell eikCell = efs.createCell(sheet, eik, 2, null, null, null);
        eikCell.setCellValue("Expense (in kIDR)");

        // Infrastructure Expense (in kIDR)
        int ieik = startRowCalculate + 3;
        Cell ieikCell = efs.createCell(sheet, ieik, 2, null, null, null);
        ieikCell.setCellValue("Infrastructure Expense (in kIDR)");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, ieik, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-(" + ieikAdd + "+$G$5/"+ ieikDiv + ")*" + efs.getIndexCol(colCalculate) + (ieik + 8));
            colCalculate += 1;
        }

        // Biaya Operasional *)
        int boRow = startRowCalculate + 4;
        Cell boCell = efs.createCell(sheet, boRow, 2, null, null, null);
        boCell.setCellValue("Biaya Operasional");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, boRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("(" + bo + "*" + efs.getIndexCol(colCalculate) + (boRow - 1) +")+($G$11*" + efs.getIndexCol(colCalculate)+ (boRow + 6) + ")");
            colCalculate += 1;
        }

        // Biaya Pemasaran
        int bpRow = startRowCalculate + 5;
        Cell bpCell = efs.createCell(sheet, bpRow, 2, null, null, null);
        bpCell.setCellValue("Biaya Pemasaran");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, bpRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(bp + "*" + efs.getIndexCol(colCalculate) + (bpRow - 2));
            colCalculate += 1;
        }

        // Biaya Tak Terduga
        int bttRow = startRowCalculate + 6;
        Cell bttCell = efs.createCell(sheet, bttRow, 2, null, null, null);
        bttCell.setCellValue("Biaya Tak Terduga");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, bttRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(btt + "");
            colCalculate += 1;
        }

        // Kebutuhan Energi harian (KWH) ==> 2
        int kehRow = startRowCalculate + 7;
        Cell kehCell = efs.createCell(sheet, kehRow, 2, null, null, null);
        kehCell.setCellValue("Kebutuhan Energi harian (KWH)");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, kehRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(colCalculate) + (kehRow + 3) + "*$C$7*$C$8*(" + keh +"+$G$9)");
            colCalculate += 1;
        }

        // ? Harga Energi (Rp/KwH) ==> first
        int heRow = startRowCalculate + 8;
        Cell heCell = efs.createCell(sheet, heRow, 2, null, null, null);
        heCell.setCellValue("Harga Energi (Rp/KwH)");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, heRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula(heMtp + "*$G$7 * (" + heMin + "-$G$6)");
            else cell.setCellFormula(efs.getIndexCol(colCalculate - 1) + heRow + "+" + heMtpN + "*" + efs.getIndexCol(colCalculate - 1) + heRow + "*(" + heMin + "-$G$6)");
            colCalculate += 1;
        }

        // Biaya Energy Tahunan ==> 2
        int betRow = startRowCalculate + 9;
        Cell betCell = efs.createCell(sheet, betRow, 2, null, null, null);
        betCell.setCellValue("Biaya Energy Tahunan");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, betRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-" + efs.getIndexCol(colCalculate) + (betRow - 2) + "*" + efs.getIndexCol(colCalculate) + (betRow - 1) + "*" + bet);
            colCalculate += 1;
        }

        // ? Jumlah SPKLU*brp sebenarnya (10% dari jumlah BEV)
        int jsRow = startRowCalculate + 10;
        Cell jsCell = efs.createCell(sheet, jsRow, 2, null, null, null);
        jsCell.setCellValue("? Jumlah SPKLU*brp sebenarnya (10% dari jumlah BEV)");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, jsRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("FLOOR(" + js + "/$C$7*" + efs.getIndexCol(colCalculate) + (jsRow + 3) + ",1)");
            colCalculate += 1;
        }

        // SPKLU Baru
        int sbRow = startRowCalculate + 11;
        Cell sbCell = efs.createCell(sheet, sbRow, 2, null, null, null);
        sbCell.setCellValue("SPKLU Baru");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, sbRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula(efs.getIndexCol(colCalculate) + (sbRow - 1) + "");
            else cell.setCellFormula(efs.getIndexCol(colCalculate) + (sbRow - 1) + "-" + efs.getIndexCol(colCalculate - 1) + (sbRow - 1));
            colCalculate += 1;
        }

        // Biaya Operasi SPKLU, Gaji Pegawai Pertahun
        int bosRow = startRowCalculate + 12;
        Cell bosCell = efs.createCell(sheet, bosRow, 2, null, null, null);
        bosCell.setCellValue("Biaya Operasi SPKLU, Gaji Pegawai Pertahun");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, bosRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-" + efs.getIndexCol(colCalculate) + (bosRow - 2) + "*" + bos);
            colCalculate += 1;
        }

        // ? Populasi Kendaraan Listrik ==> 1
        int pklRow = startRowCalculate + 13;
        Cell pklCell = efs.createCell(sheet, pklRow, 2, null, null, null);
        pklCell.setCellValue("? Populasi Kendaraan Listrik");

        colCalculate = 3;
        for (int i = 0; i < totalRowCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, pklRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("C3");
            else cell.setCellFormula("FLOOR((" + pkl + " + $G$3)*" + efs.getIndexCol(colCalculate - 1) + pklRow + ", 1)");
            colCalculate += 1;
        }






        // temp1
//        int temp1 = startRowCalculate + 4;
//        Cell temp1Cell = efs.createCell(sheet, temp1, 2, null, null, null);
//        temp1Cell.setCellType(CellType.FORMULA);
//        temp1Cell.setCellFormula("(123+567)");



        String fileLocation = "/temp.xlsx";
        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    class MergeRowCol {
        private int lastRow;
        private int LastCol;
    }




}
