package org.example.tennisscoreboard.common.functionalinterface;

import org.hibernate.Session;

public interface CounterAllRecords {
    int count(Session session);
}
