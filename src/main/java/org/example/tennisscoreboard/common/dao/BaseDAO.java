package org.example.tennisscoreboard.common.dao;

import org.example.tennisscoreboard.common.functionalinterface.FinderBy;
import org.example.tennisscoreboard.common.functionalinterface.Inserter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class BaseDAO<E> {
    protected final SessionFactory sessionFactory;

    public BaseDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    protected void insertData(Inserter inserter) {

        try (Session session = sessionFactory.openSession()) {

            try {
                inserter.insert(session);
            } catch (Exception e) {
                Transaction transaction = session.getTransaction();
                if (transaction != null) {
                    transaction.rollback();
                }
//                throw new TestException("test3");
                //кинуть ошибку здесь, которую словили(она должна быть runtime)
            }
        }
    }

    protected E findData(FinderBy<E> finderBy) {
        try (Session session = sessionFactory.openSession()) {

            try {
                return finderBy.findBy(session);

            } catch (Exception e) {
                Transaction transaction = session.getTransaction();
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new RuntimeException("test");
                //кинуть ошибку здесь, которую словили(она должна быть runtime)
            }
        }
    }
}
