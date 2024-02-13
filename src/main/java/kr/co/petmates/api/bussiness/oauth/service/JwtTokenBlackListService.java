package kr.co.petmates.api.bussiness.oauth.service;

import org.springframework.stereotype.Service;

@Service
public class JwtTokenBlackListService {
//    private final RedisTemplate<String, String> redisTemplate;
//
//    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    // 토큰을 블랙리스트에 추가하는 메서드
//    public void blacklistToken(String jti) {
//        long expireSeconds = 7 * 24 * 60 * 60; // 7일
//        redisTemplate.opsForValue().set(jti, "blacklisted", expireSeconds, TimeUnit.SECONDS);
//    }
//
//    // 주어진 토큰이 블랙리스트에 있는지 확인하는 메서드
//    public boolean isTokenBlacklisted(String jti) {
//        return redisTemplate.hasKey(jti);
//    }
}
