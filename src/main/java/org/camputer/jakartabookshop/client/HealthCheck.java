package org.camputer.jakartabookshop.client;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

@RequestScoped
@Named("healthCheck")
public class HealthCheck {

    @Inject
    Properties applicationProperties;

    public String getAPIStatus() throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var hostname = applicationProperties.getProperty("hostname");
        var protocol = applicationProperties.getProperty("protocol");
        String url = String.format("%s://%s/bookshop/api/healthcheck", protocol, hostname);
        var request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
