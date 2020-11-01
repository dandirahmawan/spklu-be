package com.bppt.spklu.model;

import lombok.Data;

import java.util.List;

@Data
public class ReqParameterTeknis {

    private String jumlahKonektor;
    private String dayaMaksimum;
    private String kapasitasKbl;
    private String rugiDayaPendukung;
    private List<ReqKwhKonektor> kwhPerKonektor;

}
