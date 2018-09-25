
package ru.grakhell.oauthlib.model;

import java.util.List;
import com.squareup.moshi.Json;

public class TokenServiceResponse {

    @Json(name = "id")
    private Integer id;
    @Json(name = "url")
    private String url;
    @Json(name = "scopes")
    private List<String> scopes = null;
    @Json(name = "token")
    private String token;
    @Json(name = "token_last_eight")
    private String tokenLastEight;
    @Json(name = "hashed_token")
    private String hashedToken;
    @Json(name = "app")
    private App app;
    @Json(name = "note")
    private String note;
    @Json(name = "note_url")
    private String noteUrl;
    @Json(name = "updated_at")
    private String updatedAt;
    @Json(name = "created_at")
    private String createdAt;
    @Json(name = "fingerprint")
    private String fingerprint;
    @Json(name = "user")
    private User user;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TokenServiceResponse() {
    }

    /**
     * 
     * @param updatedAt
     * @param tokenLastEight
     * @param id
     * @param fingerprint
     * @param app
     * @param scopes
     * @param noteUrl
     * @param token
     * @param createdAt
     * @param hashedToken
     * @param user
     * @param note
     * @param url
     */
    public TokenServiceResponse(Integer id, String url, List<String> scopes, String token, String tokenLastEight, String hashedToken, App app, String note, String noteUrl, String updatedAt, String createdAt, String fingerprint, User user) {
        super();
        this.id = id;
        this.url = url;
        this.scopes = scopes;
        this.token = token;
        this.tokenLastEight = tokenLastEight;
        this.hashedToken = hashedToken;
        this.app = app;
        this.note = note;
        this.noteUrl = noteUrl;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.fingerprint = fingerprint;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteUrl() {
        return noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
