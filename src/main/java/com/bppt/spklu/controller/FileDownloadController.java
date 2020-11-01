package com.bppt.spklu.controller;

import com.bppt.spklu.constant.FormulaEnum;
import com.bppt.spklu.entity.MainParameter;
import com.bppt.spklu.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/download")
public class FileDownloadController {

    @Autowired
    private ParameterService ps;

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> generateExcel(@PathVariable String id) {
//        String dir = "/tmp/com.bppt.spklu/";
        List<MainParameter> l = ps.getParameters();
        String dir = ps.getParam(l, FormulaEnum.dirTemp); //"/tmp/com.bppt.spklu/";

        InputStreamResource resource = null;
        String strFile = dir + "/calculate-" + id + ".xlsx";

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
