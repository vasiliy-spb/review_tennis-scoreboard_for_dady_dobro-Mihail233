package org.example.tennisscoreboard.common.dao.functional;

import org.hibernate.Session;

public interface FinderBy<E> {
    E findBy(Session session);
}
