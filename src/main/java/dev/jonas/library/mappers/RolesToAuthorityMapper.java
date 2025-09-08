package dev.jonas.library.mappers;

import dev.jonas.library.repositories.RoleRepository;
import dev.jonas.library.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RolesToAuthorityMapper {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public Collection<GrantedAuthority> mapRolesToAuthorities(Long userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(ur -> roleRepository.findById(ur.getRoleId()).orElse(null))
                .filter(Objects::nonNull)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
    }
}