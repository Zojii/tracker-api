package us.rise8.tracker.api.user.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateUserRolesDTO {
    @NotNull
    private Long roles;
}
