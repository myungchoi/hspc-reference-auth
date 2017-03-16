/*******************************************************************************
 * Copyright 2015 The MITRE Corporation
 *   and the MIT Kerberos and Internet Trust Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/**
 * 
 */
package org.mitre.oauth2.model;

import com.google.common.collect.Sets;
import com.nimbusds.jwt.JWT;
import org.mitre.oauth2.model.convert.JWTStringConverter;
import org.mitre.uma.model.Permission;
import org.smartplatforms.oauth2.LaunchContextEntity;
import org.springframework.security.oauth2.common.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author jricher
 *
 */
@Entity
@Table(name = "access_token")
@NamedQueries({
	@NamedQuery(name = OAuth2AccessTokenEntity.QUERY_ALL, query = "select a from OAuth2AccessTokenEntity a"),
	@NamedQuery(name = OAuth2AccessTokenEntity.QUERY_EXPIRED_BY_DATE, query = "select a from OAuth2AccessTokenEntity a where a.expiration <= :" + OAuth2AccessTokenEntity.PARAM_DATE),
	@NamedQuery(name = OAuth2AccessTokenEntity.QUERY_BY_REFRESH_TOKEN, query = "select a from OAuth2AccessTokenEntity a where a.refreshToken = :" + OAuth2AccessTokenEntity.PARAM_REFERSH_TOKEN),
	@NamedQuery(name = OAuth2AccessTokenEntity.QUERY_BY_CLIENT, query = "select a from OAuth2AccessTokenEntity a where a.client = :" + OAuth2AccessTokenEntity.PARAM_CLIENT),
	@NamedQuery(name = OAuth2AccessTokenEntity.QUERY_BY_ID_TOKEN, query = "select a from OAuth2AccessTokenEntity a where a.idToken = :" + OAuth2AccessTokenEntity.PARAM_ID_TOKEN),
	@NamedQuery(name = OAuth2AccessTokenEntity.QUERY_BY_TOKEN_VALUE, query = "select a from OAuth2AccessTokenEntity a where a.jwt = :" + OAuth2AccessTokenEntity.PARAM_TOKEN_VALUE),
	@NamedQuery(name = OAuth2AccessTokenEntity.QUERY_BY_RESOURCE_SET, query = "select a from OAuth2AccessTokenEntity a join a.permissions p where p.resourceSet.id = :" + OAuth2AccessTokenEntity.PARAM_RESOURCE_SET_ID)
})
@org.codehaus.jackson.map.annotate.JsonSerialize(using = OAuth2AccessTokenJackson1Serializer.class)
@org.codehaus.jackson.map.annotate.JsonDeserialize(using = OAuth2AccessTokenJackson1Deserializer.class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OAuth2AccessTokenJackson2Serializer.class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OAuth2AccessTokenJackson2Deserializer.class)
public class OAuth2AccessTokenEntity implements OAuth2AccessToken {

	public static final String QUERY_BY_TOKEN_VALUE = "OAuth2AccessTokenEntity.getByTokenValue";
	public static final String QUERY_BY_ID_TOKEN = "OAuth2AccessTokenEntity.getByIdToken";
	public static final String QUERY_BY_CLIENT = "OAuth2AccessTokenEntity.getByClient";
	public static final String QUERY_BY_REFRESH_TOKEN = "OAuth2AccessTokenEntity.getByRefreshToken";
	public static final String QUERY_EXPIRED_BY_DATE = "OAuth2AccessTokenEntity.getAllExpiredByDate";
	public static final String QUERY_ALL = "OAuth2AccessTokenEntity.getAll";
	public static final String QUERY_BY_RESOURCE_SET = "OAuth2AccessTokenEntity.getByResourceSet";

	public static final String PARAM_TOKEN_VALUE = "tokenValue";
	public static final String PARAM_ID_TOKEN = "idToken";
	public static final String PARAM_CLIENT = "client";
	public static final String PARAM_REFERSH_TOKEN = "refreshToken";
	public static final String PARAM_DATE = "date";
	public static final String PARAM_RESOURCE_SET_ID = "rsid";

	public static String ID_TOKEN_FIELD_NAME = "id_token";

	private Long id;

	private ClientDetailsEntity client;
	private AuthenticationHolderEntity authenticationHolder;
	private JWT jwtValue;
	private OAuth2AccessTokenEntity idToken;
	private Date expiration;
    private String tokenType = "Bearer";
	private OAuth2RefreshTokenEntity refreshToken;
	private Set<String> scope;

