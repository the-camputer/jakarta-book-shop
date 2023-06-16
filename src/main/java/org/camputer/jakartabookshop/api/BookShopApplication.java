package org.camputer.jakartabookshop.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

//@BasicAuthenticationMechanismDefinition(
//        realmName = "bookshop-realm"
//)
//@DeclareRoles({ "READ", "WRITE" })
@ApplicationPath("/api")
@Provider
public class BookShopApplication extends ResourceConfig {
    public BookShopApplication() {
//        register(RolesAllowedDynamicFeature.class);
        register(DeclarativeLinkingFeature.class);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
        packages(
                "org.camputer.jakartabookshop.api",
                "org.camputer.jakartabookshop.api.author",
                "org.camputer.jakartabookshop.api.book",
                "org.camputer.jakartabookshop.api.publisher"
        );
    }
}
