# Сводный отчёт по код-ревью проекта `tennis-scoreboard`

# Review на реализацию от [@dady_dobro](https://github.com/Mihail233/TennisScoreboardV2) проекта [Табло теннисного матча](https://zhukovsd.github.io/java-backend-learning-course/projects/tennis-scoreboard/)

> Ревью выполнено в формате fork-а репозитория и добавления комментариев 'In-Situ' и обучающих заметок в формате markdown.

К классам и файлам конфигурации добавлены подробные комментарии. 

Там же есть ссылки на файлы, объясняющие некоторые особо значимые темы. 

Сами файлы с заметками находятся в одном пакете с классами, к которым относится тема заметки.

```
В `TODO`-комментариях️ описаны критически важные замечания, а также места нарушения ТЗ.
```

Список всех `TODO`-комментариев сгруппированных по пакетам и классам можно посмотреть в Idea через меню: `View` -> `Tool Windows` -> `TODO`

Читать стоит в таком порядке:

1. В файле `1-functional-overview.md` описан функциональный обзор приложения.

2. В файле `2-refactoring-roadmap.md` находится план работы над исправлениями.

- В файле `Common.md` описаны замечания, относящиеся ко всему проекту или нескольким его частям. (можно читать в любое время)

- В файле `Pluses.md` описаны наиболее значимые плюсы. (можно читать в любое время)

- В файле `Conclusion.md` находится заключение. (можно читать в любое время)

PS: Комментарий в `application.properties` написан на английском из-за кодировки таких файлов.

```text
Знаком ❗️ помечены критически важные замечания, а также места нарушения ТЗ.
```

## Функциональный обзор

- Сейчас можно создать игроков с такими именами:

![](images/Screenshot_01.png)


Раз в проекте есть валидация, стоит добавить проверку, что в имени есть буквы и ввести другие уместные ограничения.

- Валидировать длину имени стоит после обрезки пробелов по краям. Сейчас можно создать игроков с такими именами:

![](images/Screenshot_02.png)

- Надпись "Score" на кнопках можно выделить как простой текст. Это можно исправить в css.

![](images/Screenshot_03.png)

- После завершения матча стоит сделать кнопки "Score" неактивными.

Сейчас их можно нажимать, что приводит к сообщению об ошибке:

![](images/Screenshot_04.png)

- Последний сыгранный матч отображается последним в списке на странице завершённых матчей — чтобы посмотреть его результат в таблице надо листать до последней страницы. Лучше, чтобы последний завершённый отображался первым в списке (на первой странице).

- При фильтрации по пустой строке сейчас не показывается ни один матч:

![](images/Screenshot_05.png)

Вместо этого можно считать это за отсутствие фильтра и показывать все матчи. 

- Когда фильтр по имени не применён, можно не показывать кнопку сброса фильтра

## Конфигурационные файлы

### pom.xml

Как и из кода, комментарии из файлов конфигурации стоит удалять перед коммитом.
```xml
<!-- Как и из кода, комментарии из файлов конфигурации стоит удалять перед коммитом -->
<!--    <spring.version>5.3.29</spring.version>-->
<spring.version>7.0.7</spring.version>
```
***

Версии других зависимостей (особенно повторяющиеся) тоже лучше вынести в properties.
```xml
<org.mapstruct.version>1.5.5.Final</org.mapstruct.version>
<!-- Версии других зависимостей (особенно повторяющиеся) тоже лучше вынести в properties -->
```
***

В зависимостях этой версии обнаружены уязвимости — стоит использовать более свежую версию.
```xml
<artifactId>jackson-databind</artifactId>

<!-- В зависимостях этой версии обнаружены уязвимости — стоит использовать более свежую версию -->
<version>2.21.0</version>
```
***

### application.properties

!The 'db.driver' property contains datasource value. It should be like this: db.driver=org.postgresql.Driver
```properties
# !The 'db.driver' property contains datasource value. It should be like this:
# db.driver=org.postgresql.Driver
db.driver=org.postgresql.ds.PGSimpleDataSource
```
***

## org.example.tennisscoreboard.application

```text
Знаком ❗️ помечены критически важные замечания, а также места нарушения ТЗ.
```

## application

- ❗️В пакете отсутствуют интерфейсы для сервисных классов. Все классы являются конкретными реализациями, от которых напрямую зависят другие компоненты приложения (например, контроллеры).

Почему это проблема:

  - Нарушение Принципа инверсии зависимостей (Dependency Inversion Principle): Принцип гласит, что модули верхних уровней не должны зависеть от модулей нижних уровней, а также они должны зависеть от абстракций. В данном случае вышестоящие модули (контроллеры) напрямую зависят от конкретных реализаций сервисов, что делает систему жёстко связанной и хрупкой.

  - Низкая тестируемость: Невозможно провести полноценное модульное тестирование компонентов, которые зависят от этих сервисов. Например, чтобы протестировать контроллеры, использующий `MatchApplicationService`, необходимо создавать полный экземпляр этого сервиса со всеми его реальными зависимостями, что превращает модульный тест в сложный интеграционный.

  - Низкая гибкость и невозможность расширения: Если потребуется создать альтернативную реализацию какого-либо сервиса, это потребует изменения кода во всех местах, где использовалась оригинальная реализация.

  - В классе-реализации публичные методы могут смешиваться с его внутренними или вспомогательными методами. Интерфейс же служит чётким, явным контрактом, который показывает, что сервис предоставляет внешнему миру, скрывая детали его внутренней работы.

Для каждого класса в этом пакете стоит создать интерфейс, который будет определять его публичный контракт, и изменить все зависимые классы так, чтобы они использовали этот интерфейс.

### MatchApplicationService

Не понятна роль слова Application в названии класса. Можно просто MatchService.
```java
public class MatchApplicationService {
```
***

Класс стоит перенести в пакет service к другим сервисам.
```java
public class MatchApplicationService {
```
***

Нет интерфейса для этого класса. (см. файл "application.md" в этом же пакете)
```java
public class MatchApplicationService {
```
***

Класс нарушает Принцип единой ответственности и работает как текущими, так и с завершёнными матчами. В проекте есть FinishedMatchesPersistenceService, поэтому работу с завершёнными матчами стоит оставить ему.
```java
public class MatchApplicationService {
```
***

Можно назвать getOngoingMatchScore.
```java
public TennisMatchResponse getGeneralScore(String uuid) {
```
***

Этот метод должен принимать готовый объект UUID, а не парсить его из строки.
```java
public TennisMatchResponse getGeneralScore(String uuid) {
```
***

Этот метод должен принимать готовый объект UUID, а не парсить его из строки.
```java
public TennisMatchResponse awardPointAndGetGeneralScore(String uuid, PointAwardingRequest pointAwardingRequest) {
```
***

Можно назвать просто awardPoint.
```java
public TennisMatchResponse awardPointAndGetGeneralScore(String uuid, PointAwardingRequest pointAwardingRequest) {
```
***

Race condition при обработке выигранного очка. Если пользователь очень быстро нажмёт кнопку выигрыша очка, браузер отправит два POST-запроса почти одновременно. Tomcat обработает эти два запроса в двух разных потоках, но так как оба потока будут работать с одним и тем же общим объектом `TennisMatch`, будет возникать ситуация, когда счёт изменится только один раз. Чтобы это исправить, нужно гарантировать, что только один поток может изменять состояние конкретного матча в один момент времени.
```java
public TennisMatchResponse awardPointAndGetGeneralScore(String uuid, PointAwardingRequest pointAwardingRequest) {
```
***

Проверка на то, что матч завершён перед удалением должна происходить в этом сервисе. Сейчас этот сервис удаляет матч из хранилища в памяти и сохраняет в БД после каждого очка. Реального удаления и сохранения не происходит только потому, что другие сервисы берут на себя лишнюю ответственность по проверке условий удаления/сохранения.
```java
ongoingMatchesDomainService.deleteFinishedMatch(uuid);
finishedMatchesPersistenceService.addFinishedMatch(tennisMatch);
```
***

Номер страницы стоит парсить как можно ближе ко входу этих данных в приложение (в контроллере), а в метод сервиса принимать уже int.
```java
public FinishedMatchesResponse getFinishedMatches(String pageFromUser, String playerName) {
```
***

### PlayerApplicationService

Не понятна роль слова Application в названии класса. Можно просто PlayerService.
```java
public class PlayerApplicationService {
```
***

Класс стоит перенести в пакет service к другим сервисам.
```java
public class PlayerApplicationService {
```
***

Если потребуется дописать функционал для игры пара на пару, то игроков станет 4 и придётся переписывать код этого класса, хотя логика сохранения игроков не изменится. Нужно, чтобы метод, сохраняющий игрока в БД сохранял только одного игрока, а клиентский код пусть вызывает для нужного количества игроков.
```java
public class PlayerApplicationService {
```
***

Нет интерфейса для этого класса. (см. файл "application.md" в этом же пакете)
```java
public class PlayerApplicationService {
```
***

Стоит удалять комментарии (вроде тех, что указаны в следующих 3-х строках) из кода перед тем, как выполнять коммит.
```java
// Стоит удалять комментарии (вроде тех, что указаны в следующих 3-х строках) из кода перед тем, как выполнять коммит
//если 2 пользователя исп app параллельно,
//у первого при select будет null, второй пользователь вставит в этот момент player,
//первый попытается вставить и у него будет exception
```
***

Название findParticipants вводит в заблуждение. Метод не ищет (не должен искать) игроков, а создаёт и сохраняет их в БД.
```java
public Participants findParticipants(String firstPlayerName, String secondPlayerName) {
```
***

Создание обоих игроков должно происходить в одной транзакции, которая будет откатываться, если хотя бы один игрок не будет создан. То есть транзакция должна оборачивать метод, который вызывает findParticipants, чтобы оба его вызова были в одной транзакции.
```java
public Participants findParticipants(String firstPlayerName, String secondPlayerName) {
```
***

При использовании persist() в dao.insert() после сохранения объектов firstPlayer и secondPlayer ID в них подставится автоматически и можно будет избавиться от этих лишних запросов на поиск.
```java
// При использовании persist() в dao.insert() после сохранения объектов firstPlayer и secondPlayer ID в них
    // подставится автоматически и можно будет избавиться от этих лишних запросов на поиск.
firstPlayer = dao.find(firstPlayerName);
secondPlayer = dao.find(secondPlayerName);
```
***

Логику преобразования Player —> Participant можно вынести в маппер.
```java
// Логику преобразования Player —> Participant можно вынести в маппер
Participant firstParticipant = Participant.createParticipant(firstPlayer.getId(), firstPlayer.getName());
Participant secondParticipant = Participant.createParticipant(secondPlayer.getId(), secondPlayer.getName());
```
***

## org.example.tennisscoreboard.common.dao

```text
Знаком ❗️ помечены критически важные замечания, а также места нарушения ТЗ.
```

## dao

