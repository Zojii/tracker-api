package us.rise8.tracker.api.user;

import java.util.Optional;

import us.rise8.tracker.api.RepositoryInterface;
import us.rise8.tracker.api.user.dto.UserDTO;

public interface UserRepository extends RepositoryInterface<User, UserDTO> {

    Optional<User> findByUsername(String username);

    Optional<User> findByKeycloakUid(String keycloakId);
}
