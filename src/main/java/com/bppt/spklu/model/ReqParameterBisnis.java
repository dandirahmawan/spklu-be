package com.bppt.spklu.model;

import lombok.Data;

@Data
public class ReqParameterBisnis {

    private String hargaJualPln;
    private String hargaJualKonsumen;
    private String pertumbuhanKblPerTahun;
    private String rasioSpklu;
    private String biayaSewaLahan;
    private String penggunaanEvsePerJam;

    private String biayaOperasional;
    private String biayaPemasaran;
    private String biayaTakTerduga;
    private String gajiPerSpklu;


}
