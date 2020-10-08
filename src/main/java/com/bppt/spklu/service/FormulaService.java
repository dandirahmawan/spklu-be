package com.bppt.spklu.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FormulaService {

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
    public List<Double> jsRes(Double pkl, Double rsbb, List<Double> pklRes) {
        List<Double> result = new ArrayList<>();
        for (Double val : pklRes) {
            Double d = floor(pkl / rsbb * val, 1);
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
            Double d = pklRes.get(i) * bcRes.get(i) * fcfehRes.get(i) * pl + pkhgRes.get(i) * bcRes.get(i) * fchhRes.get(i) * pl;
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

//    public Double piRes(Double fbeRes, Double llRes, Double lyRes) {
//        return lyRes + abs(llRes) / (fbeRes - llRes);
//    }





    public List<Double> xx_ON(List<Double> Res) {
        List<Double> result = new ArrayList<>();
        return result;
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
