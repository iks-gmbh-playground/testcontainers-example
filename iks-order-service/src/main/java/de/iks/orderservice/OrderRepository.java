package de.iks.orderservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface OrderRepository extends JpaRepository<Order, Long> {}
