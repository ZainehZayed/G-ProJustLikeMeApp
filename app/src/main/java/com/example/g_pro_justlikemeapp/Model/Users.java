package com.example.g_pro_justlikemeapp.Model;

public class Users {
    String username, imageURL, id, status,isAdmin,UserType,email;

    public Users(String username, String imageURL, String id, String status,String isAdmin,String UserType,String email) {
        this.username = username;
        this.imageURL = imageURL;
        this.id = id;
        this.status = status;
        this.isAdmin=isAdmin;
        this.UserType=UserType;
        this.email=email;
    }

    public Users() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
    public String getUserType() {
        return UserType;
    }

    public void setUserType(String username) {
        this.UserType = UserType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}



