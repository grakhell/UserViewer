
package ru.grakhell.oauthlib.model;

import java.util.List;
import com.squareup.moshi.Json;

public class TokenServiceResponse {

    @Json(name = "id")
    private Integer id;
    @Json(name = "scopes")
    private List<String> scopes = null;
    @Json(name = "token")
    private String token;
    @Json(name = "token_last_eight")
    private String tokenLastEight;
    @Json(name = "hashed_token")
    private String hashedToken;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TokenServiceResponse() {
    }

    /**
     * 
     * @param tokenLastEight
     * @param id
     * @param scopes
     * @param token
     * @param hashedToken
     */
    public TokenServiceResponse(Integer id, List<String> scopes, String token, String tokenLastEight, String hashedToken) {
        super();
        this.id = id;
        this.scopes = scopes;
        this.token = token;
        this.tokenLastEight = tokenLastEight;
        this.hashedToken = hashedToken;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenLastEight() {
        return tokenLastEight;
    }

    public void setTokenLastEight(String tokenLastEight) {
        this.tokenLastEight = tokenLastEight;
    }

    public String getHashedToken() {
        return hashedToken;
    }

    public void setHashedToken(String hashedToken) {
        this.hashedToken = hashedToken;
    }

}
