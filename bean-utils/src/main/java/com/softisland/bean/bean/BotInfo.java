package com.softisland.bean.bean;

/**
 * Created by liwx on 16/3/17.
 */
public class BotInfo {
    /**
     * 机器人ID
     */
    private long steamId;

    private String password;

    private String shared_secret;

    private String serial_number;

    private String revocation_code;

    private String uri;

    private String server_time;

    private String account_name;

    private String token_gid;

    private String identity_secret;

    private String secret_1;

    private String status;

    private String device_id;

    private String fully_enrolled;

    private String api_key;

    private String type;

    private String bot_name;

    private String trade_url;

    private String host;

    private String app_id;

    public String getTrade_url() {
        return trade_url;
    }

    public void setTrade_url(String trade_url) {
        this.trade_url = trade_url;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getSteamId() {
        return steamId;
    }

    public void setSteamId(long steamId) {
        this.steamId = steamId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShared_secret() {
        return shared_secret;
    }

    public void setShared_secret(String shared_secret) {
        this.shared_secret = shared_secret;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getRevocation_code() {
        return revocation_code;
    }

    public void setRevocation_code(String revocation_code) {
        this.revocation_code = revocation_code;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getToken_gid() {
        return token_gid;
    }

    public void setToken_gid(String token_gid) {
        this.token_gid = token_gid;
    }

    public String getIdentity_secret() {
        return identity_secret;
    }

    public void setIdentity_secret(String identity_secret) {
        this.identity_secret = identity_secret;
    }

    public String getSecret_1() {
        return secret_1;
    }

    public void setSecret_1(String secret_1) {
        this.secret_1 = secret_1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getFully_enrolled() {
        return fully_enrolled;
    }

    public void setFully_enrolled(String fully_enrolled) {
        this.fully_enrolled = fully_enrolled;
    }

    public String getBot_name() {
        return bot_name;
    }

    public void setBot_name(String bot_name) {
        this.bot_name = bot_name;
    }
}
