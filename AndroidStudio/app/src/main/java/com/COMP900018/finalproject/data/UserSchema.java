package com.COMP900018.finalproject.data;

import java.util.ArrayList;
import java.util.Date;

public class UserSchema {

    private String avatarUrl;
    private String email;

    private String uid;

    private String firstName;

    private String lastName;

    private int level;

    private int points;
    private int friendsBeat = 0;
    private String wakeStatus;

    private Date wakeTime;

    private boolean newReaction;

    private ArrayList<String> friends;

    private ArrayList<String> reaction;

    private ArrayList<String> givenReaction;

    private ArrayList<String> pendingFriend;

    public UserSchema() {
    }

    public UserSchema(String avatarUrl, String email, String uid, String firstName, String lastName, int level,
                      int points, String wakeStatus, ArrayList<String> friends,
                      ArrayList<String> reaction, ArrayList<String> givenReaction, Date wakeTime, ArrayList<String> pendingFriend, boolean newReaction) {
        this.email = email;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.level = level;
        this.points = points;
        this.friends = friends;
        this.reaction = reaction;
        this.givenReaction = givenReaction;
        this.wakeStatus = wakeStatus;
        this.wakeTime = wakeTime;
        this.avatarUrl = avatarUrl;
        this.pendingFriend = pendingFriend;
        this.newReaction = false;
    }

    public boolean isNewReaction() {
        return newReaction;
    }

    public void setNewReaction(boolean newReaction) {
        this.newReaction = newReaction;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public ArrayList<String> getPendingFriend() {
        return pendingFriend;
    }

    public void setPendingFriend(ArrayList<String> pendingFriend) {
        this.pendingFriend = pendingFriend;
    }

    public Date getWakeTime() {
        return wakeTime;
    }

    public void setWakeTime(Date wakeTime) {
        this.wakeTime = wakeTime;
    }

    public String getWakeStatus() {
        return wakeStatus;
    }

    public void setWakeStatus(String wakeStatus) {
        this.wakeStatus = wakeStatus;
    }

    public ArrayList<String> getGivenReaction() {
        return givenReaction;
    }

    public void setGivenReaction(ArrayList<String> givenReaction) {
        this.givenReaction = givenReaction;
    }

    public int getFriendsBeat() {
        return friendsBeat;
    }

    public void setFriendsBeat(int friendsBeat) {
        this.friendsBeat = friendsBeat;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getReaction() {
        return reaction;
    }

    public void setReaction(ArrayList<String> reaction) {
        this.reaction = reaction;
    }


}
