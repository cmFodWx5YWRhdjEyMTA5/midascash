package prima.optimasi.indonesia.primacash.objects;

import java.sql.Time;
import java.util.Date;

/**
 * Created by rwina on 4/20/2018.
 */

public class reminder {
    String reminder_name,reminder_note,reminder_expense,reminder_income,username;
    Date reminder_createdate;
    Time reminder_time;
    int reminder_status;

    public reminder(){
        
    }
    public reminder(String rnam,String rnot,String rexp, String rin,Date rcdat,Time rtim,int rstat,String user){
        username=user;
        reminder_name=rnam;
        reminder_note=rnot;
        reminder_expense=rexp;
        reminder_income=rin;
        reminder_createdate=rcdat;
        reminder_time=rtim;
        reminder_status=rstat;
    }

    //setter

    public void setUsername(String user){
        username = user;
    }
    public void setReminder_name(String rnam){
        reminder_name=rnam;
    }

    public void setReminder_note(String rnot){
        reminder_note=rnot;
    }

    public void setReminder_expense(String rexp){
        reminder_expense=rexp;
    }
    public void setReminder_income(String rin){
        reminder_income=rin;
    }
    public void setReminder_createdate(Date rcdat){
        reminder_createdate =rcdat;
    }
    public void setReminder_time(Time rtim){
        reminder_time=rtim;
    }
    public void setReminder_status(int stat){
        reminder_status=stat;
    }

    //getter


    public String getUsername() {
        return username;
    }

    public String getReminder_name(){
        return reminder_name;
    }
    public String getReminder_note(){
        return reminder_note;
    }
    public String getReminder_expense(){
        return reminder_expense;
    }
    public Date getReminder_createdate(){
        return reminder_createdate;
    }
    public Time getReminder_time(){
        return reminder_time;
    }
    public int getReminder_status(){
        return reminder_status;
    }
    public String getReminder_income(){
        return reminder_income;
    }

}
