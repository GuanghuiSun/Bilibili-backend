package com.bilibili.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilibili.exception.BusinessException;

import java.util.Calendar;
import java.util.Date;

import static com.bilibili.constant.MessageConstant.*;

/**
 * JWT生成token工具类
 *
 * @author sgh
 * @date 2022-8-2
 */
public class TokenUtils {

    private static final String ISSUER = "签发者";
    private static final int EXPIRE = 300000;

    /**
     * 创建token
     * @param userId 用户Id
     * @return token
     * @throws Exception 加解密异常
     */
    public static String generateToken(Long userId) throws Exception {
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, EXPIRE);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    /**
     * 验证token
     * @param token 用户token
     * @return 用户Id
     */
    public static Long verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getKeyId();
            return Long.valueOf(userId);
        }catch (TokenExpiredException e) {
            throw new BusinessException(TOKEN_ERROR_CODE,USER_STATUS_ERROR,TOKEN_EXPIRE_ERROR);
        } catch (Exception e) {
            throw new BusinessException(TOKEN_ERROR_CODE,USER_STATUS_ERROR,TOKEN_DECODE_ERROR);
        }

    }
}
