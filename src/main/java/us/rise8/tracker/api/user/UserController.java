package us.rise8.tracker.api.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.rise8.tracker.api.AbstractCRUDController;
import us.rise8.tracker.api.user.dto.UpdateUserDTO;
import us.rise8.tracker.api.user.dto.UpdateUserDisabledDTO;
import us.rise8.tracker.api.user.dto.UpdateUserRolesDTO;
import us.rise8.tracker.api.user.dto.UserDTO;
import us.rise8.tracker.config.auth.IsAdmin;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController extends AbstractCRUDController<UserEntity, UserDTO, UserService> {

    @Autowired private UserService service;
    public UserController(UserService service) {
        super(service);
    }

    @PutMapping("/{id}")
    public UserDTO updateById(@Valid @RequestBody UpdateUserDTO updateUserDTO, @PathVariable Long id) {
        return service.updateById(id, updateUserDTO).toDto();
    }

    @IsAdmin
    @PutMapping("/{id}/admin/roles")
    public UserDTO updateRolesById(@RequestBody UpdateUserRolesDTO updateUserRolesDTO, @PathVariable Long id) {
        return service.updateRolesById(id, updateUserRolesDTO).toDto();
    }

    @IsAdmin
    @PutMapping("/{id}/admin/disable")
    public UserDTO updateIsDisabledById(@RequestBody UpdateUserDisabledDTO updateUserDisabledDTO,
                                        @PathVariable Long id) {
        return service.updateIsDisabledById(id, updateUserDisabledDTO).toDto();
    }
}