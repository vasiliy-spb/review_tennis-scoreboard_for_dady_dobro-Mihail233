package org.example.tennisscoreboard.dao.player;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.common.dao.BaseDAO;
import org.example.tennisscoreboard.common.dao.DAO;
import org.example.tennisscoreboard.entity.Player;
import org.example.tennisscoreboard.exception.DatabaseException;
import org.example.tennisscoreboard.handler.ErrorMapper;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class PostgresPlayerDAO extends BaseDAO<Player> implements DAO<Player, Player> {

    public PostgresPlayerDAO(SessionFactory sessionFactory, ErrorMapper errorMapper) {
        super(sessionFactory, errorMapper);
    }

    @Override
    public void insert(Player player) {
        try {
            executeInserter(session -> {
                session.beginTransaction();
                session.merge(player);
                session.getTransaction().commit();
            });
        } catch (Exception e) {
            errorMapper.mapPostgresPlayerDAOInsertError(e);
        }
    }

    @Override
    public Player find(String playerName) {
        try {
            return executeFinderBy(session -> {
                session.beginTransaction();

                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
                Root<Player> playerRoot = criteriaQuery.from(Player.class);

                criteriaQuery.select(playerRoot).where(
                        criteriaBuilder.equal(playerRoot.get("name"), playerName));

                //кидает ошибку парень, если результат не найден
                Player player = session.createQuery(criteriaQuery).getSingleResult();

                session.getTransaction().commit();
                return player;
            });

        } catch (Exception e) {
            errorMapper.mapPostgresPlayerDAOFindError(e);
            throw new DatabaseException("Ошибка базы данных");
        }
    }
}