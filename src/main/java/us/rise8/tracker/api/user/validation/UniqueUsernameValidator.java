package us.rise8.tracker.api.user.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Setter;

import us.rise8.tracker.api.helper.HttpPathVariableIdGrabber;
import us.rise8.tracker.api.user.UserEntity;
import us.rise8.tracker.api.user.UserService;
import us.rise8.tracker.exception.EntityNotFoundException;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserService userService;

    @Setter
    private boolean isNew;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        this.isNew = constraintAnnotation.isNew();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintContext) {
        try {
            UserEntity existingUser = userService.findByUsername(username);
            if (isNew) {
                return false;
            } else {
                return HttpPathVariableIdGrabber.getPathId().equals(existingUser.getId());
            }
        } catch (EntityNotFoundException e) {
            return true;
        }
    }
}
