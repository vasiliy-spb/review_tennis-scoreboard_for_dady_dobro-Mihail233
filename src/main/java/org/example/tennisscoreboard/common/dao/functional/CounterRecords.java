package org.example.tennisscoreboard.common.dao.functional;

import org.hibernate.Session;

public interface CounterRecords {
    int count(Session session);
}
