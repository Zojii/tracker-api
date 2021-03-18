package us.rise8.tracker.api.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.rise8.tracker.api.AbstractCRUDService;
import us.rise8.tracker.api.user.dto.UpdateUserDTO;
import us.rise8.tracker.api.user.dto.UpdateUserDisabledDTO;
import us.rise8.tracker.api.user.dto.UpdateUserRolesDTO;
import us.rise8.tracker.api.user.dto.UserDTO;
import us.rise8.tracker.config.CustomProperty;
import us.rise8.tracker.exception.EntityNotFoundException;

@Service
public class UserService extends AbstractCRUDService<User, UserDTO, UserRepository> {

    CustomProperty property;

    @Autowired
    public UserService(UserRepository repository, CustomProperty property) {
        super(repository, User.class, UserDTO.class);
        this.property = property;
    }

    public User updateById(Long id, UpdateUserDTO updateUserDTO) {
        User user = getObject(id);
        user.setUsername(updateUserDTO.getUsername());
        user.setEmail(updateUserDTO.getEmail());
        user.setDisplayName(updateUserDTO.getDisplayName());

        return repository.save(user);
    }

    public User updateRolesById(Long id, UpdateUserRolesDTO updateUserRolesDTO) {
        User user = getObject(id);
        user.setRoles(updateUserRolesDTO.getRoles());

        return repository.save(user);
    }

    public User updateIsDisabledById(Long id, UpdateUserDisabledDTO updateUserDisabledDTO) {
        User user = getObject(id);

        user.setIsDisabled(updateUserDisabledDTO.isDisabled());

        return repository.save(user);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(User.class.getSimpleName(), "username", username));
    }

    public Optional<User> findByKeycloakUid(String keycloakUid) {
        return repository.findByKeycloakUid(keycloakUid);
    }
}
