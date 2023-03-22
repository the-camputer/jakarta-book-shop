package org.camputer.jakartabookshop.author;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@ApplicationScoped
public class AuthorService {

    @PersistenceUnit(unitName = "BookshopRepository")
    private EntityManagerFactory emf;

    private static final Logger log = LogManager.getLogger(AuthorService.class);

    public Author getAuthor(int id) {
        EntityManager em = emf.createEntityManager();
        return em.find(Author.class, id);
    }
}
