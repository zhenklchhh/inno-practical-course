package com.innowise.datastructure;

import java.time.LocalDateTime;

/**
 * @author Evgeniy Zaleshchenok
 */
public class Customer {
    private String customerId;
    private String name;
    private String email;
    private LocalDateTime registeredAt;
    private int age;
    private String city;

    public Customer(String customerId, String name, String email, LocalDateTime registeredAt, int age, String city) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.registeredAt = registeredAt;
        this.age = age;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
