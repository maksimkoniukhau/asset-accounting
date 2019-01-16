package com.maks.assetaccounting.util;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static boolean isAccessGranted(Class<?> securedClass) {
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
}
