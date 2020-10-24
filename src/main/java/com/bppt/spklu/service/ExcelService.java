package com.bppt.spklu.service;

import com.bppt.spklu.constant.FormulaEnum;
import com.bppt.spklu.model.CalculateParameter;
import com.bppt.spklu.entity.MainParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                "Biaya investasi lahan dan bangunan",
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
                Double.parseDouble(cp.getKondisiEkonomi().getBiayaInvestasiLahan()), //Double.parseDouble(ps.getParam(l, FormulaEnum.ieikAdd)) * 1000, //550000.0; // param Infrastructure Expense (in kIDR)
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

        // set format param
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
        efs.createCell(sheet, 12, 7, null, accountingStyle, 3000);

        // konektor
        int startRowKonektor = 10;
        int idxKonektor = 1;
        for (Integer konektor : jumlahKonektorList) {
            Cell konektorParamCell = efs.createCell(sheet, startRowKonektor, 2, null, column1CellStyle, null);
            konektorParamCell.setCellValue(">> Spesifikasi daya luaran konektor " + idxKonektor + " (kW)");
            Cell konektorValueCell = efs.createCell(sheet, startRowKonektor, 3, null, centerStyle, null);
            konektorValueCell.setCellValue(konektor);
            startRowKonektor += 1;
            idxKonektor += 1;
        }

        // Discount Rate
        int drRow = 10 + jumlahKonektorList.size() + 1;
        Double dr = Double.parseDouble(cp.getKondisiEkonomi().getDiscountRate()) / 100; //0.13; // Discount Rate
        CellStyle percentPlace1Style = efs.cellStyle(workbook, false, false, null, null);
        percentPlace1Style.setDataFormat(percentFmt.getFormat("0.0%"));

        Cell drCell = efs.createCell(sheet, drRow, 2, null, null, null);
        drCell.setCellValue("Discount Rate");
        Cell drValCell = efs.createCell(sheet, drRow, 3, null, percentPlace1Style, null);
        drValCell.setCellValue(dr);

        // Tax
        int txRow = 10 + jumlahKonektorList.size() + 2;
        Double tax = Double.parseDouble(cp.getKondisiEkonomi().getPph()) / 100; //0.25; // Tax

        Cell txCell = efs.createCell(sheet, txRow, 2, null, null, null);
        txCell.setCellValue("Tax");
        Cell txValCell = efs.createCell(sheet, txRow, 3, null, percentPlace1Style, null);
        txValCell.setCellValue(tax);

        // Inflation Rate
        int irRow = 10 + jumlahKonektorList.size() + 3;
        Double ir = Double.parseDouble(cp.getKondisiEkonomi().getInflasi()) / 100; //0.035; // Inflation Rate

        Cell irCell = efs.createCell(sheet, irRow, 2, null, null, null);
        irCell.setCellValue("Inflation Rate");
        Cell irValCell = efs.createCell(sheet, irRow, 3, null, percentPlace1Style, null);
        irValCell.setCellValue(ir);





        int ST_COL_CALC = 3;
        int LEN_COL_CALC = 0;
        int ST_ROW_CALC = 10 + jumlahKonektorList.size() + 6;


        // tahun ==> 0
        Cell tahunCell = efs.createCell(sheet, ST_ROW_CALC, 2, null, null, null);
        tahunCell.setCellValue("Tahun");

        for (int i = stYear; i <= edYear; i += 1) {
            Cell cell = efs.createCell(sheet, ST_ROW_CALC, ST_COL_CALC, null, null, 4000);
            cell.setCellValue(stYear + LEN_COL_CALC);
            ST_COL_CALC += 1;
            LEN_COL_CALC += 1;
        }

        // tahun ke ==> 1
        int tkRow = ST_ROW_CALC + 1;
        Cell tahunKeCell = efs.createCell(sheet, tkRow, 2, null, null, null);
        tahunKeCell.setCellValue("Tahun ke-");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, tkRow, ST_COL_CALC, null, null, null);
            cell.setCellValue(i);
            ST_COL_CALC += 1;
        }



        // ? Populasi Kendaraan Listrik ==> 6
        int pklRow = ST_ROW_CALC + 6;
        Cell pklCell = efs.createCell(sheet, pklRow, 2, null, null, null);
        pklCell.setCellValue("   ? Populasi Kendaraan Listrik");
        Double pkl = Double.parseDouble(ps.getParam(l, FormulaEnum.pkl)); //1.0; // param ? Populasi Kendaraan Listrik

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, pklRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("C3"); // =C3
            else cell.setCellFormula("FLOOR((" + pkl + " + $G$3)*" + efs.getIndexCol(ST_COL_CALC - 1) + pklRow + ", 1)"); // =FLOOR((1 + $G$3)*C26, 1)
            ST_COL_CALC += 1;
        }

        // ? Jumlah SPKLU*brp sebenarnya (10% dari jumlah BEV) ==> 4
        int jsRow = ST_ROW_CALC + 4;
        Cell jsCell = efs.createCell(sheet, jsRow, 2, null, null, null);
        jsCell.setCellValue("   ? Jumlah SPKLU*brp sebenarnya (10% dari jumlah BEV)");
        Double js = Double.parseDouble(ps.getParam(l, FormulaEnum.pkl)); //1.0; // param ? Populasi Kendaraan Listrik

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, jsRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("FLOOR(" + js + "/$C$7*" + efs.getIndexCol(ST_COL_CALC) + pklRow + ",1)"); // =FLOOR(1/$C$7*C26,1)
            ST_COL_CALC += 1;
        }


        // Kebutuhan Energi harian (KWH) ==> 2
        int kehRow = ST_ROW_CALC + 2;
        Cell kehCell = efs.createCell(sheet, kehRow, 2, null, null, null);
        kehCell.setCellValue("   Kebutuhan Energi harian (KWH)");
        Double keh = Double.parseDouble(ps.getParam(l, FormulaEnum.keh)); //1.0; // param Kebutuhan Energi harian (KWH)

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, kehRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(ST_COL_CALC) + (jsRow) + "*$C$7*$C$8*(" + keh +"+$G$9)"); // =C24*$C$7*$C$8*(1+$G$9)
            ST_COL_CALC += 1;
        }

        // ? Harga Energi (Rp/KwH) ==> 3
        int heRow = ST_ROW_CALC + 3;
        Cell heCell = efs.createCell(sheet, heRow, 2, null, null, null);
        heCell.setCellValue("   Harga Energi (Rp/KwH)");

        Double heMtp = Double.parseDouble(ps.getParam(l, FormulaEnum.heMtp)); //0.707; // param ? Harga Energi (Rp/KwH)
        Double heMin = Double.parseDouble(ps.getParam(l, FormulaEnum.heMin)); //1.0; // param ? Harga Energi (Rp/KwH)
        Double heMtpN = Double.parseDouble(ps.getParam(l, FormulaEnum.heMtpN)); //0.035; // param ? Harga Energi (Rp/KwH)

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, heRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula(heMtp + "*$G$7 * (" + heMin + "-$G$6)"); // =0.707*$G$7 * (1-$G$6)
            else cell.setCellFormula(efs.getIndexCol(ST_COL_CALC - 1) + heRow + "+"
                    + heMtpN + "*" + efs.getIndexCol(ST_COL_CALC - 1) + heRow + "*(" + heMin + "-$G$6)"); // =C23+0.035*C23*(1-$G$6)
            ST_COL_CALC += 1;
        }

        // SPKLU Baru ==> 5
        int sbRow = ST_ROW_CALC + 5;
        Cell sbCell = efs.createCell(sheet, sbRow, 2, null, null, null);
        sbCell.setCellValue("   SPKLU Baru");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, sbRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula(efs.getIndexCol(ST_COL_CALC) + jsRow); // =C24
            else cell.setCellFormula(efs.getIndexCol(ST_COL_CALC) + jsRow + "-" + efs.getIndexCol(ST_COL_CALC - 1) + jsRow); // =D24-C24
            ST_COL_CALC += 1;
        }

        // ? Populasi Kendaraan Hybrid (Gaikindo) ==> 7
        int pkhgRow = ST_ROW_CALC + 7;
        Cell pkhgCell = efs.createCell(sheet, pkhgRow, 2, null, null, null);
        pkhgCell.setCellValue("   ? Populasi Kendaraan Hybrid (Gaikindo)");
        Double pkhg = Double.parseDouble(ps.getParam(l, FormulaEnum.pkhg)); //1.0; // param ? Populasi Kendaraan Hybrid (Gaikindo)

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, pkhgRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("C4"); // =C4
            else cell.setCellFormula("FLOOR((" + pkhg + " + $G$4)*" + efs.getIndexCol(ST_COL_CALC - 1) + pkhgRow + ", 1)"); // =FLOOR((1 + $G$4)*C27, 1)
            ST_COL_CALC += 1;
        }

        // ? Biaya Charging ==> 8
        int bcRow = ST_ROW_CALC + 8;
        Cell bcCell = efs.createCell(sheet, bcRow, 2, null, null, null);
        bcCell.setCellValue("   ? Biaya Charging");
        Double bc = Double.parseDouble(ps.getParam(l, FormulaEnum.bc)); //1.65; // param ? Biaya Charging

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, bcRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("C8*" + bc + "*G8"); // =C8*1.65*G8
            else cell.setCellFormula(efs.getIndexCol(ST_COL_CALC - 1) + bcRow); // =C28
            ST_COL_CALC += 1;
        }

        // ? Frekuensi Charging Full EV harian ==> 9
        int fcfehRow = ST_ROW_CALC + 9;
        Cell fcfehCell = efs.createCell(sheet, fcfehRow, 2, null, null, null);
        fcfehCell.setCellValue("   ? Frekuensi Charging Full EV harian");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, fcfehRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("$C$5"); // =$C$5
            ST_COL_CALC += 1;
        }

        // ? Frekuensi Charging Hybrid harian ==> 10
        int fchhRow = ST_ROW_CALC + 10;
        Cell fchhCell = efs.createCell(sheet, fchhRow, 2, null, null, null);
        fchhCell.setCellValue("   ? Frekuensi Charging Hybrid harian");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, fchhRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("$C$6"); // =$C$6
            ST_COL_CALC += 1;
        }

        // space row ==> 11

        // Revenue (In kIDR) ==> 12
        int rikRow = ST_ROW_CALC + 12;
        CellStyle boldStyle = efs.cellStyle(workbook, false, true, null, null);
        Cell rikCell = efs.createCell(sheet, rikRow, 2, null, boldStyle, null);
        rikCell.setCellValue("Revenue (In kIDR)");

        // Pendapatan Kontrak ==> 13
        int pkRow = ST_ROW_CALC + 13;
        Cell pkCell = efs.createCell(sheet, pkRow, 2, null, null, null);
        pkCell.setCellValue("   Pendapatan Kontrak");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, pkRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("0");
            ST_COL_CALC += 1;
        }

        // Pendapatan Layanan ==> 14
        int plRow = ST_ROW_CALC + 14;
        Cell plCell = efs.createCell(sheet, plRow, 2, null, null, null);
        plCell.setCellValue("   Pendapatan Layanan");
        Double pl = Double.parseDouble(ps.getParam(l, FormulaEnum.pl)); //365.0; // param Pendapatan Layanan

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, plRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(ST_COL_CALC) + (pklRow) + "*"
                    + efs.getIndexCol(ST_COL_CALC) + (bcRow) + "*"
                    + efs.getIndexCol(ST_COL_CALC) + (fcfehRow) + "*" + pl); // =C26*C28*C29*365
            ST_COL_CALC += 1;
        }

        // Revenue (inflated) ==> 15
        int riRow = ST_ROW_CALC + 15;
        Cell riCell = efs.createCell(sheet, riRow, 2, null, null, null);
        riCell.setCellValue("   Revenue (inflated)");
        Double ri = Double.parseDouble(ps.getParam(l, FormulaEnum.ri)); //1.0; // param Revenue (inflated)

        ST_COL_CALC = 3;
