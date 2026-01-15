package com.example.quanlysanpham.auth;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * Lưu token đăng nhập trong bộ nhớ (in-memory).
 * - tokenToUserId: map token -> userId (để xác thực token)
 * - userIdToToken: map userId -> token (để 1 user chỉ giữ 1 token, login lại thì token cũ bị thay)
 */
@Component
public class TokenStore {

    private final Map<String, Long> tokenToUserId = new ConcurrentHashMap<>();
    private final Map<Long, String> userIdToToken = new ConcurrentHashMap<>();

    /**
     * Cấp token mới cho user.
     * Nếu user đã có token cũ thì xóa token cũ trước, tránh 1 user có nhiều phiên.
     */
    public String issueToken(Long userId) {
        if (userId == null) return null;

        // token cũ (nếu có)
        String oldToken = userIdToToken.get(userId);
        if (oldToken != null) {
            tokenToUserId.remove(oldToken);
        }

        // token mới
        String newToken = UUID.randomUUID().toString();
        tokenToUserId.put(newToken, userId);
        userIdToToken.put(userId, newToken);
        return newToken;
    }

    /**
     * Hủy token (logout).
     * Sau khi hủy, token không còn hợp lệ.
     */
    public void invalidate(String token) {
        if (token == null || token.isBlank()) return;

        Long userId = tokenToUserId.remove(token);
        if (userId != null) {
            userIdToToken.remove(userId);
        }
    }

    /**
     * Kiểm tra token có tồn tại trong store không.
     */
    public boolean isValid(String token) {
        if (token == null || token.isBlank()) return false;
        return tokenToUserId.containsKey(token);
    }

    /**
     * Lấy userId từ token (phục vụ cho bước bảo vệ API sau này).
     */
    public Long getUserId(String token) {
        if (token == null || token.isBlank()) return null;
        return tokenToUserId.get(token);
    }
}
