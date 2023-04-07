package org.camputer.jakartabookshop.client;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RequestScoped
@Named("healthCheck")
public class HealthCheck {

    public String getAPIStatus() throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/bookshop/api/healthcheck")).GET().build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
