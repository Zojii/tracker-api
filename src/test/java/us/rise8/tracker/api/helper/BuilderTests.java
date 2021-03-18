package us.rise8.tracker.api.helper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import us.rise8.tracker.api.user.UserEntity;

public class BuilderTests {

    @Test
    public void shouldBuildClassObject() {

        UserEntity user = Builder.build(UserEntity.class)
                .with(u -> u.setUsername("Foo")).get();
        assertThat(user.getUsername()).isEqualTo("Foo");
    }

}
