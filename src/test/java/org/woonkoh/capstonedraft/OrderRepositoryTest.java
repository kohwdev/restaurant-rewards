package org.woonkoh.capstonedraft;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.woonkoh.capstonedraft.model.Order;
import org.woonkoh.capstonedraft.model.OrderStatus;
import org.woonkoh.capstonedraft.model.User;
import org.woonkoh.capstonedraft.repository.OrderRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void findFirstByUserIdAndStatus_ShouldReturnCorrectOrder() {
        // Create a User
        User user = new User();
        // Set properties as necessary
        user.setEmail("test@email.com");
        user.setName("Woon Koh");
        user.setPassword("password");
        user = entityManager.persistAndFlush(user);

        // Create an Order with a specific status
        Order order1 = new Order();
        order1.setUser(user);
        order1.setStatus(OrderStatus.NEW);
        entityManager.persist(order1);

        Order order2 = new Order();
        order2.setUser(user);
        order2.setStatus(OrderStatus.NEW);
        entityManager.persist(order2);

        entityManager.flush();

        // Test the query method
        Order foundOrder = orderRepository.findFirstByUserIdAndStatus(user.getId(), OrderStatus.NEW);

        // Assertions
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(order1.getId());
    }

}
