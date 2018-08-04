package prima.optimasi.indonesia.primacash.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class incomeexpensetransfer implements Comparable<incomeexpensetransfer> {
    String expense_account,expense_type,expense_to,expense_notes,expense_id,usernameexp,expense_category;
    int expense_image;
    String expense_date,expense_createdate;
    double expense_amount;
    String expensedoc;

    String income_account,income_type,income_from,income_notes,income_id,usernameinc,income_category;
    int income_image;
    String income_date,income_createdate;
    double income_amount;
    String incomedoc;

    String transfer_src,transfer_dest,transfer_notes,usernametrf;
    String transfer_date,transfer_createdate;
    double transfer_amount,transfer_rate;
    String transferdoc;

    String datedata;

    public incomeexpensetransfer(){

    }

    public void setDatedata(String datedata) {
        this.datedata = datedata;
    }

    public void setExpense_account(String expense_account) {
        this.expense_account = expense_account;
    }

    public void setExpense_amount(double expense_amount) {
        this.expense_amount = expense_amount;
    }

    public void setExpense_notes(String expense_notes) {
        this.expense_notes = expense_notes;
    }

    public void setExpense_category(String expense_category) {
        this.expense_category = expense_category;
    }

    public void setExpense_createdate(String expense_createdate) {
        this.expense_createdate = expense_createdate;
    }

    public void setExpense_date(String expense_date) {
        this.expense_date = expense_date;
    }

    public void setExpense_id(String expense_id) {
        this.expense_id = expense_id;
    }

    public void setExpense_image(int expense_image) {
        this.expense_image = expense_image;
    }

    public void setExpense_to(String expense_to) {
        this.expense_to = expense_to;
    }

    public void setExpense_type(String expense_type) {
        this.expense_type = expense_type;
    }

    public void setExpensedoc(String expensedoc) {
        this.expensedoc = expensedoc;
    }

    public void setUsernameexp(String usernameexp) {
        this.usernameexp = usernameexp;
    }

    public void setIncome_account(String income_account) {
        this.income_account = income_account;
    }

    public void setIncome_category(String income_category) {
        this.income_category = income_category;
    }

    public void setIncome_from(String income_from) {
        this.income_from = income_from;
    }

    public void setIncome_id(String income_id) {
        this.income_id = income_id;
    }

    public void setIncomedoc(String incomedoc) {
        this.incomedoc = incomedoc;
    }

    public void setIncome_amount(double income_amount) {
        this.income_amount = income_amount;
    }

    public void setIncome_type(String income_type) {
        this.income_type = income_type;
    }

    public void setIncome_createdate(String income_createdate) {
        this.income_createdate = income_createdate;
    }

    public void setIncome_notes(String income_notes) {
        this.income_notes = income_notes;
    }

    public void setUsernameinc(String usernameinc) {
        this.usernameinc = usernameinc;
    }

    public void setIncome_date(String income_date) {
        this.income_date = income_date;
    }

    public void setIncome_image(int income_image) {
        this.income_image = income_image;
    }

    public String getExpense_to() {
        return expense_to;
    }

    public String getDatedata() {
        return datedata;
    }

    public String getExpense_type() {
        return expense_type;
    }

    public String getIncome_category() {
        return income_category;
    }

    public double getExpense_amount() {
        return expense_amount;
    }

    public int getExpense_image() {
        return expense_image;
    }

    public String getExpense_account() {
        return expense_account;
    }

    public String getExpense_category() {
        return expense_category;
    }

    public int getIncome_image() {
        return income_image;
    }

    public String getExpense_createdate() {
        return expense_createdate;
    }

    public double getIncome_amount() {
        return income_amount;
    }

    public String getExpense_date() {
        return expense_date;
    }

    public String getExpense_id() {
        return expense_id;
    }

    public String getExpense_notes() {
        return expense_notes;
    }

    public String getExpensedoc() {
        return expensedoc;
    }

    public String getUsernameexp() {
        return usernameexp;
    }

    public String getIncome_account() {
        return income_account;
    }

    public String getIncome_from() {
        return income_from;
    }

    public String getIncomedoc() {
        return incomedoc;
    }

    public String getIncome_createdate() {
        return income_createdate;
    }

    public String getIncome_date() {
        return income_date;
    }

    public String getIncome_type() {
        return income_type;
    }

    public String getIncome_id() {
        return income_id;
    }

    public String getIncome_notes() {
        return income_notes;
    }

    public String getUsernameinc() {
        return usernameinc;
    }

    public double getTransfer_amount() {
        return transfer_amount;
    }

    public double getTransfer_rate() {
        return transfer_rate;
    }

    public String getTransfer_createdate() {
        return transfer_createdate;
    }

    public String getTransfer_date() {
        return transfer_date;
    }

    public String getTransfer_dest() {
        return transfer_dest;
    }

    public String getTransfer_notes() {
        return transfer_notes;
    }

    public String getTransfer_src() {
        return transfer_src;
    }

    public String getTransferdoc() {
        return transferdoc;
    }

    public String getUsernametrf() {
        return usernametrf;
    }

    public void setTransfer_amount(double transfer_amount) {
        this.transfer_amount = transfer_amount;
    }

    public void setTransfer_createdate(String transfer_createdate) {
        this.transfer_createdate = transfer_createdate;
    }

    public void setTransfer_date(String transfer_date) {
        this.transfer_date = transfer_date;
    }

    public void setTransfer_dest(String transfer_dest) {
        this.transfer_dest = transfer_dest;
    }

    public void setTransfer_notes(String transfer_notes) {
        this.transfer_notes = transfer_notes;
    }

    public void setTransfer_rate(double transfer_rate) {
        this.transfer_rate = transfer_rate;
    }

    public void setTransfer_src(String transfer_src) {
        this.transfer_src = transfer_src;
    }

    public void setTransferdoc(String transferdoc) {
        this.transferdoc = transferdoc;
    }

    public void setUsernametrf(String usernametrf) {
        this.usernametrf = usernametrf;
    }


    @Override
    public int compareTo(incomeexpensetransfer o) {
        if (getDatedata() == null ||o.getDatedata()  == null)
            return 0;
        else {
            String string1 = getDatedata();
            String string = o.getDatedata();
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
