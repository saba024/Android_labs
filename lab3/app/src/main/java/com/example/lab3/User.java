package com.example.lab3;

public class User
{
    private String playerSession = "";
    private String userName = "";
    private String otherPlayer = "";
    private String loginUID = "";
    private String requestType = "";
    private String url = "";
    private boolean isGravatar = false;

    User(){}

    public String getLoginUID() {
        return loginUID;
    }

    public String getOtherPlayer() {
        return otherPlayer;
    }

    public String getPlayerSession() {
        return playerSession;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getUserName() {
        return userName;
    }

    public void setLoginUID(String loginUID) {
        this.loginUID = loginUID;
    }

    public void setOtherPlayer(String otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    public void setPlayerSession(String playerSession) {
        this.playerSession = playerSession;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isGravatar() {
        return isGravatar;
    }

    public void setGravatar(boolean gravatar) {
        isGravatar = gravatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
