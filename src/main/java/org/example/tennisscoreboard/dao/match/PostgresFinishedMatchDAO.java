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

    // Слово Postgres не нужно в названии класса. Можно изменить БД и при этом использовать этот класс.

    // Использование List для параметризации класса неидиоматично. Обычно параметрами выступают сущность и её ID.

    // Лучше вынести тексты HQL запросов в `private static final` константы и дать им понятные имена.

    // Ключевые слова в тексте HQL-запросов (`from`, `where` и др.) написаны в нижнем регистре.
        // Хотя это и не влияет на работоспособность, написание ключевых слов SQL/HQL в верхнем регистре (`UPPERCASE`) является общепринятым стандартом.
        // Это значительно улучшает читаемость запросов, так как визуально отделяет синтаксические конструкции языка от имён сущностей и полей.

    // Для единообразия кодовой базы в этом классе тоже можно использовать Criteria API (как в PostgresPlayerDAO)

    // В HQL запросах используется JOIN FETCH, что эквивалентно 'INNER JOIN' в SQL.
        //
        // `INNER JOIN` вернёт только те записи о матчах, у которых все связанные сущности (`player1`, `player2`)
        // гарантированно существуют в базе. Если по какой-либо причине (например, ошибка при импорте или
        // ручное вмешательство) в таблице `matches` окажется запись со значением `NULL` в колонке `player1`,
        // то такой матч будет молчаливо исключён из выборки.
        //
        // `LEFT JOIN` является более безопасным подходом:
            //  - Он вернёт все матчи, даже если у них нарушена связь с игроком.
            //  - Это позволит приложению либо упасть с `NullPointerException` (что явно укажет на проблему
                //  с целостностью данных), либо корректно обработать такую ситуацию, если она допустима.
                //  "Падать громко и рано" часто лучше, чем молча скрывать проблемы.
        //
        // Стоит заменить `JOIN FETCH` на `LEFT JOIN FETCH` для обоих игроков и победителя
        // для большей устойчивости запроса к потенциально некорректным данным.
        //
        // (см. файл "join-fetch-left-join-fetch.md" в этом же пакете)

    // Название каждого именованного параметра тоже лучше вынести в константу с понятным названием.

    // TODO: В блоках `catch (Exception e)` перехватывается слишком общее исключение
        // (см. файл "dao.match.md" в этом же пакете)

    // TODO: Слой DAO не должен управлять транзакциями
        // (см. файл "dao.match.md" в этом же пакете)

    public PostgresFinishedMatchDAO(SessionFactory sessionFactory, ErrorMapper errorMapper) {
        super(sessionFactory, errorMapper);
    }

    @Override
    public void insert(Match match) {
        try {
            executeInserter(session -> {
                session.beginTransaction();

                // Метод называется insert и по смыслу он должен вставлять новую сущность.
                    // merge() используется для обновления или слияния, а не для явной вставки,
                    // поэтому здесь стоит использовать persist().
                    // Использование merge() для гарантированно новой сущности нарушает принцип наименьшего удивления.
                    // (см. файл "pola.md" в этом же пакете)
                session.merge(match);
                session.getTransaction().commit();
            });
        } catch (Exception e) {

            // Текст сообщения в исключениях принято писать на английском языке.
            throw new DatabaseException("Ошибка базы данных");
        }
    }

    // Параметр offset должен приниматься числом, а не строкой. DAO не должен заниматься парсингом.
    // Параметр limit (DEFAULT_PAGE_SIZE) стоит принимать в качестве аргумента, чтобы не привязываться к константе конкретного сервиса.
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

            // Текст сообщения в исключениях принято писать на английском языке.
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

            // Текст сообщения в исключениях принято писать на английском языке.
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

            // Текст сообщения в исключениях принято писать на английском языке.
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

            // Текст сообщения в исключениях принято писать на английском языке.
            throw new DatabaseException("Ошибка базы данных");
        }
    }

    private int executeCounterRecords(CounterRecords counterRecords) {
        try (Session session = sessionFactory.openSession()) {

            try {
                return counterRecords.count(session);
            } catch (Exception e) {
                Transaction transaction = session.getTransaction();

                // TODO: Перед откатом транзакции надо проверить, что она активна (isActive())
                if (transaction != null) {

                    // TODO: Вызов `transaction.rollback()` не обёрнут в `try-catch` (см. файл "dao.match.md" в этом же пакете)
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
}
