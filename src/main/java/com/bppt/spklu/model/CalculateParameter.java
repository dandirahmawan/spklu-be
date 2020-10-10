package com.bppt.spklu.model;

import lombok.Data;

@Data
public class CalculateParameter {

    private ReqKondisiEkonomi kondisiEkonomi;
    private ReqParameterBisnis parameterBisnis;
    private ReqParameterTeknis parameterTeknis;
    private ReqYear years;

}
