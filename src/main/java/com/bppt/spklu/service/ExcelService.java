package com.bppt.spklu.service;

import com.bppt.spklu.constant.FormulaEnum;
import com.bppt.spklu.model.CalculateParameter;
import com.bppt.spklu.model.MainParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ExcelService {

    @Autowired
    private ParameterService ps;

    public String generate(CalculateParameter cp) throws IOException {
        List<MainParameter> l = ps.getParameters();

        String[] param1 = {
                "Jumlah Kendaraan Full EV pada tahun baseline",
                "Jumlah Kendaraan Hybrid EV pada tahun baseline",
                "Frekuensi charging Full EV per hari",
                "Frekuensi charging Hybrid EV per hari",
                "Rasio SPKLU Banding BEV, 1:N",
                "Kapasitas pengisian 1 kendaraan listrik (kWh)",
                "Jumlah Konektor",
        };
        Double[] value1 = {
                Double.parseDouble(cp.getKondisiEkonomi().getJumlahKendaraanInisial()),
                0.0, // TODO
                1.0, // TODO
                0.0, // TODO
                Double.parseDouble(cp.getParameterBisnis().getRasioSpklu()),
                Double.parseDouble(cp.getParameterTeknis().getKapasitasKbl()),
                Double.parseDouble(cp.getParameterTeknis().getJumlahKonektor()),
        };

        String[] param2 = {
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
        Double[] value2 = {
                (Double.parseDouble(cp.getParameterBisnis().getPertumbuhanKblPerTahun()) / 100),
                0.0, // TODO
                Double.parseDouble(cp.getKondisiEkonomi().getBiayaSpklu()),
                0.0, // TODO
                Double.parseDouble(cp.getParameterBisnis().getHargaJualPln()),
                Double.parseDouble(cp.getParameterBisnis().getHargaJualKonsumen()),
                (Double.parseDouble(cp.getParameterTeknis().getRugiDayaPendukung()) / 100),
                20.0, // TODO
                Double.parseDouble(cp.getParameterBisnis().getBiayaSewaLahan()), //0.0, // TODO
        };

        Integer jumlahKonektor = Integer.valueOf(cp.getParameterTeknis().getJumlahKonektor());
        List<Integer> jumlahKonektorList = new ArrayList<>(); //Arrays.asList(50, 43); // TODO
        for (int i = 0; i < jumlahKonektor; i+=1) {
            jumlahKonektorList.add(0); // TODO
        }


        Integer stYear = Integer.parseInt(cp.getYears().getStartYear()); //2020;
        Integer edYear = Integer.parseInt(cp.getYears().getFinishYear()); //2030;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Kalkulasi");

        ExcelFunctionService efs = new ExcelFunctionService();

        // header = input variable model
//        CellStyle inputVariableModelCellStyle = efs.cellStyle(workbook, true, true,
//                IndexedColors.YELLOW.getIndex(), null);
        CellStyle inputVariableModelCellStyle = efs.cellStyle(workbook, true, true, null, null);
        Cell inputVariableModelCell = efs.createCell(sheet, 2, 2,
                new MergeRowCol(2, 7), inputVariableModelCellStyle, null);
        inputVariableModelCell.setCellValue("Input Variable Model");


//        CellStyle column1CellStyle = efs.cellStyle(workbook, false, false,
//                IndexedColors.LIGHT_YELLOW.getIndex(), null);
        CellStyle column1CellStyle = efs.cellStyle(workbook, false, false, null, null);

        CellStyle centerStyle = efs.cellStyle(workbook, true, false, null, null);

        // param 1
        int startRowParam = 3;
        for (String param : param1) {
            Cell column1Cell = efs.createCell(sheet, startRowParam, 2, null, column1CellStyle, 12000);
            column1Cell.setCellValue(param);
            startRowParam += 1;
        }
        startRowParam = 3;
        for (Double param : value1) {
            Cell column1Cell = efs.createCell(sheet, startRowParam, 3, null, centerStyle, 3000);
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
            Cell column1Cell = efs.createCell(sheet, startRowParam, 7, null, centerStyle, 3000);
            column1Cell.setCellValue(param);
            startRowParam += 1;
        }

        DataFormat percentFmt = workbook.createDataFormat();
        CellStyle percentStyle = efs.cellStyle(workbook, true, false, null, null);
        percentStyle.setDataFormat(percentFmt.getFormat("0%"));
        efs.createCell(sheet, 3, 7, null, percentStyle, 3000);
        efs.createCell(sheet, 4, 7, null, percentStyle, 3000);
        efs.createCell(sheet, 6, 7, null, percentStyle, 3000);
        efs.createCell(sheet, 9, 7, null, percentStyle, 3000);

        DataFormat accountingFmt = workbook.createDataFormat();
        CellStyle accountingStyle = efs.cellStyle(workbook, false, false,
                null, null);
        accountingStyle.setDataFormat(accountingFmt.getFormat("_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);_(@_)"));
        efs.createCell(sheet, 5, 7, null, accountingStyle, 3000);

        // konektor
        int startKonektor = 10;
        int idxKonektor = 1;
        for (Integer konektor : jumlahKonektorList) {
            Cell konektorParamCell = efs.createCell(sheet, startKonektor, 2, null, column1CellStyle, null);
            konektorParamCell.setCellValue(">> Spesifikasi daya luaran konektor " + idxKonektor + " (kW)");
            Cell konektorValueCell = efs.createCell(sheet, startKonektor, 3, null, centerStyle, null);
            konektorValueCell.setCellValue(konektor);
            startKonektor += 1;
            idxKonektor += 1;
        }

        int colCalculate = 3;
        int totalColumnCalculate = 0;

        // tahun
        int startRowCalculate = 10 + jumlahKonektorList.size() + 2;
        Cell tahunCell = efs.createCell(sheet, startRowCalculate, 2, null, null, null);
        tahunCell.setCellValue("Tahun");

        for (int i = stYear; i <= edYear; i += 1) {
            Cell cell = efs.createCell(sheet, startRowCalculate, colCalculate, null, null, 4000);
            cell.setCellValue(stYear + totalColumnCalculate);
            colCalculate += 1;
            totalColumnCalculate += 1;
        }

        // tahun ke
        int tahunKeRowNum = startRowCalculate + 1;
        Cell tahunKeCell = efs.createCell(sheet, tahunKeRowNum, 2, null, null, null);
        tahunKeCell.setCellValue("Tahun ke-");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, tahunKeRowNum, colCalculate, null, null, null);
            cell.setCellValue(i);
            colCalculate += 1;
        }

        // Expense (in kIDR)
        int eikRow = startRowCalculate + 2;
        Cell eikCell = efs.createCell(sheet, eikRow, 2, null, null, null);
        eikCell.setCellValue("Expense (in kIDR)");

        // Infrastructure Expense (in kIDR)
        int ieikRow = startRowCalculate + 3;
        Cell ieikCell = efs.createCell(sheet, ieikRow, 2, null, null, null);
        ieikCell.setCellValue("Infrastructure Expense (in kIDR)");

        Double ieikAdd = Double.parseDouble(ps.getParam(l, FormulaEnum.ieikAdd)); //550000.0; // param Infrastructure Expense (in kIDR)
        Double ieikDiv = Double.parseDouble(ps.getParam(l, FormulaEnum.ieikDiv)); //1000.0; // param Infrastructure Expense (in kIDR)

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, ieikRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-(" + ieikAdd + "+$G$5/"+ ieikDiv + ")*" + efs.getIndexCol(colCalculate) + (ieikRow + 8));
            colCalculate += 1;
        }

        // Biaya Operasional *)
        int boRow = startRowCalculate + 4;
        Cell boCell = efs.createCell(sheet, boRow, 2, null, null, null);
        boCell.setCellValue("   Biaya Operasional");

        Double bo = Double.parseDouble(ps.getParam(l, FormulaEnum.bo)); //0.02; // param Biaya Operasional *)

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, boRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("(" + bo + "*" + efs.getIndexCol(colCalculate) + (boRow - 1) +")+($G$11*" + efs.getIndexCol(colCalculate)+ (boRow + 6) + ")");
            colCalculate += 1;
        }

        // Biaya Pemasaran
        int bpRow = startRowCalculate + 5;
        Cell bpCell = efs.createCell(sheet, bpRow, 2, null, null, null);
        bpCell.setCellValue("   Biaya Pemasaran");

        Double bp = Double.parseDouble(ps.getParam(l, FormulaEnum.bp)); //0.02; // param Biaya Pemasaran

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, bpRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(bp + "*" + efs.getIndexCol(colCalculate) + (bpRow - 2));
            colCalculate += 1;
        }

        // Biaya Tak Terduga
        int bttRow = startRowCalculate + 6;
        Cell bttCell = efs.createCell(sheet, bttRow, 2, null, null, null);
        bttCell.setCellValue("   Biaya Tak Terduga");

        Double btt = Double.parseDouble(ps.getParam(l, FormulaEnum.btt)); //100000.0; // param Biaya Tak Terduga

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, bttRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-" + btt);
            colCalculate += 1;
        }

        // Kebutuhan Energi harian (KWH) ==> 2
        int kehRow = startRowCalculate + 7;
        Cell kehCell = efs.createCell(sheet, kehRow, 2, null, null, null);
        kehCell.setCellValue("   Kebutuhan Energi harian (KWH)");

        Double keh = Double.parseDouble(ps.getParam(l, FormulaEnum.keh)); //1.0; // param Kebutuhan Energi harian (KWH)

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, kehRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(colCalculate) + (kehRow + 3) + "*$C$7*$C$8*(" + keh +"+$G$9)");
            colCalculate += 1;
        }

        // ? Harga Energi (Rp/KwH) ==> first
        int heRow = startRowCalculate + 8;
        Cell heCell = efs.createCell(sheet, heRow, 2, null, null, null);
        heCell.setCellValue("   Harga Energi (Rp/KwH)");

        Double heMtp = Double.parseDouble(ps.getParam(l, FormulaEnum.heMtp)); //0.707; // param ? Harga Energi (Rp/KwH)
        Double heMin = Double.parseDouble(ps.getParam(l, FormulaEnum.heMin)); //1.0; // param ? Harga Energi (Rp/KwH)
        Double heMtpN = Double.parseDouble(ps.getParam(l, FormulaEnum.heMtpN)); //0.035; // param ? Harga Energi (Rp/KwH)

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, heRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula(heMtp + "*$G$7 * (" + heMin + "-$G$6)");
            else cell.setCellFormula(efs.getIndexCol(colCalculate - 1) + heRow + "+" + heMtpN + "*" + efs.getIndexCol(colCalculate - 1) + heRow + "*(" + heMin + "-$G$6)");
            colCalculate += 1;
        }

        // Biaya Energy Tahunan ==> 2
        int betRow = startRowCalculate + 9;
        Cell betCell = efs.createCell(sheet, betRow, 2, null, null, null);
        betCell.setCellValue("   Biaya Energy Tahunan");

        Double bet = Double.parseDouble(ps.getParam(l, FormulaEnum.bet)); //365.0; // param Biaya Energy Tahunan

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, betRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-" + efs.getIndexCol(colCalculate) + (betRow - 2) + "*" + efs.getIndexCol(colCalculate) + (betRow - 1) + "*" + bet);
            colCalculate += 1;
        }

        // ? Jumlah SPKLU*brp sebenarnya (10% dari jumlah BEV)
        int jsRow = startRowCalculate + 10;
        Cell jsCell = efs.createCell(sheet, jsRow, 2, null, null, null);
        jsCell.setCellValue("   ? Jumlah SPKLU*brp sebenarnya (10% dari jumlah BEV)");

        Double js = Double.parseDouble(ps.getParam(l, FormulaEnum.pkl)); //1.0; // param ? Populasi Kendaraan Listrik

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, jsRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("FLOOR(" + js + "/$C$7*" + efs.getIndexCol(colCalculate) + (jsRow + 3) + ",1)");
            colCalculate += 1;
        }

        // SPKLU Baru
        int sbRow = startRowCalculate + 11;
        Cell sbCell = efs.createCell(sheet, sbRow, 2, null, null, null);
        sbCell.setCellValue("   SPKLU Baru");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, sbRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula(efs.getIndexCol(colCalculate) + (sbRow - 1) + "");
            else cell.setCellFormula(efs.getIndexCol(colCalculate) + (sbRow - 1) + "-" + efs.getIndexCol(colCalculate - 1) + (sbRow - 1));
            colCalculate += 1;
        }

        // Biaya Operasi SPKLU, Gaji Pegawai Pertahun
        int bosRow = startRowCalculate + 12;
        Cell bosCell = efs.createCell(sheet, bosRow, 2, null, null, null);
        bosCell.setCellValue("   Biaya Operasi SPKLU, Gaji Pegawai Pertahun");

        Double bos = Double.parseDouble(ps.getParam(l, FormulaEnum.bos)); //57600.0; // param Biaya Operasi SPKLU, Gaji Pegawai Pertahun

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, bosRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-" + efs.getIndexCol(colCalculate) + (bosRow - 2) + "*" + bos);
            colCalculate += 1;
        }

        // ? Populasi Kendaraan Listrik ==> 1
        int pklRow = startRowCalculate + 13;
        Cell pklCell = efs.createCell(sheet, pklRow, 2, null, null, null);
        pklCell.setCellValue("   ? Populasi Kendaraan Listrik");

        Double pkl = Double.parseDouble(ps.getParam(l, FormulaEnum.pkl)); //1.0; // param ? Populasi Kendaraan Listrik

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, pklRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("C3");
            else cell.setCellFormula("FLOOR((" + pkl + " + $G$3)*" + efs.getIndexCol(colCalculate - 1) + pklRow + ", 1)");
            colCalculate += 1;
        }

        // ? Populasi Kendaraan Hybrid (Gaikindo)
        int pkhgRow = startRowCalculate + 14;
        Cell pkhgCell = efs.createCell(sheet, pkhgRow, 2, null, null, null);
        pkhgCell.setCellValue("   ? Populasi Kendaraan Hybrid (Gaikindo)");

        Double pkhg = Double.parseDouble(ps.getParam(l, FormulaEnum.pkhg)); //1.0; // param ? Populasi Kendaraan Hybrid (Gaikindo)

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, pkhgRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("C4");
            else cell.setCellFormula("FLOOR((" + pkhg + " + $G$4)*" + efs.getIndexCol(colCalculate - 1) + pkhgRow + ", 1)");
            colCalculate += 1;
        }

        // ? Biaya Charging
        int bcRow = startRowCalculate + 15;
        Cell bcCell = efs.createCell(sheet, bcRow, 2, null, null, null);
        bcCell.setCellValue("   ? Biaya Charging");

        Double bc = Double.parseDouble(ps.getParam(l, FormulaEnum.bc)); //1.65; // param ? Biaya Charging

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, bcRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("C8*" + bc + "*G8");
            else cell.setCellFormula(efs.getIndexCol(colCalculate - 1) + bcRow);
            colCalculate += 1;
        }

        // ? Frekuensi Charging Full EV harian
        int fcfehRow = startRowCalculate + 16;
        Cell fcfehCell = efs.createCell(sheet, fcfehRow, 2, null, null, null);
        fcfehCell.setCellValue("   ? Frekuensi Charging Full EV harian");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, fcfehRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("$C$5");
            colCalculate += 1;
        }

        // ? Frekuensi Charging Hybrid harian
        int fchhRow = startRowCalculate + 17;
        Cell fchhCell = efs.createCell(sheet, fchhRow, 2, null, null, null);
        fchhCell.setCellValue("   ? Frekuensi Charging Hybrid harian");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, fchhRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("$C$6");
            colCalculate += 1;
        }

        // Total Expense (inflated)
        int teiRow = startRowCalculate + 18;
        Cell teiCell = efs.createCell(sheet, teiRow, 2, null, null, null);
        teiCell.setCellValue("   Total Expense (inflated)");

        Double tei = Double.parseDouble(ps.getParam(l, FormulaEnum.tei)); //1.0; // param Total Expense (inflated)

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, teiRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("(SUM(" + efs.getIndexCol(colCalculate) + (teiRow - 15)
                    + ":" + efs.getIndexCol(colCalculate) + (teiRow - 12)
                    + ")+" + efs.getIndexCol(colCalculate) + (teiRow - 9)
                    + "+SUM(" + efs.getIndexCol(colCalculate) + (teiRow - 6)
                    +":" + efs.getIndexCol(colCalculate) + (teiRow - 6) + "))");

            else cell.setCellFormula(efs.getIndexCol(colCalculate) + (teiRow - 9)
                    + "+" + efs.getIndexCol(colCalculate) + (teiRow - 15) + "+(SUM("
                    + efs.getIndexCol(colCalculate) + (teiRow - 14) + ":" + efs.getIndexCol(colCalculate) + (teiRow - 12) + ")+ "
                    + efs.getIndexCol(colCalculate) + (teiRow - 6) + ")*(" + tei + "+("
                    + efs.getIndexCol(colCalculate) + (teiRow - 18) + "-$C$" + (teiRow - 18) + ")*$C$" + (teiRow + 19) + ")");
            colCalculate += 1;
        }

        // Income (In kIDR)
        int iikRow = startRowCalculate + 19;
        Cell iikCell = efs.createCell(sheet, iikRow, 2, null, null, null);
        iikCell.setCellValue("Income (In kIDR)");

        // Pendapatan Kontrak
        int pkRow = startRowCalculate + 20;
        Cell pkCell = efs.createCell(sheet, pkRow, 2, null, null, null);
        pkCell.setCellValue("   Pendapatan Kontrak");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, pkRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("0");
            colCalculate += 1;
        }

        // Pendapatan Layanan
        int plRow = startRowCalculate + 21;
        Cell plCell = efs.createCell(sheet, plRow, 2, null, null, null);
        plCell.setCellValue("   Pendapatan Layanan");

        Double pl = Double.parseDouble(ps.getParam(l, FormulaEnum.pl)); //365.0; // param Pendapatan Layanan

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, plRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(colCalculate) + (plRow - 8) + "*"
                    + efs.getIndexCol(colCalculate) + (plRow - 6) + "*"
                    + efs.getIndexCol(colCalculate) + (plRow - 5) + "*" + pl);
            colCalculate += 1;
        }

        // Revenue (inflated)
        int riRow = startRowCalculate + 22;
        Cell riCell = efs.createCell(sheet, riRow, 2, null, null, null);
        riCell.setCellValue("   Revenue (inflated)");

        Double ri = Double.parseDouble(ps.getParam(l, FormulaEnum.ri)); //1.0; // param Revenue (inflated)

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, riRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("SUM(" + efs.getIndexCol(colCalculate) + (riRow - 2) + ":" + efs.getIndexCol(colCalculate) + (riRow - 1) + ")");
            else cell.setCellFormula("SUM("+ efs.getIndexCol(colCalculate) + (riRow - 2) +":"
                    + efs.getIndexCol(colCalculate) + (riRow - 1) +")*("+ ri +"+("
                    + efs.getIndexCol(colCalculate) + (riRow - 22) + "-$C$" + (riRow - 22) + ")*$C$" + (riRow + 15) + ")");
            colCalculate += 1;
        }

        // Gross Profit (In kIDR)
        int gpikRow = startRowCalculate + 23;
        Cell gpikCell = efs.createCell(sheet, gpikRow, 2, null, null, null);
        gpikCell.setCellValue("Gross Profit (In kIDR)");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, gpikRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(colCalculate) + (gpikRow - 2) + "+" + efs.getIndexCol(colCalculate) + (gpikRow - 5));
            colCalculate += 1;
        }

        // Tax
        int taxRow = startRowCalculate + 24;
        Cell taxCell = efs.createCell(sheet, taxRow, 2, null, null, null);
        taxCell.setCellValue("Tax");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, taxRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("IF(" + efs.getIndexCol(colCalculate) + (taxRow - 1) + ">0,"
                    + efs.getIndexCol(colCalculate) + (taxRow - 1) + "*$C$" + (taxRow + 10) +",0)");
            colCalculate += 1;
        }

        // Cash Flow
        int csRow = startRowCalculate + 25;
        Cell csCell = efs.createCell(sheet, csRow, 2, null, null, null);
        csCell.setCellValue("Cash Flow");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, csRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(colCalculate) + (csRow - 2) + "-" + efs.getIndexCol(colCalculate) + (csRow - 1));
            colCalculate += 1;
        }

        // Discounted Cash Flow
        int dcsRow = startRowCalculate + 26;
        Cell dcsCell = efs.createCell(sheet, dcsRow, 2, null, null, null);
        dcsCell.setCellValue("Discounted Cash Flow");

        Double dcs = Double.parseDouble(ps.getParam(l, FormulaEnum.dcs)); //1.0; // param Discounted Cash Flow

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, dcsRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(colCalculate) + (dcsRow - 1) + "/("+ dcs +"+$C$"+(dcsRow + 7) + ")^("
                    + efs.getIndexCol(colCalculate) + (dcsRow - 25) + "+" + dcs + ")");
            colCalculate += 1;
        }

        // Cummulative Flow
        int cfRow = startRowCalculate + 27;
        Cell cfCell = efs.createCell(sheet, cfRow, 2, null, null, null);
        cfCell.setCellValue("Cummulative Flow");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, cfRow, colCalculate, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula(efs.getIndexCol(colCalculate) + (cfRow - 2));
            else cell.setCellFormula("SUM($C$" + (cfRow - 1) + ":" + efs.getIndexCol(colCalculate) + (cfRow - 1) + ")");
            colCalculate += 1;
        }

        // Payback Period
        int ppRow = startRowCalculate + 28;
        Cell ppCell = efs.createCell(sheet, ppRow, 2, null, null, null);
        ppCell.setCellValue("Payback Period");

        colCalculate = 3;
        for (int i = 0; i < totalColumnCalculate; i += 1) {
            Cell cell = efs.createCell(sheet, ppRow, colCalculate, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("IF("+ efs.getIndexCol(colCalculate) + (ppRow - 1) +"<1,\"0\",\"1\")");
            colCalculate += 1;
        }

        // First Break Even
        colCalculate = 3;
        int fbeRow = startRowCalculate + 30;

        Cell fbeCell = efs.createCell(sheet, fbeRow, 10, null, null, null);
        fbeCell.setCellValue("First Break Even");
        sheet.setArrayFormula("MIN(IF(" + efs.getIndexCol(colCalculate) + cfRow + ":"
                + efs.getIndexCol(totalColumnCalculate + 2) + cfRow + ">0,"
                + efs.getIndexCol(colCalculate) + cfRow + ":" + efs.getIndexCol(totalColumnCalculate + 2) + cfRow + "))",
                efs.getCellRangeAddressByNumber(fbeRow, fbeRow, 11, 11));
//        efs.createCell(sheet, fbeRow, 11, null, accountingStyle, null);

        // Last loss
        colCalculate = 3;
        int llRow = startRowCalculate + 31;

        Cell llCell = efs.createCell(sheet, llRow, 10, null, null, null);
        llCell.setCellValue("Last loss");
        sheet.setArrayFormula("MAX(IF(" + efs.getIndexCol(colCalculate) + cfRow + ":"
                        + efs.getIndexCol(totalColumnCalculate + 2) + cfRow + "<0,"
                        + efs.getIndexCol(colCalculate) + cfRow + ":" + efs.getIndexCol(totalColumnCalculate + 2) + cfRow + "))",
                efs.getCellRangeAddressByNumber(llRow, llRow, 11, 11));
//        efs.createCell(sheet, llRow, 11, null, accountingStyle, null);

        // Loss Years
        colCalculate = 3;
        int lyRow = startRowCalculate + 32;

        Cell lyCell = efs.createCell(sheet, lyRow, 10, null, null, null);
        lyCell.setCellValue("Loss Years");
        Cell lyValCell = efs.createCell(sheet, lyRow, 11, null, null, null);
        lyValCell.setCellType(CellType.FORMULA);
        lyValCell.setCellFormula("COUNTIF(" + efs.getIndexCol(colCalculate) + ppRow + ":"
                + efs.getIndexCol(totalColumnCalculate + 2) + ppRow + ", 0)");

        // Net Present Value
        CellStyle npvStyle = efs.cellStyle(workbook, false, false, null, null);
        npvStyle.setDataFormat(accountingFmt.getFormat("_(* #,##0_);_(* (#,##0);_(* \"-\"??_);_(@_)"));
        colCalculate = 3;
        int npvRow = startRowCalculate + 30;

        Cell npvCell = efs.createCell(sheet, npvRow, 2, null, null, null);
        npvCell.setCellValue("Net Present Value");
        Cell npvValCell = efs.createCell(sheet, npvRow, 3, null, npvStyle, null);
        npvValCell.setCellType(CellType.FORMULA);
        npvValCell.setCellFormula("NPV(" + efs.getIndexCol(colCalculate) + (npvRow + 3) + ","
                + efs.getIndexCol(colCalculate) + csRow + ":"
                + efs.getIndexCol(totalColumnCalculate + 2) + csRow + ")");
        Cell npvX1000Cell = efs.createCell(sheet, npvRow, 4, null, null, null);
        npvX1000Cell.setCellValue("xRp 1000");

        // IRR
        int irrRow = startRowCalculate + 31;
        CellStyle percentRightStyle = efs.cellStyle(workbook, false, false, null, null);
        percentRightStyle.setDataFormat(percentFmt.getFormat("0%"));

        Cell irrCell = efs.createCell(sheet, irrRow, 2, null, null, null);
        irrCell.setCellValue("IRR");
        Cell irrValCell = efs.createCell(sheet, irrRow, 3, null, percentRightStyle, null);
        irrValCell.setCellType(CellType.FORMULA);
        irrValCell.setCellFormula("IRR(" + efs.getIndexCol(colCalculate) + csRow + ":"
                + efs.getIndexCol(totalColumnCalculate + 2) + csRow + ")");

        // MIRR
        int mirrRow = startRowCalculate + 32;

        Cell mirrCell = efs.createCell(sheet, mirrRow, 2, null, null, null);
        mirrCell.setCellValue("MIRR");
        Cell mirrValCell = efs.createCell(sheet, mirrRow, 3, null, percentRightStyle, null);
        mirrValCell.setCellType(CellType.FORMULA);
        mirrValCell.setCellFormula("MIRR(" + efs.getIndexCol(colCalculate) + csRow + ":"
                + efs.getIndexCol(totalColumnCalculate + 2) + csRow + ",0,0)");

        // Discount Rate
        int drRow = startRowCalculate + 33;
        Double dr = Double.parseDouble(cp.getKondisiEkonomi().getDiscountRate()) / 100; //0.13; // Discount Rate
        CellStyle percentPlace1Style = efs.cellStyle(workbook, false, false, null, null);
        percentPlace1Style.setDataFormat(percentFmt.getFormat("0.0%"));

        Cell drCell = efs.createCell(sheet, drRow, 2, null, null, null);
        drCell.setCellValue("Discount Rate");
        Cell drValCell = efs.createCell(sheet, drRow, 3, null, percentPlace1Style, null);
        drValCell.setCellValue(dr);

        // Tax
        int txRow = startRowCalculate + 34;
        Double tax = Double.parseDouble(cp.getKondisiEkonomi().getPph()) / 100; //0.25; // Tax

        Cell txCell = efs.createCell(sheet, txRow, 2, null, null, null);
        txCell.setCellValue("Tax");
        Cell txValCell = efs.createCell(sheet, txRow, 3, null, percentPlace1Style, null);
        txValCell.setCellValue(tax);

        // Payback Periode
        int ppLastRow = startRowCalculate + 35;

        Cell ppLastCell = efs.createCell(sheet, ppLastRow, 2, null, null, null);
        ppLastCell.setCellValue("Payback Periode");
        Cell ppLastValCell = efs.createCell(sheet, ppLastRow, 3, null, accountingStyle, null);
        ppLastValCell.setCellType(CellType.FORMULA);
        ppLastValCell.setCellFormula(efs.getIndexCol(11) + lyRow + "+ABS("
                + efs.getIndexCol(11) + llRow + ")/( "
                + efs.getIndexCol(11) + fbeRow + "-" + efs.getIndexCol(11) + llRow + ")");

        // Profitability Index
        colCalculate = 3;
        Double pi = Double.parseDouble(ps.getParam(l, FormulaEnum.pi)); //0.05; // param Profitability Index
        int piRow = startRowCalculate + 36;

        Cell piCell = efs.createCell(sheet, piRow, 2, null, null, null);
        piCell.setCellValue("Profitability Index");
        Cell piValCell = efs.createCell(sheet, piRow, 3, null, accountingStyle, null);
        piValCell.setCellType(CellType.FORMULA);
        piValCell.setCellFormula("ABS(NPV(" + pi + ","
                + efs.getIndexCol(colCalculate) + riRow + ":"
                + efs.getIndexCol(totalColumnCalculate + 2) + riRow + ")/NPV("
                + pi + "," + efs.getIndexCol(colCalculate) + teiRow + ":"
                + efs.getIndexCol(totalColumnCalculate + 2) + teiRow + "))");

        // Inflation Rate
        int irRow = startRowCalculate + 37;
        Double ir = Double.parseDouble(cp.getKondisiEkonomi().getInflasi()) / 100; //0.035; // Inflation Rate

        Cell irCell = efs.createCell(sheet, irRow, 2, null, null, null);
        irCell.setCellValue("Inflation Rate");
        Cell irValCell = efs.createCell(sheet, irRow, 3, null, percentPlace1Style, null);
        irValCell.setCellValue(ir);

        // Break Event Point (BEP)
        colCalculate = 3;
        int bepRow = startRowCalculate + 38;
        Double bep = Double.parseDouble(ps.getParam(l, FormulaEnum.bep)); //1.0; // param Break Event Point (BEP)

        Cell bepCell = efs.createCell(sheet, bepRow, 2, null, null, null);
        bepCell.setCellValue("Break Event Point (BEP)");
        Cell bepValCell = efs.createCell(sheet, bepRow, 3, null, accountingStyle, null);
        bepValCell.setCellType(CellType.FORMULA);
        bepValCell.setCellFormula("ABS(SUM("
                + efs.getIndexCol(colCalculate) + ieikRow +":"+efs.getIndexCol(totalColumnCalculate + 2) + ieikRow + "))/("
                + bep + "- (ABS(SUM(" + efs.getIndexCol(colCalculate) + boRow + ":" + efs.getIndexCol(totalColumnCalculate + 2) + bttRow + "))+ABS(SUM("
                + efs.getIndexCol(colCalculate) + betRow + ":" + efs.getIndexCol(totalColumnCalculate + 2) + betRow + ")) + ABS(SUM("
                + efs.getIndexCol(colCalculate) + bosRow + ":" + efs.getIndexCol(totalColumnCalculate + 2) + bosRow + ")))/ABS(SUM("
                + efs.getIndexCol(colCalculate) + plRow + ":" + efs.getIndexCol(totalColumnCalculate + 2) + plRow + ")))");
        Cell bepX1000Cell = efs.createCell(sheet, bepRow, 4, null, null, null);
        bepX1000Cell.setCellValue("xRp 1000 (Nilai Penjualan)");

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dir = "/tmp/com.bppt.spklu/";
        Files.createDirectories(Paths.get(dir));

        String fileLocation = dir + "calculate-" + sdf.format(now) + ".xlsx";
        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();

        return sdf.format(now); //fileLocation;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    class MergeRowCol {
        private int lastRow;
        private int LastCol;
    }

}

