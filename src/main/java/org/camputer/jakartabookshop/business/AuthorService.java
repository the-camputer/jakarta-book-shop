package org.camputer.jakartabookshop.business;

import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camputer.jakartabookshop.persistence.Author;


@ManagedBean("authorService")
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
