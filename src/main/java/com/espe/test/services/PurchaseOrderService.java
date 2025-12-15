package com.espe.test.services;

import com.espe.test.models.entities.PurchaseOrder;
import java.util.List;

public interface PurchaseOrderService {
    List<PurchaseOrder> findAll();
    PurchaseOrder findById(Long id);
    PurchaseOrder save(PurchaseOrder purchaseOrder);
    void deleteById(Long id);
}
