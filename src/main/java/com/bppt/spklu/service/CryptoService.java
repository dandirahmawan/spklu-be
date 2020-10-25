package com.bppt.spklu.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CryptoService {

    public static String genToken(String username, String encPass) {
        return encPass(username + ":" + encPass);
    }

    public static String[] getUserPass(String token) {
        String userPass = decPass(token);
        return userPass.split(":");
    }

    public static String encPass(String passDec) {
        String enc1 =  Base64.getEncoder().encodeToString(passDec.getBytes());
        String enc2 =  Base64.getEncoder().encodeToString(enc1.getBytes());
        String enc3 =  Base64.getEncoder().encodeToString(enc2.getBytes());
        return enc3;
    }

    public static String decPass(String passEnc) {
        try {
            byte[] dec1 =  Base64.getDecoder().decode(passEnc);
            byte[] dec2 =  Base64.getDecoder().decode(dec1);
            byte[] dec3 =  Base64.getDecoder().decode(dec2);
            return new String(dec3, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

}
