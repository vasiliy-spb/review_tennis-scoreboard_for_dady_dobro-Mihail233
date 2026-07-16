package org.example.tennisscoreboard.dao.player;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.tennisscoreboard.common.dao.BaseDAO;
import org.example.tennisscoreboard.common.dao.DAO;
import org.example.tennisscoreboard.entity.Player;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class H2PlayerDAO extends BaseDAO<Player> implements DAO<Player, Player> {
    //Supplier, Consumer, Function

    public H2PlayerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void insert(Player player) {
        //здесь сначала прилетает ошибка на catch родителя а потом сюда(если сделать try catch)
        try {
            insertData(session -> {
                session.beginTransaction();
                session.merge(player);
                session.getTransaction().commit();
            });
        } catch (Exception e) {
            int i = 0;
            throw new RuntimeException("test3");
        }
    }

    @Override
    public Player find(String playerName) {
        try {
            return findData(session -> {
                session.beginTransaction();

                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
                Root<Player> playerRoot = criteriaQuery.from(Player.class);

                criteriaQuery.select(playerRoot).where(
                        criteriaBuilder.equal(playerRoot.get("name"), playerName));

                Player player = session.createQuery(criteriaQuery).getSingleResult();

                session.getTransaction().commit();
                return player;
            });

        } catch (Exception e) {
            //ИДЕЯ КАСТОМИЗАЦИИ ОШИБОК для SERVLETS
            //словили ошибку необходимо ее здесь словить и перебросить нужную
            //проброс в baseDao
            throw new RuntimeException("test2");
        }
    }
}