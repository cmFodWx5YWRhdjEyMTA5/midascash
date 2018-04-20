package midascash.indonesia.optima.prima.midascash.sqlite;

import java.math.BigInteger;
import java.sql.Date;

/**
 * Created by rwina on 4/20/2018.
 */

public class account {
    String account_name;
    String account_category;
    Date account_createdate;
    BigInteger account_balance;
    String username;
    int account_status;

    public account(){

    }
    public account(String accname,String acccat,Date accdate,BigInteger accbal,String user,int stat){
        account_name=accname;
        account_balance = accbal;
        account_category=acccat;
        account_createdate =accdate;
        username = user;
        account_status = stat;
    }

    //setter

    public void setAccount_name(String accname){
        account_name=accname;
    }

    public void setAccount_category(String acccat){
        account_category=acccat;
    }

    public void setAccount_createdate(Date accdat){
        account_createdate=accdat;
    }

    public void setUsername (String user){
        username=user;
    }

    public void setAccount_balance(BigInteger accbal){
        account_balance=accbal;
    }
    public void setAccount_status(int stat){
        account_status=stat;
    }

    //getter

    public String getAccount_name(){
        return account_name;
    }

    public String getAccount_category(){
        return account_category;
    }

    public String getUsername(){
        return username;
    }

    public BigInteger getAccount_balance(){
        return account_balance;
    }
    public int getAccount_status(){
        return account_status;
    }
    public Date getAccount_createdate(){
        return account_createdate;
    }

}
