package com.sbm.security;

import com.sbm.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static CustomUserPrincipal getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserPrincipal)) {
            throw new UnauthorizedException("Not authenticated");
        }
        return (CustomUserPrincipal) auth.getPrincipal();
    }

    public static Long getCurrentBusinessId() {
        CustomUserPrincipal user = getCurrentUser();
        if (user.getBusinessId() == null) {
            throw new UnauthorizedException("No business associated with this user");
        }
        return user.getBusinessId();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
