
package ru.grakhell.oauthlib.model;

import java.util.List;
import com.squareup.moshi.Json;

//Autogenerated

public class Query {

    @Json(name = "client_id")
    private String clientId;
    @Json(name = "client_secret")
    private String clientSecret;
    @Json(name = "scopes")
    private List<String> scopes = null;
    @Json(name = "note")
    private String note;

    /**
     * No args constructor for use in serialization
     *
     */
    public Query() {
    }

    /**
     *
     * @param scopes - list of scopes
     * @param clientSecret - client api secret
     * @param clientId - client api id
     * @param note - key note
     */
    public Query(String clientId, String clientSecret, List<String> scopes, String note){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scopes = scopes;
        this.note = note;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}