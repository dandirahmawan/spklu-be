package com.bppt.spklu.model;

import lombok.Data;

import java.util.List;

@Data
public class ReqKondisiEkonomi {

    private String pph;
    private String inflasi;
    private String discountRate;
    private String jumlahKendaraanInisial;
    private String biayaSpklu;
    private String biayaInvestasiLahan;
    private String subsidiEnergi;
    private List<ReqHargaEvse> haraPerEvse;

}
