package us.rise8.tracker.api.task.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import us.rise8.tracker.api.AbstractDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements AbstractDTO {
    private Long id;
    private String email;
    private LocalDateTime creationDate;
}

