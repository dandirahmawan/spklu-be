package com.bppt.spklu.constant;

public enum FormulaEnum {
    pkl, // param ? Populasi Kendaraan Listrik

    ieikAdd, // param Infrastructure Expense (in kIDR)
    ieikDiv, // param Infrastructure Expense (in kIDR)

    bo,// param Biaya Operasional *)

    bp, // param Biaya Pemasaran

    btt, // param Biaya Tak Terduga

    keh, // param Kebutuhan Energi harian (KWH)

    heMtp,// param ? Harga Energi (Rp/KwH)
    heMin,// param ? Harga Energi (Rp/KwH)
    heMtpN, // param ? Harga Energi (Rp/KwH)

    bet, // param Biaya Energy Tahunan

    bos, // param Biaya Operasi SPKLU, Gaji Pegawai Pertahun

    pkhg, // param ? Populasi Kendaraan Hybrid (Gaikindo)

    bc, // param ? Biaya Charging

    tei, // param Total Expense (inflated)

    pl, // param Pendapatan Layanan

    ri, // param Revenue (inflated)

    dcs, // param Discounted Cash Flow

    pi, // param Profitability Index

    bep, // param Break Event Point (BEP)

    dirTemp,

    endpointUrl,


//    Double jkfev = 600.0; // Jumlah Kendaraan Full EV pada tahun baseline
//    Double pkl = 1.0; // param ? Populasi Kendaraan Listrik
//    Double ptkev = 0.23; // Pertumbuhan Tahunan Kendaraan EV
//
//    Integer stYear = 2020;
//    Integer edYear = 2030;
//    List<Double> yearRes = new ArrayList<>(); // tahun 2020, 2021, 2022, ...
//        for (Integer i = stYear; i <= edYear; i+=1) {
//        yearRes.add(Double.parseDouble(i + ""));
//    }
//    List<Double> yearTo = new ArrayList<>(); // tahun ke 0, 1, 2, ...
//    Double yt = 0.0;
//        for (Integer i = stYear; i <= edYear; i+=1) {
//        yearTo.add(yt);
//        yt += 1;
//    }
//    int length = yearTo.size() - 1; //10; // range year 0 - 10 => 11
//
//    Double rsbb = 38.0; // Rasio SPKLU Banding BEV, 1:N
//
//    Double hs = 800000000.0; // Harga 1 SPKLU
//    Double ieikAdd = 550000.0; // param Infrastructure Expense (in kIDR)
//    Double ieikDiv = 1000.0; // param Infrastructure Expense (in kIDR)
//
//    Double bo = 0.02; // param Biaya Operasional *)
//    Double bsl = 0.0; // Biaya Sewa Lahan
//
//    Double bp = 0.02; // param Biaya Pemasaran
//
//    Double btt = 100000.0; // param Biaya Tak Terduga
//
//    Double kpkl = 25.0; // Kapasitas pengisian 1 kendaraan listrik (kWh)
//    Double keh = 1.0; // param Kebutuhan Energi harian (KWH)
//    Double rdkdp = 0.1; // Rugi-rugi dan kebutuhan daya pendukung
//
//    Double heMtp = 0.707; // param ? Harga Energi (Rp/KwH)
//    Double rhblp = 1.2; // Rasio harga beli listrik PLN (Q, Rp 707 x Q)
//    Double heMin = 1.0; // param ? Harga Energi (Rp/KwH)
//    Double sep = 0.0; // Subsidi energi Persen
//    Double heMtpN = 0.035; // param ? Harga Energi (Rp/KwH)
//
//    Double bet = 365.0; // param Biaya Energy Tahunan
//
//    Double bos = 57600.0; // param Biaya Operasi SPKLU, Gaji Pegawai Pertahun
//
//    Double jkhev = 0.0; // Jumlah Kendaraan Hybrid EV pada tahun baseline
//    Double pkhg = 1.0; // param ? Populasi Kendaraan Hybrid (Gaikindo)
//    Double ptkhev = 0.0; // Pertumbuhan Tahunan Kendaraan Hybrid EV
//
//    Double bc = 1.65; // param ? Biaya Charging
//    Double rhjls = 1.5; // Rasio harga jual listrik SPKLU (N, Rp 1650 x N)
//
//    Double fcfeph = 1.0; // Frekuensi charging Full EV per hari
//
//    Double fcheph = 0.0; // Frekuensi charging Hybrid EV per hari
//
//    Double tei = 1.0; // param Total Expense (inflated)
//    Double ir = 0.035; // Inflation Rate
//
//    Double pl = 365.0; // param Pendapatan Layanan
//
//    Double ri = 1.0; // param Revenue (inflated)
//
//    Double tax = 0.25; // Tax
//
//    Double dcs = 1.0; // param Discounted Cash Flow
//    Double dr = 0.13; // Discount Rate
//
//    Double pi = 0.05; // param Profitability Index
//
//    Double bep = 1.0; // param Break Event Point (BEP)
}
