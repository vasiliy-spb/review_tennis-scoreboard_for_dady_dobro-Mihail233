package org.example.tennisscoreboard.dao.match;

import org.example.tennisscoreboard.common.dao.BaseDAO;
import org.example.tennisscoreboard.common.dao.ExtendedDAO;
import org.example.tennisscoreboard.common.dao.functional.CounterRecords;
import org.example.tennisscoreboard.entity.Match;
import org.example.tennisscoreboard.exception.DatabaseException;
import org.example.tennisscoreboard.handler.ErrorMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.tennisscoreboard.service.FinishedMatchesFindingService.DEFAULT_PAGE_SIZE;

@Component
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {

    public PostgresFinishedMatchDAO(SessionFactory sessionFactory, ErrorMapper errorMapper) {
        super(sessionFactory, errorMapper);
    }

    @Override
    public void insert(Match match) {
        try {
            executeInserter(session -> {
                session.beginTransaction();
                session.merge(match);
                session.getTransaction().commit();
            });
        } catch (Exception e) {
            throw new DatabaseException("Ошибка базы данных");
        }
    }

    @Override
    public List<Match> find(String offset) {
        try {
            return executeFinderBy(session -> {
                Query<Match> result = session.createQuery(
                                """
                                            select m from Match m
                                                join fetch m.playerOne p1
                                                join fetch m.playerTwo p2
                                                join fetch m.winner w
                                                order by m.id asc
                                        """, Match.class)
                        .setMaxResults(DEFAULT_PAGE_SIZE)
                        .setFirstResult(Integer.parseInt(offset));

                return result.list();
            });

        } catch (Exception e) {
            throw new DatabaseException("Ошибка базы данных");
        }
    }

    @Override
    public List<Match> findMatchesByNameAndPage(String playerName, String offset) {
        try {
            return executeFinderBy((session) -> {
                session.beginTransaction();

                Query<Match> recordCount = session.createQuery(
                                """
                                               select m from Match m
                                               join fetch m.playerOne p1
                                               join fetch m.playerTwo p2
                                               join fetch m.winner w
                                               where playerOne.name = :playerOne or playerTwo.name = :playerTwo
                                               order by m.id asc
                                        """, Match.class)
                        .setParameter("playerOne", playerName)
                        .setParameter("playerTwo", playerName)
                        .setMaxResults(DEFAULT_PAGE_SIZE)
                        .setFirstResult(Integer.parseInt(offset));

                session.getTransaction().commit();
                return recordCount.list();
            });
        } catch (Exception e) {
            throw new DatabaseException("Ошибка базы данных");
        }
    }

    @Override
    public int countAllMatches() {
        try {
            return executeCounterRecords((session) -> {
                session.beginTransaction();
                Query<Long> recordCount = session.createQuery("select count(m) from Match m", Long.class);
                session.getTransaction().commit();

                return recordCount.getSingleResult().intValue();
            });
        } catch (Exception e) {
            throw new DatabaseException("Ошибка базы данных");
        }
    }

    @Override
    public int countAllMatchesByPlayerName(String playerName) {
        try {
            return executeCounterRecords((session) -> {
                session.beginTransaction();

                Query<Long> recordCount = session.createQuery(
                                """
                                        select count(m) from Match m
                                        where playerOne.name = :playerOne or playerTwo.name = :playerTwo
                                        """, Long.class)
                        .setParameter("playerOne", playerName)
                        .setParameter("playerTwo", playerName);

                session.getTransaction().commit();
                return recordCount.getSingleResult().intValue();
            });
        } catch (Exception e) {
            throw new DatabaseException("Ошибка базы данных");
        }
    }

    private int executeCounterRecords(CounterRecords counterRecords) {
        try (Session session = sessionFactory.openSession()) {

            try {
                return counterRecords.count(session);
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
