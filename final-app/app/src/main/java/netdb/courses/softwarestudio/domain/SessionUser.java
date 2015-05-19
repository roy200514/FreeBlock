package netdb.courses.softwarestudio.domain;

import netdb.courses.softwarestudio.rest.model.Resource;

/**
 * Created by Mark on 1/17/2015.
 */
public class SessionUser extends Resource {

    private String userid;
    private String password;

    public SessionUser() {

    }

    public SessionUser(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public static String getCollectionName() {
        return "session";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}