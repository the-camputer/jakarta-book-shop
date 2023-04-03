package org.camputer.jakartabookshop;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
@Provider
public class BookShopApplication extends ResourceConfig {

    public BookShopApplication() {
        register(DeclarativeLinkingFeature.class);
        packages("org.camputer.jakartabookshop.author", "org.camputer.jakartabookshop.book", "org.camputer.jakartabookshop.publisher");
    }
}