- Пакет dao стоит вынести на уровень выше. Пакет common добавляет лишний уровень вложенности и не несёт ясной смысловой нагрузки.

- ❗️Сейчас слой DAO управляет транзакциями самостоятельно:

Границы транзакции должны определяться бизнес-операцией, а не технической операцией доступа к данным. Бизнес-операция может включать в себя несколько вызовов DAO (например, сохранить игрока, а затем обновить матч). Все эти вызовы должны выполняться в рамках одной транзакции. Когда DAO сам управляет транзакцией, это становится невозможным. Ответственность за управление транзакциями должна лежать на сервисном слое, поэтому стоит перенести управление транзакциями именно в него.

В проекте на Spring можно управлять транзакциями через аннотации.

- Классы DAO используют `sessionFactory.openSession()` для получения сессии.

```java
public <T> T executeInTransaction(Function<Session, T> work) {
    // ...
    try (Session session = sessionFactory.openSession()) {
        // ...
    }
}
```

Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию"), который имеет два критических недостатка:

  - Низкая производительность: Создание объекта `Session` в Hibernate — это относительно "дорогая" операция. Она включает в себя получение соединения с базой данных из пула, инициализацию кэшей и тд. Создавать и уничтожать сессию при каждом вызове метода DAO неэффективно и создаёт лишнюю нагрузку.

  - Невозможность управления транзакциями: Самое главное — этот подход делает невозможным объединение нескольких операций DAO в одну бизнес-транзакцию. Например, если сервису нужно сохранить двух игроков, каждый вызов `playerRepository.save()` будет выполнен в отдельной, независимой транзакции. Если второй вызов не удастся, первый уже будет закоммичен, что нарушает атомарность бизнес-операции и приводит к несогласованности данных.

Один из вариантов исправления — перейти на паттерн "Session-per-Request" ("сессия на запрос"). Его суть в том, что одна сессия Hibernate используется всеми сервисами и DAO на протяжении всей обработки HTTP-запроса.

<details>

<summary><b>💡 Вот как это можно реализовать 💡</b></summary>

---

Использовать `getCurrentSession()` (`sessionFactory.getCurrentSession()`) — этот метод возвращает сессию текущего контекста. Так в одном потоке (HTTP-запросе) будет использоваться одна и та же сессия. В этом случае закрытие сессии будет происходить автоматически (даже без try-with-resources).

Для получения сессии через `getCurrentSession()` надо добавить в `hibernate.cfg.xml` свойство `hibernate.current_session_context_class`.

```xml
<property name="hibernate.current_session_context_class">thread</property> <!-- thread — для режима одна-сессия-на-поток -->
```

---

</details>

- ❗️В блоке `catch` вызов `transaction.rollback()` не обёрнут в `try-catch`.

Если во время отката транзакции произойдёт ещё одно исключение (например, из-за проблем с сетевым соединением с БД), это новое исключение "замаскирует" исходную ошибку, которая инициировала откат. В логах останется только ошибка отката, и разработчик не сможет узнать, что послужило первопричиной сбоя, что сильно усложняет отладку.

Стоит обернуть `transaction.rollback()` в собственный блок `try-catch` и, в случае ошибки, добавить новое исключение к исходному с помощью `originalException.addSuppressed(rollbackException)`.

<details>

<summary><b>💡 Например, так 💡</b></summary>

---

```java
private void safeRollback(Transaction transaction, Exception originalException) {
    if (transaction != null && transaction.isActive()) {
        try {
            transaction.rollback();
        } catch (Exception rollbackException) {
            originalException.addSuppressed(rollbackException);
        }
    }
}
```

</details>

### Хрупкий базовый класс (The Fragile Base Class)

Хрупкий базовый класс — это проблема проектирования в ООП, когда изменения в базовом (родительском) классе могут неожиданно сломать поведение производных (дочерних) классов, даже если код дочерних классов не изменялся.

Есть две основные формы проявления хрупкости: из-за состояния и из-за поведения.

### 1. Хрупкость из-за состояния (Coupling of Implementation)

Возникает, когда базовый класс предоставляет подклассам прямой доступ к своим внутренним полям, как правило, через модификатор `protected`.

#### Концепция

Когда подкласс напрямую обращается к полям своего родителя, он становится зависимым не от его публичного контракта (поведения), а от его **деталей реализации**. Базовый класс больше не может свободно изменять свою внутреннюю структуру, так как любое такое изменение рискует сломать все подклассы, которые на эту структуру завязаны.

#### Пример: `DocumentBuilder`

Представим базовый класс для построения документов:

```java
// Базовый класс
public abstract class DocumentBuilder {
    // Поля, "защищённые" для удобства наследников
    protected List<String> header = new ArrayList<>();
    protected List<String> body = new ArrayList<>();
    protected List<String> footer = new ArrayList<>();

    public abstract String build();
}
```

Подкласс для создания HTML-документов может напрямую использовать эти списки:

```java
// Подкласс
public class HtmlDocumentBuilder extends DocumentBuilder {
    @Override
    public String build() {
        StringBuilder doc = new StringBuilder("<html>
");

        // Прямое обращение к полям родителя
        body.forEach(line -> doc.append("  <p>").append(line).append("</p>
"));

        doc.append("</html>");
        return doc.toString();
    }
}
```

**В чем хрупкость?**

Код работает. Но со временем автор `DocumentBuilder` замечает, что для больших документов использование `List<String>` неэффективно по памяти. Он решает провести "безопасный" внутренний рефакторинг, заменив списки на `StringBuilder`:

```java
// Базовый класс после рефакторинга
public abstract class DocumentBuilder {
    // Внутренний рефакторинг для оптимизации
    protected StringBuilder header = new StringBuilder();
    protected StringBuilder body = new StringBuilder();
    protected StringBuilder footer = new StringBuilder();

    public abstract String build();
}
```

Это изменение, которое должно было быть внутренним делом `DocumentBuilder`, **полностью ломает `HtmlDocumentBuilder`**. Его метод `build()` больше не компилируется, так как у `StringBuilder` нет метода `forEach`. Базовый класс оказался "хрупким".

#### Решение

Решение заключается в строгой инкапсуляции. Состояние должно быть `private`, а взаимодействие с ним — через `protected` методы.

```java
// Крепкий базовый класс
public abstract class DocumentBuilder {
    private final StringBuilder content = new StringBuilder();

    // Наследники управляют состоянием через методы, не зная о его структуре
    protected void addHeaderLine(String line) {
        content.append("<header>").append(line).append("</header>
");
    }

    protected void addBodyLine(String line) {
        content.append("<p>").append(line).append("</p>
");
    }

    protected String getFinalContent() {
        return content.toString();
    }

    public abstract String build();
}
```

Теперь базовый класс может менять свою внутреннюю реализацию (например, заменить `StringBuilder` на массив байт), и пока контракт `protected` методов сохраняется, подклассы не сломаются.

### 2. Хрупкость из-за поведения (Implicit Contract Violation)

Возникает, когда подкласс переопределяет метод, нарушая неглавные предположения (implicit contract), на которые опирается базовый класс.

#### Концепция

Методы в базовом классе могут вызывать другие свои же методы (которые могут быть переопределены). Это называется "self-use". Если подкласс переопределяет такой метод и меняет его поведение (например, добавляет побочные эффекты), он может нарушить логику работы базового класса.

#### Пример: `InstrumentedSet` (пример из "Effective Java")

Предположим, мы хотим расширить `HashSet` и посчитать, сколько всего элементов было в него добавлено.

```java
// Неправильный подкласс HashSet
public class InstrumentedSet<E> extends HashSet<E> {
    private int addCount = 0;

    public InstrumentedSet(Collection<E> c) {
        super(c);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}
```

Проверим его работу:
```java
public static void main(String[] args) {
    InstrumentedSet<String> set = new InstrumentedSet<>();
    set.addAll(List.of("apple", "banana", "orange"));
    System.out.println(set.getAddCount()); // Ожидаем 3, но получаем 6!
}
```

**В чем хрупкость?**

Проблема в том, что мы не знали (и не должны были знать) деталь реализации `HashSet`: его метод `addAll` внутри себя вызывает метод `add` для каждого элемента коллекции.

Что происходит:
1.  Вызывается переопределенный `addAll`. Он прибавляет к `addCount` 3.
2.  Затем он вызывает `super.addAll()`.
3.  `super.addAll()` внутри `HashSet` итерируется по списку и трижды вызывает метод `add()`. Но так как `add()` переопределен в классе-наследнике, вызывается `add()` наследника, который ещё три раза инкрементирует `addCount`.

Нарушен неявный контракт. Логика базового класса сломалась из-за вмешательства. Базовый класс (`HashSet`) оказался хрупким по отношению к расширению.

#### Решение

**Композиция**. Вместо наследования нужно создать новый класс, который **содержит** `HashSet` как приватное поле и делегирует ему вызовы.

```java
// Надёжная реализация с использованием композиции
public class CountingSet<E> implements Set<E> {
    private final Set<E> set; // Композиция, а не наследование
    private int addCount = 0;

    public CountingSet(Set<E> set) {
        this.set = set;
    }

    public int getAddCount() {
        return addCount;
    }

    @Override
    public boolean add(E e) {
        if (set.add(e)) {
            addCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) { // Вызываем свой собственный, контролируемый add
                modified = true;
            }
        }
        return modified;
    }

    // ... делегировать все остальные методы интерфейса Set<E> полю "set" ...
    @Override public int size() { return set.size(); }
    @Override public boolean isEmpty() { return set.isEmpty(); }
    // ... и так далее
}
```

При таком подходе подкласс полностью контролирует логику, а `HashSet` используется как "чёрный ящик".

### Резюме

1. **Хрупкость из-за состояния** возникает при использовании `protected` полей и решается строгой инкапсуляцией.
2. **Хрупкость из-за поведения** возникает из-за переопределения методов с неявными контрактами и решается переходом к композиции.

## functional

- Можно назвать пакет action.

### BaseDAO

Вместо модификаторов protected лучше сделать поля private и предоставить protected геттеры к ним. (см. файл "fragile-base-class.md" в этом же пакете)
```java
protected final SessionFactory sessionFactory;
```
***

Класс использует `sessionFactory.openSession()` для получения сессии. Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию") (см. файл "dao.md" в этом же пакете).
```java
public abstract class BaseDAO<E> {
```
***

В блоках `catch` вызов `transaction.rollback()` не обёрнут в `try-catch`. (см. файл "dao.md" в этом же пакете)
```java
public abstract class BaseDAO<E> {
```
***

Слой DAO не должен управлять транзакциями самостоятельно (см. файл "dao.md" в этом же пакете).
```java
public abstract class BaseDAO<E> {
```
***

