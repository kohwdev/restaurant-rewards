package org.woonkoh.capstonedraft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.woonkoh.capstonedraft.model.Role;
import org.woonkoh.capstonedraft.repository.RoleRepository;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        // Clear database to ensure a clean state for each test
        entityManager.getEntityManager()
                .createQuery("DELETE FROM Role").executeUpdate();
    }

    @Test
    public void whenFindByName_thenReturnRole() {
        // given
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        entityManager.persistAndFlush(adminRole);

        // when
        Role found = roleRepository.findByName("ROLE_ADMIN");

        // then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo(adminRole.getName());
    }
}
