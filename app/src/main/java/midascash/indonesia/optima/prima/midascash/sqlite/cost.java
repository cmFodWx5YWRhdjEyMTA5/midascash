package midascash.indonesia.optima.prima.midascash.sqlite;

import android.content.Intent;

import org.xml.sax.DTDHandler;

import java.math.BigInteger;
import java.sql.Date;

/**
 * Created by rwina on 4/20/2018.
 */

public class cost {
    String cost_name,cost_image,username,cost_category;
    BigInteger cost_amount;
    int cost_type,cost_duration;
    Date cost_createdate;

    public cost(){

    }

    public cost(String cnam, String cim, String user, String ccat, BigInteger cmon, int ctype, int cdur, Date cdat){
       cost_amount=cmon;
       cost_category=ccat;
       cost_duration=cdur;
       cost_type=ctype;
       cost_createdate=cdat;
       cost_image=cim;
       username=user;
       cost_name=cnam;
    }
    //setter

    public  void setCost_image(String cim){
        cost_image=cim;
    }
    public  void setUsername(String user){
        username=user;
    }
    public  void setCost_name(String cnam){
        cost_name=cnam;
    }
    public  void setCost_type(int ctype){
        cost_type=ctype;
    }
    public  void setCost_category(String ccat){
        cost_category=ccat;
    }
    public  void setCost_amount(BigInteger cmon){
        cost_amount=cmon;
    }
    public void  setCost_duration(int cdat){
        cost_duration=cdat;
    }
    public  void setCost_createdate(Date cdat){
        cost_createdate=cdat;
    }

    //getter

    public String getCost_name(){
        return  cost_name;
    }
    public String getCost_image(){
        return cost_image;
    }
    public String getUsername(){
        return username;
    }
    public String getCost_category(){
        return cost_category;
    }
    public Date getCost_createdate(){
        return cost_createdate;
    }
    public BigInteger getCost_amount(){
        return cost_amount;
    }
    public int getCost_type(){
        return cost_type;
    }
    public int getCost_duration(){
        return cost_duration;
    }
}