//        for (int i = 0; i < LEN_COL_CALC; i += 1) {
//            Cell cell = efs.createCell(sheet, riRow, ST_COL_CALC, null, accountingStyle, null);
//            cell.setCellType(CellType.FORMULA);
//            if (i == 0) cell.setCellFormula("SUM(" + efs.getIndexCol(ST_COL_CALC) + pkRow
//                    + ":" + efs.getIndexCol(ST_COL_CALC) + plRow + ")"); // =SUM(C34:C35)
//            else cell.setCellFormula("SUM("+ efs.getIndexCol(ST_COL_CALC) + pkRow +":"
//                    + efs.getIndexCol(ST_COL_CALC) + plRow +")*("+ ri +"+("
//                    + efs.getIndexCol(ST_COL_CALC) + ST_ROW_CALC + "-$C$" + ST_ROW_CALC + ")*$C$" + irRow + ")"); // =SUM(D34:D35)*(1+(D20-$C$20)*$C$15)
//            ST_COL_CALC += 1;
//        }

        // Expense (in kIDR) ==> 16
        int eikRow = ST_ROW_CALC + 16;
        Cell eikCell = efs.createCell(sheet, eikRow, 2, null, boldStyle, null);
        eikCell.setCellValue("Expense (in kIDR)");

        // Biaya Energy Tahunan ==> 17
        int betRow = ST_ROW_CALC + 17;
        Cell betCell = efs.createCell(sheet, betRow, 2, null, null, null);
        betCell.setCellValue("   Biaya Energy Tahunan");
        Double bet = Double.parseDouble(ps.getParam(l, FormulaEnum.bet)); //365.0; // param Biaya Energy Tahunan

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, betRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-" + efs.getIndexCol(ST_COL_CALC) + kehRow
                    + "*" + efs.getIndexCol(ST_COL_CALC) + heRow + "*" + bet); // =-C22*C23*365
            ST_COL_CALC += 1;
        }

        // Biaya Operasi SPKLU, Gaji Pegawai Pertahun ==> 18
        int bosRow = ST_ROW_CALC + 18;
        Cell bosCell = efs.createCell(sheet, bosRow, 2, null, null, null);
        bosCell.setCellValue("   Biaya Operasi SPKLU, Gaji Pegawai Pertahun");
        Double bos = Double.parseDouble(ps.getParam(l, FormulaEnum.bos)); //57600.0; // param Biaya Operasi SPKLU, Gaji Pegawai Pertahun

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, bosRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-" + efs.getIndexCol(ST_COL_CALC) + jsRow + "*" + bos); // =-C24*57600
            ST_COL_CALC += 1;
        }

        // Infrastructure Expense (in kIDR) ==> 19
        int ieikRow = ST_ROW_CALC + 19;
        Cell ieikCell = efs.createCell(sheet, ieikRow, 2, null, null, null);
        ieikCell.setCellValue("   Infrastructure Expense (in kIDR)");
        Double ieikAdd = Double.parseDouble(ps.getParam(l, FormulaEnum.ieikAdd)); //550000.0; // param Infrastructure Expense (in kIDR)
        Double ieikDiv = Double.parseDouble(ps.getParam(l, FormulaEnum.ieikDiv)); //1000.0; // param Infrastructure Expense (in kIDR)

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, ieikRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-(($G$12/1000)+$G$5/"+ ieikDiv + ")*" + efs.getIndexCol(ST_COL_CALC) + sbRow); // =-(550000+$G$5/1000)*C25
//            cell.setCellFormula("-(" + ieikAdd + "+$G$5/"+ ieikDiv + ")*" + efs.getIndexCol(ST_COL_CALC) + sbRow); // =-(550000+$G$5/1000)*C25
            ST_COL_CALC += 1;
        }

        // Biaya Operasional *) ==> 20
        int boRow = ST_ROW_CALC + 20;
        Cell boCell = efs.createCell(sheet, boRow, 2, null, null, null);
        boCell.setCellValue("   Biaya Operasional");
        Double bo = Double.parseDouble(ps.getParam(l, FormulaEnum.bo)); //0.02; // param Biaya Operasional *)

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, boRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("(" + bo + "*" + efs.getIndexCol(ST_COL_CALC) + ieikRow +")+($G$11*" + efs.getIndexCol(ST_COL_CALC)+ jsRow + ")"); // =(0.02*C40)+($G$11*C24)
            ST_COL_CALC += 1;
        }

        // Biaya Pemasaran ==> 21
        int bpRow = ST_ROW_CALC + 21;
        Cell bpCell = efs.createCell(sheet, bpRow, 2, null, null, null);
        bpCell.setCellValue("   Biaya Pemasaran");
        Double bp = Double.parseDouble(ps.getParam(l, FormulaEnum.bp)); //0.02; // param Biaya Pemasaran

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, bpRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(bp + "*" + efs.getIndexCol(ST_COL_CALC) + ieikRow); // =0.02*C40
            ST_COL_CALC += 1;
        }

        // Biaya Tak Terduga ==> 22
        int bttRow = ST_ROW_CALC + 22;
        Cell bttCell = efs.createCell(sheet, bttRow, 2, null, null, null);
        bttCell.setCellValue("   Biaya Tak Terduga");
        Double btt = Double.parseDouble(ps.getParam(l, FormulaEnum.btt)); //100000.0; // param Biaya Tak Terduga

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, bttRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("-" + btt); // =-100000
            ST_COL_CALC += 1;
        }

        // Total Expense (inflated) ==> 23
        int teiRow = ST_ROW_CALC + 23;
        Cell teiCell = efs.createCell(sheet, teiRow, 2, null, boldStyle, null);
        teiCell.setCellValue("Total Expense (inflated)");
        Double tei = Double.parseDouble(ps.getParam(l, FormulaEnum.tei)); //1.0; // param Total Expense (inflated)

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, teiRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula("(SUM(" + efs.getIndexCol(ST_COL_CALC) + ieikRow
                    + ":" + efs.getIndexCol(ST_COL_CALC) + bttRow
                    + ")+" + efs.getIndexCol(ST_COL_CALC) + betRow
                    + "+SUM(" + efs.getIndexCol(ST_COL_CALC) + bosRow
                    +":" + efs.getIndexCol(ST_COL_CALC) + bosRow + "))"); // =(SUM(C40:C43)+C38+SUM(C39:C39))

            else cell.setCellFormula(efs.getIndexCol(ST_COL_CALC) + betRow
                    + "+" + efs.getIndexCol(ST_COL_CALC) + ieikRow + "+(SUM("
                    + efs.getIndexCol(ST_COL_CALC) + boRow + ":" + efs.getIndexCol(ST_COL_CALC) + bttRow + ")+ "
                    + efs.getIndexCol(ST_COL_CALC) + bosRow + ")*(" + tei + "+("
                    + efs.getIndexCol(ST_COL_CALC) + ST_ROW_CALC + "-$C$" + ST_ROW_CALC + ")*$C$" + irRow + ")"); // =D38+D40+(SUM(D41:D43)+ D39)*(1+(D20-$C$20)*$C$15)
            ST_COL_CALC += 1;
        }

        // Gross Profit (In kIDR) ==> 24
        int gpikRow = ST_ROW_CALC + 24;
        Cell gpikCell = efs.createCell(sheet, gpikRow, 2, null, boldStyle, null);
        gpikCell.setCellValue("Gross Profit (In kIDR)");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, gpikRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(ST_COL_CALC) + plRow + "+" + efs.getIndexCol(ST_COL_CALC) + teiRow); // =C35+C44
            ST_COL_CALC += 1;
        }

        // Tax ==> 25
        int taxRow = ST_ROW_CALC + 25;
        Cell taxCell = efs.createCell(sheet, taxRow, 2, null, null, null);
        taxCell.setCellValue("Tax");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, taxRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("IF(" + efs.getIndexCol(ST_COL_CALC) + gpikRow + ">0,"
                    + efs.getIndexCol(ST_COL_CALC) + gpikRow + "*$C$" + txRow +",0)"); // =IF(C45>0,C45*$C$14,0)
            ST_COL_CALC += 1;
        }

        // Cash Flow ==> 26
        int csRow = ST_ROW_CALC + 26;
        Cell csCell = efs.createCell(sheet, csRow, 2, null, boldStyle, null);
        csCell.setCellValue("Cash Flow");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, csRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(ST_COL_CALC) + gpikRow + "-" + efs.getIndexCol(ST_COL_CALC) + taxRow); // =C45-C46
            ST_COL_CALC += 1;
        }

        // Discounted Cash Flow ==> 27
        int dcsRow = ST_ROW_CALC + 27;
        Cell dcsCell = efs.createCell(sheet, dcsRow, 2, null, null, null);
        dcsCell.setCellValue("Discounted Cash Flow");
        Double dcs = Double.parseDouble(ps.getParam(l, FormulaEnum.dcs)); //1.0; // param Discounted Cash Flow

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, dcsRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(efs.getIndexCol(ST_COL_CALC) + csRow + "/("+ dcs +"+$C$"+ drRow + ")^("
                    + efs.getIndexCol(ST_COL_CALC) + tkRow + "+" + dcs + ")"); // =C47/(1+$C$13)^(C21+1)
            ST_COL_CALC += 1;
        }

        // Cummulative Flow ==> 28
        int cfRow = ST_ROW_CALC + 28;
        Cell cfCell = efs.createCell(sheet, cfRow, 2, null, null, null);
        cfCell.setCellValue("Cummulative Flow");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, cfRow, ST_COL_CALC, null, accountingStyle, null);
            cell.setCellType(CellType.FORMULA);
            if (i == 0) cell.setCellFormula(efs.getIndexCol(ST_COL_CALC) + csRow); // =C47
            else cell.setCellFormula("SUM($C$" + dcsRow + ":" + efs.getIndexCol(ST_COL_CALC) + dcsRow + ")"); // =SUM($C$48:D48)
            ST_COL_CALC += 1;
        }

        // Payback Period ==> 29
        int ppRow = ST_ROW_CALC + 29;
        Cell ppCell = efs.createCell(sheet, ppRow, 2, null, null, null);
        ppCell.setCellValue("Payback Period");

        ST_COL_CALC = 3;
        for (int i = 0; i < LEN_COL_CALC; i += 1) {
            Cell cell = efs.createCell(sheet, ppRow, ST_COL_CALC, null, null, null);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("IF("+ efs.getIndexCol(ST_COL_CALC) + cfRow +"<1,\"0\",\"1\")");
            ST_COL_CALC += 1;
        }

        //space row ==> 30


        // First Break Even ==> J-31
        ST_COL_CALC = 3;
        int fbeRow = ST_ROW_CALC + 31;
        Cell fbeCell = efs.createCell(sheet, fbeRow, 10, null, null, null);
        fbeCell.setCellValue("First Break Even");

        sheet.setArrayFormula("MIN(IF(" + efs.getIndexCol(ST_COL_CALC) + cfRow + ":"
                        + efs.getIndexCol(LEN_COL_CALC + 2) + cfRow + ">0,"
                        + efs.getIndexCol(ST_COL_CALC) + cfRow + ":" + efs.getIndexCol(LEN_COL_CALC + 2) + cfRow + "))",
                efs.getCellRangeAddressByNumber(fbeRow, fbeRow, 11, 11));
