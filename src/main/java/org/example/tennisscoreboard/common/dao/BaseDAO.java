package org.example.tennisscoreboard.common.dao;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.common.dao.functional.FinderBy;
import org.example.tennisscoreboard.common.dao.functional.Inserter;
import org.example.tennisscoreboard.handler.ErrorMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public abstract class BaseDAO<E> {

    protected final SessionFactory sessionFactory;
    protected final ErrorMapper errorMapper;

    protected void executeInserter(Inserter inserter) {

        try (Session session = sessionFactory.openSession()) {

            try {
                inserter.insert(session);
            } catch (Exception e) {
                Transaction transaction = session.getTransaction();
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    protected E executeFinderBy(FinderBy<E> finderBy) {
        try (Session session = sessionFactory.openSession()) {

            try {
                return finderBy.findBy(session);

            } catch (Exception e) {
                Transaction transaction = session.getTransaction();
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
}
