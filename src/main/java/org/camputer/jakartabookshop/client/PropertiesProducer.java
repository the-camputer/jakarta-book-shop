package org.camputer.jakartabookshop.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

@ApplicationScoped
public class PropertiesProducer implements Serializable {

    private static final Logger log = LogManager.getLogger(PropertiesProducer.class);

    @Produces
    public Properties getApplicationProperties() {
        Properties props = new Properties();
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties")) {
            props.load(stream);
        } catch(IOException e) {
            log.error("Unable to load server.properties, setting defaults");
            props.setProperty("hostname", "localhost:8080");
            props.setProperty("protocol", "http");
        }
        return props;
    }
}