	private Set<Permission> permissions;

    private Set<LaunchContextEntity> launchContextParams = Sets.newHashSet();

	public OAuth2AccessTokenEntity() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
        return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	public Map<String, Object> getAdditionalInformation() {
		Map<String, Object> map = new HashMap<String, Object>(); //super.getAdditionalInformation();
		if (getIdToken() != null) {
			map.put(ID_TOKEN_FIELD_NAME, getIdTokenString());
        }
        if (getLaunchContext() != null) {
            for (LaunchContextEntity cparam : getLaunchContext()){
                // Hack to cast back boolean values that have been converted into strings
                String val = cparam.getValue();
                if (val.equals("true")) {
                    map.put(cparam.getName(), true);
                } else if (val.equals("false")) {
                    map.put(cparam.getName(), false);
                } else {
                    map.put(cparam.getName(), val);
                }
            }
        }
        return map;
    }

	@ManyToOne
	@JoinColumn(name = "auth_holder_id")
	public AuthenticationHolderEntity getAuthenticationHolder() {
        return this.authenticationHolder;
	}

	public void setAuthenticationHolder(AuthenticationHolderEntity authenticationHolder) {
		this.authenticationHolder = authenticationHolder;
	}

	@ManyToOne
	@JoinColumn(name = "client_id")
	public ClientDetailsEntity getClient() {
        return this.client;
	}

	public void setClient(ClientDetailsEntity client) {
		this.client = client;
	}

	@Transient
	public String getValue() {
        return this.jwtValue.serialize();
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiration")
	public Date getExpiration() {
        return this.expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	@Basic
	@Column(name="token_type")
	public String getTokenType() {
        return this.tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@ManyToOne
	@JoinColumn(name="refresh_token_id")
	public OAuth2RefreshTokenEntity getRefreshToken() {
        return this.refreshToken;
	}

	public void setRefreshToken(OAuth2RefreshTokenEntity refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void setRefreshToken(OAuth2RefreshToken refreshToken) {
		if (!(refreshToken instanceof OAuth2RefreshTokenEntity)) {
			throw new IllegalArgumentException("Not a storable refresh token entity!");
        } else {
            this.setRefreshToken((OAuth2RefreshTokenEntity)refreshToken);
		}
	}

    @ElementCollection(
        fetch = FetchType.EAGER
    )
	@CollectionTable(
        joinColumns = {            @JoinColumn(
                name = "owner_id"
            )},
			name="token_scope"
			)
	public Set<String> getScope() {
        return this.scope;
	}

	public void setScope(Set<String> scope) {
		this.scope = scope;
	}

	@Transient
	public boolean isExpired() {
        return this.getExpiration() == null?false:System.currentTimeMillis() > this.getExpiration().getTime();
	}

    @OneToOne(
        cascade = {CascadeType.ALL}
    )
	@JoinColumn(name = "id_token_id")
	public OAuth2AccessTokenEntity getIdToken() {
        return this.idToken;
	}

	public void setIdToken(OAuth2AccessTokenEntity idToken) {
		this.idToken = idToken;
	}

	@Transient
	public String getIdTokenString() {
        return this.idToken != null?this.idToken.getValue():null;
	}

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="access_token_id")
    public Set<LaunchContextEntity> getLaunchContext() {
        return launchContextParams;
    }

    /**
     * @param launchContextParams the LaunchContextParams to set
     */
    public void setLaunchContext(Set<LaunchContextEntity> launchContextParams) {
        this.launchContextParams = launchContextParams;
    }

    /**
	 * @return the jwtValue
	 */
	@Basic
	@Column(name="token_value")
	@Convert(converter = JWTStringConverter.class)
	public JWT getJwt() {
        return this.jwtValue;
	}

	public void setJwt(JWT jwt) {
		this.jwtValue = jwt;
	}

	@Transient
	public int getExpiresIn() {
        if(this.getExpiration() == null) {
            return -1;
		} else {
            int secondsRemaining = (int)((this.getExpiration().getTime() - System.currentTimeMillis()) / 1000L);
            return this.isExpired()?0:secondsRemaining;
		}
	}

    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = {CascadeType.ALL}
    )
	@JoinTable(
			name = "access_token_permissions",
        joinColumns = {            @JoinColumn(
                name = "access_token_id"
            )},
        inverseJoinColumns = {            @JoinColumn(
                name = "permission_id"
            )}
	)
	public Set<Permission> getPermissions() {
        return this.permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

}
