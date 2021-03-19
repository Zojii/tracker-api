package us.rise8.tracker.api.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import us.rise8.tracker.api.AbstractEntity;
import us.rise8.tracker.api.user.dto.UserDTO;

@Entity @Getter @Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity<UserDTO> {

    @Column(columnDefinition = "VARCHAR(100)")
    private String email;

    public UserDTO toDto() {
        return new UserDTO(id, email, creationDate);
    }

    public User(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return this.hashCode() == that.hashCode();
    }
}
