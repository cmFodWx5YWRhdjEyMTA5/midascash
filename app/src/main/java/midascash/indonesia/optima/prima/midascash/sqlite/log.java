package midascash.indonesia.optima.prima.midascash.sqlite;

import java.util.Date;

/**
 * Created by rwina on 4/24/2018.
 */

public class log {
    String username,log_action;
    Date log_date;

    public log(){}
    public log(String user,String action,Date ldat){
        log_date=ldat;
        username=user;
        log_action=action;
    }

    //setter

    public void setUsername(String user){
        username=user;
    }
    public void setLog_action(String lact){
        log_action=lact;
    }
    public void setLog_date(Date ldat){
        log_date=ldat;
    }

    //getter


    public String getLog_action() {
        return log_action;
    }

    public String getUsername() {
        return username;
    }

    public Date getLog_date() {
        return log_date;
    }
}
