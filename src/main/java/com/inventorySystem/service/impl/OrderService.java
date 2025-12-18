package com.inventorySystem.service.impl;

import com.inventorySystem.config.DatabaseConfig;
import com.inventorySystem.entity.Order;
import com.inventorySystem.entity.Product;
import com.inventorySystem.repository.impl.OrderRepository;
import com.inventorySystem.repository.impl.ProductRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class OrderService {
    private final ProductRepository productRepo = new ProductRepository();
    private final OrderRepository orderRepo = new OrderRepository();

    public void placeOrder(Long product_id,Integer quantity){
        Connection connection = null;
        try {
            connection= DatabaseConfig.getConnection();

            Product product = productRepo.findById(product_id,connection);
            if (product == null) {
                throw new RuntimeException("Ürün bulunamadı!");
            }
            if (product.getStock() < quantity) {
                throw new RuntimeException("Stok yetersiz! Mevcut: " + product.getStock());
            }

            connection.setAutoCommit(false);

            productRepo.updateStock(product_id,quantity,connection);
            Order order = new Order();
            order.setProductId(product_id);
            order.setQuantity(quantity);
            orderRepo.save(order,connection); // Siparişi kaydet
            connection.commit(); // Onayla
            System.out.println("Sipariş başarıyla oluşturuldu.");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
