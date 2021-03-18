package us.rise8.tracker.config.auth.platform1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import us.rise8.tracker.api.user.UserEntity;
import us.rise8.tracker.api.user.UserService;
import us.rise8.tracker.enums.Roles;

@Component
public class PlatformOneAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Autowired
    public PlatformOneAuthenticationProvider(UserService service) {
        this.userService = service;
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        PlatformOneAuthenticationToken token = (PlatformOneAuthenticationToken) authentication;
        Optional<UserEntity> optionalUser = userService.findByKeycloakUid(token.getKeycloakUid());
        UserEntity user = optionalUser.orElseGet(() -> userService.create(token));

        List<GrantedAuthority> authorityList = new ArrayList<>(getRoles(user));
        authorityList.add(new SimpleGrantedAuthority("IS_AUTHENTICATED"));

        Authentication auth = new PlatformOneAuthenticationToken(user, null, authorityList);
        auth.setAuthenticated(true);
        return auth;
    }

    private List<GrantedAuthority> getRoles(UserEntity user) {

        List<GrantedAuthority> authorityList = new ArrayList<>();
        Map<Roles, Boolean> rolesMap = Roles.getRoles((user.getRoles()));
        rolesMap.forEach((role, hasRole) -> {
            if (hasRole) {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getName());
                authorityList.add(simpleGrantedAuthority);
            }
        });

        return authorityList;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PlatformOneAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
