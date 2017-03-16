SET AUTOCOMMIT = 0;

START TRANSACTION;

-- My Web App
INSERT INTO client_details (client_id, client_name, logo_uri, access_token_validity_seconds, token_endpoint_auth_method) VALUES
	('my_web_app', 'My Web App', 'https://fhir.smartplatforms.org/images/apps/my.png', 86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'my_web_app'), 'launch'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'patient/*.read');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'my_web_app'), 'authorization_code');


COMMIT;

SET AUTOCOMMIT = 1;
