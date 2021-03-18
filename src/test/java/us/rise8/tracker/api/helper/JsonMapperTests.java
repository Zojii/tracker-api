package us.rise8.tracker.api.helper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.junit.jupiter.api.Test;

import us.rise8.tracker.api.user.UserEntity;
import us.rise8.tracker.config.auth.platform1.PlatformOneAuthenticationToken;

public class JsonMapperTests {

    private final UserEntity user = Builder.build(UserEntity.class)
            .with(u -> u.setId(1L))
            .with(u -> u.setKeycloakUid("Hello")).get();

    @Test
    public void should_throw_error_if_private_constructor_is_called() throws Exception {
        Class<?> clazz = JsonMapper.class;
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertThat(exception.getCause().getClass()).isEqualTo(IllegalStateException.class);
    }

    @Test
    public void should_get_keycloak_uid_from_auth() throws AuthenticationCredentialsNotFoundException {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("IS_AUTHENTICATED"));
        Authentication auth = new PlatformOneAuthenticationToken(user, null, authorityList);

        assertThat(JsonMapper.getKeycloakUidFromAuth(auth)).isEqualTo("Hello");
    }

}
