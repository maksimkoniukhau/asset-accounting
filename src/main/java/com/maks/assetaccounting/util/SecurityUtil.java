package com.maks.assetaccounting.util;

import com.maks.assetaccounting.entity.User;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static boolean isAccessGranted(final Class<?> securedClass) {
        final Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
//         All views require authentication.
        if (!isUserLoggedIn(userAuthentication)) {
            return false;
        }
//         Allow if no roles are required.
        final Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
        if (secured == null) {
            return true;
        }
        final List<String> allowedRoles = Arrays.asList(secured.value());
        return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(allowedRoles::contains);
    }

    public static boolean isUserLoggedIn() {
        return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
    }

    private static boolean isUserLoggedIn(final Authentication authentication) {
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static User getCurrentUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        final Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((User) principal);
        }
        return null;
    }

    public static User getAuthUser() {
        final User user = getCurrentUser();
        Objects.requireNonNull(user, "No authorized user found");
        return user;
    }

    public static String getAuthUsername() {
        return getAuthUser().getUsername();
    }

    public static Long getAuthUserId() {
        return getAuthUser().getId();
    }
}
