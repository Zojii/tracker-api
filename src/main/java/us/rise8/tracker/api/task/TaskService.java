//package us.rise8.tracker.api.task;
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import us.rise8.tracker.api.AbstractCRUDService;
//import us.rise8.tracker.api.user.dto.UserDTO;
//import us.rise8.tracker.config.CustomProperty;
//import us.rise8.tracker.exception.EntityNotFoundException;
//
//@Service
//public class TaskService extends AbstractCRUDService<Task, UserDTO, TaskRepository> {
//
//    CustomProperty property;
//
//    @Autowired
//    public TaskService(TaskRepository repository, CustomProperty property) {
//        super(repository, Task.class, UserDTO.class);
//        this.property = property;
//    }
//
//    public Task updateById(Long id, UpdateUserDTO updateUserDTO) {
//        Task task = getObject(id);
//        task.setUsername(updateUserDTO.getUsername());
//        task.setEmail(updateUserDTO.getEmail());
//        task.setDisplayName(updateUserDTO.getDisplayName());
//
//        return repository.save(task);
//    }
//
//    public Task updateRolesById(Long id, UpdateUserRolesDTO updateUserRolesDTO) {
//        Task task = getObject(id);
//        task.setRoles(updateUserRolesDTO.getRoles());
//
//        return repository.save(task);
//    }
//
//    public Task updateIsDisabledById(Long id, UpdateUserDisabledDTO updateUserDisabledDTO) {
//        Task task = getObject(id);
//
//        task.setIsDisabled(updateUserDisabledDTO.isDisabled());
//
//        return repository.save(task);
//    }
//
//    public Task findByUsername(String username) {
//        return repository.findByUsername(username).orElseThrow(
//                () -> new EntityNotFoundException(Task.class.getSimpleName(), "username", username));
//    }
//
//    public Optional<Task> findByKeycloakUid(String keycloakUid) {
//        return repository.findByKeycloakUid(keycloakUid);
//    }
//}
