package us.rise8.tracker.api.init;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.rise8.tracker.api.ControllerTestHarness;
import us.rise8.tracker.api.helper.Builder;
import us.rise8.tracker.api.user.UserEntity;
import us.rise8.tracker.api.user.UserService;
import us.rise8.tracker.api.user.dto.UserDTO;
import us.rise8.tracker.config.CustomProperty;

@WebMvcTest(InitController.class)
public class InitControllerTest extends ControllerTestHarness {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomProperty property;
    @MockBean
    private UserService userService;

    @BeforeEach
    public void init() {
        UserEntity authUser = Builder.build(UserEntity.class)
                .with(u -> u.setRoles(1L))
                .with(u -> u.setKeycloakUid("abc-123"))
                .with(u -> u.setUsername("grogu")).get();
        when(userService.findByKeycloakUid(any())).thenReturn(Optional.of(authUser));
        when(userService.getUserFromAuth(any())).thenReturn(authUser);
    }

    @Test
    public void should_get_public_info() throws Exception {

        String endpoint = "/init/info";

        when(property.getClassification()).thenReturn("UNCLASS");
        when(property.getCaveat()).thenReturn("IL2");

        mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.classification").isMap());
    }

    @Test
    public void should_get_from_public_info() throws Exception {
        when(property.getClassification()).thenReturn("UNCLASS");
        when(property.getCaveat()).thenReturn("IL2");

        mockMvc.perform(get("/init/info"))
                .andExpect(jsonPath("$.classification").isMap())
                .andExpect(jsonPath("$.classification.name").isNotEmpty())
                .andExpect(jsonPath("$.classification.caveat").isNotEmpty())
                .andExpect(jsonPath("$.classification.backgroundColor").isNotEmpty())
                .andExpect(jsonPath("$.classification.textColor").isNotEmpty())
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[*].name").isNotEmpty())
                .andExpect(jsonPath("$.roles[*].offset").isNotEmpty())
                .andExpect(jsonPath("$.roles[*].description").isNotEmpty());
    }

    @Test
    public void should_login_init_locally() throws Exception {
        UserDTO userDTO = Builder.build(UserDTO.class).with(u -> u.setId(1L)).get();

        mockMvc.perform(get("/init/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.keycloakUid").value("abc-123"));
    }
}
