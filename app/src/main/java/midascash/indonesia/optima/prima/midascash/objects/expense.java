package midascash.indonesia.optima.prima.midascash.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rwina on 4/20/2018.
 */


public class expense implements Comparable<expense> {
    String expense_account,expense_type,expense_to,expense_notes,expense_id,username,expense_category;
    int expense_image;
    String expense_date,expense_createdate;
    double expense_amount;

    public expense(){}

    public expense(String iacc,String itype,String ifr,String inot,String iid,String idat,String icdat,double imon,int image,String icat,String user){
        username=user;
        expense_account=iacc;
        expense_amount=imon;
        expense_createdate=icdat;
        expense_to=ifr;
        expense_date=idat;
        expense_type=itype;
        expense_notes=inot;
        expense_id=iid;
        expense_image= image;
    }

    //setter


    public void setexpense_category(String expense_category) {
        this.expense_category = expense_category;
    }

    public void setexpense_account(String iacc){
        expense_account=iacc;
    }


    public void setUsername(String user){
        username=user;
    }

    public void setexpense_image(int image){ expense_image=image;}

    public void setexpense_type(String itype){
        expense_type=itype;
    }

    public void setexpense_to(String ifr){
        expense_to=ifr;
    }

    public void setexpense_notes(String inot){
        expense_notes=inot;
    }

    public void setexpense_id(String iid){
        expense_id=iid;
    }

    public void setexpense_date(String idat){
        expense_date=idat;
    }

    public void setexpense_createdate(String icdat){
        expense_createdate=icdat;
    }
    public void setexpense_amount(double imon){
        expense_amount=imon;
    }

    //getter


    public String getexpense_category() {
        return expense_category;
    }

    public String getUsername() {
        return username;
    }

    public int getexpense_image() {
        return expense_image;
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
    public double getexpense_amount(){
        return expense_amount;
    }
    public String getexpense_id(){
        return expense_id;
    }
    public String getexpense_date(){
        return expense_date;
    }
    public String getexpense_createdate(){
        return expense_createdate;
    }

    @Override
    public int compareTo(expense o) {
        if (getexpense_date() == null ||o.getexpense_date()  == null)
            return 0;
        else {
            String string1 = getexpense_date();
            String string = o.getexpense_date();
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
