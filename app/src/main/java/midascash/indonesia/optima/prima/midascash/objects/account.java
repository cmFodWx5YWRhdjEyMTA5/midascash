package midascash.indonesia.optima.prima.midascash.objects;


/**
 * Created by rwina on 4/20/2018.
 */

public class account{
    String account_name;
    String account_category;
    String account_currency;
    String fullaccount_currency;
    Object account_createdate;
    String account_balance;
    String account_balance_current;
    String username;
    int account_status;
    int createorlast;

    public account(){

    }
    public account(String accname,String acccat,Object accdate,String accbal,String accbalcur,String user,int stat,String account_currency,String fullaccount_currency){
        account_name=accname;
        account_balance = accbal;
        account_category=acccat;
        account_createdate =accdate;
        username = user;
        account_balance_current=accbalcur;
        account_status = stat;
        this.fullaccount_currency=fullaccount_currency;
        this.account_currency=account_currency;
    }
    public account(String accname,String acccat,Object accdate,String accbal,String accbalcur,String user,int stat,String account_currency,String fullaccount_currency,int last){
        account_name=accname;
        account_balance = accbal;
        account_category=acccat;
        account_createdate =accdate;
        username = user;
        account_balance_current=accbalcur;
        account_status = stat;
        this.fullaccount_currency=fullaccount_currency;
        this.account_currency=account_currency;
        createorlast = last;
    }

    //setter

    public void setAccount_currency(String account_currency) {
        this.account_currency = account_currency;
    }

    public void setFullaccount_currency(String fullaccount_currency) {
        this.fullaccount_currency = fullaccount_currency;
    }

    public void setCreateorlast(int createorlast) {
        this.createorlast = createorlast;
    }

    public void setAccount_name(String accname){
        account_name=accname;
    }

    public void setAccount_category(String acccat){
        account_category=acccat;
    }

    public void setAccount_createdate(Object accdat){
        account_createdate=accdat;
    }

    public void setUsername (String user){
        username=user;
    }

    public void setAccount_balance(String accbal){
        account_balance=accbal;
    }
    public void setAccount_status(int stat){
        account_status=stat;
    }

    public void setAccount_balance_current(String account_balance_current) {
        this.account_balance_current = account_balance_current;
    }
//getter


    public int getCreateorlast() {
        return createorlast;
    }

    public String getAccount_name(){
        return account_name;
    }

    public String getAccount_category(){
        return account_category;
    }

    public String getUsername(){
        return username;
    }

    public String getAccount_balance(){
        return account_balance;
    }
    public int getAccount_status(){
        return account_status;
    }
    public Object getAccount_createdate(){
        return account_createdate;
    }

    public String getAccount_currency() {
        return account_currency;
    }

    public String getAccount_balance_current() {
        return account_balance_current;
    }

    public String getFullaccount_currency() {
        return fullaccount_currency;
    }

}
