package com.bppt.spklu.service;

import com.bppt.spklu.constant.FormulaEnum;
import com.bppt.spklu.entity.MainParameter;
import com.bppt.spklu.repo.MainParameterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParameterService {

    @Autowired
    private MainParameterRepo mainParameterRepo;

    public String getParam(List<MainParameter> list, FormulaEnum fe) {
        String key = "unknown";

        if (fe == FormulaEnum.pkl)
            key = "pkl";
        if (fe == FormulaEnum.ieikAdd)
            key = "ieikAdd";
        if (fe == FormulaEnum.ieikDiv)
            key = "ieikDiv";
        if (fe == FormulaEnum.bo)
            key = "bo";
        if (fe == FormulaEnum.bp)
            key = "bp";
        if (fe == FormulaEnum.btt)
            key = "btt";
        if (fe == FormulaEnum.keh)
            key = "keh";
        if (fe == FormulaEnum.heMtp)
            key = "heMtp";
        if (fe == FormulaEnum.heMin)
            key = "heMin";
        if (fe == FormulaEnum.heMtpN)
            key = "heMtpN";
        if (fe == FormulaEnum.bet)
            key = "bet";
        if (fe == FormulaEnum.bos)
            key = "bos";
        if (fe == FormulaEnum.pkhg)
            key = "pkhg";
        if (fe == FormulaEnum.bc)
            key = "bc";
        if (fe == FormulaEnum.tei)
            key = "tei";
        if (fe == FormulaEnum.pl)
            key = "pl";
        if (fe == FormulaEnum.ri)
            key = "ri";
        if (fe == FormulaEnum.dcs)
            key = "dcs";
        if (fe == FormulaEnum.pi)
            key = "pi";
        if (fe == FormulaEnum.bep)
            key = "bep";
        if (fe == FormulaEnum.dirTemp)
            key = "dirTemp";
        if (fe == FormulaEnum.endpointUrl)
            key = "endpointUrl";

        String finalKey = key + "";
        String mp = list.stream().filter(e -> finalKey.equalsIgnoreCase(e.getKey())).findFirst().map(MainParameter::getValue).orElse("0.0");
        return mp;
    }

    public List<MainParameter> getParameters() {
        List<MainParameter> list = mainParameterRepo.findAll();
        return list;
    }

}
