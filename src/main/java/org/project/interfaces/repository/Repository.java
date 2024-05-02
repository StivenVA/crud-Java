package org.project.interfaces.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Repository<T,M> {
    void save(T entity) throws Exception;
    void deleteById(M id) throws SQLException;
    boolean update(T entity) throws SQLException;
    T findById(M id) throws SQLException;
}
