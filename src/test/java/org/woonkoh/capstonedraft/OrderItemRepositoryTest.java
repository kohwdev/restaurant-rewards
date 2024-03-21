package org.woonkoh.capstonedraft;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.woonkoh.capstonedraft.model.OrderItem;
import org.woonkoh.capstonedraft.model.Product;
import org.woonkoh.capstonedraft.repository.OrderItemRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Test
    public void findAllByProductId_ShouldReturnOrderItems() {
        Product product = new Product();
        product = entityManager.persistAndFlush(product);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setProduct(product);
        entityManager.persist(orderItem1);

        OrderItem orderItem2 = new OrderItem();
        entityManager.persist(orderItem2);

        entityManager.flush();

        List<OrderItem> foundOrderItems = orderItemRepository.findAllByProductId(product.getId());

        // Assertions
        assertThat(foundOrderItems).isNotEmpty();
        assertThat(foundOrderItems.size()).isEqualTo(1);
        assertThat(foundOrderItems.get(0)).isEqualToComparingFieldByField(orderItem1);
    }

}
