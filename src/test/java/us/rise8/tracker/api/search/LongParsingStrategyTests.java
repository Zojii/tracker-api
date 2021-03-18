package us.rise8.tracker.api.search;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.rise8.tracker.api.helper.Builder;
import us.rise8.tracker.api.user.UserEntity;
import us.rise8.tracker.api.user.UserRepository;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LongParsingStrategyTests {

    private final UserEntity testUser1 = Builder.build(UserEntity.class)
            .with(u -> u.setKeycloakUid("abc-123"))
            .with(u -> u.setUsername("foo"))
            .with(u -> u.setEmail("a.b@c"))
            .with(u -> u.setDisplayName("Mr. Foo")).get();
    private final UserEntity testUser2 = Builder.build(UserEntity.class)
            .with(u -> u.setKeycloakUid("def-456"))
            .with(u -> u.setUsername("foobar"))
            .with(u -> u.setEmail("d.e@f"))
            .with(u -> u.setDisplayName("foobar")).get();
    @Autowired
    TestEntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void init() {
        entityManager.persist(testUser1);
        entityManager.persist(testUser2);
        entityManager.flush();
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_long_greater_than_equal_to() {
        SearchCriteria criteria = new SearchCriteria("id", ">=", null, "1", null);
        Specification<UserEntity> specs = new SpecificationImpl<>(criteria);
        List<UserEntity> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_long_greater_than() {
        SearchCriteria criteria = new SearchCriteria("id", ">", null, "1", null);
        Specification<UserEntity> specs = new SpecificationImpl<>(criteria);
        List<UserEntity> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_long_less_than() {
        SearchCriteria criteria = new SearchCriteria("id", "<", null, "2", null);
        Specification<UserEntity> specs = new SpecificationImpl<>(criteria);
        List<UserEntity> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_long_less_than_equal_to() {
        SearchCriteria criteria = new SearchCriteria("id", "<=", null, "2", null);
        Specification<UserEntity> specs = new SpecificationImpl<>(criteria);
        List<UserEntity> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_long_equal_to() {
        SearchCriteria criteria = new SearchCriteria("id", ":", null, "2", null);
        Specification<UserEntity> specs = new SpecificationImpl<>(criteria);
        List<UserEntity> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_long_not_equal_to() {
        SearchCriteria criteria = new SearchCriteria("id", "!", null, "2", null);
        Specification<UserEntity> specs = new SpecificationImpl<>(criteria);
        List<UserEntity> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(1);
    }

}
