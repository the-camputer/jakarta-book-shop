package org.camputer.jakartabookshop.api.publisher;

import jakarta.persistence.*;
import jakarta.ws.rs.core.Link;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import java.util.List;

@Entity
@Table(name="publisher")
public class Publisher {

    @Id
    @Column(name="publisher_id")
    private int publisherId;

    @Column(name="publisher_name")
    private String publisherName;

    @Transient
    @InjectLinks({
            @InjectLink( value = "/publisher/${instance.getPublisherId()}", rel = "self" ),
            @InjectLink( value = "/publisher/${instance.getPublisherId()}/books", rel = "books")
    })
    public List<Link> links;

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}
