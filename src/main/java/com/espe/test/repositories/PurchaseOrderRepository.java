package com.espe.test.repositories;

import com.espe.test.models.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    boolean existsByOrderNumber(String orderNumber);
}
