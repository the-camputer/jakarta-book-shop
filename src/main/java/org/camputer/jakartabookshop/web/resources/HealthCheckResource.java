package org.camputer.jakartabookshop.web.resources;

import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camputer.jakartabookshop.business.AuthorService;
import org.camputer.jakartabookshop.persistence.Author;

import java.util.HashMap;

@Path("/healthcheck")
@Transactional
public class HealthCheckResource {

    private final Logger log = LogManager.getLogger(HealthCheckResource.class);

    @Inject
    private AuthorService authorService;

    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatus() {
        HashMap<String, Object> results = new HashMap<>();
        try {
            Author testAuthor = authorService.getAuthor(1);
            results.put("dbConnection", testAuthor != null);
        } catch(Exception e) {
            e.printStackTrace();
            results.put("error", e.getMessage());
        }
        log.info("healthcheck: " + results);
        return new Gson().toJson(results);
    }
}
