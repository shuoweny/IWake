package com.COMP900018.finalproject.data;

public class Friend {

    private  String uId;
    private String avatarUrl;
    private String name;
    private String status;

    private String receiveReaction;

    private String givenReaction;

    private String email;

    public Friend(String uId, String name, String email, String status, String reaction, String givenReaction, String avatarUrl) {
        this.uId = uId;
        this.name = name;
        this.status = status;
        this.receiveReaction = reaction;
        this.givenReaction = givenReaction;
        this.avatarUrl = avatarUrl;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getReceiveReaction() {
        return receiveReaction;
    }

    public void setReceiveReaction(String receiveReaction) {
        this.receiveReaction = receiveReaction;
    }

    public String getGivenReaction() {
        return givenReaction;
    }

    public void setGivenReaction(String givenReaction) {
        this.givenReaction = givenReaction;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
