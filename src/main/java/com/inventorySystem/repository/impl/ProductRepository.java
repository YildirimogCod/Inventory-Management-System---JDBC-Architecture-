package com.inventorySystem.repository.impl;

import com.inventorySystem.entity.Product;
import com.inventorySystem.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements Repository<Product> {
    @Override
    public void save(Product product, Connection connection) throws SQLException {
        String sql = """
                INSERT INTO products
                (name,price,stock)
                VALUES(?,?,?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setInt(3, product.getStock());

            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1)); // Java nesnesini güncelledik
                } else {
                    throw new SQLException("Ürün oluşturuldu ancak ID alınamadı.");
                }
            }
            System.out.println("Ürün kaydedildi, yeni ID: " + product.getId());
        }
    }
    @Override
    public Product findById(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ? ";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    Product product = new Product();
                    product.setId(rs.getLong("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setStock(rs.getInt("stock"));
                    return product;
                }
            }
        }
        //! kayıt yoksa null
        return null;
    }

    @Override
    public List<Product> findAll(Connection connection) throws SQLException {
        String sql = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setStock(rs.getInt("stock"));
                products.add(product);
            }
        }

        return products;
    }
    public void updateStock(Long productId,Integer quantity,Connection connection)  throws SQLException{
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,quantity);
            ps.setLong(2,productId);
            int affectedRows = ps.executeUpdate();
            //! Eğer hiçbir satır güncellenmediyse (id yanlışsa veya stok yetersizse)
            //! bir hata fırlatmalıyız ki Service katmanı bunu duyup rollback yapabilsin.
            if (affectedRows == 0){
                throw new SQLException("Stock güncellenemedi. " + productId);
            }
            System.out.println("Stok başarıyla düşürüldü. Ürün ID: " + productId);
        }
    }
}
