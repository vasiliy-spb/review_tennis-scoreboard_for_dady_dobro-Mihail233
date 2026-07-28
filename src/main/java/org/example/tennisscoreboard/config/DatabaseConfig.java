package org.example.tennisscoreboard.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfig {

    // При использовании setDataSourceClassName() все специфичные для драйвера свойства (такие как url, user, password) должны быть переданы через addDataSourceProperty()
        // https://deepwiki.com/brettwooldridge/HikariCP/4-configuration#datasource-properties
        // Или можно использовать setDriverClassName (JDBC-драйвер).

    // TODO: В свойстве db.driver и поле databaseDriver лежит datasource.
    @Value("${db.driver}")
    private String databaseDriver;

    @Value("${db.url}")
    private String databaseUrl;

    @Value("${db.username}")
    private String databaseUsername;

    @Value("${db.password}")
    private String databasePassword;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2DdlAuto;

    @Value("${hibernate.format_sql}")
    private String hibernateFormatSql;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName(databaseDriver); // TODO: Драйвер устанавливается так: hikariConfig.setDriverClassName(databaseDriver);
        hikariConfig.setJdbcUrl(databaseUrl);
        hikariConfig.setUsername(databaseUsername);
        hikariConfig.setPassword(databasePassword);
        hikariConfig.setPoolName("myHicariCp");

        return new HikariDataSource(hikariConfig);
    }

    // Можно перейти на LocalContainerEntityManagerFactoryBean
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);
        localSessionFactoryBean.setPackagesToScan("org/example/tennisscoreboard/entity"); // В качестве разделителя для пути обычно используется точка
        localSessionFactoryBean.setHibernateProperties(getHibernateProperties());
        return localSessionFactoryBean;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty("hibernate.show_sql", hibernateShowSql);
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2DdlAuto);
        properties.setProperty("hibernate.format_sql", hibernateFormatSql);
        return properties;
    }
}
