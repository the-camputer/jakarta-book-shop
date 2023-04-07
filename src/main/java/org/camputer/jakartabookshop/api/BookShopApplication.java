package org.camputer.jakartabookshop.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
@Provider
public class BookShopApplication extends ResourceConfig {

    public BookShopApplication() {
        register(DeclarativeLinkingFeature.class);
        packages(
                "org.camputer.jakartabookshop.api",
                "org.camputer.jakartabookshop.api.author",
                "org.camputer.jakartabookshop.api.book",
                "org.camputer.jakartabookshop.api.publisher"
        );
    }
}
