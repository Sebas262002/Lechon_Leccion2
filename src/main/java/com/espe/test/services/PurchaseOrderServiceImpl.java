package com.espe.test.services;

import com.espe.test.models.entities.PurchaseOrder;
import com.espe.test.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository repository;

    @Override
    public List<PurchaseOrder> findAll() {
        return repository.findAll();
    }

    @Override
    public PurchaseOrder findById(Long id) {
        Optional<PurchaseOrder> result = repository.findById(id);
        return result.orElse(null);
    }

    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        return repository.save(purchaseOrder);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
