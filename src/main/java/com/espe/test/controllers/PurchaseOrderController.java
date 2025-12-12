package com.espe.test.controllers;

import com.espe.test.models.entities.PurchaseOrder;
import com.espe.test.models.entities.PurchaseOrderStatus;
import com.espe.test.models.entities.Currency;
import com.espe.test.services.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/purchase-orders")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService service;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) BigDecimal minTotal,
            @RequestParam(required = false) BigDecimal maxTotal,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        // Validaciones de parámetros
        final PurchaseOrderStatus statusEnum;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = PurchaseOrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status value");
            }
        } else {
            statusEnum = null;
        }

        final Currency currencyEnum;
        if (currency != null && !currency.isBlank()) {
            try {
                currencyEnum = Currency.valueOf(currency.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid currency value");
            }
        } else {
            currencyEnum = null;
        }

        if (minTotal != null && minTotal.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("minTotal must be >= 0");
        }
        if (maxTotal != null && maxTotal.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("maxTotal must be >= 0");
        }

        final LocalDateTime fromDate;
        if (from != null && !from.isBlank()) {
            try {
                fromDate = LocalDateTime.parse(from);
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid 'from' date format");
            }
        } else {
            fromDate = null;
        }
        final LocalDateTime toDate;
        if (to != null && !to.isBlank()) {
            try {
                toDate = LocalDateTime.parse(to);
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid 'to' date format");
            }
        } else {
            toDate = null;
        }
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'from' must be before or equal to 'to'");
        }

        List<PurchaseOrder> all = service.findAll();
        List<PurchaseOrder> filtered = all.stream()
            .filter(po -> {
                // Filtro q (orderNumber, supplierName, case-insensitive)
                if (q != null && !q.isBlank()) {
                    String qLower = q.toLowerCase();
                    if (!(po.getOrderNumber().toLowerCase().contains(qLower) || po.getSupplierName().toLowerCase().contains(qLower))) {
                        return false;
                    }
                }
                return true;
            })
            .filter(po -> statusEnum == null || po.getStatus() == statusEnum)
            .filter(po -> currencyEnum == null || po.getCurrency() == currencyEnum)
            .filter(po -> minTotal == null || po.getTotalAmount().compareTo(minTotal) >= 0)
            .filter(po -> maxTotal == null || po.getTotalAmount().compareTo(maxTotal) <= 0)
            .filter(po -> {
                if (fromDate != null && toDate != null) {
                    return !po.getCreatedAt().isBefore(fromDate) && !po.getCreatedAt().isAfter(toDate);
                } else if (fromDate != null) {
                    return !po.getCreatedAt().isBefore(fromDate);
                } else if (toDate != null) {
                    return !po.getCreatedAt().isAfter(toDate);
                }
                return true;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/{id}")
    public PurchaseOrder getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public PurchaseOrder create(@RequestBody PurchaseOrder purchaseOrder) {
        return service.save(purchaseOrder);
    }

    @PutMapping("/{id}")
    public PurchaseOrder update(@PathVariable Long id, @RequestBody PurchaseOrder purchaseOrder) {
        purchaseOrder.setId(id);
        return service.save(purchaseOrder);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
