package org.example.tennisscoreboard.common.functionalinterface;

import org.hibernate.Session;

public interface FinderBy<E> {
    E findBy(Session session);
}
