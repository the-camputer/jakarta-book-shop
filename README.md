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