package com.bppt.spklu.controller;

import com.bppt.spklu.entity.MainFormValue;
import com.bppt.spklu.service.MainFormValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController extends CommonController {

    @Override
    protected boolean isSecure() {
        return true;
    }

    @Autowired
    private MainFormValueService mainFormValueService;

    @GetMapping("/form-value")
    public ResponseEntity getAllFormValue(HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("isSecure", false);
        return res(req, res, null, mainFormValueService::getAll);
    }

    @GetMapping("/form-value/{name}")
    public ResponseEntity getOneByNameFormValue(HttpServletRequest req, HttpServletResponse res, @PathVariable String name) {
        req.setAttribute("isSecure", false);
        return res(req, res, null, () -> mainFormValueService.getOneByName(name));
    }

    @PostMapping("/form-value")
    public ResponseEntity saveFormValue(HttpServletRequest req, HttpServletResponse res, @RequestBody MainFormValue mainFormValue) {
        return res(req, res, mainFormValue, () -> mainFormValueService.save(mainFormValue));
    }

    @PostMapping("/form-values")
    public ResponseEntity saveFormValues(HttpServletRequest req, HttpServletResponse res, @RequestBody List<MainFormValue> mainFormValue) {
        return res(req, res, mainFormValue, () -> mainFormValueService.saves(mainFormValue));
    }
}
