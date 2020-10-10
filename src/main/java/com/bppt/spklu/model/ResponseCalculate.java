package com.bppt.spklu.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseCalculate {

    private List<String> year = new ArrayList<>();
    private List<String> yearTo = new ArrayList<>();

    private List<String> pkl = new ArrayList<>();
    private List<String> js = new ArrayList<>();
    private List<String> sb = new ArrayList<>();
    private List<String> ieik = new ArrayList<>();

    private List<String> bo = new ArrayList<>();
    private List<String> bp = new ArrayList<>();
    private List<String> btt = new ArrayList<>();
    private List<String> keh = new ArrayList<>();
    private List<String> he = new ArrayList<>();
    private List<String> bet = new ArrayList<>();
    private List<String> bos = new ArrayList<>();

    private List<String> pkhg = new ArrayList<>();
    private List<String> bc = new ArrayList<>();
    private List<String> fcfeh = new ArrayList<>();
    private List<String> fchh = new ArrayList<>();
    private List<String> tei = new ArrayList<>();
    private List<String> pl = new ArrayList<>();

    private List<String> pk = new ArrayList<>();

    private List<String> ri = new ArrayList<>();
    private List<String> gpik = new ArrayList<>();
    private List<String> tax = new ArrayList<>();
    private List<String> cs = new ArrayList<>();
    private List<String> dcs = new ArrayList<>();
    private List<String> cf = new ArrayList<>();
    private List<String> ppParam = new ArrayList<>();

    private String npv;
    private String irr;
    private String mirr;
    private String fbe;
    private String ll;
    private String ly;
    private String pprd;
    private String pi;
    private String bep;

    private String description = "Infrastructure Expense (in kIDR) => ieik |\n" +
            "Biaya Operasional *) => bo |\n" +
            "Biaya Pemasaran => bp |\n" +
            "Biaya Tak Terduga => btt |\n" +
            "Kebutuhan Energi harian (KWH) => keh |\n" +
            "? Harga Energi (Rp/KwH) => he |\n" +
            "Biaya Energy Tahunan => bet |\n" +
            "? Jumlah SPKLU*brp sebenarnya (10% dari jumlah BEV) => js |\n" +
            "SPKLU Baru => sb |\n" +
            "Biaya Operasi SPKLU, Gaji Pegawai Pertahun => bos |\n" +
            "? Populasi Kendaraan Listrik => pkl |\n" +
            "? Populasi Kendaraan Hybrid (Gaikindo) => pkhg |\n" +
            "? Biaya Charging => bc |\n" +
            "? Frekuensi Charging Full EV harian => fcfeh |\n" +
            "? Frekuensi Charging Hybrid harian => fchh |\n" +
            "Total Expense (inflated) => tei |\n" +
            "Pendapatan Layanan => pl |\n" +
            "Revenue (inflated) => ri |\n" +
            "Gross Profit (In kIDR) => gpik |\n" +
            "Tax => tax |\n" +
            "Cash Flow => cs |\n" +
            "Discounted Cash Flow => dcs |\n" +
            "Cummulative Flow => cf |\n" +
            "Payback Period => ppParam |\n" +
            "First Break Even => fbe |\n" +
            "Last loss => ll |\n" +
            "Loss Years => ly |\n" +
            "Payback Periode => pprd |\n" +
            "Profitability Index => pi |\n" +
            "Break Event Point (BEP) => bep |";

}
