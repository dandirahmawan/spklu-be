package com.bppt.spklu.controller;

import com.bppt.spklu.constant.FormulaEnum;
import com.bppt.spklu.entity.MainParameter;
import com.bppt.spklu.model.CalculateParameter;
import com.bppt.spklu.model.ResponseCalculate;
import com.bppt.spklu.model.ResponseRest;
import com.bppt.spklu.service.ExcelService;
import com.bppt.spklu.service.FormulaService;
import com.bppt.spklu.service.ParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/calculate")
public class CalculateController extends CommonController {

    @Override
    protected boolean isSecure() {
        return false;
    }

    @Autowired
    private FormulaService fs;

    @PostMapping
    public ResponseRest<ResponseCalculate> calculate(HttpServletRequest req, HttpServletResponse res, @RequestBody CalculateParameter cp) {
        return res(req, res, cp, () -> fs.genFormula(cp));
//        ResponseCalculate res = fs.genFormula(cp);
//        return new ResponseRest<>("success", true, res);
    }

    @Autowired
    private ExcelService excelService;

    @PostMapping("/excel")
    public ResponseRest<String> generateExcel(@RequestBody CalculateParameter cp) {
        String res = "";
        try {
            String n = excelService.generate(cp);
            res = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/download/")
                    .path(n)
                    .toUriString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseRest<>("success", true, res);
    }

    @Autowired
    private ParameterService ps;

    @PostMapping("/excel-direct")
    public ResponseEntity<InputStreamResource> generateExcelDirect(@RequestBody CalculateParameter cp) {
        String res = "";
        List<MainParameter> l = ps.getParameters();
        String dir = ps.getParam(l, FormulaEnum.dirTemp); //"/tmp/com.bppt.spklu/";

        try {
            res = excelService.generate(cp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String strFile = dir + "calculate-" + res + ".xlsx";

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(strFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File file = new File(strFile);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}