Управление жизненным циклом транзакций разорвано: здесь транзакции получаются из сессии и откатываются, а открываются и коммитятся в другом месте. Это нарушение принципа единой ответственности (SRP). Ответственность за управление жизненным транзакций должна находиться в одном классе. Также при таком подходе наследники вынуждены знать внутренние детали управления транзакциями (когда вызывать beginTransaction, когда commit). Это повышает связность между базовым и дочерними классами, а также снижает переиспользуемость. Если логика транзакций изменится (например, понадобится добавить уровень изоляции), придётся править все наследники, а не только базовый класс.
```java
public abstract class BaseDAO<E> {
```
***

Перед откатом транзакции надо проверить, что она активна (isActive()).
```java
// TODO: Перед откатом транзакции надо проверить, что она активна (isActive())
if (transaction != null) {
    transaction.rollback();
}
```
***

Перед откатом транзакции надо проверить, что она активна (isActive()).
```java
// TODO: Перед откатом транзакции надо проверить, что она активна (isActive())
if (transaction != null) {
    transaction.rollback();
}
```
***

### DAO

По логике методов K и E являются одинаковыми типами. Это некорректный подход к параметризации — он не имеет смысла.
```java
public interface DAO<K, E>  {
```
***

У DAO игрока и DAO матча есть только один общий функционал — сохранение в БД. Создавать ради этого общий интерфейс избыточно, а попытка сделать универсальными другие методы неизбежно будет ухудшать архитектуру. Наиболее чистым решением будет просто написать два простых DAO класса. Можно использовать Spring Data JPA (spring-data-jpa). Это позволит значительно сократить код и использовать удобные интерфейсы Page и Pageable.
```java
public interface DAO<K, E>  {
```
***

Реализация этого метода в DAO матча возвращает не E, а List<E>, а также принимает offset в виде строки. Метод выборки матчей должен принимать offset в виде числа (int), а также принимать int limit. А методу поиска игрока стоит возвращать Optional<Player>.
```java
E find(String criterion);
```
***

### ExtendedDAO

Интерфейс параметризованный, но методы имеют названия, специфичные для DAO матчей. Дженерики не нужны этому интерфейсу.
```java
public interface ExtendedDAO<K, E> extends DAO<K, E> {
```
***

Использование `long` для счётчиков, получаемых из БД, является более правильной практикой, так как SQL-функция `COUNT` возвращает 64-битное число.
```java
int countAllMatches();
```
***

Использование `long` для счётчиков, получаемых из БД, является более правильной практикой, так как SQL-функция `COUNT` возвращает 64-битное число.
```java
int countAllMatchesByPlayerName(String playerName);
```
***

Реализация этого метода в PostgresFinishedMatchDAO возвращает на E, а List<E>.
```java
E findMatchesByNameAndPage(String playerName, String pageNumber);
```
***

Название метода говорит, что он "ищет матчи по имени [игрока] и номеру страницы", хотя в реализации String pageNumber превращается в String offset. Корректная сигнатура метода выборки матчей может быть такой: List<Match> findAllByPlayerName(String playerName, int offset, int limit).
```java
E findMatchesByNameAndPage(String playerName, String pageNumber);
```
***

Числа должны передаваться числами (int).
```java
E findMatchesByNameAndPage(String playerName, String pageNumber);
```
***

### CounterRecords

Можно просто Counter.
```java
public interface CounterRecords {
```
***

Использование `long` для счётчиков, получаемых из БД, является более правильной практикой, так как SQL-функция `COUNT` возвращает 64-битное число.
```java
int count(Session session);
```
***

### FinderBy

Можно назвать SingleFinder.
```java
public interface FinderBy<E> {
```
***

### FinderRecords

Можно назвать AllFinder.
```java
public interface FinderRecords {
```
***

Из сигнатуры метода не понятно, что за параметр он принимает.
```java
List<Match> find(String indexOfPage);
```
***

## org.example.tennisscoreboard.config

### DatabaseConfig

При использовании setDataSourceClassName() все специфичные для драйвера свойства (такие как url, user, password) должны быть переданы через addDataSourceProperty(). Или можно использовать setDriverClassName (JDBC-драйвер).
```java
public class DatabaseConfig {
```
***

В свойстве db.driver и поле databaseDriver лежит datasource.
```java
// TODO: В свойстве db.driver и поле databaseDriver лежит datasource.
@Value("${db.driver}")
private String databaseDriver;
```
***

Драйвер устанавливается так: hikariConfig.setDriverClassName(databaseDriver).
```java
hikariConfig.setDataSourceClassName(databaseDriver); // TODO: Драйвер устанавливается так: hikariConfig.setDriverClassName(databaseDriver);
```
***

Можно перейти на LocalContainerEntityManagerFactoryBean.
```java
public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
```
***

В качестве разделителя для пути обычно используется точка.
```java
localSessionFactoryBean.setPackagesToScan("org/example/tennisscoreboard/entity");
```
***

### SpringConfig

В Spring обычно @Bean-методы объявляются как public.
```java
OngoingMatchesDomainService createOngoingMatchesDomainService() {
```
***

Вместо ручного создания бина можно использовать аннотацию @Component или @Service над OngoingMatchesDomainService.
```java
OngoingMatchesDomainService createOngoingMatchesDomainService() {
```
***

## org.example.tennisscoreboard.controller

### FinishedMatchController

String pageFromUser стоит сделать int.
```java
public ResponseEntity<FinishedMatchesResponse> getFinishedMatches(@RequestParam(required = false, value = "page") String pageFromUser, @RequestParam(required = false, value = "player_name") String playerName) {
```
***

Можно использовать Spring Data JPA (spring-data-jpa). Это позволит использовать удобный интерфейс Pageable.
```java
public ResponseEntity<FinishedMatchesResponse> getFinishedMatches(@RequestParam(required = false, value = "page") String pageFromUser, @RequestParam(required = false, value = "player_name") String playerName) {
```
***

### MatchScoreController

В проекте есть TennisScoreboardExceptionHandler, поэтому стоит убрать BindingResult из аргументов и обрабатывать ошибки валидации в хендлере.
```java
public ResponseEntity<TennisMatchResponse> awardPointAndGetGeneralScore(@PathVariable("uuid") String uuid, @RequestBody @Valid PointAwardingRequest pointAwardingRequest, BindingResult bindingResult) {
```
***

Можно назвать просто awardPoint.
```java
public ResponseEntity<TennisMatchResponse> awardPointAndGetGeneralScore(@PathVariable("uuid") String uuid, @RequestBody @Valid PointAwardingRequest pointAwardingRequest, BindingResult bindingResult) {
```
***

### OngoingMatchController

В проекте есть TennisScoreboardExceptionHandler, поэтому стоит убрать BindingResult из аргументов и обрабатывать ошибки валидации в хендлере.
```java
public ResponseEntity<RegisteredMatchResponse> createNewMatch(@RequestBody @Valid MatchCreationRequest matchCreationRequest, BindingResult bindingResult) {
```
***

Можно назвать getMatchScore.
```java
public ResponseEntity<TennisMatchResponse> getGeneralScore(@PathVariable(value = "uuid") String uuid) {
```
***

В @PathVariable(value = "uuid") по умолчанию значение required = true, поэтому ручная проверка не нужна.
```java
// В @PathVariable(value = "uuid") по умолчанию значение required = true, поэтому ручная проверка не нужна
if (uuid == null || uuid.isBlank()) {
    throw new IllegalArgumentException("UUID не может быть пустым");
}
```
***

## org.example.tennisscoreboard.dao.match

```text
Знаком ❗️ помечены критически важные замечания, а также места нарушения ТЗ.
```

## dao.match

- ❗️Для обработки ошибок используется конструкция `catch (Exception e)`. Этот подход является антипаттерном, так как он перехватывает абсолютно все исключения, а не только те, которые связаны с операциями доступа к данным.

Блок `catch (Exception e)` перехватывает не только ожидаемые ошибки Hibernate (например, сбой подключения к БД), но и любые другие ошибки времени выполнения (`RuntimeException`), такие как `NullPointerException`, `IllegalArgumentException` или `ClassCastException`. Эти исключения почти всегда указывают на наличие бага в коде.

Код, содержащий программную ошибку, должен "падать" как можно быстрее (Принцип Fail Fast) и с максимально понятным сообщением об ошибке (например, `NullPointerException` с точным указанием строки). Перехват `Exception` мешает этому, затягивая обнаружение и исправление дефектов.

Стоит заменить `catch (Exception e)` на перехват более специфичного исключения, которое является базовым для ошибок используемой технологии персистентности. Поскольку в проекте используется Hibernate, таким исключением является `org.hibernate.HibernateException`. Также можно ловить `jakarta.persistence.PersistenceException`.

Это даст возможность чётко различать ошибки доступа к данным (которые будут перехвачены и обёрнуты в специально созданное в приложении исключение) и программные баги (которые вызовут падение с оригинальным `RuntimeException`, указывая прямо на проблему в коде). Код станет более предсказуемым и устойчивым, так как его логика обработки ошибок сфокусирована исключительно на тех проблемах, для которых она предназначена — сбоях при работе с базой данных.

- ❗️Сейчас слой DAO управляет транзакциями самостоятельно:

Границы транзакции должны определяться бизнес-операцией, а не технической операцией доступа к данным. Бизнес-операция может включать в себя несколько вызовов DAO (например, сохранить игрока, а затем обновить матч). Все эти вызовы должны выполняться в рамках одной транзакции. Когда DAO сам управляет транзакцией, это становится невозможным. Ответственность за управление транзакциями должна лежать на сервисном слое, поэтому стоит перенести управление транзакциями именно в него.

В проекте на Spring можно управлять транзакциями через аннотации.

- ❗️В блоке `catch` вызов `transaction.rollback()` не обёрнут в `try-catch`.

Если во время отката транзакции произойдёт ещё одно исключение (например, из-за проблем с сетевым соединением с БД), это новое исключение "замаскирует" исходную ошибку, которая инициировала откат. В логах останется только ошибка отката, и разработчик не сможет узнать, что послужило первопричиной сбоя, что сильно усложняет отладку.

Стоит обернуть `transaction.rollback()` в собственный блок `try-catch` и, в случае ошибки, добавить новое исключение к исходному с помощью `originalException.addSuppressed(rollbackException)`.

<details>

<summary><b>💡 Например, так 💡</b></summary>

---

```java
private void safeRollback(Transaction transaction, Exception originalException) {
    if (transaction != null && transaction.isActive()) {
        try {
            transaction.rollback();
        } catch (Exception rollbackException) {
            originalException.addSuppressed(rollbackException);
        }
    }
}
```

</details>

### JOIN FETCH и LEFT JOIN FETCH в JPA/Hibernate

#### 1. Что такое JOIN FETCH?

`JOIN FETCH` – это специальная конструкция в JPQL (Java Persistence Query Language), которая позволяет загрузить связанные сущности (ассоциации) в одном запросе с основной сущностью, избегая так называемой проблемы N+1 запроса. Обычно, если у сущности есть ленивая (LAZY) ассоциация, при обращении к ней Hibernate выполняет отдельный SQL-запрос для каждой такой сущности. Использование `JOIN FETCH` заставляет Hibernate выполнить SQL JOIN (объединение) и сразу получить все необходимые данные, заполнив ассоциацию в объекте.