//    public void generate(int a) throws IOException {
//        //
//        Workbook workbook = new XSSFWorkbook();
//
//        // create sheet
//        Sheet sheet = workbook.createSheet("Kalkulasi");
//
//        // et size column => A, B, C, D ...
//        sheet.setColumnWidth(0, 1000);
//        sheet.setColumnWidth(1, 2000);
//        sheet.setColumnWidth(2, 4000);
//        sheet.setColumnWidth(3, 7000);
//
//        // create row => 1, 2, 3, 4 ...
//        Row header = sheet.createRow(0);
//
//        // set cell style
//        CellStyle headerStyle = workbook.createCellStyle();
//        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//        // set font style
//        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 16);
//        font.setBold(true);
//        headerStyle.setFont(font);
//
//        // create cell on row 0, column 0 & 1 (header)
//        Cell headerCell = header.createCell(0);
//        headerCell.setCellValue("Name");
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(1);
//        headerCell.setCellValue("Age");
//        headerCell.setCellStyle(headerStyle);
//
//        ///
////        CellStyle style = workbook.createCellStyle();
////        style.setWrapText(true);
//
//        Row row = sheet.createRow(1);
//        Cell cell = row.createCell(0);
//        cell.setCellValue("John Smith");
////        cell.setCellStyle(style);
//
//        cell = row.createCell(1);
//        cell.setCellValue(20);
////        cell.setCellStyle(style);
//
//        ///
//        File currDir = new File("/");
//        String path = currDir.getAbsolutePath();
//        String fileLocation = "/temp.xlsx"; //path.substring(0, path.length() - 1) + "temp.xlsx";
//
//        FileOutputStream outputStream = new FileOutputStream(fileLocation);
//        workbook.write(outputStream);
//        workbook.close();
//    }
