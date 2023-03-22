package org.camputer.jakartabookshop.book;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camputer.jakartabookshop.author.AuthorService;

@ApplicationScoped
public class BookService {

    @PersistenceUnit(unitName = "BookshopRepository")
    private EntityManagerFactory emf;

    private static final Logger log = LogManager.getLogger(AuthorService.class);

    public Book getBook(int id) {
        EntityManager em = emf.createEntityManager();
        return em.find(Book.class, id);
    }
}