Синтаксис:

```postgres-sql
SELECT e FROM Entity e JOIN FETCH e.association
```

Здесь `association` – это поле сущности, помеченное аннотациями `@OneToMany`, `@ManyToOne` и т.п.

#### 2. JOIN FETCH (INNER JOIN FETCH)

По умолчанию `JOIN FETCH` эквивалентен **INNER JOIN** в SQL. Это означает, что в результат попадут только те записи основной сущности, для которых существует связанная запись (по условию соединения). Сами связанные сущности будут загружены и инициализированы.

**Пример:**

Допустим, есть сущности `Order` (заказ) и `OrderItem` (позиция заказа). У заказа может быть много позиций. Чтобы получить все заказы, у которых **есть хотя бы одна позиция**, и сразу загрузить эти позиции, используем:

```postgres-sql
SELECT o FROM Order o JOIN FETCH o.items
```

Такой запрос вернет только заказы с позициями. Если у заказа нет позиций, он не будет включён в результат.

#### 3. LEFT JOIN FETCH

`LEFT JOIN FETCH` соответствует **LEFT OUTER JOIN** в SQL. Он возвращает все записи основной сущности, даже если для них нет связанных записей. Для тех, у кого связь отсутствует, ассоциация будет заполнена пустой коллекцией (или `null` для одиночных связей), но сама основная сущность попадёт в результат.

**Пример:**
```jpql
SELECT o FROM Order o LEFT JOIN FETCH o.items
```
Этот запрос вернёт **все** заказы, включая те, у которых нет позиций. Для заказов без позиций поле `items` будет пустым списком (если тип коллекции) или `null` (если это одиночная связь).

#### 4. Основные отличия

| Характеристика           | JOIN FETCH (INNER)                     | LEFT JOIN FETCH                        |
|--------------------------|----------------------------------------|----------------------------------------|
| Тип SQL JOIN             | INNER JOIN                             | LEFT OUTER JOIN                        |
| Включение сущностей без связи | Не включаются                        | Включаются, ассоциация пустая/null     |
| Результат запроса        | Только сущности, имеющие связанные     | Все сущности основной таблицы          |
| Использование            | Когда нужны только те, у кого есть связь | Когда нужны все, но с загрузкой связи  |

### Принцип наименьшего удивления (Principle of Least Astonishment, POLA)

**Система должна вести себя так, как от неё ожидает большинство пользователей (разработчиков), и не должна вызывать удивление или замешательство.**

Это означает, что API, класс, метод или даже однострочное выражение должны быть **интуитивно понятными** и **предсказуемыми** для другого разработчика.

### Ключевые аспекты принципа

- Следование общепринятым соглашениям и идиомам: Имена методов и классов должны точно отражать их поведение. Геттеры начинаются с `get`/`is`, сеттеры — с `set`. Классы — `CamelCase`, переменные — `camelCase`, константы — `UPPER_SNAKE_CASE`.

- Предсказуемость поведения: Поведение методов должно быть интуитивно понятным и соответствовать тому, что подразумевает их имя и сигнатура.

- Соблюдение контрактов методов:

  - Если метод называется `getSomething()`, он должен возвращать что-то, а не изменять состояние.

  - Если метод называется `calculateSomething(params)`, он должен вычислять и возвращать результат, а не изменять переданные параметры.

- Следование единому стилю: Если в одном методе используется порядок параметров `(source, destination)`, то его следует придерживаться во всех похожих методах. Нельзя в другом методе делать `(destination, source)`.

- Согласованные возвращаемые значения: Если семейство методов возвращает `-1` при ошибке, не стоит в одном из них возвращать `0` или бросать исключение без веской причины.

Принцип наименьшего удивления в программировании — это о снижении когнитивной нагрузки на других разработчиков. Это создание кода, который ведёт себя так, как от него ждут, потому что он следует установленным правилам, здравому смыслу и согласованности. Следование этому принципу напрямую ведёт к созданию более чистого, поддерживаемого и надёжного кода.

### PostgresFinishedMatchDAO

Слово Postgres не нужно в названии класса. Можно изменить БД и при этом использовать этот класс.
```java
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {
```
***

Использование List для параметризации класса неидиоматично. Обычно параметрами выступают сущность и её ID.
```java
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {
```
***

Лучше вынести тексты HQL запросов в `private static final` константы и дать им понятные имена.
```java
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {
```
***

Ключевые слова в тексте HQL-запросов (`from`, `where` и др.) написаны в нижнем регистре. Хотя это и не влияет на работоспособность, написание ключевых слов SQL/HQL в верхнем регистре (`UPPERCASE`) является общепринятым стандартом. Это значительно улучшает читаемость запросов, так как визуально отделяет синтаксические конструкции языка от имён сущностей и полей.
```java
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {
```
***

Для единообразия кодовой базы в этом классе тоже можно использовать Criteria API (как в PostgresPlayerDAO).
```java
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {
```
***

В HQL запросах используется JOIN FETCH, что эквивалентно 'INNER JOIN' в SQL. `INNER JOIN` вернёт только те записи о матчах, у которых все связанные сущности (`player1`, `player2`) гарантированно существуют в базе. Если по какой-либо причине (например, ошибка при импорте или ручное вмешательство) в таблице `matches` окажется запись со значением `NULL` в колонке `player1`, то такой матч будет молчаливо исключён из выборки. `LEFT JOIN` является более безопасным подходом: - Он вернёт все матчи, даже если у них нарушена связь с игроком. - Это позволит приложению либо упасть с `NullPointerException` (что явно укажет на проблему с целостностью данных), либо корректно обработать такую ситуацию, если она допустима. "Падать громко и рано" часто лучше, чем молча скрывать проблемы. Стоит заменить `JOIN FETCH` на `LEFT JOIN FETCH` для обоих игроков и победителя для большей устойчивости запроса к потенциально некорректным данным. (см. файл "join-fetch-left-join-fetch.md" в этом же пакете)
```java
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {
```
***

Название каждого именованного параметра тоже лучше вынести в константу с понятным названием.
```java
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {
```
***

В блоках `catch (Exception e)` перехватывается слишком общее исключение (см. файл "dao.match.md" в этом же пакете).
```java
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {
```
***

Слой DAO не должен управлять транзакциями (см. файл "dao.match.md" в этом же пакете).
```java
public class PostgresFinishedMatchDAO extends BaseDAO<List<Match>> implements ExtendedDAO<Match, List<Match>> {
```
***

Метод называется insert и по смыслу он должен вставлять новую сущность. merge() используется для обновления или слияния, а не для явной вставки, поэтому здесь стоит использовать persist(). Использование merge() для гарантированно новой сущности нарушает принцип наименьшего удивления. (см. файл "pola.md" в этом же пакете)
```java
// Метод называется insert и по смыслу он должен вставлять новую сущность.
    // merge() используется для обновления или слияния, а не для явной вставки,
    // поэтому здесь стоит использовать persist().
    // Использование merge() для гарантированно новой сущности нарушает принцип наименьшего удивления.
    // (см. файл "pola.md" в этом же пакете)
session.merge(match);
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new DatabaseException("Ошибка базы данных");
```
***

Параметр offset должен приниматься числом, а не строкой. DAO не должен заниматься парсингом.
```java
public List<Match> find(String offset) {
```
***

Параметр limit (DEFAULT_PAGE_SIZE) стоит принимать в качестве аргумента, чтобы не привязываться к константе конкретного сервиса.
```java
public List<Match> find(String offset) {
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new DatabaseException("Ошибка базы данных");
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new DatabaseException("Ошибка базы данных");
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new DatabaseException("Ошибка базы данных");
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new DatabaseException("Ошибка базы данных");
```
***

Перед откатом транзакции надо проверить, что она активна (isActive()).
```java
// TODO: Перед откатом транзакции надо проверить, что она активна (isActive())
if (transaction != null) {

    // TODO: Вызов `transaction.rollback()` не обёрнут в `try-catch` (см. файл "dao.match.md" в этом же пакете)
    transaction.rollback();
}
```
***

Вызов `transaction.rollback()` не обёрнут в `try-catch` (см. файл "dao.match.md" в этом же пакете).
```java
// TODO: Вызов `transaction.rollback()` не обёрнут в `try-catch` (см. файл "dao.match.md" в этом же пакете)
transaction.rollback();
```
***

## org.example.tennisscoreboard.dao.player

```text
Знаком ❗️ помечены критически важные замечания, а также места нарушения ТЗ.
```

## dao.player

- ❗️Для обработки ошибок используется конструкция `catch (Exception e)`. Этот подход является антипаттерном, так как он перехватывает абсолютно все исключения, а не только те, которые связаны с операциями доступа к данным.

Блок `catch (Exception e)` перехватывает не только ожидаемые ошибки Hibernate (например, сбой подключения к БД), но и любые другие ошибки времени выполнения (`RuntimeException`), такие как `NullPointerException`, `IllegalArgumentException` или `ClassCastException`. Эти исключения почти всегда указывают на наличие бага в коде.

Код, содержащий программную ошибку, должен "падать" как можно быстрее (Принцип Fail Fast) и с максимально понятным сообщением об ошибке (например, `NullPointerException` с точным указанием строки). Перехват `Exception` мешает этому, затягивая обнаружение и исправление дефектов.

Стоит заменить `catch (Exception e)` на перехват более специфичного исключения, которое является базовым для ошибок используемой технологии персистентности. Поскольку в проекте используется Hibernate, таким исключением является `org.hibernate.HibernateException`. Также можно ловить `jakarta.persistence.PersistenceException`.

Это даст возможность чётко различать ошибки доступа к данным (которые будут перехвачены и обёрнуты в специально созданное в приложении исключение) и программные баги (которые вызовут падение с оригинальным `RuntimeException`, указывая прямо на проблему в коде). Код станет более предсказуемым и устойчивым, так как его логика обработки ошибок сфокусирована исключительно на тех проблемах, для которых она предназначена — сбоях при работе с базой данных.

- ❗️Сейчас слой DAO управляет транзакциями самостоятельно:

Границы транзакции должны определяться бизнес-операцией, а не технической операцией доступа к данным. Бизнес-операция может включать в себя несколько вызовов DAO (например, сохранить игрока, а затем обновить матч). Все эти вызовы должны выполняться в рамках одной транзакции. Когда DAO сам управляет транзакцией, это становится невозможным. Ответственность за управление транзакциями должна лежать на сервисном слое, поэтому стоит перенести управление транзакциями именно в него.

В проекте на Spring можно управлять транзакциями через аннотации.

### Принцип наименьшего удивления (Principle of Least Astonishment, POLA)

**Система должна вести себя так, как от неё ожидает большинство пользователей (разработчиков), и не должна вызывать удивление или замешательство.**

