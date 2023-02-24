-- Borrowing basic structures/columns from https://github.com/bbrumm/databasestar/tree/main/sample_databases
CREATE TABLE author (
                        author_id INT,
                        author_name VARCHAR(400),
                        CONSTRAINT pk_author PRIMARY KEY (author_id)
);

CREATE SEQUENCE seq_author_id minvalue 100;

CREATE TABLE publisher (
                           publisher_id INT,
                           publisher_name VARCHAR(400),
                           CONSTRAINT pk_publisher PRIMARY KEY (publisher_id)
);

CREATE SEQUENCE seq_publisher_id minvalue 100;

CREATE TABLE book (
                      book_id INT,
                      title VARCHAR(400),
                      isbn13 VARCHAR(13),
                      num_pages INT,
                      publication_date DATE,
                      publisher_id INT,
                      CONSTRAINT pk_book PRIMARY KEY (book_id),
                      CONSTRAINT fk_book_pub FOREIGN KEY (publisher_id) REFERENCES publisher (publisher_id)
);

CREATE SEQUENCE seq_book_id minvalue 10;

CREATE TABLE book_author (
                             book_id INT,
                             author_id INT,
                             CONSTRAINT pk_bookauthor PRIMARY KEY (book_id, author_id),
                             CONSTRAINT fk_ba_book FOREIGN KEY (book_id) REFERENCES book (book_id),
                             CONSTRAINT fk_ba_author FOREIGN KEY (author_id) REFERENCES author (author_id)
);