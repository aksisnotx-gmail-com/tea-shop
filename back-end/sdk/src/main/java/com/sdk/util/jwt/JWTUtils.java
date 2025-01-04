package com.sdk.util.jwt;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.sdk.exception.GlobalException;

import java.util.Map;

/**
 * @author xxl
 * @since 2024/2/28
 */
public class JWTUtils {

    public final static ThreadLocal<String> LOCAL = new ThreadLocal<>();

    private static final String JWT_HEADER = "e10adc3949ba59abbe56e057f20f883e";

    /**
     * 毫秒为单位
     */
    public static final long HALF_A_MONTH = 1000 * 60 * 60 * 24 * 15;

    private static final String EXPIRE_TIME = "expire_time";
    private static final String USER_ID = "uid";

    public static String getToken(String userId) {
        return getToken(HALF_A_MONTH,userId);
    }

    public static String getToken(long time,String userId) {
        return JWTUtil.createToken(Map.of(USER_ID, userId, EXPIRE_TIME, System.currentTimeMillis() + time), JWT_HEADER.getBytes());
    }

    public static boolean verifyToken(String token) {
        if (JWTUtil.verify(token, JWT_HEADER.getBytes())) {
            JWT jwt = JWTUtil.parseToken(token);
            if (Long.parseLong(jwt.getPayload(EXPIRE_TIME).toString()) < System.currentTimeMillis()) {
                throw new GlobalException("token过期");
            }
            String userId = jwt.getPayload(USER_ID).toString();
            LOCAL.set(userId);
            return true;
        }
        return false;
    }

    public static String getUserId() {
        return LOCAL.get();
    }
}