Это означает, что API, класс, метод или даже однострочное выражение должны быть **интуитивно понятными** и **предсказуемыми** для другого разработчика.

### Ключевые аспекты принципа

- Следование общепринятым соглашениям и идиомам: Имена методов и классов должны точно отражать их поведение. Геттеры начинаются с `get`/`is`, сеттеры — с `set`. Классы — `CamelCase`, переменные — `camelCase`, константы — `UPPER_SNAKE_CASE`.

- Предсказуемость поведения: Поведение методов должно быть интуитивно понятным и соответствовать тому, что подразумевает их имя и сигнатура.

- Соблюдение контрактов методов:

  - Если метод называется `getSomething()`, он должен возвращать что-то, а не изменять состояние.

  - Если метод называется `calculateSomething(params)`, он должен вычислять и возвращать результат, а не изменять переданные параметры.

- Следование единому стилю: Если в одном методе используется порядок параметров `(source, destination)`, то его следует придерживаться во всех похожих методах. Нельзя в другом методе делать `(destination, source)`.

- Согласованные возвращаемые значения: Если семейство методов возвращает `-1` при ошибке, не стоит в одном из них возвращать `0` или бросать исключение без веской причины.

Принцип наименьшего удивления в программировании — это о снижении когнитивной нагрузки на других разработчиков. Это создание кода, который ведёт себя так, как от него ждут, потому что он следует установленным правилам, здравому смыслу и согласованности. Следование этому принципу напрямую ведёт к созданию более чистого, поддерживаемого и надёжного кода.

### PostgresPlayerDAO

Слово Postgres не нужно в названии класса. Можно изменить БД и при этом использовать этот класс.
```java
public class PostgresPlayerDAO extends BaseDAO<Player> implements DAO<Player, Player> {
```
***

Управление жизненным циклом транзакций разорвано: здесь транзакции создаются и коммитятся, а откатываются в другом месте. Это разрывает ответственность за управление транзакциями на несколько классов и нарушает Принцип единой ответственности (SRP). Ответственность за управление жизненным транзакций должна находиться в одном классе.
```java
public class PostgresPlayerDAO extends BaseDAO<Player> implements DAO<Player, Player> {
```
***

Слой DAO не должен управлять транзакциями (см. файл "dao.player.md" в этом же пакете).
```java
public class PostgresPlayerDAO extends BaseDAO<Player> implements DAO<Player, Player> {
```
***

В блоках `catch (Exception e)` перехватывается слишком общее исключение (см. файл "dao.player.md" в этом же пакете).
```java
public class PostgresPlayerDAO extends BaseDAO<Player> implements DAO<Player, Player> {
```
***

Метод называется insert и по смыслу он должен вставлять новую сущность. merge() используется для обновления или слияния, а не для явной вставки, поэтому здесь стоит использовать persist(). Использование merge() для гарантированно новой сущности нарушает принцип наименьшего удивления. (см. файл "pola.md" в этом же пакете)
```java
// Метод называется insert и по смыслу он должен вставлять новую сущность.
    // merge() используется для обновления или слияния, а не для явной вставки,
    // поэтому здесь стоит использовать persist().
    // Использование merge() для гарантированно новой сущности нарушает принцип наименьшего удивления.
    // (см. файл "pola.md" в этом же пакете)
session.merge(player);
```
***

Стоит возвращать Optional<Player>. Optional специально придуман для того, чтобы безопасно (без null) и без исключений вернуть пустой результат в случае отсутствия записи в БД.
```java
public Player find(String playerName) {
```
***

Код этого метода был бы проще и читался бы лучше без использования Criteria API.
```java
public Player find(String playerName) {
```
***

Этот метод можно выполнять без транзакции.
```java
public Player find(String playerName) {
```
***

Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит.
```java
// Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит
//кидает ошибку парень, если результат не найден
Player player = session.createQuery(criteriaQuery).getSingleResult();
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new DatabaseException("Ошибка базы данных");
```
***

## org.example.tennisscoreboard.domain.model

## Принцип Единого источника истины (Single Source of Truth, SSOT)

Принцип Single Source of Truth (SSOT), или "Единый источник истины", в контексте программирования и управления данными означает архитектурный подход, при котором все данные о конкретной сущности или состоянии системы хранятся и управляются в одном единственном, авторитетном месте.

Суть принципа заключается в том, чтобы избежать дублирования информации и обеспечить ее согласованность. Если данные существуют в нескольких местах, всегда есть риск их расхождения, что приводит к ошибкам, путанице и неверным решениям.

#### Преимущества:

- Согласованность данных: невозможно иметь противоречивое состояние.
- Упрощение отладки и поддержки: данные берутся и изменяются только в одном месте.
- Упрощение тестирования: легче тестировать, так как состояние определяется одним источником.
- Повышение надежности: уменьшает количество ошибок и повышает уверенность в точности информации.

#### Возможные недостатки:
- Производительность: постоянное вычисление может быть дороже, чем хранение поля.
- Сложность вычислений: иногда вычисление сложное.

Следование принципу SSOT делает код более предсказуемым, надёжным и понятным.

### Game

Название Game сбивает с толку. Классу, отвечающему за логику обработки счёта в сете, больше подойдёт название TennisSet.
```java
public class Game {
```
***

Для этой константы тоже стоит использовать примитивный тип int.
```java
public static final Integer MINIMUM_GAMES_IN_SET = 6;
```
***

Модификатор этой константы должен быть private.
```java
public static final Integer MINIMUM_GAMES_IN_SET = 6;
```
***

Для этой константы тоже стоит использовать примитивный тип int.
```java
public static final Integer MINIMUM_POINT_MARGIN_IN_TIEBREAK = 2;
```
***

Константа MINIMUM_POINT_MARGIN_IN_TIEBREAK должна находиться в классе тай-брейка.
```java
public static final Integer MINIMUM_POINT_MARGIN_IN_TIEBREAK = 2;
```
***

Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса. В сете всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно. Можно просто хранить два поля для игроков и для счёта.
```java
// TODO: Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса.
// В сете всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно.
    // Можно просто хранить два поля для игроков и для счёта.
@Getter
private Map<String, Integer> games;
```
***

Если в классе есть хоть один конструктор, то конструктор по умолчанию (публичный конструктор без аргументов) создан не будет, поэтому не нужно объявлять его как private.
```java
private Game() {
```
***

Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы. Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот. Этот метод (как и validateGames) стоит удалить.
```java
protected static Game createCustomGame(String firstParticipantName, int firstParticipantGames, String secondParticipantName,
                                       int secondParticipantGames) {
```
***

Метод с названием "является ли тай-брейком" в классе, отвечающем за логику в сете, сбивает с толку. Название метода должно означать "нужно ли начать тай-брейк" и метод должен быть приватным. За создание тай-брейка должен отвечать этот класс.
```java
protected boolean isTiebreak() {
```
***

Лучше назвать pointWonBy(name).
```java
protected void updateGames(String name) {
```
***

Нет проверки на то, что сет не завершён. Попытка начислить очко в уже завершённом сете — это не нормальная ситуация и должна приводить к исключению.
```java
protected void updateGames(String name) {
```
***

Метод нарушает инкапсуляцию и позволяет классам в этом же пакете изменять своё состояние в любое время. Этот метод должен быть private.
```java
protected void incrementGames(String name) {
```
***

Отсутствует явный модификатор доступа.
```java
boolean isWinningSet() {
```
***

Использование методов Collections.max()/Collections.min() порождает создание лишних объектов (итераторов). Переход на хранение счёта в двух переменных исправит это.
```java
boolean isWinningSet() {
```
***

Название метода читается как "является ли выигрышным сетом". Лучше назвать isFinished().
```java
boolean isWinningSet() {
```
***

Метод нарушает инкапсуляцию и позволяет классам в этом же пакете изменять своё состояние в любое время. Этот метод должен быть private.
```java
protected void finishSet() {
```
***

Значение Point.LOVE не относится к счёту в сете. Использовать здесь эту константу неуместно.
```java
protected void finishSet() {
```
***

Значение Point.LOVE не относится к счёту в сете. Использовать здесь эту константу неуместно. В теннисе нет понятия "сбросить сет (или счёт)", поэтому классу, представляющему сет, достаточно иметь метод boolean isFinished().
```java
protected boolean isReset() {
```
***

### Participant

Участник теннисного матча называется игрок, поэтому класс доменной модели игрока можно назвать TennisPlayer.
```java
public class Participant {
```
***

Константы и методы для валидации имени не нужны доменной модели игрока. Валидация происходит на входе данных в приложение. Этим не должен заниматься доменный слой.
```java
public class Participant {
```
***

Константы должны быть final.
```java
private static int MIN_LENGTH_NAME = 5;
```
***

Класс можно преобразовать в record.
```java
public class Participant {
```
***

### Participants

Создавать отдельный класс для пары участников — избыточно. Там где нужно можно просто хранить два поля.
```java
public record Participants(Participant firstParticipant, Participant secondParticipant) {
```
***

### Point

Класс нарушает Принцип единой ответственности (SRP). Он: - отвечает за преставление счёта в гейме - отвечает за логику обработки счёта в гейме.
```java
public class Point {
```
***

Название Point сбивает с толку. Point — подходящее название для класса, представляющего модель счёта в обычном гейме. А классу, отвечающему за логику обработки счёта в гейме, больше подойдёт название RegularGame.
```java
public class Point {
```
***

"Кодирование" счёта. Класс, представляющий модель счёта в обычном гейме может (и должен) быть перечислением (enum). Так как он имеет ограниченное число уникальных значений.
```java
public class Point {
```
***

В теннисном гейме нет понятия одно очко, два очка (ONE_POINT, TWO_POINT) и тд. Вместо этого счёт произносится как "пятнадцать", "тридцать" и тд. Поэтому константам стоит дать именно такие названия.
```java
public class Point {
```
***

Все константы для счёта должны быть private.
```java
public static final int AD = -1;
```
***

Чтобы список значений был неизменяемым его не нужно оборачивать в new ArrayList<>().
```java
private static final ArrayList<Integer> ENABLE_POINT = new ArrayList<>(List.of(AD, LOVE, ONE_POINT, TWO_POINT, THREE_POINT));
```
***

Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса. В гейме всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно. Можно просто хранить два поля для игроков и для счёта.
```java
// TODO: Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса.
// В гейме всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно.
    // Можно просто хранить два поля для игроков и для счёта.
@Getter
private Map<String, Integer> points;
```
***

Если в классе есть хоть один конструктор, то конструктор по умолчанию (публичный конструктор без аргументов) создан не будет, поэтому не нужно объявлять его как private.
```java
private Point() {
```
***

Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы. Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот. Этот метод (как и validatePoints) стоит удалить.
```java
protected static Point createCustomPoint(String firstParticipantName, int firstParticipantPoints, String secondParticipantName,
                                         int secondParticipantPoints) {
```
***

