package org.woonkoh.capstonedraft;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.woonkoh.capstonedraft.model.Order;
import org.woonkoh.capstonedraft.model.OrderStatus;
import org.woonkoh.capstonedraft.model.User;
import org.woonkoh.capstonedraft.repository.OrderRepository;
import org.woonkoh.capstonedraft.repository.UserRepository;
import org.woonkoh.capstonedraft.service.OrderService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;

    // Predefined user and order for all tests to use
    private User user = new User();
    private Order order = new Order();

    @BeforeEach
    public void setup() {
        // Assuming findById always returns the predefined user and order objects
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
    }

    @ParameterizedTest
    @CsvSource({
            // Test case 1: Enough points, order completed, expect exception
            "100, COMPLETED, IllegalStateException.class",
            // Test case 2: Not enough points, order new, expect no exception
            "99, NEW, null",
            // Test case 3: Enough points, order new, expect no exception
            "100, NEW, null"
    })
    public void testApplyRewardToOrder(int points, OrderStatus status, Class<? extends Exception> expectedException) {
        user.setPoints(points);
        order.setStatus(status);

        if (expectedException != null) {
            assertThrows(expectedException, () -> orderService.applyRewardToOrder(1L, 1L));
        } else {
            assertDoesNotThrow(() -> orderService.applyRewardToOrder(1L, 1L));
        }
    }
}
