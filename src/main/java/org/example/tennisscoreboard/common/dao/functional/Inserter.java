package org.example.tennisscoreboard.common.dao.functional;

import org.hibernate.Session;

public interface Inserter {
    void insert(Session session);
}
