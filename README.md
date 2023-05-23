Jakarta Book Shop
===
## Overview
This repository is a training exercise on learning how to build a simple CRUD website based on
the Jakarta EE 9 web profile.

## Goals
The goals of this exercise are as follows:
* Create a simple REST API representing a bookshop
  * Learn to utilize advanced REST concepts such as HATEOAS
* Create a simple UI over top of the API using JSF
* Learn the following Jakarta EE Components
  * JAX-RS 3.0
  * JSF 3.0
  * JSONB 2.0
  * Annotations 2.0
  * JPA 3.0
  * Managed Beans 2.0
  * CDI 3.0
  * Security 2.0
  * Authentication 2.0

## Setup
The following infrastructure is required to build and deploy locally
* Gradle or use of `gradlew`
* JDK 11+
* Glassfish 6 Web profile
  * default database java:comp/DefaultDataSource
  * initial database setup via scripts stored in `src/main/resources/META-INF/sql`
  * server.properties file stored in `<domain>/lib/classes/server.properties`
    * required props: `hostname` and `protocol` that enable access to API from JSF servlet
  * a `bookshop-realm` Realm setup in the domain's security for basic auth
    * Example: Create a "bookshop-keyfile" in `domain1/config` and build a File-based Realm over top of it.

## Results
* The documentation from Jakarta is unapproachable when it comes to using it for quick reference
* The community is severely lacking or relatively non-existent, with most Q/A results being ~10 years old
* The requirement of Jakarta to maintain a high level of backward compatibility, coupled with the lack of current community, makes it extremely difficult to understand what's a current standard and what's outdated
* There's nothing inherent to most Jakarta EE components/specs that proves to be of sufficient desirability that cannot be found in another ecosystem
* JSF is just painfully frustrating to work with and there doesn't seem to be any documentation on the common tag libraries
* Automatic Bean Discovery and the litany of easy-to-use tags in JAX-RS are really nice for quickly generating REST APIs

**Conclusion**: The specifications are a great means of ensuring expected functionality baked into the language, but without a proper ecosystem like Spring to help manage it, as a well as an active community championing it, it's more effort than it's worth