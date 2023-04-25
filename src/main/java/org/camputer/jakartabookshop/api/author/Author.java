package org.camputer.jakartabookshop.api.author;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Link;
import org.camputer.jakartabookshop.api.book.Book;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import java.util.List;

@Entity
@Table(name="AUTHOR")
@NamedQuery(name="Author.getAllAuthors", query="select a from Author a")
public class Author {

    @Id
    @Column(name="author_id")
    private int authorId;

    @Column(name="author_name")
    private String authorName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="book_author",
                joinColumns = { @JoinColumn(name="author_id") },
                inverseJoinColumns = { @JoinColumn(name="book_id") }
    )
    @JsonbTransient
    private List<Book> books;

    @Transient
    @InjectLinks({
            @InjectLink(value = "author/${instance.getAuthorId()}", rel = "self"),
            @InjectLink(value = "author/${instance.getAuthorId()}/books", rel = "books")
    })
    public List<Link> links;


    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
