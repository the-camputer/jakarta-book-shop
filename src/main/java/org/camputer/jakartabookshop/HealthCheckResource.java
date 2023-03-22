package org.camputer.jakartabookshop;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Path("/healthcheck")
@Transactional
public class HealthCheckResource {

    private final Logger log = LogManager.getLogger(HealthCheckResource.class);

    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatus() {
        HashMap<String, Object> results = new HashMap<>();
        try {
            results.put("dbConnection", em.isOpen());
        } catch(Exception e) {
            e.printStackTrace();
            results.put("error", e.getMessage());
        }
        log.info("healthcheck: " + results);
        return new Gson().toJson(results);
    }
}
