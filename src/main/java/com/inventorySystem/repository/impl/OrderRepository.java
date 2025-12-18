package com.inventorySystem.repository.impl;

import com.inventorySystem.entity.Order;
import com.inventorySystem.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements Repository<Order> {

    @Override
    public void save(Order entity, Connection connection) throws SQLException {
        String sql = """
        INSERT INTO orders
        (product_id,quantity)
        VALUES(?,?)
    """;
        try(PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setLong(1,entity.getProductId());
            ps.setInt(2,entity.getQuantity());
            ps.executeUpdate();

            try(ResultSet generatedKey = ps.getGeneratedKeys()){
                if (generatedKey.next()){
                    entity.setId(generatedKey.getLong(1));
                } else {
                    throw new SQLException("Ürün oluşturuldu ancak ID alınamadı.");
                }
            }
            System.out.println("Sipariş kaydedildi, yeni ID: " + entity.getId());
        }
    }

    @Override
    public Order findById(Long id, Connection connection) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setProductId(rs.getLong("productId"));
                    order.setQuantity(rs.getInt("quantity"));
                    return order;
                }
            }
        }
        return null;
    }

    @Override
    public List<Order> findAll(Connection connection) throws SQLException {
       String sql = "SELECT * FROM orders";
       List<Order> orders = new ArrayList<>();
       try (PreparedStatement ps = connection.prepareStatement(sql);
           ResultSet rs = ps.executeQuery()){
           while (rs.next()){
               Order order = new Order();
               order.setId(rs.getLong("id"));
               order.setProductId(rs.getLong("productId"));
               order.setQuantity(rs.getInt("quantity"));
               orders.add(order);
           }
       }
       return orders;
    }

}
