# README #

Welcome to the HSPC Reference Authorization server!  The HSPC Reference Authorization server contains a MITRE OpenID Connect server in two flavors, a MySQL-backed and an LDAP-backed web application.  This version is the LDAP-backed version.

## reference-auth-server-ldap-webapp ##
LDAP-backed overly of the webapp module that overlays MITRE's OpenID Connect server webapp.  MySQL is used to store the OpenID Connect configuration and LDAP is used to store the user information.

## How do I get set up? ##

### Preconditions ###
    MySQL must be installed for both the webapp and the ldap-webapp (using InnoDB tables). These files exist in the reference-auth-server-webapp repository.
    From MySQL
    mysql> create database oic;  **Note: use the "latin1 - default collation" when creating the oic schema.  This will prevent a "Row size too large error" when running mysql_database_tables.sql.
    mysql> use oic;
    mysql> source {install path}/reference-auth-server-webapp/src/main/resources/db/openidconnect/mysql/mysql_database_tables.sql;
    mysql> source {install path}/reference-auth-server-webapp/src/main/resources/db/openidconnect/mysql/mysql_users.sql;
    mysql> source {install path}/reference-auth-server-webapp/src/main/resources/db/openidconnect/mysql/mysql_system_scopes.sql;
    mysql> source {install path}/reference-auth-server-webapp/src/main/resources/db/openidconnect/mysql/mysql_clients.sql;
    *note See HSPC applications for loading individual OAuth client configurations

### Alternate ###
    The complete configuration for the HSPC codebase may be loaded using a single script.
    See the reference-impl repository for details.

### Install ApacheDS LDAP Server ###

Home:
    https://directory.apache.org/apacheds/

Download:
    https://directory.apache.org/apacheds/downloads.html

Apache Directory Studio (UI For ApacheDS, distributes with it's own ApacheDS)
    https://directory.apache.org/studio/downloads.html


Create User account/ Group etc...
    export from \reference-auth\ldap\src\main\resources\ldap\hspc.ldif

### Build and Run ###
    mvn clean install
    deploy reference-auth-server-ldap-webapp/target/hspc-reference-authorization.war to Tomcat

### Verify ###
* http://localhost:8080/hspc-reference-authorization/

## Configuration of webapp ##
|Property | Default Value | Notes
|---|---|---|
| oidc.issuer | http://localhost:8080/hspc-reference-authorization/ |  |
| oidc.datasource.mysql.url | jdbc:mysql://localhost/oic |  |
| oidc.datasource.mysql.username | root |  |
| oidc.datasource.mysql.password | [mysql password] |  |
| CONTEXT_FHIR_ENDPOINT | http://localhost:8080/hspc-reference-api/data | Used by the SMART context resolver to reference the FHIR server |
| CONTEXT_RESOLVE_ENDPOINT | http://localhost:8080/hspc-sandbox-manager |  |

## Configuration of ldap-webapp ##
|Property | Default Value | Notes
|---|---|---|
| ldap.url | ldap://sandbox.hspconsortium.org:10389 |  |
| ldap.server | ldap://sandbox.hspconsortium.org:10389/dc=hspconsortium,dc=org |  |
| ldap.base | dc=hspconsortium,dc=org |  |
| ldap.userDn | uid=admin,ou=system |  |
| ldap.password | [ldap secret] |  |
| ldap.pooled | false |  |

## Where to go from here ##
https://healthservices.atlassian.net/wiki/display/HSPC/Healthcare+Services+Platform+Consortium