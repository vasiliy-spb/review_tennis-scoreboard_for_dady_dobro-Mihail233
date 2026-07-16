package org.example.tennisscoreboard.common.functionalinterface;

import org.hibernate.Session;

public interface Inserter {
    void insert(Session session);
}
