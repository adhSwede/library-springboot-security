package dev.jonas.library.services.auth;

import dev.jonas.library.entities.User;
import dev.jonas.library.mappers.RolesToAuthorityMapper;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.security.CustomUserDetails;
import dev.jonas.library.utils.EntityFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RolesToAuthorityMapper rolesToAuthorityMapper;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = EntityFetcher.getUserOrThrow(username, userRepository);

        Collection<GrantedAuthority> authorities = rolesToAuthorityMapper.mapRolesToAuthorities(user.getUserId());

        return new CustomUserDetails(user, authorities);
    }
}