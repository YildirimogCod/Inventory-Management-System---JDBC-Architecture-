package com.inventorySystem;

import com.inventorySystem.config.DatabaseConfig;
import com.inventorySystem.entity.Product;
import com.inventorySystem.repository.impl.ProductRepository;
import com.inventorySystem.service.impl.OrderService;

import java.math.BigDecimal;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        ProductRepository productRepo = new ProductRepository();

        // TEST HAZIRLIĞI: Veritabanına bir ürün ekleyelim (Eğer daha önce eklemediysen)
        try (Connection conn = DatabaseConfig.getConnection()) {
            Product p = new Product();
            p.setName("Akıllı Telefon");
            p.setPrice(new BigDecimal("5000"));
            p.setStock(10);
            productRepo.save(p, conn);
            System.out.println("Test ürünü eklendi. ID: " + p.getId());

            // --- SENARYO 1: Başarılı Sipariş ---
            System.out.println("Senaryo 1 Başlatılıyor: 3 adet sipariş veriliyor...");
            orderService.placeOrder(p.getId(), 3);
            // Beklenen: Stok 7'ye düşmeli, orders tablosuna kayıt gelmeli.

            // --- SENARYO 2: Yetersiz Stok ---
            System.out.println("\nSenaryo 2 Başlatılıyor: 15 adet sipariş veriliyor (Hata bekleniyor)...");
            orderService.placeOrder(p.getId(), 15);
            // Beklenen: RuntimeException fırlamalı, stok hala 7 kalmalı (Rollback).

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
