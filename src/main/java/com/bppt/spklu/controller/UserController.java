package com.bppt.spklu.controller;

import com.bppt.spklu.model.MainUser;
import com.bppt.spklu.model.ResponseRest;
import com.bppt.spklu.service.ExcelService;
import com.bppt.spklu.service.FormulaService;
import com.bppt.spklu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController<dd> {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ResponseRest<MainUser> login(@RequestBody Map<String, String> body) {
        ResponseRest<MainUser> res = new ResponseRest<>("username or password not found", false, null);
        String username = "";
        String password = "";

        if (body.get("username") != null || body.get("password") != null) {
            username = body.get("username");
            password = body.get("password");
        } else {
            return res;
        }

        ResponseRest<MainUser> user = userService.login(username, password);
        if (user.getSuccess())
            return new ResponseRest<>("success", true, user.getData());
        return res;
    }






    @GetMapping
    public void main() {
//        double[] cs = {
//                -13327389.55,
//                2582918.76,
//                4016576.10,
//                3271937.18,
//                3927097.58,
//                5072574.65,
//                5474146.28,
//                4972315.53,
//                6299717.80,
//                5819992.91,
//                5991559.86
//        };
//        double resmirr = FormulaService.mirr(cs, 0, 0);
//        log.info("mirr=" + String.valueOf(FormulaService.round((resmirr * 100), 0)));
//
//        double resirr = FormulaService.irr(cs);
//        log.info("irr=" + String.valueOf(FormulaService.round((resirr * 100), 0)));
//
//        double npv = FormulaService.npv(0.13, cs);
//        log.info("npv=" + String.valueOf(FormulaService.round(npv, 0)));

        try {
            excelService.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private ExcelService excelService;

    public ResponseRest<MainUser> register(@RequestBody Map<String, String> body) {
        return null;
    }



}
