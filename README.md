# HSPC Reference Auth Parent

Welcome to the HSPC Reference Authorization server!  The HSPC Reference Authorization server contains a MITRE OpenID Connect server in two flavors, a MySQL-backed and an LDAP-backed web application.  This version is the LDAP-backed version.

# HSPC Sandbox

*Note:* If you are wanting to build and test SMART on FHIR Apps, it is recommended that you use the free cloud-hosted version of the HSPC Sandbox.

[HSPC Sandbox](https://sandbox.hspconsortium.org)

## reference-auth-server-ldap-webapp
LDAP-backed overly of the webapp module that overlays MITRE's OpenID Connect server webapp.  MySQL is used to store the OpenID Connect configuration and LDAP is used to store the user information.

## How do I get set up?

### Preconditions
    MySQL must be installed for both the webapp and the ldap-webapp (using InnoDB tables). These files exist in the reference-auth-server-webapp repository.
    From MySQL
    mysql> create database oic;  **Note: use the "latin1 - default collation" when creating the oic schema.  This will prevent a "Row size too large error" when running mysql_database_tables.sql.
    mysql> use oic;
    mysql> source {install path}/reference-auth-server-webapp/src/main/resources/db/openidconnect/mysql/mysql_database_tables.sql;
    mysql> source {install path}/reference-auth-server-webapp/src/main/resources/db/openidconnect/mysql/mysql_users.sql;
    mysql> source {install path}/reference-auth-server-webapp/src/main/resources/db/openidconnect/mysql/mysql_system_scopes.sql;
    mysql> source {install path}/reference-auth-server-webapp/src/main/resources/db/openidconnect/mysql/mysql_clients.sql;
    *note See HSPC applications for loading individual OAuth client configurations

### Alternate
    The complete configuration for the HSPC codebase may be loaded using a single script.
    See the reference-impl repository for details.

### Install ApacheDS LDAP Server

Home:
    https://directory.apache.org/apacheds/

Download:
    https://directory.apache.org/apacheds/downloads.html

Apache Directory Studio (UI For ApacheDS, distributes with it's own ApacheDS)
    https://directory.apache.org/studio/downloads.html


Create User account/ Group etc...
    export from \reference-auth\ldap\src\main\resources\ldap\hspc.ldif

### Build and Run
    mvn clean install
    deploy reference-auth-server-ldap-webapp/target/hspc-reference-authorization.war to Tomcat

### Verify
* http://localhost:8060/

## Where to go from here
https://healthservices.atlassian.net/wiki/display/HSPC/Healthcare+Services+Platform+Consortium