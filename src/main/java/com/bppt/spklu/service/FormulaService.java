package com.bppt.spklu.service;

import com.bppt.spklu.constant.FormulaEnum;
import com.bppt.spklu.model.CalculateParameter;
import com.bppt.spklu.entity.MainParameter;
import com.bppt.spklu.model.CalcOptmz;
import com.bppt.spklu.model.ResponseCalculate;
import com.bppt.spklu.model.ResultOptimize;
import com.bppt.spklu.repo.MainParameterRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FormulaService {

    @Autowired
    private FormulaService fs;

    @Autowired
    private MainParameterRepo mpr;

    public ResultOptimize optHargaEvse(CalculateParameter cp) {
        long stTime = System.currentTimeMillis();

        double cost = 9E9; //Double.parseDouble(cp.getKondisiEkonomi().getBiayaSpklu());
        CalcOptmz ro = fs.genFormula(cp);
        double ppStart = Double.parseDouble(ro.getResponseCalculate().getPprd());

        MainParameter mp = mpr.findFirstByKey("ppOptimize");
        double ppOptz = Double.parseDouble(mp.getValue());

        if (ppOptz != ppStart) {
            String len = String.format("%.0f", cost);
            int zero = len.length() - 1;

            double pp = 0;
            double result = 0;
            int loop = 0;

            while (true) {
                if (result == 0)
                    result = cost - Math.pow(10, zero);
                else {
                    if (result == Math.pow(10, zero)) {
                        zero -= 1;
                        result = result - Math.pow(10, zero);
                    } else {
                        result = result - Math.pow(10, zero);
                    }
                }

                cp.getKondisiEkonomi().setBiayaSpklu(String.format("%.0f", result));
                pp = Double.parseDouble(fs.genFormula(cp).getPaybackPeriod());

                log.info("pp={} hasilKurang={}", pp, String.format("%.0f", result));

                if (pp < ppOptz) {
                    if (result == 0)
                        result = cost + Math.pow(10, zero);
                    else
                        result = result + Math.pow(10, zero);
                    zero -= 1;
                }

                if (pp == ppOptz) {
                    log.info("akhir pp={} hasilKurang={}", pp, String.format("%.0f", result));
                    break;
                }

                loop += 1;
                if (loop == 90) {
                    break;
                }
            }
            long edTime = (System.currentTimeMillis() - stTime);
            log.info("hasil waktu={} hasil loop={}", String.format("%.3f", Double.parseDouble(String.valueOf(edTime)) / 1000), loop);
        }
        ResultOptimize opt = new ResultOptimize();
        opt.setRequestCalculate(cp);
        opt.setResponseCalculate(fs.genFormula(cp).getResponseCalculate());
        return opt;
    }


    public ResultOptimize optRasioSpklu(CalculateParameter cp) {
        CalcOptmz ro = fs.genFormula(cp);
        double ppStart = Double.parseDouble(ro.getResponseCalculate().getPprd());

        Double startData = new Double(0);
        Double param = new Double(cp.getParameterBisnis().getRasioSpklu());
        if(ppStart > new Double(3.5)){
            while (true) {
                startData = param * 2;
                System.out.println("start data : " + startData);
                cp.getParameterBisnis().setRasioSpklu(String.format("%.0f", startData));
                Double pp = Double.parseDouble(fs.genFormula(cp).getPaybackPeriod());
                if (pp < 3.5) break;
            }
        }else{
            startData = param;
        }

        int lengthNumb = String.format("%.0f", startData).length();
        if(lengthNumb > 2){
            int pw = lengthNumb - 1;
            int initStart = 9;
            int setPar = 0;
            int lastPar = 0;
            while(true){
                int par = (pw > 0) ? (int) Math.pow(10, pw) * initStart : initStart;
                par += setPar;

                System.out.println("par : "+par);
                initStart -= 1;

                cp.getParameterBisnis().setRasioSpklu(String.valueOf(par));
                Double pp = Double.parseDouble(fs.genFormula(cp).getPaybackPeriod());

                if(pp > 3.5) {
                    setPar = par;
                    lastPar = (lastPar < par && par < 3.5) ? par : lastPar;
                    cp.getParameterBisnis().setRasioSpklu(String.valueOf(lastPar));
                    pw -= 1;
                    initStart = 9;
                }

                if(initStart == 0){
                    pw -= 1;
                    initStart = 9;
                    System.out.println("pw : "+pw);
                }

                System.out.println("pp : "+pp);
                System.out.println("----------");
                lastPar = par;
                if(pw < 0) break;
            }
        }

        if(lengthNumb == 2){
            /*set max value*/
            int lp = (Integer.parseInt(String.format("%.0f", startData)) + 10) / 10;
            lp = lp * 10;

            while (true){
                System.out.println("lp : "+lp);
                cp.getParameterBisnis().setRasioSpklu(String.valueOf(lp));
                Double pp = Double.parseDouble(fs.genFormula(cp).getPaybackPeriod());

                if(pp > 3.5) break;
                lp -= 10;
            }

            int x = 9;
            lp = lp + 9;
            while (true){
                cp.getParameterBisnis().setRasioSpklu(String.valueOf(lp));
                Double pp = Double.parseDouble(fs.genFormula(cp).getPaybackPeriod());

                if(x == 0 || pp > 3.5){
                    lp += 1;
                    /*set new parameter optimize rasio spklu*/
                    cp.getParameterBisnis().setRasioSpklu(String.valueOf(lp));
                    break;
                }

                lp -= 1;
                x -= 1;
            }
        }

        ResultOptimize opt = new ResultOptimize();
        opt.setRequestCalculate(cp);
        opt.setResponseCalculate(fs.genFormula(cp).getResponseCalculate());
        return opt;
    }

    public ResultOptimize optRasioHargaListrikPln(CalculateParameter cp) {
        Double max = new Double(2);
        Double min = new Double(0.8);
        Double lastMax = new Double(0);
        while (true){
            cp.getParameterBisnis().setHargaJualPln(min.toString());
            Double pp = Double.parseDouble(fs.genFormula(cp).getPaybackPeriod());
            if(pp > 3.5 || min > max) {
                if(min > max){
                    lastMax = new Double(2);
                }else{
                    lastMax = (min > 0.8) ? min - 0.1 : 0.8;
                }
                break;
            }
            min += 0.1;
        }

        if(lastMax < max && lastMax > 0.8){
            lastMax += 0.09;
            while (true){
                cp.getParameterBisnis().setHargaJualPln(lastMax.toString());
                Double pp = Double.parseDouble(fs.genFormula(cp).getPaybackPeriod());
                System.out.println("strt : "+lastMax);
                if(pp <= 3.5) break;
                lastMax -= 0.01;
            }
        }

        cp.getParameterBisnis().setHargaJualPln(String.valueOf(round(lastMax, 2)));
        ResultOptimize opt = new ResultOptimize();
        opt.setRequestCalculate(cp);
        opt.setResponseCalculate(fs.genFormula(cp).getResponseCalculate());
        return opt;
    }

    public ResultOptimize optRasioHargaJualKonsumen(CalculateParameter cp) {
        Double max = new Double(1.5);
        Double min = new Double(0);
        Double lastMax = new Double(0);
        while (true){
            cp.getParameterBisnis().setHargaJualKonsumen(min.toString());
            Double pp = Double.parseDouble(fs.genFormula(cp).getPaybackPeriod());
//            System.out.println("pp : "+pp);
//            System.out.println("pr : "+min);
            if(pp < 3.5 || min > max) {
                if(min > max){
                    lastMax = new Double(1.5);
                }else{
                    lastMax = (min > 0) ? min : 0;
                }
                break;
            }
            min += 0.1;
        }

        if(lastMax < max){
            lastMax -= 0.1;
            lastMax += 0.09;
            while (true){
                cp.getParameterBisnis().setHargaJualKonsumen(lastMax.toString());
                Double pp = Double.parseDouble(fs.genFormula(cp).getPaybackPeriod());
//                System.out.println("strt : "+lastMax);
                if(pp >= 3.5) break;
                lastMax -= 0.01;
            }
        }

        cp.getParameterBisnis().setHargaJualKonsumen(String.valueOf(round(lastMax, 2)));
        ResultOptimize opt = new ResultOptimize();
        opt.setRequestCalculate(cp);
        opt.setResponseCalculate(fs.genFormula(cp).getResponseCalculate());
        return opt;
    }

    @Autowired
    private ParameterService ps;

    public CalcOptmz genFormula(CalculateParameter cp) {
        List<MainParameter> l = ps.getParameters();

        Double jkfev = Double.parseDouble(cp.getKondisiEkonomi().getJumlahKendaraanInisial()); //600.0; // Jumlah Kendaraan Full EV pada tahun baseline
        Double pkl = Double.parseDouble(ps.getParam(l, FormulaEnum.pkl)); //1.0; // param ? Populasi Kendaraan Listrik
        Double ptkev = Double.parseDouble(cp.getParameterBisnis().getPertumbuhanKblPerTahun()) / 100; //0.23; // Pertumbuhan Tahunan Kendaraan EV

        Integer stYear = Integer.parseInt(cp.getYears().getStartYear()); //2020;
        Integer edYear = Integer.parseInt(cp.getYears().getFinishYear()); //2030;
        List<Double> yearRes = new ArrayList<>(); // tahun 2020, 2021, 2022, ...
        for (Integer i = stYear; i <= edYear; i+=1) {
            yearRes.add(Double.parseDouble(i + ""));
        }
        List<Double> yearToRes = new ArrayList<>(); // tahun ke 0, 1, 2, ...
        Double yt = 0.0;
        for (Integer i = stYear; i <= edYear; i+=1) {
            yearToRes.add(yt);
            yt += 1;
        }
        int length = yearToRes.size() - 1; //10; // range year 0 - 10 => 11

        Double rsbb = Double.parseDouble(cp.getParameterBisnis().getRasioSpklu()); //38.0; // Rasio SPKLU Banding BEV, 1:N

        Double hs = Double.parseDouble(cp.getKondisiEkonomi().getBiayaSpklu()); //800000000.0; // Harga 1 SPKLU
        Double ieikAdd = Double.parseDouble(cp.getKondisiEkonomi().getBiayaInvestasiLahan()) / 1000; //Double.parseDouble(ps.getParam(l, FormulaEnum.ieikAdd)); //550000.0; // param Infrastructure Expense (in kIDR)
        Double ieikDiv = Double.parseDouble(ps.getParam(l, FormulaEnum.ieikDiv)); //1000.0; // param Infrastructure Expense (in kIDR)

        Double bo = Double.parseDouble(ps.getParam(l, FormulaEnum.bo)); //0.02; // param Biaya Operasional *)
        Double bsl = Double.parseDouble(cp.getParameterBisnis().getBiayaSewaLahan()); //0.0; // Biaya Sewa Lahan TODO

        Double bp = Double.parseDouble(ps.getParam(l, FormulaEnum.bp)); //0.02; // param Biaya Pemasaran

        Double btt = Double.parseDouble(ps.getParam(l, FormulaEnum.btt)); //100000.0; // param Biaya Tak Terduga

        Double kpkl = Double.parseDouble(cp.getParameterTeknis().getKapasitasKbl()); //25.0; // Kapasitas pengisian 1 kendaraan listrik (kWh)
        Double keh = Double.parseDouble(ps.getParam(l, FormulaEnum.keh)); //1.0; // param Kebutuhan Energi harian (KWH)
        Double rdkdp = Double.parseDouble(cp.getParameterTeknis().getRugiDayaPendukung()) / 100; //0.1; // Rugi-rugi dan kebutuhan daya pendukung

        Double heMtp = Double.parseDouble(ps.getParam(l, FormulaEnum.heMtp)); //0.707; // param ? Harga Energi (Rp/KwH)
        Double rhblp = Double.parseDouble(cp.getParameterBisnis().getHargaJualPln()); //1.2; // Rasio harga beli listrik PLN (Q, Rp 707 x Q)
        Double heMin = Double.parseDouble(ps.getParam(l, FormulaEnum.heMin)); //1.0; // param ? Harga Energi (Rp/KwH)
        Double sep = 0.0; // Subsidi energi Persen TODO
        Double heMtpN = Double.parseDouble(ps.getParam(l, FormulaEnum.heMtpN)); //0.035; // param ? Harga Energi (Rp/KwH)

        Double bet = Double.parseDouble(ps.getParam(l, FormulaEnum.bet)); //365.0; // param Biaya Energy Tahunan

        Double bos = Double.parseDouble(ps.getParam(l, FormulaEnum.bos)); //57600.0; // param Biaya Operasi SPKLU, Gaji Pegawai Pertahun

        Double jkhev = 0.0; // Jumlah Kendaraan Hybrid EV pada tahun baseline TODO
        Double pkhg = Double.parseDouble(ps.getParam(l, FormulaEnum.pkhg)); //1.0; // param ? Populasi Kendaraan Hybrid (Gaikindo)
        Double ptkhev = 0.0; // Pertumbuhan Tahunan Kendaraan Hybrid EV TODO

        Double bc = Double.parseDouble(ps.getParam(l, FormulaEnum.bc)); //1.65; // param ? Biaya Charging
        Double rhjls = Double.parseDouble(cp.getParameterBisnis().getHargaJualKonsumen()); //1.5; // Rasio harga jual listrik SPKLU (N, Rp 1650 x N)

        Double fcfeph = 1.0; // Frekuensi charging Full EV per hari TODO

        Double fcheph = 0.0; // Frekuensi charging Hybrid EV per hari TODO

        Double tei = Double.parseDouble(ps.getParam(l, FormulaEnum.tei)); //1.0; // param Total Expense (inflated)
        Double ir = Double.parseDouble(cp.getKondisiEkonomi().getInflasi()) / 100; //0.035; // Inflation Rate

        Double pl = Double.parseDouble(ps.getParam(l, FormulaEnum.pl)); //365.0; // param Pendapatan Layanan

        Double ri = Double.parseDouble(ps.getParam(l, FormulaEnum.ri)); //1.0; // param Revenue (inflated)

        Double tax = Double.parseDouble(cp.getKondisiEkonomi().getPph()) / 100; //0.25; // Tax

        Double dcs = Double.parseDouble(ps.getParam(l, FormulaEnum.dcs)); //1.0; // param Discounted Cash Flow
        Double dr = Double.parseDouble(cp.getKondisiEkonomi().getDiscountRate()) / 100; //0.13; // Discount Rate

        Double pi = Double.parseDouble(ps.getParam(l, FormulaEnum.pi)); //0.05; // param Profitability Index

        Double bep = Double.parseDouble(ps.getParam(l, FormulaEnum.bep)); //1.0; // param Break Event Point (BEP)

        List<Double> pklRes = pklRes(jkfev, pkl, ptkev, length);
        List<Double> jsRes = jsRes(pkl, rsbb, pklRes);
        List<Double> sbRes = sbRes(jsRes);
        List<Double> ieikRes = ieikRes(sbRes, hs, ieikAdd, ieikDiv);

        List<Double> boRes = boRes(bo, ieikRes, bsl, jsRes);
        List<Double> bpRes = bpRes(bp, ieikRes);
        List<Double> bttRes = bttRes(btt, length);
        List<Double> kehRes = kehRes(jsRes, rsbb, kpkl, keh, rdkdp);
        List<Double> heRes = heRes(heMtp, rhblp, heMin, sep, heMtpN, length);
        List<Double> betRes = betRes(kehRes, heRes, bet);
        List<Double> bosRes = bosRes(jsRes, bos);

        List<Double> pkhgRes = pkhgRes(jkhev, pkhg, ptkhev, length);
        List<Double> bcRes = bcRes(kpkl, bc, rhjls, length);
        List<Double> fcfehRes = fcfehRes(fcfeph, length);
        List<Double> fchhRes = fchhRes(fcheph, length);
        List<Double> teiRes = teiRes(ieikRes, boRes, bpRes, bttRes, betRes, bosRes, tei, yearRes, ir);
        List<Double> plRes = plRes(pklRes, bcRes, fcfehRes, pkhgRes, fchhRes, pl);
        List<Double> pkRes = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            pkRes.add(0.0);
        }
        List<Double> riRes = riRes(pkRes, plRes, ri, yearRes, ir);
        List<Double> gpikRes = gpikRes(teiRes, plRes);
        List<Double> taxRes = taxRes(gpikRes, tax);
        List<Double> csRes = csRes(gpikRes, taxRes);
        List<Double> dcsRes = dcsRes(csRes, dcs, dr, yearToRes);
        List<Double> cfRes = cfRes(csRes, dcsRes);
        List<Double> ppParamRes = ppParamRes(cfRes);

        double[] csResD = new double[csRes.size()];
        for (int i = 0; i < csRes.size(); i++) {
            csResD[i] = csRes.get(i);
        }

        double npvRes = FormulaService.npv(dr, csResD);
//        log.info("npvRes={}", FormulaService.round(npvRes, 0));

        double irrRes = FormulaService.irr(csResD);
//        log.info("irrRes={}", FormulaService.round(irrRes, 2));

        double mirrRes = FormulaService.mirr(csResD, 0, 0);
//        log.info("mirrRes={}", FormulaService.round(mirrRes, 2));

        Double fbeRes = fbeRes(cfRes);
//        log.info("fbeRes={}", FormulaService.round(fbeRes, 3));

        Double llRes = llRes(cfRes);
//        log.info("llRes={}", FormulaService.round(llRes, 4));

        Double lyRes = lyRes(ppParamRes);
//        log.info("lyRes={}", FormulaService.round(lyRes, 0));

        Double pprdRes = pprdRes(fbeRes, llRes, lyRes);
//        log.info("pprdRes={}", FormulaService.round(pprdRes, 2));

        Double piRes = piRes(pi, plRes, teiRes, length); // riRes => plRes
//        log.info("piRes={}", FormulaService.round(piRes, 2));

        Double bepRes = bepRes(ieikRes, boRes, bpRes, bttRes, betRes, bosRes, plRes, bep);
//        log.info("bepRes={}", FormulaService.round(bepRes, 2));

        ResponseCalculate rc = new ResponseCalculate();
        rc.setYear(yearRes.stream().map(e -> String.format("%.0f", round(e, 2))).collect(Collectors.toList()));
        rc.setYearTo(yearToRes.stream().map(e -> String.format("%.0f", round(e, 2))).collect(Collectors.toList()));
        rc.setPkl(pklRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setJs(jsRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setSb(sbRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setIeik(ieikRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setBo(boRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setBp(bpRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setBtt(bttRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setKeh(kehRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setHe(heRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setBet(betRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setBos(bosRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setPkhg(pkhgRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setBc(bcRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setFcfeh(fcfehRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setFchh(fchhRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setTei(teiRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setPl(plRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setPk(pkRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setRi(riRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setGpik(gpikRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setTax(taxRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setCs(csRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setDcs(dcsRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setCf(cfRes.stream().map(e -> String.format("%.2f", round(e, 2))).collect(Collectors.toList()));
        rc.setPpParam(ppParamRes.stream().map(e -> String.format("%.0f", round(e, 2))).collect(Collectors.toList()));

        rc.setNpv(String.format("%.0f", round(npvRes, 2)));
        rc.setIrr(String.format("%.0f", (round(irrRes, 2) * 100)) + "%");
        rc.setMirr(String.format("%.0f", (round(mirrRes, 2) * 100)) + "%");
        rc.setFbe(String.format("%.3f", round(fbeRes, 3)));
        rc.setLl(String.format("%.4f", round(llRes, 4)));
        rc.setLy(String.format("%.0f", round(lyRes, 2)));
        rc.setPprd(String.format("%.2f", round(pprdRes, 2)));
        rc.setPi(String.format("%.2f", round(piRes, 2)));
        rc.setBep(String.format("%.2f", round(bepRes, 2)));

        CalcOptmz ro = new CalcOptmz();
        ro.setResponseCalculate(rc);

        String len = String.format( "%.0f", Double.parseDouble(cp.getKondisiEkonomi().getBiayaSpklu()) );
        int nol = len.length() - 2;
        ro.setPaybackPeriod(String.format("%."+nol+"f", round(pprdRes, nol)));

        return ro;
    }

    // Infrastructure Expense (in kIDR)
    public List<Double> ieikRes(List<Double> sbRes, Double hs, Double ieikAdd, Double ieikDiv) {
        List<Double> result = new ArrayList<>();
        for (Double val : sbRes) {
            Double d = -(ieikAdd + hs / ieikDiv) * val;
            result.add(d);
        }
        return result;
    }

    // Biaya Operasional *)
    public List<Double> boRes(Double bo, List<Double> ieikRes, Double bsl, List<Double> jsRes) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (ieikRes.size() - 1); i++) {
            Double d = (bo * ieikRes.get(i)) + (bsl * jsRes.get(i));
            result.add(d);
        }
        return result;
    }

    // Biaya Pemasaran
    public List<Double> bpRes(Double bp, List<Double> ieikRes) {
        List<Double> result = new ArrayList<>();
        for (Double val : ieikRes) {
            Double d = bp * val;
            result.add(d);
        }
        return result;
    }

    // Biaya Tak Terduga
    public List<Double> bttRes(Double btt, int length) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            result.add(-btt);
        }
        return result;
    }

    // Kebutuhan Energi harian (KWH)
    public List<Double> kehRes(List<Double> jsRes, Double rsbb, Double kpkl, Double keh, Double rdkdp) {
        List<Double> result = new ArrayList<>();
        for (Double val : jsRes) {
            Double d = val * rsbb * kpkl * (keh + rdkdp);
            result.add(d);
        }
        return result;
    }

    // ? Harga Energi (Rp/KwH)
    public List<Double> heRes(Double heMtp, Double rhblp, Double heMin, Double sep, Double heMtpN, int length) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            Double d = 0.0;
            if (i == 0) d = heMtp * rhblp * (heMin - sep);
            else d = result.get(i - 1) + heMtpN * result.get(i - 1) * (heMin - sep);
            result.add(d);
        }
        return result;
    }

    // Biaya Energy Tahunan
    public List<Double> betRes(List<Double> kehRes, List<Double> heRes, Double bet) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (kehRes.size() - 1); i++) {
            Double d = -kehRes.get(i) * heRes.get(i) * bet;
            result.add(d);
        }
        return result;
    }

    // ? Jumlah SPKLU*brp sebenarnya (10% dari jumlah BEV)
    public List<Double> jsRes(Double js, Double rsbb, List<Double> pklRes) {
        List<Double> result = new ArrayList<>();
        for (Double val : pklRes) {
            Double d = floor(js / rsbb * val, 1);
            result.add(d);
        }
        return result;
    }

    // SPKLU Baru
    public List<Double> sbRes(List<Double> jsRes) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (jsRes.size() - 1) ; i++) {
            Double d = 0.0;
            if (i == 0) d = jsRes.get(i);
            else d = jsRes.get(i) - jsRes.get(i - 1);
            result.add(d);
        }
        return result;
    }

    // Biaya Operasi SPKLU, Gaji Pegawai Pertahun
    public List<Double> bosRes(List<Double> jsRes, Double bos) {
        List<Double> result = new ArrayList<>();
        for (Double val : jsRes) {
            Double d = -val * bos;
            result.add(d);
        }
        return result;
    }

    // ? Populasi Kendaraan Listrik
    public List<Double> pklRes(Double jkfev, Double pkl, Double ptkev, int length) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            Double d = 0.0;
            if (i == 0) d = jkfev;
            else d = floor((pkl + ptkev) * result.get(i -1), 1);
            result.add(d);
        }
        return result;
    }

    // ? Populasi Kendaraan Hybrid (Gaikindo)
    public List<Double> pkhgRes(Double jkhev, Double pkhg, Double ptkhev, int length) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            Double d = 0.0;
            if (i == 0) d = jkhev;
            else d = floor((pkhg + ptkhev) * result.get(i -1), 1);
            result.add(d);
        }
        return result;
    }

    // ? Biaya Charging
    public List<Double> bcRes(Double kpkl, Double bc, Double rhjls, int length) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            Double d = 0.0;
            if (i == 0) d = kpkl * bc * rhjls;
            else d = result.get(i - 1);
            result.add(d);
        }
        return result;
    }

    // ? Frekuensi Charging Full EV harian
    public List<Double> fcfehRes(Double fcfeph, int length) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            Double d = fcfeph;
            result.add(d);
        }
        return result;
    }

    // ? Frekuensi Charging Hybrid harian
    public List<Double> fchhRes(Double fcheph, int length) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            Double d = fcheph;
            result.add(d);
        }
        return result;
    }

    // Total Expense (inflated)
    public List<Double> teiRes(List<Double> ieikRes, List<Double> boRes, List<Double> bpRes, List<Double> bttRes,
                            List<Double> betRes, List<Double> bosRes, Double tei, List<Double> yearRes, Double ir) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (ieikRes.size() - 1); i++) {
            Double d = 0.0;
            if (i == 0) {
                Double t1 = Arrays.asList(ieikRes.get(0), boRes.get(0), bpRes.get(0), bttRes.get(0)).stream()
                        .mapToDouble(e -> e).sum();
                d = t1 + betRes.get(0) + bosRes.get(0);
            } else {
                Double t1 = Arrays.asList(boRes.get(i), bpRes.get(i), bttRes.get(i)).stream()
                        .mapToDouble(e -> e).sum();
                d = betRes.get(i) + ieikRes.get(i) + (t1 + bosRes.get(i)) * (tei + (yearRes.get(i) - yearRes.get(0)) * ir);
            }
            result.add(d);
        }
        return result;
    }

    // Income (In kIDR)
    // iik not calculate

    // Pendapatan Kontrak
    // pk => 0

    // Pendapatan Layanan
    public List<Double> plRes(List<Double> pklRes, List<Double> bcRes,
                              List<Double> fcfehRes, List<Double> pkhgRes, List<Double> fchhRes, Double pl) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (pklRes.size() - 1); i++) {
            Double d = pklRes.get(i) * bcRes.get(i) * fcfehRes.get(i) * pl; // + pkhgRes.get(i) * bcRes.get(i) * fchhRes.get(i) * pl;
            result.add(d);
        }
        return result;
    }

    // Revenue (inflated)
    public List<Double> riRes(List<Double> pkRes, List<Double> plRes, Double ri, List<Double> yearRes, Double ir) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (plRes.size() - 1); i++) {
            Double d = 0.0;
            if (i == 0) {
                d = Arrays.asList(pkRes.get(0), plRes.get(0)).stream().mapToDouble(e -> e).sum();
            } else {
                Double sum = Arrays.asList(pkRes.get(i),
                        plRes.get(i)
                ).stream().mapToDouble(e -> e).sum();
                d = sum * (ri + (yearRes.get(i) - yearRes.get(0)) * ir);
            }
            result.add(d);
        }
        return result;
    }

    // Gross Profit (In kIDR)
    public List<Double> gpikRes(List<Double> teiRes, List<Double> plRes) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (teiRes.size() - 1); i++) {
            Double d = plRes.get(i) + teiRes.get(i);
            result.add(d);
        }
        return result;
    }

    // Tax
    public List<Double> taxRes(List<Double> gpikRes, Double tax) {
        List<Double> result = new ArrayList<>();
        for (Double val : gpikRes) {
            Double d = (val > 0) ? (val * tax) : 0;
            result.add(d);
        }
        return result;
    }

    // Cash Flow
    public List<Double> csRes(List<Double> gpikRes, List<Double> taxRes) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (gpikRes.size() - 1); i++) {
            Double d = gpikRes.get(i) - taxRes.get(i);
            result.add(d);
        }
        return result;
    }

    // Discounted Cash Flow
    public List<Double> dcsRes(List<Double> csRes, Double dcs, Double dr, List<Double> yearTo) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (csRes.size() - 1); i++) {
            Double d = Math.pow((dcs + dr), (yearTo.get(i) + dcs));
            d = csRes.get(i) / d;
            result.add(d);
        }
        return result;
    }

    // Cummulative Flow
    public List<Double> cfRes(List<Double> csRes, List<Double> dcsRes) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= (csRes.size() - 1); i++) {
            Double d = 0.0;
            if (i == 0) d = csRes.get(0);
            else {
                List<Double> ds = new ArrayList<>();
                for (int j = 0; j <= i; j++) {
                    ds.add(dcsRes.get(j));
                }
                d = ds.stream().mapToDouble(e -> e).sum();
            }
            result.add(d);
        }
        return result;
    }

    // Payback Period
    public List<Double> ppParamRes(List<Double> cfRes) {
        List<Double> result = new ArrayList<>();
        for (Double val : cfRes) {
            Double d = (val < 1) ? 0.0 : 1.0;
            result.add(d);
        }
        return result;
    }

    // First Break Even
    public Double fbeRes(List<Double> cfRes) {
        return cfRes.stream().filter(e -> e > 0).sorted().findFirst().orElse(0.0);
    }

    // Last loss
    public Double llRes(List<Double> cfRes) {
        return cfRes.stream().filter(e -> e < 0).sorted((a, b) -> Double.compare(b, a)).findFirst().orElse(0.0);
    }

    // Loss Years
    public Double lyRes(List<Double> ppParamRes) {
        return Double.parseDouble(String.valueOf(ppParamRes.stream().filter(e -> e == 0).count()));
    }

    // Payback Periode
    public Double pprdRes(Double fbeRes, Double llRes, Double lyRes) {
        return lyRes + abs(llRes) / (fbeRes - llRes);
    }

    // Profitability Index
    public Double piRes(Double pi, List<Double> plRes, List<Double> teiRes, int length) { // riRes => plRes
        double[] riResD = new double[plRes.size()];
        for (int i = 0; i < plRes.size(); i++) {
            riResD[i] = plRes.get(i);
        }
        double[] teiResD = new double[teiRes.size()];
        for (int i = 0; i < teiRes.size(); i++) {
            teiResD[i] = teiRes.get(i);
        }
        return abs(npv(pi, riResD) / npv(pi, teiResD));
    }

    // Break Event Point (BEP)
    public Double bepRes(List<Double> ieikRes, List<Double> boRes, List<Double> bpRes, List<Double> bttRes,
                         List<Double> betRes, List<Double> bosRes, List<Double> plRes, Double bep) {
        double ieikSum = ieikRes.stream().mapToDouble(e -> e).sum();
        double bSum = Stream.of(boRes, bpRes, bttRes).flatMap(Collection::stream).mapToDouble(e -> e).sum();
        double betSum = betRes.stream().mapToDouble(e -> e).sum();
        double bosSum = bosRes.stream().mapToDouble(e -> e).sum();
        double plSum = plRes.stream().mapToDouble(e -> e).sum();
        return abs(ieikSum) / (bep - (abs(bSum) + abs(betSum) + abs(bosSum)) / abs(plSum));
    }

    // excel function

    public static double floor(double n, double s) {
        double f;

        if ((n<0 && s>0) || (n>0 && s<0) || (s==0 && n!=0)) {
            f = Double.NaN;
        }
        else {
            f = (n==0 || s==0) ? 0 : Math.floor(n/s) * s;
        }

        return f;
    }

    public static double npv(double r, double[] cfs) {
        double npv = 0;
        double r1 = r + 1;
        double trate = r1;
        for (int i=0, iSize=cfs.length; i<iSize; i++) {
            npv += cfs[i] / trate;
            trate *= r1;
        }
        return npv;
    }

    public static double irr(double[] income) {
        return irr(income, 0.1d);
    }
    public static double irr(double[] values, double guess) {
        int maxIterationCount = 20;
        double absoluteAccuracy = 1E-7;

        double x0 = guess;
        double x1;

        int i = 0;
        while (i < maxIterationCount) {

            // the value of the function (NPV) and its derivate can be calculated in the same loop
            double fValue = 0;
            double fDerivative = 0;
            for (int k = 0; k < values.length; k++) {
                fValue += values[k] / Math.pow(1.0 + x0, k);
                fDerivative += -k * values[k] / Math.pow(1.0 + x0, k + 1);
            }

            // the essense of the Newton-Raphson Method
            x1 = x0 - fValue/fDerivative;

            if (Math.abs(x1 - x0) <= absoluteAccuracy) {
                return x1;
            }

            x0 = x1;
            ++i;
        }
        // maximum number of iterations is exceeded
        return Double.NaN;
    }

    public static double mirr(double[] in, double financeRate, double reinvestRate) {
        double value = 0;
        int numOfYears = in.length - 1;
        double pv = 0;
        double fv = 0;

        int indexN = 0;
        for (double anIn : in) {
            if (anIn < 0) {
                pv += anIn / Math.pow(1 + financeRate + reinvestRate, indexN++);
            }
        }

        for (double anIn : in) {
            if (anIn > 0) {
                fv += anIn * Math.pow(1 + financeRate, numOfYears - indexN++);
            }
        }

        if (fv != 0 && pv != 0) {
            value = Math.pow(-fv / pv, 1d / numOfYears) - 1;
        }
        return value;
    }

    public static double abs(double a) {
        return (a <= 0.0D) ? 0.0D - a : a;
    }

    public static double round(double n, int p) {
        double retval;

        if (Double.isNaN(n) || Double.isInfinite(n)) {
            retval = Double.NaN;
        }
        else {
            retval = new java.math.BigDecimal(NumberToTextConverter.toText(n)).setScale(p, java.math.RoundingMode.HALF_UP).doubleValue();
        }

        return retval;
    }

    public static double roundX(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

//    void listClass() {
//         NumericFunction
//         MathX
//         Math
//    }

}
