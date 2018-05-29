package midascash.indonesia.optima.prima.midascash.sqlite;

import java.util.Date;

public class user {
    String username,password;
    Date regisdate;
    int isadmin,online;

    public user(){}

    public user(String user ,String pass , Date rdat , int isadmin,int online){
        username=user;
        password=pass;
        regisdate=rdat;
        this.isadmin=isadmin;
        this.online=online;
    }

    public void setIsadmin(int isadmin) {
        this.isadmin = isadmin;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegisdate(Date regisdate) {
        this.regisdate = regisdate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Date getRegisdate() {
        return regisdate;
    }

    public int getIsadmin() {
        return isadmin;
    }

    public int getOnline() {
        return online;
    }

    public String getPassword() {
        return password;
    }
}
