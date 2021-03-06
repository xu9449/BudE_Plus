package com.sean.debug12.util;


import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class HibernateUtil {
    private SessionFactory sessionFactory;
    private Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    @Bean
    public  SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {

                String[] modelPackages = {"com.sean.debug12.model"};
                String dbDriver = System.getProperty("database.driver");
                String dbDialect = System.getProperty("database.dialect");
                String dbUrl = "jdbc:postgresql://"+System.getProperty("database.url")+
                               ":"+System.getProperty("database.port")+"/"+System.getProperty("database.name");
                String dbUser = System.getProperty("database.user");
                String dbPassword = System.getProperty("database.password");
                Configuration configuration = new Configuration();
                Properties settings = new Properties();


                settings.put(Environment.DRIVER, dbDriver);
                settings.put(Environment.DIALECT, dbDialect);
                settings.put(Environment.URL, dbUrl);
                settings.put(Environment.USER, dbUser);
                settings.put(Environment.PASS, dbPassword);
                settings.put(Environment.SHOW_SQL, "true");
                //validate schema
                settings.put(Environment.HBM2DDL_AUTO, "validate");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                configuration.setProperties(settings);

                EntityScanner.scanPackages(modelPackages).addTo(configuration);
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
                ServiceRegistry serviceRegistry = registryBuilder.applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                logger.error("fail to generate hibernate sessionfactory", e);
            }
        }
        return sessionFactory;
    }

}
