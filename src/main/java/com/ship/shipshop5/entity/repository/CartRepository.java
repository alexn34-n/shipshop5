package com.ship.shipshop5.entity.repository;

import com.ship.shipshop5.entity.Cart;
import com.ship.shipshop5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    @Query("select c from Cart c where  c.order is null")
    Optional<Cart> findCarByOrderIdIsNull();

    @Query("select c from  Cart c where c.user=:user and c.order is null")
    Optional<Cart> findByUser(User user);
}
//