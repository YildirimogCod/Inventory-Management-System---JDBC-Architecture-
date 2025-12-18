package com.inventorySystem.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Repository<T> {
    void save(T entity, Connection connection) throws SQLException;
    T findById(Long id, Connection conn) throws SQLException;
    List<T> findAll(Connection connection) throws  SQLException;

}
