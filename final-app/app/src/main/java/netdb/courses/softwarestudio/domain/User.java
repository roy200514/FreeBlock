package netdb.courses.softwarestudio.domain;


import netdb.courses.softwarestudio.rest.model.Resource;

public class User extends Resource {

	private String userid;
	private String password;
    private String name;
    private String phonenumber;

	public User() {
	}

	public User(String userid, String password, String name, String phonenumber) {
		this.userid = userid;
		this.password = password;
        this.name = name;
        this.phonenumber = phonenumber;
	}
    public static String getCollectionName(){ return "users";}

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

    public void setPhonenumber(String phonenumber){this.phonenumber = phonenumber;}

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setUsername(String name) {
        this.name = name;
    }

    public String getUsername() {
        return this.name;
    }
}
