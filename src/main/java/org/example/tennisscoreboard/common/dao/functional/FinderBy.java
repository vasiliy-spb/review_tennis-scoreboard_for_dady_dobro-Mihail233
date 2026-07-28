package org.example.tennisscoreboard.common.dao.functional;

import org.hibernate.Session;

public interface FinderBy<E> {

    // Можно назвать SingleFinder

    E findBy(Session session);
}
