TRUNCATE TABLE BOOK_AUTHOR;
TRUNCATE TABLE PUBLISHER;
TRUNCATE TABLE BOOK;

TRUNCATE TABLE AUTHOR;

insert into AUTHOR(author_id, author_name)
VALUES (42, 'N.K. Jemisin');

insert into AUTHOR(author_id, author_name)
VALUES(2112, 'Rick Yancey');

insert into PUBLISHER(PUBLISHER_ID, publisher_name)
VALUES (100, 'Hachette Book Group'),
       (101, 'G.P. Putnam''s Sons Books for Young Readers');

insert into BOOK(book_id, title, isbn13, num_pages, publication_date, publisher_id)
VALUES (100, 'The Fifth Season', '9780316229302', 512, '2015-09-04', 100);
insert into BOOK(book_id, title, isbn13, num_pages, publication_date, publisher_id)
VALUES (101, 'The Obelisk Gate', '9780316229289', 448, '2016-09-16', 100);
-- insert into BOOK(book_id, title, isbn13, num_pages, publication_date, publisher_id)
-- VALUES (102, 'The Stone Sky', '9780316229258', 464, '2017-09-15', 100);

insert into BOOK_AUTHOR(book_id, author_id)
VALUES (100, 42),
       (101, 42);
--     (102, 42);