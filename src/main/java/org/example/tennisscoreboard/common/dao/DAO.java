package org.example.tennisscoreboard.common.dao;

public interface DAO<K, E>  {
    void insert(K entity);
    E find(String criterion);
}
