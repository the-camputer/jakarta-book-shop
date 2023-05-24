package org.camputer.jakartabookshop.api;

import jakarta.annotation.security.DeclareRoles;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@BasicAuthenticationMechanismDefinition(
        realmName = "bookshop-realm"
)
@DeclareRoles({ "READ", "WRITE" })
@ApplicationPath("/api")
@Provider
public class BookShopApplication extends ResourceConfig {
    public BookShopApplication() {
        register(RolesAllowedDynamicFeature.class);
        register(DeclarativeLinkingFeature.class);
        packages(
                "org.camputer.jakartabookshop.api",
                "org.camputer.jakartabookshop.api.author",
                "org.camputer.jakartabookshop.api.book",
                "org.camputer.jakartabookshop.api.publisher"
        );
    }
}
