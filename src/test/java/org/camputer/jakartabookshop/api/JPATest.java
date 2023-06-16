package org.camputer.jakartabookshop.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.sql.SQLException;

public class JPATest {
    protected static EntityManagerFactory emf;
    protected static EntityManager entityManager;

    @BeforeAll
    public static void init() throws FileNotFoundException, SQLException {
        emf = Persistence.createEntityManagerFactory("MockBookshopRepository");
        entityManager = emf.createEntityManager();
    }

    @BeforeEach
    public void initializeDatabase() {
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                try (InputStream is = classloader.getResourceAsStream("mock_bookshop.sql")) {
                    RunScript.execute(connection, new InputStreamReader(is));
                }
            } catch (IOException e) {
                throw new RuntimeException("could not initialize with script");
            }
        });
    }
}
