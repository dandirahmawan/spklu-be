package com.bppt.spklu.controller;

import com.bppt.spklu.model.CalculateParameter;
import com.bppt.spklu.model.ResponseCalculate;
import com.bppt.spklu.model.ResponseRest;
import com.bppt.spklu.service.FormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calculate")
public class CalculateController {

    @Autowired
    private FormulaService fs;

    @PostMapping
    public ResponseRest<ResponseCalculate> calculate(@RequestBody CalculateParameter cp) {
        ResponseCalculate res = fs.genFormula(cp);
        return new ResponseRest<>("success", true, res);
    }

}
