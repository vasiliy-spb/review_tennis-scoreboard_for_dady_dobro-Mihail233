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

    // Вместо модификаторов protected лучше сделать поля private и предоставить protected геттеры к ним.
        // (см. файл "fragile-base-class.md" в этом же пакете)

    // TODO: Класс использует `sessionFactory.openSession()` для получения сессии. Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию")
        // (см. файл "dao.md" в этом же пакете)

    // TODO: В блоках `catch` вызов `transaction.rollback()` не обёрнут в `try-catch`.
        // (см. файл "dao.md" в этом же пакете)

    // TODO: Слой DAO не должен управлять транзакциями самостоятельно
        // (см. файл "dao.md" в этом же пакете)

    // TODO: Управление жизненным циклом транзакций разорвано:
        // здесь транзакции получаются из сессии и откатываются,
        // а открываются и коммитятся в другом месте.
        // Это нарушение принципа единой ответственности (SRP)
        // Ответственность за управление жизненным транзакций должна находиться в одном классе.
        //
        // Также при таком подходе наследники вынуждены знать внутренние детали управления транзакциями
        // (когда вызывать beginTransaction, когда commit).
        // Это повышает связность между базовым и дочерними классами, а также снижает переиспользуемость.
        // Если логика транзакций изменится (например, понадобится добавить уровень изоляции),
        // придётся править все наследники, а не только базовый класс.

    protected final SessionFactory sessionFactory;
    protected final ErrorMapper errorMapper;

    protected void executeInserter(Inserter inserter) {

        try (Session session = sessionFactory.openSession()) {

            try {
                inserter.insert(session);
            } catch (Exception e) {
                Transaction transaction = session.getTransaction();

                // TODO: Перед откатом транзакции надо проверить, что она активна (isActive())
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

                // TODO: Перед откатом транзакции надо проверить, что она активна (isActive())
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
}
