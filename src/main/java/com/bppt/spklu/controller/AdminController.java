package com.bppt.spklu.controller;

import com.bppt.spklu.entity.MainFormValue;
import com.bppt.spklu.model.ResponseRest;
import com.bppt.spklu.service.MainFormValueService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseRest<List<MainFormValue>> getAllFormValue(HttpServletRequest req, HttpServletResponse res) {
        return res(req, res, null, mainFormValueService::getAll);
    }

    @GetMapping("/form-value/{name}")
    public ResponseRest<MainFormValue> getOneByNameFormValue(HttpServletRequest req, HttpServletResponse res, @PathVariable String name) {
        return res(req, res, null, () -> mainFormValueService.getOneByName(name));
    }

    @PostMapping("/form-value")
    public ResponseRest<MainFormValue> saveFormValue(HttpServletRequest req, HttpServletResponse res, @RequestBody MainFormValue mainFormValue) {
        return res(req, res, mainFormValue, () -> mainFormValueService.save(mainFormValue));
    }


}
