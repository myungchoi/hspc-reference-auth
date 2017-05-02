package org.hspconsortium.platform.authorization.repository.impl;

/**
 * Created by mike on 4/29/17.
 */
public class FirebaseUserProfileDto {
    private String email;
    private String uid;
    private String displayName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
