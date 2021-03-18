package us.rise8.tracker.api.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import us.rise8.tracker.api.helper.Builder;
import us.rise8.tracker.api.search.SpecificationsBuilder;
import us.rise8.tracker.api.user.dto.UpdateUserDTO;
import us.rise8.tracker.api.user.dto.UpdateUserDisabledDTO;
import us.rise8.tracker.api.user.dto.UpdateUserRolesDTO;
import us.rise8.tracker.api.user.dto.UserDTO;
import us.rise8.tracker.config.CustomProperty;
import us.rise8.tracker.exception.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
@Import(UserService.class)
public class UserServiceTests {

    @Autowired
    UserService userService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    CustomProperty property;
    @Captor
    ArgumentCaptor<User> userCaptor;

    private final LocalDateTime CREATION_DATE = LocalDateTime.now();
    private final User user1 = Builder.build(User.class)
            .with(u -> u.setId(1L))
            .with(u -> u.setKeycloakUid("def-456"))
            .with(u -> u.setUsername("yoda"))
            .with(u -> u.setEmail("e.b@c"))
            .with(u -> u.setDisplayName("master yoda"))
            .with(u -> u.setCreationDate(CREATION_DATE))
            .with(u -> u.setDodId(123L))
            .with(u -> u.setRoles(0L)).get();
    private final User user2 = Builder.build(User.class)
            .with(u -> u.setId(2L))
            .with(u -> u.setKeycloakUid("abc-123"))
            .with(u -> u.setUsername("grogu"))
            .with(u -> u.setEmail("a.b@c"))
            .with(u -> u.setDisplayName("baby yoda"))
            .with(u -> u.setCreationDate(CREATION_DATE))
            .with(u -> u.setDodId(789L))
            .with(u -> u.setRoles(1L)).get();
    private final List<String> groups = List.of("tracker-IL2-admin");
    private final List<User> users = List.of(user1, user2);
    private final Page<User> page = new PageImpl<User>(users);

    @Test
    public void should_get_user_by_id() throws EntityNotFoundException {
        when(userRepository.findById(any())).thenReturn(Optional.of(user1));

        assertThat(userService.getObject(1L)).isEqualTo(user1);
    }

    @Test
    public void should_get_user_by_username() throws EntityNotFoundException {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user1));

        assertThat(userService.findByUsername("foo")).isEqualTo(user1);
    }

    @Test
    public void should_throw_exception_not_found_user_by_username() throws EntityNotFoundException {
        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () ->
                userService.findByUsername("foobar"));
        assertThat(e).hasMessage("Failed to find User by username: foobar");
    }

    @Test
    public void should_throw_error_when_id_null() throws EntityNotFoundException {
        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () ->
                userService.getObject(null));
        assertThat(e).hasMessage("Failed to find User");
    }

    @Test
    public void should_return_true_when_exists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(users.get(0)));

        assertTrue(userService.existsById(1L));
    }

    @Test
    public void should_return_false_when_exists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertFalse(userService.existsById(1L));
    }

    @Test
    public void should_throw_error_when_id_not_found() throws EntityNotFoundException {
        when(userRepository.findById(any())).thenReturn(java.util.Optional.empty());

        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () -> userService.getObject(1L));
        assertThat(e).hasMessage("Failed to find User with id 1");
    }

    @Test
    public void should_get_user_and_return_user() throws EntityNotFoundException {
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(new User()));

        userService.findById(1L);

        verify(userRepository).findById(1L);
    }

    @Test
    public void should_update_user() {
        UpdateUserDTO updateDTO = Builder.build(UpdateUserDTO.class)
                .with(u -> u.setUsername("Master Yoda"))
                .with(u -> u.setEmail("yo.diddy@force.net"))
                .with(u -> u.setDisplayName("YoDiddy")).get();

        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(user1);

        userService.updateById(1L, updateDTO);

        verify(userRepository, times(1)).save(userCaptor.capture());
        User userCaptured = userCaptor.getValue();

        assertThat(userCaptured.getUsername()).isEqualTo(updateDTO.getUsername());
        assertThat(userCaptured.getEmail()).isEqualTo(updateDTO.getEmail());
        assertThat(userCaptured.getDisplayName()).isEqualTo(updateDTO.getDisplayName());
    }

    @Test
    public void should_update_user_roles_by_id() {
        UpdateUserRolesDTO updateDTO = Builder.build(UpdateUserRolesDTO.class)
                .with(p -> p.setRoles(0L)).get();

        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(user1);

        userService.updateRolesById(1L, updateDTO);
        verify(userRepository, times(1)).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getRoles()).isEqualTo(0L);
    }

    @Test
    public void should_update_is_disabled_by_id() {
        UpdateUserDisabledDTO updateDTO = Builder.build(UpdateUserDisabledDTO.class)
                .with(p -> p.setDisabled(true)).get();

        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(user1);

        userService.updateIsDisabledById(1L, updateDTO);
        verify(userRepository, times(1)).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getIsDisabled()).isEqualTo(true);
    }

    @Test
    public void should_prepare_paged_response() {
        List<UserDTO> results = userService.preparePageResponse(page, new MockHttpServletResponse());

        assertThat(results).isEqualTo(users.stream().map(User::toDto).collect(Collectors.toList()));
    }

    @Test
    public void should_retrieve_all_users() {
        SpecificationsBuilder<User> builder = new SpecificationsBuilder<>();
        Specification<User> specs = builder.withSearch("id:1").build();

        when(userRepository.findAll(eq(specs), any(PageRequest.class))).thenReturn(page);

        assertThat(userService.search(specs, 1, null, null, null).stream().findFirst())
                .isEqualTo(Optional.of(users.get(0)));
    }

    @Test
    public void should_delete_by_id() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        userService.deleteById(1L);

        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    public void should_delete_all() {
        userService.deleteAll();

        verify(userRepository, times(1)).deleteAll();
    }

    @Test
    void should_get_user_by_keycloak_uid() {
        when(userRepository.findByKeycloakUid(any())).thenReturn(Optional.of(user1));

        assertThat(userService.findByKeycloakUid("abc-123")).isEqualTo(Optional.of(user1));
    }
}
