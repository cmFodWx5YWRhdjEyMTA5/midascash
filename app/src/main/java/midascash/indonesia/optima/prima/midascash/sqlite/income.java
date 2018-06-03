package midascash.indonesia.optima.prima.midascash.sqlite;

import java.util.Date;

public class income {
    String income_account,income_type,income_from,income_notes,income_id,username;
    int income_image;
    Date income_date,income_createdate;
    double income_amount;

    public income(){}

    public income(String iacc,String itype,String ifr,String inot,String iid,Date idat,Date icdat,double imon,int image,String user){
        username=user;
        income_account=iacc;
        income_amount=imon;
        income_createdate=icdat;
        income_from=ifr;
        income_date=idat;
        income_type=itype;
        income_notes=inot;
        income_id=iid;
        income_image= image;
    }

    //setter

    public void setIncome_account(String iacc){
        income_account=iacc;
    }

    public void setUsername(String user){
        username=user;
    }

    public void setIncome_image(int image){ income_image=image;}

    public void setIncome_type(String itype){
        income_type=itype;
    }

    public void setIncome_from(String ifr){
        income_from=ifr;
    }

    public void setIncome_notes(String inot){
        income_notes=inot;
    }

    public void setIncome_id(String iid){
        income_id=iid;
    }

    public void setIncome_date(Date idat){
        income_date=idat;
    }

    public void setIncome_createdate(Date icdat){
        income_createdate=icdat;
    }
    public void setIncome_amount(double imon){
        income_amount=imon;
    }

    //getter


    public String getUsername() {
        return username;
    }

    public int getIncome_image() {
        return income_image;
    }



    public String getIncome_account(){
        return income_account;
    }
    public String getIncome_type(){
        return income_type;
    }
    public String getIncome_from(){
        return income_from;
    }
    public String getIncome_notes(){
        return income_notes;
    }
    public double getIncome_amount(){
        return income_amount;
    }
    public String getIncome_id(){
        return income_id;
    }
    public Date getIncome_date(){
        return income_date;
    }
    public Date getIncome_createdate(){
        return income_createdate;
    }

}