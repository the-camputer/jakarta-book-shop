package org.camputer.jakartabookshop.client;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RequestScoped
@Named("authorController")
public class AuthorController {

    private final Logger log = LogManager.getLogger(AuthorController.class);
    private List<Map<String, Object>> authorData;

    @Inject
    Properties applicationProperties;

    @PostConstruct
    public void initializeData() {
        var client = HttpClient.newHttpClient();
        var hostname = applicationProperties.getProperty("hostname");
        var protocol = applicationProperties.getProperty("protocol");
        String url = String.format("%s://%s/bookshop/api/authors/", protocol, hostname);
        var request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
//            log.error(response.body());
            authorData = gson.fromJson(response.body(), List.class);
        } catch(IOException e) {
            log.error(e.getMessage());
        } catch(InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    public String getBooksLinkForAuthor(int authorId) {
        Map<String, Object> author = authorData.stream().filter(authorObj -> ((Double) authorObj.get("authorId")).intValue() == authorId).findFirst().orElseThrow();
        List<Map<String, Object>> links = (List<Map<String, Object>>) author.get("links");
        Map<String, Object> booksLink = links.stream().filter(linkObj -> linkObj.get("rel").equals("books")).findFirst().orElseThrow();

        return (String) booksLink.get("uri");
    }

    public List<Map<String, Object>> getAuthorData() { return this.authorData; }
}
