package midascash.indonesia.optima.prima.midascash.sqlite;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by rwina on 4/20/2018.
 */

public class income {
    String income_account,income_type,income_from,income_notes,income_id;
    Date income_date,income_createdate;
    BigInteger income_amount;

    public income(){}

    public income(String iacc,String itype,String ifr,String inot,String iid,Date idat,Date icdat,BigInteger imon){
        income_account=iacc;
        income_amount=imon;
        income_createdate=icdat;
        income_from=ifr;
        income_date=idat;
        income_type=itype;
        income_notes=inot;
        income_id=iid;
    }

    //setter

    public void setIncome_account(String iacc){
        income_account=iacc;
    }

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
    public void setIncome_amount(BigInteger imon){
        income_amount=imon;
    }

    //getter

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
    public BigInteger getIncome_amount(){
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
