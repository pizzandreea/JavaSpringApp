package com.soupapp.soup.repositories;

import com.soupapp.soup.models.Order;
import com.soupapp.soup.models.OrderStatus;
import com.soupapp.soup.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderStartedByUserAndStatus(User user, OrderStatus orderStatus);
}
