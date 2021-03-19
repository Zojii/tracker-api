package us.rise8.tracker.api.task;

import java.util.Optional;

import us.rise8.tracker.api.RepositoryInterface;
import us.rise8.tracker.api.user.dto.UserDTO;

public interface TaskRepository extends RepositoryInterface<Task, UserDTO> {

    Optional<Task> findByEmail(String username);

}
