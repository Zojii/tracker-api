package us.rise8.tracker.api.user;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import us.rise8.tracker.api.AbstractCRUDService;
import us.rise8.tracker.api.helper.Builder;
import us.rise8.tracker.api.helper.JsonMapper;
import us.rise8.tracker.api.user.dto.UpdateUserDTO;
import us.rise8.tracker.api.user.dto.UpdateUserDisabledDTO;
import us.rise8.tracker.api.user.dto.UpdateUserRolesDTO;
import us.rise8.tracker.api.user.dto.UserDTO;
import us.rise8.tracker.config.CustomProperty;
import us.rise8.tracker.config.auth.platform1.PlatformOneAuthenticationToken;
import us.rise8.tracker.enums.Roles;
import us.rise8.tracker.exception.EntityNotFoundException;

@Service
public class UserService extends AbstractCRUDService<UserEntity, UserDTO, UserRepository> {

    CustomProperty property;

    @Autowired
    public UserService(UserRepository repository, CustomProperty property) {
        super(repository, UserEntity.class, UserDTO.class);
        this.property = property;
    }

    public UserEntity create(PlatformOneAuthenticationToken token) {
        Boolean isAdmin = token.getGroups().stream().anyMatch(g -> g.contains(property.getJwtAdminGroup()));  //add group name in application.yml
        Long rolesAsLong = Roles.setRoles(0L, Map.of(Roles.ADMIN, isAdmin));
        UserEntity user = Builder.build(UserEntity.class)
                .with(u -> u.setKeycloakUid(token.getKeycloakUid()))
                .with(u -> u.setDodId(token.getDodId()))
                .with(u -> u.setDisplayName(token.getDisplayName()))
                .with(u -> u.setRoles(rolesAsLong))
                .with(u -> u.setEmail(token.getEmail())).get();
        return repository.save(user);
    }

    public UserEntity updateById(Long id, UpdateUserDTO updateUserDTO) {
        UserEntity user = getObject(id);
        user.setUsername(updateUserDTO.getUsername());
        user.setEmail(updateUserDTO.getEmail());
        user.setDisplayName(updateUserDTO.getDisplayName());

        return repository.save(user);
    }

    public UserEntity updateRolesById(Long id, UpdateUserRolesDTO updateUserRolesDTO) {
        UserEntity user = getObject(id);
        user.setRoles(updateUserRolesDTO.getRoles());

        return repository.save(user);
    }

    public UserEntity updateIsDisabledById(Long id, UpdateUserDisabledDTO updateUserDisabledDTO) {
        UserEntity user = getObject(id);

        user.setIsDisabled(updateUserDisabledDTO.isDisabled());

        return repository.save(user);
    }

    public UserEntity findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(UserEntity.class.getSimpleName(), "username", username));
    }

    public Optional<UserEntity> findByKeycloakUid(String keycloakUid) {
        return repository.findByKeycloakUid(keycloakUid);
    }

    public UserEntity getUserFromAuth(Authentication auth) {
        String keycloakUid = JsonMapper.getKeycloakUidFromAuth(auth);

        return findByKeycloakUid(keycloakUid).orElseThrow(() ->
                new EntityNotFoundException(
                        UserEntity.class.getSimpleName(),
                        "keycloakUid",
                        String.valueOf(keycloakUid)
                ));
    }
}