Метод, запускающий обработку выигранного очка должен быть публичным. Поскольку в проекте есть доменная модель участника матча (Participant), в этот метод лучше принимать её, а не отдельно имя игрока. Или можно ввести enum TennisSide {FIRST, SECOND} и сделать аргумент этого типа.
```java
protected void updatePointInStandardGame(String name) {
```
***

Нет проверки на то, что гейм не завершён. Попытка начислить очко в уже завершённом гейме — это не нормальная ситуация и должна приводить к исключению.
```java
protected void updatePointInStandardGame(String name) {
```
***

Лучше назвать setAdvantageTo(name).
```java
private void startAdvantageGame(String name) {
```
***

Название метода стоит уточнить. Сейчас не понятно у кого он проверяет наличие преимущества.
```java
private boolean isAdvantage() {
```
***

Слово return в java имеет определённое значение, поэтому лучше его не использовать в названиях методов.
```java
private void returnToDeuceOrFinishAdvantageGame(String name) {
```
***

Можно назвать handleAdvantage.
```java
private void returnToDeuceOrFinishAdvantageGame(String name) {
```
***

Реальному теннисному матчу больше бы соответствовал подход, где сет содержит несколько геймов, а не обнуляет счёт одного и того же объекта.
```java
private void finishGame() {
```
***

Слово return в java имеет определённое значение, поэтому лучше его не использовать в названиях методов.
```java
private void returnToDeuce(String name) {
```
***

Метод нарушает Принцип единой ответственности на уровне метода — выполняет более одной логично связанной операции. Стоит разделить его на два метода или провести другой рефакторинг, который исправит это.
```java
private void continueOrFinishStandardGame(String name) {
```
***

В теннисе нет понятия "сбросить гейм (или счёт)", поэтому классу, представляющему гейм, лучше иметь метод boolean isFinished().
```java
protected boolean isReset() {
```
***

### Score

Класс нарушает Принцип единой ответственности (SRP) и забирает часть логики у каждого нижестоящего класса модели. А также он предоставляет статические методы для создания объектов с предустановленными параметрами для тестов. За логику обработки счёта должны отвечать сами матч-сет-гейм: - Матч запускает обработку очка у сета и если тот завершён, увеличивает счёт у себя - Сет запускает обработку очка у гейма/тай-брейка и если тот завершён, увеличивает счёт у себя Класс не имеет самостоятельной логики и оправданного назначения, поэтому не должен существовать.
```java
public class Score {
```
***

Если в классе есть хоть один конструктор, то конструктор по умолчанию (публичный конструктор без аргументов) создан не будет, поэтому не нужно объявлять его как private.
```java
private Score() {
```
***

Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы. Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот. Этот метод стоит удалить.
```java
protected static Score createCustomScoreWithTiebreak(String firstParticipantName, String secondParticipantName,
                                                     int firstParticipantPoints, int secondParticipantPoints,
                                                     int firstParticipantGames, int secondParticipantGames,
                                                     int firstParticipantSets, int secondParticipantSets,
                                                     int firstParticipantTiebreakPoints, int secondParticipantTiebreakPoints
) {
```
***

Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы. Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот. Этот метод стоит удалить.
```java
protected static Score createCustomScoreWithoutTiebreak(String firstParticipantName, String secondParticipantName,
                                                        int firstParticipantPoints, int secondParticipantPoints,
                                                        int firstParticipantGames, int secondParticipantGames,
                                                        int firstParticipantSets, int secondParticipantSets) {
```
***

### Set

Название Set сбивает с толку. А также в java есть интерфейс с названием Set, поэтому создание класса с таким же именем может вводить в заблуждение. Классу, отвечающему за логику обработки счёта в матче, больше подойдёт название TennisMatch.
```java
public class Set {
```
***

Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса. В матче всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно. Можно просто хранить два поля для игроков и для счёта.
```java
// TODO: Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса.
// В матче всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно.
    // Можно просто хранить два поля для игроков и для счёта.
@Getter
private Map<String, Integer> sets;
```
***

Если в классе есть хоть один конструктор, то конструктор по умолчанию (публичный конструктор без аргументов) создан не будет, поэтому не нужно объявлять его как private.
```java
private Set() {
```
***

Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы. Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот. Этот метод (как и validateSet) стоит удалить.
```java
protected static Set createCustomSet(String firstParticipantName, int firstParticipantSets, String secondParticipantName,
                                     int secondParticipantSets) {
```
***

Лучше назвать pointWonBy(name).
```java
protected void updateSets(String name) {
```
***

Нет проверки на то, что матч не завершён. Попытка начислить очко в уже завершённом матче — это не нормальная ситуация и должна приводить к исключению.
```java
protected void updateSets(String name) {
```
***

### TennisMatch

Хранение поля Participant winner вынуждает следить не только за состоянием счёта, но и за этим полем. Это нарушает Принцип Единого источника истины (см. файл "ssot-principle.md" в этом же пакете). Победителя в матче можно вычислять по счёту. Вместо простого геттера и метода void findWinner() лучше иметь метод Optional<Participant> getWinner(), который никогда не вернёт null и будет вычислять победителя "на лету".
```java
// TODO: Хранение поля Participant winner вынуждает следить не только за состоянием счёта, но и за этим полем.
    // Это нарушает Принцип Единого источника истины (см. файл "ssot-principle.md" в этом же пакете).
    // Победителя в матче можно вычислять по счёту.
// Вместо простого геттера и метода void findWinner() лучше иметь метод Optional<Participant> getWinner(),
    // который никогда не вернёт null и будет вычислять победителя "на лету".
@Getter
private Participant winner;
```
***

Если в классе есть хоть один конструктор, то конструктор по умолчанию (публичный конструктор без аргументов) создан не будет, поэтому не нужно объявлять его как private.
```java
private TennisMatch() {
```
***

Стоит удалять закомментированный код перед тем, как выполнять коммит.
```java
// Стоит удалять закомментированный код перед тем, как выполнять коммит
//this.uuid = UUID.fromString("f609a413-255a-4eae-925a-dedddd67e470");
```
***

Матч не должен сам генерировать свой ID — это ответственность класса, который его создаёт или сохраняет в БД/хранилище.
```java
// Матч не должен сам генерировать свой ID — это ответственность класса, который его создаёт или сохраняет в БД/хранилище.
this.uuid = UUID.randomUUID();
```
***

Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы. Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот. Этот метод стоит удалить.
```java
public static TennisMatch createCustomMatchWithTiebreak(Participants participants,
                                                        int firstParticipantPoints, int secondParticipantPoints,
                                                        int firstParticipantGames, int secondParticipantGames,
                                                        int firstParticipantSets, int secondParticipantSets,
                                                        int firstParticipantTiebreakPoints, int secondParticipantTiebreakPoints) {
```
***

Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы. Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот. Этот метод стоит удалить.
```java
public static TennisMatch createCustomMatchWithoutTiebreak(Participants participants,
                                                           int firstParticipantPoints, int secondParticipantPoints,
                                                           int firstParticipantGames, int secondParticipantGames,
                                                           int firstParticipantSets, int secondParticipantSets
) {
```
***

Лучше назвать pointWonBy(name).
```java
public void awardPoint(String winnerName) {
```
***

Здесь аргумент метода назван winnerName, а в других классах просто name — лучше придерживаться единообразия.
```java
public void awardPoint(String winnerName) {
```
***

Определение победителя в этом методе является "побочным эффектом". Ничто не мешает в уже завершённом матче вызвать несколько раз метод awardPoint для проигравшего и тем самым изменить победителя. Метод findWinner() (который после рефакторинга преобразуется в Optional<Participant> getWinner()) должен запускаться отдельно.
```java
public void awardPoint(String winnerName) {
```
***

Нет проверки на то, что матч не завершён. Попытка начислить очко в уже завершённом матче — это не нормальная ситуация и должна приводить к исключению.
```java
public void awardPoint(String winnerName) {
```
***

### Tiebreak

Слово Tiebreak можно убрать из названий методов и констант — этот контекст понятен из названия самого класса.
```java
public class Tiebreak {
```
***

Для этой константы тоже стоит использовать примитивный тип int.
```java
private static final Integer MINIMUM_POINTS_IN_TIEBREAK = 7;
```
***

Лучше назвать MINIMUM_POINTS_TO_WIN.
```java
private static final Integer MINIMUM_POINTS_IN_TIEBREAK = 7;
```
***

Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса. В тай-брейке всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно. Можно просто хранить два поля для игроков и для счёта.
```java
// TODO: Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса.
// В тай-брейке всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно.
    // Можно просто хранить два поля для игроков и для счёта.
@Getter
private Map<String, Integer> tiebreakPoints;
```
***

Если в классе есть хоть один конструктор, то конструктор по умолчанию (публичный конструктор без аргументов) создан не будет, поэтому не нужно объявлять его как private.
```java
private Tiebreak() {}
```
***

Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы. Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот. Этот метод (как и validateTiebreak) стоит удалить.
```java
protected static Tiebreak createCustomTiebreak(String firstParticipantName, Integer firstParticipantPoints, String secondParticipantName, Integer secondParticipantPoints) {
```
***

Нет проверки на то, что тай-брейк не завершён. Попытка начислить очко в уже завершённом тай-брейке — это не нормальная ситуация и должна приводить к исключению.
```java
protected void updatePointInTiebreak(String name) {
```
***

В теннисе нет понятия "сбросить тай-брейк (или счёт)", поэтому классу, представляющему тай-брейк, достаточно иметь метод boolean isFinished().
```java
protected boolean isReset() {
```
***

Лучше назвать initScore.
```java
private void startTiebreak() {
```
***

Можно назвать addPointTo(name).
```java
private void incrementPointInTiebreak(String name) {
```
***

Использование методов Collections.max()/Collections.min() порождает создание лишних объектов (итераторов). Переход на хранение счёта в двух переменных исправит это.
```java
private boolean isTiebreakFinished() {
```
***

Константа MINIMUM_POINT_MARGIN_IN_TIEBREAK должна находиться в этом классе.
```java
private boolean isTiebreakFinished() {
```
***

Реальному теннисному матчу больше бы соответствовал подход, где сет содержит несколько тай-брейков/геймов, а не обнуляет счёт одного и того же объекта.
```java
private void finishTiebreak() {
```
***

## org.example.tennisscoreboard.domain.service

```text
Знаком ❗️ помечены критически важные замечания, а также места нарушения ТЗ.
```

## service

- ❗️В пакете отсутствуют интерфейсы для сервисных классов. Все классы являются конкретными реализациями, от которых напрямую зависят другие компоненты приложения (например, сервлеты).

Почему это проблема:

  - Нарушение Принципа инверсии зависимостей (Dependency Inversion Principle): Принцип гласит, что модули верхних уровней не должны зависеть от модулей нижних уровней, а также они должны зависеть от абстракций. В данном случае вышестоящие модули (сервлеты) напрямую зависят от конкретных реализаций сервисов, что делает систему жёстко связанной и хрупкой.

  - Низкая гибкость и невозможность расширения: Если потребуется создать альтернативную реализацию какого-либо сервиса (например, перейти на хранение текущих матчей в БД), это потребует изменения кода во всех местах, где использовалась оригинальная реализация.