//        efs.createCell(sheet, fbeRow, 11, null, accountingStyle, null);

        // Last loss ==> J-32
        ST_COL_CALC = 3;
        int llRow = ST_ROW_CALC + 32;
        Cell llCell = efs.createCell(sheet, llRow, 10, null, null, null);
        llCell.setCellValue("Last loss");

        sheet.setArrayFormula("MAX(IF(" + efs.getIndexCol(ST_COL_CALC) + cfRow + ":"
                        + efs.getIndexCol(LEN_COL_CALC + 2) + cfRow + "<0,"
                        + efs.getIndexCol(ST_COL_CALC) + cfRow + ":" + efs.getIndexCol(LEN_COL_CALC + 2) + cfRow + "))",
                efs.getCellRangeAddressByNumber(llRow, llRow, 11, 11));
//        efs.createCell(sheet, llRow, 11, null, accountingStyle, null);

        // Loss Years ==> J-33
        ST_COL_CALC = 3;
        int lyRow = ST_ROW_CALC + 33;
        Cell lyCell = efs.createCell(sheet, lyRow, 10, null, null, null);
        lyCell.setCellValue("Loss Years");

        Cell lyValCell = efs.createCell(sheet, lyRow, 11, null, null, null);
        lyValCell.setCellType(CellType.FORMULA);
        lyValCell.setCellFormula("COUNTIF(" + efs.getIndexCol(ST_COL_CALC) + ppRow + ":"
                + efs.getIndexCol(LEN_COL_CALC + 2) + ppRow + ", 0)");



        // Net Present Value ==> B-31
        CellStyle npvStyle = efs.cellStyle(workbook, false, false, null, null);
        npvStyle.setDataFormat(accountingFmt.getFormat("_(* #,##0_);_(* (#,##0);_(* \"-\"??_);_(@_)"));
        ST_COL_CALC = 3;
        int npvRow = ST_ROW_CALC + 31;

        Cell npvCell = efs.createCell(sheet, npvRow, 2, null, null, null);
        npvCell.setCellValue("Net Present Value");
        Cell npvValCell = efs.createCell(sheet, npvRow, 3, null, npvStyle, null);
        npvValCell.setCellType(CellType.FORMULA);
        npvValCell.setCellFormula("NPV(" + efs.getIndexCol(ST_COL_CALC) + drRow + ","
                + efs.getIndexCol(ST_COL_CALC) + csRow + ":"
                + efs.getIndexCol(LEN_COL_CALC + 2) + csRow + ")");
        Cell npvX1000Cell = efs.createCell(sheet, npvRow, 4, null, null, null);
        npvX1000Cell.setCellValue("xRp 1000");

        // IRR ==> B-32
        int irrRow = ST_ROW_CALC + 32;
        CellStyle percentRightStyle = efs.cellStyle(workbook, false, false, null, null);
        percentRightStyle.setDataFormat(percentFmt.getFormat("0%"));

        Cell irrCell = efs.createCell(sheet, irrRow, 2, null, null, null);
        irrCell.setCellValue("IRR");
        Cell irrValCell = efs.createCell(sheet, irrRow, 3, null, percentRightStyle, null);
        irrValCell.setCellType(CellType.FORMULA);
        irrValCell.setCellFormula("IRR(" + efs.getIndexCol(ST_COL_CALC) + csRow + ":"
                + efs.getIndexCol(LEN_COL_CALC + 2) + csRow + ")");

        // MIRR ==> B-33
        int mirrRow = ST_ROW_CALC + 33;

        Cell mirrCell = efs.createCell(sheet, mirrRow, 2, null, null, null);
        mirrCell.setCellValue("MIRR");
        Cell mirrValCell = efs.createCell(sheet, mirrRow, 3, null, percentRightStyle, null);
        mirrValCell.setCellType(CellType.FORMULA);
        mirrValCell.setCellFormula("MIRR(" + efs.getIndexCol(ST_COL_CALC) + csRow + ":"
                + efs.getIndexCol(LEN_COL_CALC + 2) + csRow + ",0,0)");

        // Payback Periode ==> B-34
        int ppLastRow = ST_ROW_CALC + 34;

        Cell ppLastCell = efs.createCell(sheet, ppLastRow, 2, null, null, null);
        ppLastCell.setCellValue("Payback Periode");
        Cell ppLastValCell = efs.createCell(sheet, ppLastRow, 3, null, accountingStyle, null);
        ppLastValCell.setCellType(CellType.FORMULA);
        ppLastValCell.setCellFormula(efs.getIndexCol(11) + lyRow + "+ABS("
                + efs.getIndexCol(11) + llRow + ")/( "
                + efs.getIndexCol(11) + fbeRow + "-" + efs.getIndexCol(11) + llRow + ")");

        // Profitability Index ==> B-35
        ST_COL_CALC = 3;
        Double pi = Double.parseDouble(ps.getParam(l, FormulaEnum.pi)); //0.05; // param Profitability Index
        int piRow = ST_ROW_CALC + 35;

        Cell piCell = efs.createCell(sheet, piRow, 2, null, null, null);
        piCell.setCellValue("Profitability Index");
        Cell piValCell = efs.createCell(sheet, piRow, 3, null, accountingStyle, null);
        piValCell.setCellType(CellType.FORMULA);
        piValCell.setCellFormula("ABS(NPV(" + pi + ","
                + efs.getIndexCol(ST_COL_CALC) + plRow + ":" // riRow => plRow
                + efs.getIndexCol(LEN_COL_CALC + 2) + plRow + ")/NPV(" // riRow => plRow
                + pi + "," + efs.getIndexCol(ST_COL_CALC) + teiRow + ":"
                + efs.getIndexCol(LEN_COL_CALC + 2) + teiRow + "))");


        // Break Event Point (BEP) ==> B-36
        ST_COL_CALC = 3;
        int bepRow = ST_ROW_CALC + 36;
        Double bep = Double.parseDouble(ps.getParam(l, FormulaEnum.bep)); //1.0; // param Break Event Point (BEP)

        Cell bepCell = efs.createCell(sheet, bepRow, 2, null, null, null);
        bepCell.setCellValue("Break Event Point (BEP)");
        Cell bepValCell = efs.createCell(sheet, bepRow, 3, null, accountingStyle, null);
        bepValCell.setCellType(CellType.FORMULA);
        bepValCell.setCellFormula("ABS(SUM("
                + efs.getIndexCol(ST_COL_CALC) + ieikRow +":"+efs.getIndexCol(LEN_COL_CALC + 2) + ieikRow + "))/("
                + bep + "- (ABS(SUM(" + efs.getIndexCol(ST_COL_CALC) + boRow + ":" + efs.getIndexCol(LEN_COL_CALC + 2) + bttRow + "))+ABS(SUM("
                + efs.getIndexCol(ST_COL_CALC) + betRow + ":" + efs.getIndexCol(LEN_COL_CALC + 2) + betRow + ")) + ABS(SUM("
                + efs.getIndexCol(ST_COL_CALC) + bosRow + ":" + efs.getIndexCol(LEN_COL_CALC + 2) + bosRow + ")))/ABS(SUM("
                + efs.getIndexCol(ST_COL_CALC) + plRow + ":" + efs.getIndexCol(LEN_COL_CALC + 2) + plRow + ")))");
        Cell bepX1000Cell = efs.createCell(sheet, bepRow, 4, null, null, null);
        bepX1000Cell.setCellValue("xRp 1000 (Nilai Penjualan)");


//        // Income (In kIDR)
//        int iikRow = ST_ROW_CALC + 19;
//        Cell iikCell = efs.createCell(sheet, iikRow, 2, null, null, null);
//        iikCell.setCellValue("Income (In kIDR)");


        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dir = ps.getParam(l, FormulaEnum.dirTemp); //"/tmp/com.bppt.spklu/";
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
