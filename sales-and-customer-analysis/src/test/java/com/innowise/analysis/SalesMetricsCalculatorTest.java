package test.java.com.innowise.analysis;

import com.innowise.analysis.SalesMetricsCalculator;
import com.innowise.datastructure.Customer;
import com.innowise.datastructure.Order;
import com.innowise.datastructure.OrderItem;
import com.innowise.datastructure.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Evgeniy Zaleshchenok
 */
class SalesMetricsCalculatorTest {

    private SalesMetricsCalculator salesMetricsCalculator;

    private Customer customer1, customer2, customer3;
    private Order order1, order2, order3, order4, order5;

    @BeforeEach
    void setUp() {
        salesMetricsCalculator = new SalesMetricsCalculator();
        customer1 = new Customer("1", "John Doe", "john.doe@example.com", LocalDateTime.now(), 30, "Minsk");
        customer2 = new Customer("2", "Jane Smith", "jane.smith@example.com", LocalDateTime.now(), 40, "Grodno");
        customer3 = new Customer("3", "Peter Jones", "peter.jones@example.com", LocalDateTime.now(), 25, "Minsk");

        order1 = new Order("o1", LocalDateTime.now(), customer1, Arrays.asList(
                new OrderItem("Laptop", 1, 1500.0, null),
                new OrderItem("Mouse", 2, 25.0, null)
        ), OrderStatus.DELIVERED);

        order2 = new Order("o2", LocalDateTime.now(), customer2, Arrays.asList(
                new OrderItem("Mouse", 5, 25.0, null),
                new OrderItem("Keyboard", 1, 75.0, null)
        ), OrderStatus.DELIVERED);

        order3 = new Order("o3", LocalDateTime.now(), customer3, Collections.singletonList(
                new OrderItem("Keyboard", 2, 75.0, null)
        ), OrderStatus.PROCESSING);

        order4 = new Order("o4", LocalDateTime.now(), customer1, Collections.singletonList(
                new OrderItem("Laptop", 1, 1600.0, null)
        ), OrderStatus.CANCELLED);

        order5 = new Order("o5", LocalDateTime.now(), customer2, Collections.emptyList(),
                OrderStatus.DELIVERED);
    }

    @Test
    void test_getAllOrderCities_noOrders() {
        List<Order> orders = new ArrayList<>();
        List<String> cities = salesMetricsCalculator.getAllOrderCities(orders);
        assertTrue(cities.isEmpty());
    }

    @Test
    void test_getAllOrderCities_withDuplicateCities() {
        List<Order> orders = Arrays.asList(order1, order2, order3);
        List<String> cities = salesMetricsCalculator.getAllOrderCities(orders);
        assertEquals(2, cities.size());
        assertTrue(cities.contains("Minsk"));
        assertTrue(cities.contains("Grodno"));
    }

    @Test
    void test_getTotalIncome_noOrders() {
        List<Order> orders = new ArrayList<>();
        double income = salesMetricsCalculator.getTotalIncome(orders);
        assertEquals(0.0, income);
    }

    @Test
    void test_getTotalIncome_withMixedStatuses() {
        List<Order> orders = Arrays.asList(order1, order2, order3, order4);
        // order1 (1*1500 + 2*25 = 1550) + order2 (5*25 + 1*75 = 200) = 1750
        double income = salesMetricsCalculator.getTotalIncome(orders);
        assertEquals(1750.0, income);
    }

    @Test
    void test_getTotalIncome_withNoDeliveredOrders() {
        List<Order> orders = Arrays.asList(order3, order4);
        double income = salesMetricsCalculator.getTotalIncome(orders);
        assertEquals(0.0, income);
    }

    @Test
    void test_getMostPopularProduct_noOrders() {
        List<Order> orders = new ArrayList<>();
        String mostPopularProduct = salesMetricsCalculator.getMostPopularProduct(orders);
        assertEquals("None", mostPopularProduct);
    }

    @Test
    void test_getMostPopularProduct_withClearWinner() {
        List<Order> orders = Arrays.asList(order1, order2, order3);
        // Mouse: 2 + 5 = 7
        // Laptop: 1 = 1
        // Keyboard: 1 + 2 = 3
        String mostPopularProduct = salesMetricsCalculator.getMostPopularProduct(orders);
        assertEquals("Mouse", mostPopularProduct);
    }

    @Test
    void test_getMostPopularProduct_withATie() {
        Order tieOrder = new Order("o_tie", LocalDateTime.now(), customer1, Arrays.asList(
                new OrderItem("Laptop", 6, 1500.0, null)
        ), OrderStatus.DELIVERED);
        List<Order> orders = Arrays.asList(order1, order2, order3, tieOrder);
        // Mouse: 2 + 5 = 7
        // Laptop: 1 + 6 = 7
        // Keyboard: 1 + 2 = 3
        String mostPopularProduct = salesMetricsCalculator.getMostPopularProduct(orders);
        assertTrue(mostPopularProduct.equals("Mouse") || mostPopularProduct.equals("Laptop"));
    }

    @Test
    void test_getAverageCheckForDeliveredOrders_noOrders() {
        List<Order> orders = new ArrayList<>();
        double averageCheck = salesMetricsCalculator.getAverageCheckForDeliveredOrders(orders);
        assertEquals(0.0, averageCheck);
    }

    @Test
    void test_getAverageCheckForDeliveredOrders_withMixedStatuses() {
        List<Order> orders = Arrays.asList(order1, order2, order3, order4, order5);
        // Доставлены: order1 (1550), order2 (200), order5 (0)
        // Средний чек: (1550 + 200 + 0) / 3 = 583.33...
        double averageCheck = salesMetricsCalculator.getAverageCheckForDeliveredOrders(orders);
        assertEquals(1750.0 / 3.0, averageCheck, 0.001);
    }

    @Test
    void test_getCustomersWithMoreThanFiveOrders_noOrders() {
        List<Order> orders = new ArrayList<>();
        List<Customer> customers = salesMetricsCalculator.getCustomersWithMoreThanFiveOrders(orders);
        assertTrue(customers.isEmpty());
    }

    @Test
    void test_getCustomersWithMoreThanFiveOrders_withQualifiedCustomer() {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            orderList.add(new Order("o" + i, LocalDateTime.now(),
                    customer1, Collections.emptyList(), OrderStatus.DELIVERED));
        }
        for (int i = 0; i < 5; i++) {
            orderList.add(new Order("o" + (i+6), LocalDateTime.now(),
                    customer2, Collections.emptyList(), OrderStatus.DELIVERED));
        }

        List<Customer> customers = salesMetricsCalculator.getCustomersWithMoreThanFiveOrders(orderList);

        assertEquals(1, customers.size());
        assertEquals(customer1.getCustomerId(), customers.get(0).getCustomerId());
    }

    @Test
    void test_getCustomersWithMoreThanFiveOrders_withBoundaryOfFive() {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderList.add(new Order("o" + i, LocalDateTime.now(),
                    customer2, Collections.emptyList(), OrderStatus.DELIVERED));
        }

        List<Customer> customers = salesMetricsCalculator.getCustomersWithMoreThanFiveOrders(orderList);
        assertTrue(customers.isEmpty());
    }
}