Для каждого класса в этом пакете стоит создать интерфейс, который будет определять его публичный контракт, и изменить все зависимые классы так, чтобы они использовали этот интерфейс.

### OngoingMatchesDomainService

Можно добавить классу аннотацию @Component.
```java
public class OngoingMatchesDomainService {
```
***

Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)
```java
public class OngoingMatchesDomainService {
```
***

Этот класс не относится к домену, а только выступает хранилищем текущих матчей (доменных моделей), поэтому стоит перенести его в пакет service (или dao.inmemory).
```java
public class OngoingMatchesDomainService {
```
***

Этот метод не должен получать ID из `TennisMatch`. Хранилище должно само создавать ID для матча (по аналогии с БД) и возвращать его из этого метода.
```java
public void addNewMatch(TennisMatch tennisMatch) {
```
***

Можно просто add или save.
```java
public void addNewMatch(TennisMatch tennisMatch) {
```
***

Этот метод должен принимать готовый объект UUID, а не парсить его из строки.
```java
public TennisMatch getOngoingMatch(String uuid) {
```
***

Можно просто get или find.
```java
public TennisMatch getOngoingMatch(String uuid) {
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new OngoingMatchNotFoundException("Текущий матч не найден");
```
***

Этот метод должен принимать готовый объект UUID, а не парсить его из строки.
```java
public void deleteFinishedMatch(String uuid) {
```
***

Можно просто delete или remove.
```java
public void deleteFinishedMatch(String uuid) {
```
***

Этот метод (и класс хранилища) не должны ничего знать о бизнес-логике (что удаляются только завершённые матчи). Его задача — просто работать с хранилищем и удалять матч, если он существует без дополнительных условий.
```java
// Этот метод (и класс хранилища) не должны ничего знать о бизнес-логике
    // (что удаляются только завершённые матчи). Его задача — просто работать с хранилищем
    // и удалять матч, если он существует без дополнительных условий.
if (tennisMatch.getWinner() != null) {
    ongoingMatches.remove(UUID.fromString(uuid));
}
```
***

## org.example.tennisscoreboard.dto

### MatchResponse

Можно назвать FinishedMatchResponse.
```java
public record MatchResponse(String firstPlayerName, String secondPlayerName, String winnerName) {
```
***

### PointAwardingRequest

Слово "первого" лишнее в сообщении.
```java
@NotBlank(message = "Имя первого игрока не может быть пустым")
```
***

### TennisMatchResponse

Можно назвать OngoingMatchResponse.
```java
public record TennisMatchResponse (
```
***

