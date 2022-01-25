package com.example.shop.repository;

import com.example.shop.domain.Order;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    public final EntityManager em;

    public Order save(Order order){
        em.persist(order);
        return order;
    }

    public Optional<Order> findById(Long id){
        Order order = em.find(Order.class,id);
        return Optional.ofNullable(order);
    }

    public List<Order> findAll(){
        return em.createQuery("select o from Order o").getResultList();
    }

}
