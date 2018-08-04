package prima.optimasi.indonesia.primacash.objects;

import android.widget.ScrollView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class income implements Comparable<income>  {
    String income_account,income_type,income_from,income_notes,income_id,username,income_category;
    int income_image;
    String income_date,income_createdate;
    double income_amount;
    String incomedoc;


    public income(){}

    public income(String iacc,String itype,String ifr,String inot,String iid,String idat,String icdat,double imon,int image,String icat,String user){
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


    public void setIncome_category(String income_category) {
        this.income_category = income_category;
    }

    public void setIncomedoc(String incomedoc) {
        this.incomedoc = incomedoc;
    }

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

    public void setIncome_date(String idat){
        income_date=idat;
    }

    public void setIncome_createdate(String icdat){
        income_createdate=icdat;
    }
    public void setIncome_amount(double imon){
        income_amount=imon;
    }

    //getter


    public String getIncome_category() {
        return income_category;
    }

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

    public String getIncomedoc() {
        return incomedoc;
    }

    public String getIncome_id(){
        return income_id;
    }
    public String getIncome_date(){
        return income_date;
    }
    public String getIncome_createdate(){
        return income_createdate;
    }

    @Override
    public int compareTo(income o) {
        if (getIncome_date() == null ||o.getIncome_date()  == null)
            return 0;
        else {
            String string1 = getIncome_date();
            String string = o.getIncome_date();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date = null;
            Date date1 = null;
            try {
                date = format.parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                date1 = format.parse(string1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date1.compareTo(date);
        }
    }
}