Сейчас все поля, относящиеся к счёту игрока, дублируются для первого и второго игрока. Такой подход делает классы большими и громоздкими и нарушает принцип DRY (Don't Repeat Yourself). Также, чтобы добавить счёт в тай-брейке для каждого игрока, понадобится добавить два поля. Можно ввести DTO для счёта одного игрока и хранить два таких DTO внутри MatchScoreDto. Вероятно сейчас причина этому — ошибка в ТЗ.
```java
public record TennisMatchResponse (
```
***

Для полей, которые не могут быть null можно использовать примитивные типы.
```java
public record TennisMatchResponse (
```
***

## org.example.tennisscoreboard.entity

### Использование зарезервированных слов в качестве названий в БД

Использование зарезервированного слова (например, `USER`, `ORDER`, `GROUP`) в качестве названия таблицы в базе данных — это плохая практика, которая может привести к ряду проблем.

Вот основные из них:

### 1. Синтаксические ошибки

Это самая главная и частая проблема. SQL-парсер видит зарезервированное слово и ожидает определённой синтаксической конструкции, а не названия таблицы.

**Пример:**
При попытке получить все записи из таблицы с названием `ORDER`.
```sql
SELECT * FROM ORDER;
```
этот запрос, скорее всего, вызовет ошибку, потому что `ORDER` — это ключевое слово для `ORDER BY` (сортировка). Парсер будет ожидать после него `BY` и не поймёт, что `ORDER` — это название таблицы.

### 2. Необходимость экранирования (Quoting)

Чтобы обойти синтаксические ошибки, придётся постоянно заключать название таблицы в специальные кавычки, которые зависят от конкретной СУБД:

* **MySQL / MariaDB:** обратные кавычки (`` ` ``)
    ```sql
    SELECT * FROM `ORDER`;
    ```
* **PostgreSQL / Стандарт SQL:** двойные кавычки (`" "`)
    ```sql
    SELECT * FROM "ORDER";
    ```
* **SQL Server:** квадратные скобки (`[ ]`)
    ```sql
    SELECT * FROM [ORDER];
    ```

### 3. Снижение читаемости и усложнение кода

Из-за необходимости постоянного экранирования код становится менее читаемым. Разработчики могут легко забыть поставить кавычки, что приведёт к ошибкам, на поиск которых уйдёт время.

### 4. Проблемы с ORM и другими инструментами

Инструменты, которые автоматически генерируют SQL-запросы (например, Hibernate, JPA, SQLAlchemy и другие ORM), а также различные GUI-клиенты и утилиты для миграции, могут не справиться с такими названиями. Они могут не знать, что `ORDER` нужно экранировать, и будут генерировать нерабочий SQL-код. Это потребует дополнительной конфигурации или ручного вмешательства.

### 5. Потеря переносимости

Ключевые слова могут отличаться в разных СУБД. Слово, которое не зарезервировано в одной системе, может быть зарезервировано в другой. Если команда сменит СУБД, проект с такими названиями таблиц потребует значительной доработки.

---

**Лучшая практика:**

**Никогда не использовать зарезервированные слова для названий таблиц, столбцов и других объектов в БД.**

Всегда проверять список зарезервированных слов для основных СУБД. Чтобы избежать случайных совпадений, можно придерживаться соглашений об именовании, например:
* Использовать префиксы: `tbl_order`.
* Использовать множественное число (если слово во множественном числе не зарезервировано): `orders` (слово `orders` не зарезервировано).
* Добавлять суффиксы: `order_data`.

### Match

сеттеры не нужны — позволяют создать объект с установленным id или изменить состав игроков после создания (например сделать постороннего игрока победителем).
```java
@Setter
```
***

Спецификация JPA требует наличия конструктора без аргументов для создания экземпляров сущностей, однако ему не обязательно быть `public`. Когда конструктор публичный, он становится частью общедоступного API класса. Это позволяет использовать его для создания "пустых", невалидных объектов (без установки обязательных полей) в любом месте приложения, хотя он предназначен исключительно для внутреннего использования фреймворком (JPA). Хорошим подходом будет ограничить область видимости этого конструктора до `protected`. Это делает его недоступным для прямого вызова из других пакетов, но оставляет видимым для JPA и дочерних классов. В Lombok это можно сделать с помощью параметра `access`.
```java
@NoArgsConstructor
```
***

"Matches" является зарезервированным словом в некоторых СУБД. Здесь проблем не будет, но лучше не выбирать такие названия. (см. файл "sql-keywords.md" в этом же пакете)
```java
@Table(name = "Matches")
```
***

Связи `@ManyToOne` не имеют явного указания о стратегии загрузки. По умолчанию для `@ManyToOne` используется `FetchType.EAGER`, что приводит к немедленной загрузке связанных сущностей при загрузке `Match`. Это может вызывать проблемы производительности (N+1 запросов) и излишнюю загрузку данных, особенно если связанные объекты не всегда нужны.
```java
@ManyToOne(optional = false)
```
***

Колонки игроков и победителя в `@JoinColumn` названы `Player1`, `Player2`, `Winner`. Для колонок, хранящих внешний ключ, уместно добавлять суффикс `_id`, чтобы было очевидно, что в них хранится идентификатор, а не какая-то другая информация.
```java
@JoinColumn(name = "Player1")
```
***

Здесь игроки называются Player playerOne и Player playerTwo, а в FinishedMatchesPersistenceService — Player firstPlayer и Player secondPlayer. Стоит выбрать один подход для единообразия. А также числительные в названиях колонок БД тоже можно писать буквами для единообразия.
```java
@JoinColumn(name = "Player1")
```
***

1. Можно добавить updatable = false. 2. Числительные в названиях колонок тоже можно писать буквами для единообразия.
```java
@JoinColumn(name = "Player1")
```
***

1. Можно добавить updatable = false. 2. Числительные в названиях колонок тоже можно писать буквами для единообразия.
```java
@JoinColumn(name = "Player2")
```
***

Можно добавить updatable = false.
```java
@JoinColumn(name = "Winner")
```
***

### Player

сеттеры не нужны — позволяют создать объект с установленным id или изменить имя игрока после создания.
```java
@Setter
```
***

Спецификация JPA требует наличия конструктора без аргументов для создания экземпляров сущностей, однако ему не обязательно быть `public`. Когда конструктор публичный, он становится частью общедоступного API класса. Это позволяет использовать его для создания "пустых", невалидных объектов (без установки обязательных полей) в любом месте приложения, хотя он предназначен исключительно для внутреннего использования фреймворком (JPA). Хорошим подходом будет ограничить область видимости этого конструктора до `protected`. Это делает его недоступным для прямого вызова из других пакетов, но оставляет видимым для JPA и дочерних классов. В Lombok это можно сделать с помощью параметра `access`.
```java
@NoArgsConstructor
```
***

Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит.
```java
// Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит
//@OneToMany - один игрок множество матчей, при этом возратятся все матчи игрока
```
***

## org.example.tennisscoreboard.exception

### DatabaseException

Оригинальное исключение не передаётся в конструктор супер-класса и молча "проглатывается". Это скрывает причину происшествия и затрудняет отладку.
```java
public DatabaseException(String message) {
```
***

## org.example.tennisscoreboard.handler

### ErrorMapper

Более уместным было бы расположить этот класс в пакете dao. Так как он не относится к глобальным хэндлерам и преобразует исключения для двух конкретных методов.
```java
public class ErrorMapper {
```
***

Ответственность этого класса слишком простая и имеет очень узкую направленность тесно связанную с логикой слоя DAO. Поэтому избыточно выносить её в отдельный класс.
```java
public class ErrorMapper {
```
***

Все методы "проглатывают" исходное исключение. Оригинальное исключение (причину) лучше передавать в конструктор исключения-обёртки, чтобы было возможным установить причину происшествия.
```java
public class ErrorMapper {
```
***

Пустой блок if не имеет смысла.
```java
if (e instanceof org.hibernate.exception.ConstraintViolationException) {
    // Пустой блок if не имеет смысла
}
```
***

Длинная цепочка вызовов ухудшает читаемость кода. В таких случаях стоит вводить переменные с понятными именами. Оборачивать любое ConstraintViolationException в IllegalArgumentException не всегда корректно. Стоит разработать кастомное исключение внутри приложения и использовать его. А ещё ConstraintViolationException не всегда означает нарушение уникальности имени игрока.
```java
throw new IllegalArgumentException(((ConstraintViolationException) e).getConstraintViolations().stream().findFirst().get().getMessage());
```
***

Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит.
```java
// Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит
//здесь можно добавить метод mapCommonErrors
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new DatabaseException("Ошибка базы данных");
```
***

В методе mapPostgresPlayerDAOInsertError по умолчанию бросается DatabaseException, а в этом методе — нет.
```java
public void mapPostgresPlayerDAOFindError(Exception e) {
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new PlayerNotFoundException("Игрок не найден");
```
***

### TennisScoreboardExceptionHandler

Можно использовать HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase().
```java
return new ExceptionResponse("Internal server error");
```
***

## org.example.tennisscoreboard.mapper

### MatchMapper

Для "spring" в mapstruct есть специальная константа: MappingConstants.ComponentModel.SPRING.
```java
@Mapper(componentModel = "spring")
```
***

Можно назвать EntityDtoMatchMapper.
```java
public interface MatchMapper {
```
***

Точнее назвать toDtoList.
```java
List<MatchResponse> toDTO(List<Match> match);
```
***

Писать и аннотации и реализацию метода избыточно. Можно оставить только аннотации @Mapping и объявление метода: MatchResponse toDTO(Match match). Его реализация будет сгенерирована автоматически.
```java
default MatchResponse toDTO(Match match) {
```
***

### TennisMatchMapper

Для "spring" в mapstruct есть специальная константа: MappingConstants.ComponentModel.SPRING.
```java
@Mapper(componentModel = "spring")
```
***

Можно назвать ModelDtoMatchMapper.
```java
public interface TennisMatchMapper {
```
***

Многие методы дублируются для каждого игрока. Стоит поискать решение, как избавиться от такого дублирования.
```java
public interface TennisMatchMapper {
```
***

## org.example.tennisscoreboard.service

```text
Знаком ❗️ помечены критически важные замечания, а также места нарушения ТЗ.
```

## service

- ❗️В пакете отсутствуют интерфейсы для сервисных классов. Все классы являются конкретными реализациями, от которых напрямую зависят другие компоненты приложения (например, контроллеры).

Почему это проблема:

  - Нарушение Принципа инверсии зависимостей (Dependency Inversion Principle): Принцип гласит, что модули верхних уровней не должны зависеть от модулей нижних уровней, а также они должны зависеть от абстракций. В данном случае вышестоящие модули (контроллеры) напрямую зависят от конкретных реализаций сервисов, что делает систему жёстко связанной и хрупкой.

  - Низкая тестируемость: Невозможно провести полноценное модульное тестирование компонентов, которые зависят от этих сервисов. Например, чтобы протестировать контроллеры, использующий `MatchApplicationService`, необходимо создавать полный экземпляр этого сервиса со всеми его реальными зависимостями, что превращает модульный тест в сложный интеграционный.

  - Низкая гибкость и невозможность расширения: Если потребуется создать альтернативную реализацию какого-либо сервиса, это потребует изменения кода во всех местах, где использовалась оригинальная реализация.

  - В классе-реализации публичные методы могут смешиваться с его внутренними или вспомогательными методами. Интерфейс же служит чётким, явным контрактом, который показывает, что сервис предоставляет внешнему миру, скрывая детали его внутренней работы.

Для каждого класса в этом пакете стоит создать интерфейс, который будет определять его публичный контракт, и изменить все зависимые классы так, чтобы они использовали этот интерфейс.

### FinishedMatchesFindingService

Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)
```java
public class FinishedMatchesFindingService {
```
***

Всю работу с завершёнными матчами можно реализовать в одном сервисе. Иначе эта ответственность получается слишком раздробленной.
```java
public class FinishedMatchesFindingService {
```
***

Размер страницы и номер по умолчанию более уместно хранить в контроллере, так как в идеале он должен приходить с фронтенда. А сервис должен принимать это значение в качестве аргумента в методы.
```java
public class FinishedMatchesFindingService {
```
***

Методы этого сервиса должны принимать номер страницы в int, а не парсить его из строки.
```java
public class FinishedMatchesFindingService {
```
***

Лучше назвать NUMBER_INDEX_DIFFERENCE.
```java
private final static int TO_INDEX = 1;
```
***

EMPTY_PAGE_NUMBER читалось бы лучше.
```java
private final static int PAGES_NOT_FOUND = 0;
```
***

Просто ZERO читалось бы лучше. Или можно вообще не выносить 0 в константу в этом случае.
```java
private final static int RECORDS_NOT_FOUND = 0;
```
***

Можно просто find.
```java
public FinishedMatchesResponse findFinishedMatches(String pageFromUser) {
```
***

Номер страницы стоит парсить как можно ближе ко входу этих данных в приложение (в контроллере), а в метод сервиса принимать уже int.
```java
public FinishedMatchesResponse findFinishedMatches(String pageFromUser) {
```
***

Можно просто findWithPlayerName.
```java
public FinishedMatchesResponse findFinishedMatchesByPlayer(String playerName, String pageFromUser) {
```
***

Номер страницы стоит парсить как можно ближе ко входу этих данных в приложение (в контроллере), а в метод сервиса принимать уже int.
```java
public FinishedMatchesResponse findFinishedMatchesByPlayer(String playerName, String pageFromUser) {
```
***

Раз STARTED_PAGE == 1, то когда записей нет, стоит возвращать её, а не PAGES_NOT_FOUND (0).
```java
if (recordCount == RECORDS_NOT_FOUND) {

    // Раз STARTED_PAGE == 1, то когда записей нет, стоит возвращать её, а не PAGES_NOT_FOUND (0)
    return new FinishedMatchesResponse(new ArrayList<>(), page, PAGES_NOT_FOUND);
```
***

Не нужно здесь конвертировать offset в строку, чтобы потом в DAO парсить обратно.
```java
List<Match> matches = finderRecords.find(String.valueOf(offset));
```
***

Номер страницы стоит парсить как можно ближе ко входу этих данных в приложение (в контроллере), а в методы сервиса принимать уже int.
```java
private int convertAndValidatePage(String pageFromUser) {
```
***

Достаточно ловить здесь более узкое NumberFormatException.
```java
} catch (Exception e) {
```
***

Сообщение об ошибке "Передан не номер страницы" не подходит для случая page < 1.
```java
throw new InvalidPageException("Передан не номер страницы");
```
***

Текст сообщения в исключениях принято писать на английском языке.
```java
throw new InvalidPageException("Передан не номер страницы");
```
***

Не указано сообщение в исключении — это осложнит отладку.
```java
throw new RuntimeException();
```
***

В проекте есть InvalidPageException, которое здесь было бы более уместным.
```java
throw new RuntimeException();
```
***

### FinishedMatchesPersistenceService

Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)
```java
public class FinishedMatchesPersistenceService {
```
***

Этот метод (и класс) не должны ничего знать о бизнес-логике (что сохраняются только завершённые матчи). Его задача — просто работать с репозиторием и сохранять матч без дополнительных условий.
```java
// TODO: Этот метод (и класс) не должны ничего знать о бизнес-логике (что сохраняются только завершённые матчи).
    // Его задача — просто работать с репозиторием и сохранять матч без дополнительных условий.
if (tennisMatch.getWinner() != null) {
```
***

## org.example.tennisscoreboard.util

### ValidationUtil

Класс спроектирован как утилитный, но при этом не объявлен как final и имеет публичный конструктор. Можно использовать @UtilityClass из Lombok.
```java
public class ValidationUtil {
```
***

Вместо возврата только первого сообщения можно формировать строку из всех сообщений.
```java
.findFirst()
```
***

Сообщение можно вынести в константу и дать ей понятное имя.
```java
.orElse("Something went wrong");
```
***

## Тесты

### TennisMatchTest

Как и в основном коде, в тестах тоже стоит избегать длинных цепочек вызовов. Строки вроде: tennisMatch.getScore().getTiebreakPoint().getTiebreakPoints() снижают читаемость кода.
```java
public class TennisMatchTest {
```
***

Каждая доменная модель должны иметь собственные юнит-тесты, где проверяется вся их логика изолированно.
```java
public class TennisMatchTest {
```
***

Методы, приводящие матч в нужное только для тестов состояние должны находиться в тестах, а не в доменном слое.
```java
public class TennisMatchTest {
```
***

```text
Знаком ❗️ помечены критически важные замечания, а также места нарушения ТЗ.
```

## В целом по проекту

- Местами в некоторых классах немного не хватает форматирования. Перед `git commit` можно нажимать (`cmd + alt + l` в Idea на mac os). Это работает как для текущего класса, так и для всего пакета: если выделить пакет и нажать комбинацию клавиш, то исправление форматирования будет выполнено для всех классов в этом пакете.

- В некоторых классах есть неиспользуемые импорты. Перед `git commit` можно нажимать (`ctrl + alt + o` в Idea на mac os). Это работает как для текущего класса, так и для всего пакета: если выделить пакет и нажать комбинацию клавиш, то оптимизация импорта будет выполнена для всех классов в этом пакете.

- Комментарии из кода и других файлов стоит удалять перед коммитом.

- Слой DAO имеет 9 классов, разнесённых на 3 пакета. После рефакторинга можно реализовать его значительно проще.

- ❗️В проекте часто встречается неточные или вводящие в заблуждение названия классов и методов. Понятные и точные названия — важная составляющая качественного кода, поэтому стоит уделить этому отдельное внимание.

- Нет учёта контекстного пути в переходе по страницам. Поэтому если приложение развёрнуто не в корне сервера, переход по страницам не будет работать.

## Плюсы

- Есть разделение на слои (Controller -> Service -> Repository)
- Реализованы специализированные классы исключений
- Используются транзакции
- Нет проблемы N+1 в запросах к БД
- Используется ConcurrentHashMap для хранения текущих матчей
- Корректная реализация основной бизнес-логики
- Проведена декомпозиция предметной области
- Логика подсчёта очков находится в доменных моделях (хоть и стоит их доработать)
- Есть тесты для бизнес-логики (хоть и стоит их доработать)
- Реализована валидация
- Реализованы мапперы
- Используется MapStruct
- Используются DTO
- Есть централизованная обработка исключений
- Работает фильтрация матчей по имени игрока
- Работает пагинация на странице поиска матчей
- Используется Lombok для уменьшения boilerplate-кода
- Есть README
- Успешный деплой приложения
- Красивый кастомный фронтенд

## Заключение

Проект представляет собой веб-приложение с корректно работающей бизнес-логикой. В коде прослеживается стремление автора следовать современным практикам и находить нетривиальные решения.

Сохраняется значимый учебный потенциал.

Рефакторинг по описанным замечаниям повысит читаемость и упростит поддержку проекта и выведет его на качественно новый уровень в этих аспектах. А также станет полезной практикой для разработчика и укрепит профессиональные навыки.

