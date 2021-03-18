package us.rise8.tracker.api.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.rise8.tracker.api.ControllerTestHarness;
import us.rise8.tracker.api.helper.Builder;
import us.rise8.tracker.api.helper.JsonMapper;
import us.rise8.tracker.api.user.dto.UpdateUserDTO;
import us.rise8.tracker.api.user.dto.UpdateUserDisabledDTO;
import us.rise8.tracker.api.user.dto.UpdateUserRolesDTO;
import us.rise8.tracker.api.user.dto.UserDTO;
import us.rise8.tracker.exception.EntityNotFoundException;

@WebMvcTest({UserController.class})
public class UserControllerTests extends ControllerTestHarness {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService userService;

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
    private final UpdateUserDTO updateUserDTO = Builder.build(UpdateUserDTO.class)
            .with(u -> u.setUsername("Master Yoda"))
            .with(u -> u.setEmail("mayTheForceBeWithYou@agalaxy.far.away"))
            .with(u -> u.setDisplayName("Jedi Master")).get();
    private User user1Updated = new User();
    private List<User> users = List.of(user1, user2);

    @BeforeEach
    public void init() throws Exception {
        BeanUtils.copyProperties(user1, user1Updated);

        when(userService.findByKeycloakUid(any())).thenReturn(Optional.of(user2));
    }

    @Test
    public void should_get_user_by_id() throws Exception {
        when(userService.findById(1L)).thenReturn(user1);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.username").value(user1.getUsername()));
    }

    @Test
    public void should_update_user() throws Exception {
        user1Updated.setUsername(updateUserDTO.getUsername());
        user1Updated.setEmail(updateUserDTO.getEmail());
        user1Updated.setDisplayName(updateUserDTO.getDisplayName());

        when(userService.findByUsername(any())).thenReturn(user1);
        when(userService.updateById(1L, updateUserDTO)).thenReturn(user1Updated);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(updateUserDTO))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.username").value(user1Updated.getUsername()))
                .andExpect(jsonPath("$.email").value(user1Updated.getEmail()))
                .andExpect(jsonPath("$.displayName").value(user1Updated.getDisplayName()));
    }

    @Test
    public void should_throw_unique_name_exception_on_update_user() throws Exception {
        String expectedMessage = "username already in use";

        when(userService.findByUsername(any())).thenReturn(user2);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(updateUserDTO))
        )
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors[0]").value(expectedMessage));
    }

    @Test
    public void should_update_user_roles() throws Exception {
        UpdateUserRolesDTO updateUserRolesDTO = Builder.build(UpdateUserRolesDTO.class)
                .with(p -> p.setRoles(1L)).get();
        user1Updated.setRoles(1L);

        when(userService.updateRolesById(1L, updateUserRolesDTO)).thenReturn(user1Updated);

        mockMvc.perform(put("/api/users/1/admin/roles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(updateUserRolesDTO))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.roles").value(user1Updated.getRoles()));
    }

    @Test
    public void should_toggle_user_disabled() throws Exception {
        UpdateUserDisabledDTO updateUserDisabledDTO = Builder.build(UpdateUserDisabledDTO.class)
                .with(p -> p.setDisabled(true)).get();
        user1Updated.setIsDisabled(true);

        when(userService.updateIsDisabledById(1L, updateUserDisabledDTO)).thenReturn(user1Updated);

        mockMvc.perform(put("/api/users/1/admin/disable")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(updateUserDisabledDTO))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.isDisabled").value(user1Updated.getIsDisabled()));
    }

    @Test
    public void should_search_users() throws Exception {
        Page<User> page = new PageImpl<>(users);
        List<UserDTO> userDTOs = users.stream().map(User::toDto).collect(Collectors.toList());

        when(userService.search(any(), any(), any(), any(), any())).thenReturn(page);
        when(userService.preparePageResponse(any(), any())).thenReturn(userDTOs);

        mockMvc.perform(get("/api/users?search=id>=1 AND user.username:\"yoda\" OR username:grogu"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(JsonMapper.dateMapper().writeValueAsString(userDTOs))
                );
    }

    @Test
    public void should_throw_entity_not_found_when_id_not_found() throws Exception {
        EntityNotFoundException expectedError = new EntityNotFoundException(User.class.getSimpleName(), 1L);
        when(userService.findById(any())).thenThrow(expectedError);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value(expectedError.getMessage()));
    }

    @Test
    public void should_delete_by_id() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteById(1L);
    }

}
