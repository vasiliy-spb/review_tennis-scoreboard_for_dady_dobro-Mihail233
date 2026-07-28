package org.example.tennisscoreboard.common.dao.functional;

import org.hibernate.Session;

public interface CounterRecords {

    // Можно просто Counter

    // Использование `long` для счётчиков, получаемых из БД, является более правильной практикой, так как SQL-функция `COUNT` возвращает 64-битное число.
    int count(Session session);
}
