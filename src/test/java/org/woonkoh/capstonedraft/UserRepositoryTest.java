package org.woonkoh.capstonedraft;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.woonkoh.capstonedraft.model.User;
import org.woonkoh.capstonedraft.model.UserDto;
import org.woonkoh.capstonedraft.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        User newUser = new User("Woon Koh","abc@abc.com","password");
        entityManager.persist(newUser);
        entityManager.flush();

        User foundUser = userRepository.findByEmail(newUser.getEmail());

        assertThat(foundUser.getEmail()).isEqualTo(newUser.getEmail());
    }

}
