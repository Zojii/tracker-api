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
import us.rise8.tracker.api.user.User;
import us.rise8.tracker.api.user.UserRepository;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StringParsingStrategyTests {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    private final User testUser1 = Builder.build(User.class)
            .with(u -> u.setKeycloakUid("abc-123"))
            .with(u -> u.setUsername("foo"))
            .with(u -> u.setEmail("a.b@c"))
            .with(u -> u.setDisplayName("Mr.Foo")).get();
    private final User testUser2 = Builder.build(User.class)
            .with(u -> u.setKeycloakUid("def-456"))
            .with(u -> u.setUsername("foobar"))
            .with(u -> u.setEmail("d.e@f"))
            .with(u -> u.setDisplayName("foobar")).get();

    @BeforeEach
    public void init() {
        entityManager.persist(testUser1);
        entityManager.persist(testUser2);
        entityManager.flush();
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_equal_to() {
        SearchCriteria criteria = new SearchCriteria("username", ":", null, "foo", null);
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.get(0).getUsername()).isEqualTo(testUser1.getUsername());
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_not_equal_to() {
        SearchCriteria criteria = new SearchCriteria("username", "!", null, "foobar", null);
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.get(0).getUsername()).isEqualTo(testUser1.getUsername());
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_greater_than() {
        SearchCriteria criteria = new SearchCriteria("username", ">", null, "fooba", null);
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getUsername()).isEqualTo(testUser2.getUsername());
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_less_than() {
        SearchCriteria criteria = new SearchCriteria("username", "<", null, "foobar", null);
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getUsername()).isEqualTo(testUser1.getUsername());
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_starts_with() {
        SearchCriteria criteria = new SearchCriteria("username", ":", null, "foo", "*");
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_ends_with() {
        SearchCriteria criteria = new SearchCriteria("username", ":", "*", "bar", null);
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getUsername()).isEqualTo(testUser2.getUsername());
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_contains() {
        SearchCriteria criteria = new SearchCriteria("username", ":", "*", "oo", "*");
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_does_not_starts_with() {
        SearchCriteria criteria = new SearchCriteria("username", "!", null, "foo", "*");
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_does_not_ends_with() {
        SearchCriteria criteria = new SearchCriteria("username", "!", "*", "bar", null);
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getUsername()).isEqualTo(testUser1.getUsername());
    }

    @Test
    public void should_search_by_spec_and_parsing_strat_string_does_not_contain() {
        SearchCriteria criteria = new SearchCriteria("username", "!", "*", "oo", "*");
        Specification<User> specs = new SpecificationImpl<>(criteria);
        List<User> users = userRepository.findAll(specs);

        assertThat(users.size()).isEqualTo(0);
    }

}
