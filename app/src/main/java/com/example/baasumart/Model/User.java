package com.example.baasumart.Model;

public class User {
    private String email;
    private String username;
    private String profilePic;
    private String id;
    private String displayName;
    private boolean isPremium;
    private String token;

    public User(String email, String username, String profilePic, String id, String displayName, boolean isPremium, String token) {
        this.email = email;
        this.username = username;
        this.profilePic = profilePic;
        this.id = id;
        this.displayName = displayName;
        this.isPremium = isPremium;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
