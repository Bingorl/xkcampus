package com.biu.wifi.campus.Tool;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;

import java.util.Map;

public class AccessTokenUtils {

    public static final String TOKEN_SECRET = "timeless";

    public static <T> String generateAccessToken(Map claims) {
        JWTSigner signer = new JWTSigner(TOKEN_SECRET);
        return signer.sign(claims);
    }

    public static <T> T getValueFromAccessToken(String accessToken, String key) throws Exception {
        JWTVerifier verifier = new JWTVerifier(TOKEN_SECRET);
        Map<String, Object> decoded = verifier.verify(accessToken);
        return (T) decoded.get(key);
    }

    public static void main(String[] args) throws Exception {
        // Map<String, Object> hashMap = new HashMap<>();
        // hashMap.put("username", "001");
        // hashMap.put("userId", 1);
        // hashMap.put("userType", 1);
        // String token = generateAccessToken(hashMap);
        // System.out.println(token);
        //
        // System.out.println(AccessTokenUtils.getValueFromAccessToken(token,"username"));
        System.out.println(AccessTokenUtils.getValueFromAccessToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyVHlwZSI6MSwidXNlcm5hbWUiOiIwMDEifQ.BVjVu4xAz7tnevOUEmeNTelSN-8xxc9nOJf2lwTb6cc", "username"));
    }


    // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyVHlwZSI6MSwidXNlcklkIjpudWxsLCJ1c2VybmFtZSI6bnVsbH0.BG4jPSCmctP0vl5JUZeMIxzztVOfxg_KYRugbGuVnFo
}
