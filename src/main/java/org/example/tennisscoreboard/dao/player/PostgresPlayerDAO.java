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

    // Слово Postgres не нужно в названии класса. Можно изменить БД и при этом использовать этот класс.

    // TODO: Управление жизненным циклом транзакций разорвано:
        // здесь транзакции создаются и коммитятся, а откатываются в другом месте.
        // Это разрывает ответственность за управление транзакциями на несколько классов и
        // нарушает Принцип единой ответственности (SRP).
        // Ответственность за управление жизненным транзакций должна находиться в одном классе.

    // TODO: Слой DAO не должен управлять транзакциями
        // (см. файл "dao.player.md" в этом же пакете)

    // TODO: В блоках `catch (Exception e)` перехватывается слишком общее исключение
        // (см. файл "dao.player.md" в этом же пакете)

    public PostgresPlayerDAO(SessionFactory sessionFactory, ErrorMapper errorMapper) {
        super(sessionFactory, errorMapper);
    }

    @Override
    public void insert(Player player) {
        try {
            executeInserter(session -> {
                session.beginTransaction();

                // Метод называется insert и по смыслу он должен вставлять новую сущность.
                    // merge() используется для обновления или слияния, а не для явной вставки,
                    // поэтому здесь стоит использовать persist().
                    // Использование merge() для гарантированно новой сущности нарушает принцип наименьшего удивления.
                    // (см. файл "pola.md" в этом же пакете)
                session.merge(player);
                session.getTransaction().commit();
            });
        } catch (Exception e) {
            errorMapper.mapPostgresPlayerDAOInsertError(e);
        }
    }

    // Стоит возвращать Optional<Player>. Optional специально придуман для того, чтобы безопасно (без null)
        // и без исключений вернуть пустой результат в случае отсутствия записи в БД.
    // Код этого метода был бы проще и читался бы лучше без использования Criteria API.
    // Этот метод можно выполнять без транзакции.
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

                // Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит
                //кидает ошибку парень, если результат не найден
                Player player = session.createQuery(criteriaQuery).getSingleResult();

                session.getTransaction().commit();
                return player;
            });

        } catch (Exception e) {
            errorMapper.mapPostgresPlayerDAOFindError(e);

            // Текст сообщения в исключениях принято писать на английском языке.
            throw new DatabaseException("Ошибка базы данных");
        }
    }
}