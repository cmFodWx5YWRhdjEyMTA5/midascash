package midascash.indonesia.optima.prima.midascash.sqlite;

import java.util.Date;

/**
 * Created by rwina on 4/20/2018.
 */

public class expense {
    String expense_account,username,expense_to,expense_notes,expense_type,expense_id,expense_image;
    Date expense_date,expense_createdate;
    float expense_amount;

    public expense(){}

    public expense(String eacc,String etype,String efr,String enot,String eid,Date edat,Date ecdat,float emon,String user){
        username = user;
        expense_account=eacc;
        expense_amount=emon;
        expense_createdate=ecdat;
        expense_to=efr;
        expense_date=edat;
        expense_type=etype;
        expense_notes=enot;
        expense_id=eid;
    }

    //setter
    public void setUsername(String euser){username=euser;}

    public void setExpense_image(String eim){expense_image=eim;}

    public void setexpense_account(String eacc){
        expense_account=eacc;
    }

    public void setexpense_type(String etype){
        expense_type=etype;
    }

    public void setexpense_from(String efr){
        expense_to=efr;
    }

    public void setexpense_notes(String enot){
        expense_notes=enot;
    }

    public void setexpense_id(String eid){
        expense_id=eid;
    }

    public void setexpense_date(Date edat){
        expense_date=edat;
    }

    public void setexpense_createdate(Date ecdat){
        expense_createdate=ecdat;
    }
    public void setexpense_amount(float emon){
        expense_amount=emon;
    }

    //getter


    public String getUsername() {
        return username;
    }

    public String getExpense_image() {
        return expense_image;
    }

    public Date getExpense_createdate() {
        return expense_createdate;
    }

    public String getexpense_account(){
        return expense_account;
    }
    public String getexpense_type(){
        return expense_type;
    }
    public String getexpense_to(){
        return expense_to;
    }
    public String getexpense_notes(){
        return expense_notes;
    }
    public float getexpense_amount(){
        return expense_amount;
    }
    public String getexpense_id(){
        return expense_id;
    }
    public Date getexpense_date(){
        return expense_date;
    }
}
