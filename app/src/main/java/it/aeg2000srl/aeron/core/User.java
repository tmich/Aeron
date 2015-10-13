package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 13/10/2015.
 */
public class User {
    protected String username;
    protected String code;
    protected long id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
