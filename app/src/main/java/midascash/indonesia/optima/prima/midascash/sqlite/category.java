package midascash.indonesia.optima.prima.midascash.sqlite;

import java.util.Date;

/**
 * Created by rwina on 4/20/2018.
 */

public class category {
    String category_name,username;
    int category_image;
    Date category_createdate;
    int category_status;

    public category(){

    }
    public category(String catname,int catim,String user,Date catdat,int catstat){
        category_name =catname;
        category_image =catim;
        username=user;
        category_createdate =catdat;
        category_status =catstat;

    }

    //setter

    public void setCategory_name(String catnam)
    {
        category_name=catnam;
    }

    public void setUsername(String user){
        username = user;
    }

    public void setCategory_image(int catim){
        category_image =catim;
    }

    public void setCategory_createdate(Date catdat){
        category_createdate=catdat;
    }

    public void setCategory_status(int stat){
        category_status=stat;
    }
    //getter

    public String getCategory_name(){
        return category_name;
    }
    public int getCategory_image(){
        return category_image;
    }
    public String getUsername(){
        return username;
    }

    public Date getCategory_createdate(){
        return category_createdate;
    }

    public int getCategory_status(){
        return category_status;
    }

}
