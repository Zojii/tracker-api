package us.rise8.tracker.api.user;

import java.util.Optional;

import us.rise8.tracker.api.RepositoryInterface;
import us.rise8.tracker.api.user.dto.UserDTO;

public interface UserRepository extends RepositoryInterface<UserEntity, UserDTO> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByKeycloakUid(String keycloakId);
}
