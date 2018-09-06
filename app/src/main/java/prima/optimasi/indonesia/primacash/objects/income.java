package prima.optimasi.indonesia.primacash.objects;

import android.widget.ScrollView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class income implements Comparable<income>  {
    String income_account,income_type,income_from,income_notes,income_id,username,income_category;
    int income_image,income_isdone;
    String income_date,income_createdate;
    byte[] income_imagechosen;
    double income_amount;
    Integer income_isdated,income_count,income_times;
    String income_period;
    String incomedoc;


    public income(){
    }

    //setter


    public void setIncome_isdone(int income_isdone) {
        this.income_isdone = income_isdone;
    }

    public void setIncome_times(Integer income_times) {
        this.income_times = income_times;
    }

    public void setIncome_count(Integer income_count) {
        this.income_count = income_count;
    }

    public void setIncome_isdated(Integer income_isdated) {
        this.income_isdated = income_isdated;
    }

    public void setIncome_period(String income_period) {
        this.income_period = income_period;
    }

    public void setIncome_imagechosen(byte[] income_imagechosen) {
        this.income_imagechosen = income_imagechosen;
    }

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


    public Integer getIncome_times() {
        return income_times;
    }

    public Integer getIncome_count() {
        return income_count;
    }

    public Integer getIncome_isdated() {
        return income_isdated;
    }

    public String getIncome_period() {
        return income_period;
    }
    public String getIncome_category() {
        return income_category;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getIncome_imagechosen() {
        return income_imagechosen;
    }

    public int getIncome_image() {
        return income_image;
    }


    public int getIncome_isdone() {
        return income_isdone;
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
