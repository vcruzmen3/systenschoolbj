package com.school.school_management_api.security;

import com.school.school_management_api.entity.ApplicationUser;
import com.school.school_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findWithRolesAndPermissionsByIdentifier(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con el identificador: " + usernameOrEmail
                ));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Mapear Roles (ROLE_ADMIN, ROLE_TEACHER, etc.)
        user.getUserRoles().forEach(ur -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + ur.getRole().getName()));
            // Mapear Permisos individuales asociados al Rol
            ur.getRole().getRolePermissions().forEach(rp ->
                    authorities.add(new SimpleGrantedAuthority(rp.getPermission().getName()))
            );
        });

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getIsActive(),
                true,
                true,
                true,
                authorities
        );
    }
}