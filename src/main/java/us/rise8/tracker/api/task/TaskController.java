//package us.rise8.tracker.api.task;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import us.rise8.tracker.api.AbstractCRUDController;
//import us.rise8.tracker.api.user.dto.UserDTO;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api/users")
//public class TaskController extends AbstractCRUDController<Task, UserDTO, TaskService> {
//
//    @Autowired private TaskService service;
//    public TaskController(TaskService service) {
//        super(service);
//    }
//
//    @PutMapping("/{id}")
//    public UserDTO updateById(@Valid @RequestBody UpdateUserDTO updateUserDTO, @PathVariable Long id) {
//        return service.updateById(id, updateUserDTO).toDto();
//    }
//
//    @PutMapping("/{id}/admin/roles")
//    public UserDTO updateRolesById(@RequestBody UpdateUserRolesDTO updateUserRolesDTO, @PathVariable Long id) {
//        return service.updateRolesById(id, updateUserRolesDTO).toDto();
//    }
//
//    @PutMapping("/{id}/admin/disable")
//    public UserDTO updateIsDisabledById(@RequestBody UpdateUserDisabledDTO updateUserDisabledDTO,
//                                        @PathVariable Long id) {
//        return service.updateIsDisabledById(id, updateUserDisabledDTO).toDto();
//    }
//}
