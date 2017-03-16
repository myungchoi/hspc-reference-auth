package org.smartplatforms.oauth2;

import org.mitre.oauth2.token.StructuredScopeAwareOAuth2RequestValidator;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;

public class ScopeValidator extends StructuredScopeAwareOAuth2RequestValidator {

	@Override
	public void validateScope(AuthorizationRequest authorizationRequest, ClientDetails client) throws InvalidScopeException {
		super.validateScope(authorizationRequest, client);
		if (authorizationRequest.getExtensions().get("invalid_launch") != null) {
	 		throw new InvalidScopeException((String)authorizationRequest.getExtensions().get("invalid_launch"));
		}
	}

}
