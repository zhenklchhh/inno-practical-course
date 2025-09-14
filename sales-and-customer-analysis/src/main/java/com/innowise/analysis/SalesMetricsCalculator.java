    package com.innowise.analysis;

    import com.innowise.datastructure.Customer;
    import com.innowise.datastructure.Order;
    import com.innowise.datastructure.OrderItem;
    import com.innowise.datastructure.OrderStatus;

    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    /**
     * @author Evgeniy Zaleshchenok
     */
    public class SalesMetricsCalculator {
        public List<String> getAllOrderCities(List<Order> orders) {
            return orders.stream()
                    .map(Order::getCustomer)
                    .map(Customer::getCity)
                    .distinct()
                    .collect(Collectors.toList());
        }

        public double getTotalIncome(List<Order> orders) {
            return orders.stream()
                    .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                    .flatMap(order -> order.getItems().stream())
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
        }

        public String getMostPopularProduct(List<Order> orders) {
            return orders.stream()
                    .flatMap(order -> order.getItems().stream())
                    .collect(Collectors.groupingBy(
                            OrderItem::getProductName,
                            Collectors.summingInt(OrderItem::getQuantity)
                    ))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("None");
        }

        public double getAverageCheckForDeliveredOrders(List<Order> orders) {
            return orders.stream()
                    .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                    .mapToDouble(order -> order.getItems().stream()
                                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                                .sum()
                    )
                    .average()
                    .orElse(0);
        }

        public List<Customer> getCustomersWithMoreThanFiveOrders(List<Order> orders) {
            return orders.stream()
                    .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()))
                    .entrySet().stream()
                    .filter(entry -> entry.getValue() > 5)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }
