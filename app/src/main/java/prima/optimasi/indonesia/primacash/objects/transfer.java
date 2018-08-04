package prima.optimasi.indonesia.primacash.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class transfer implements Comparable<transfer> {
    String transfer_src,transfer_dest,transfer_notes,usernametrf;
    String transfer_date,transfer_createdate;
    double transfer_amount,transfer_rate;
    String transfer_isdone;
    String transferdoc;

    public String getUsernametrf() {
        return usernametrf;
    }

    public String getTransferdoc() {
        return transferdoc;
    }

    public String getTransfer_src() {
        return transfer_src;
    }

    public String getTransfer_notes() {
        return transfer_notes;
    }

    public String getTransfer_dest() {
        return transfer_dest;
    }

    public String getTransfer_date() {
        return transfer_date;
    }

    public String getTransfer_createdate() {
        return transfer_createdate;
    }

    public double getTransfer_rate() {
        return transfer_rate;
    }

    public double getTransfer_amount() {
        return transfer_amount;
    }

    public void setUsernametrf(String usernametrf) {
        this.usernametrf = usernametrf;
    }

    public void setTransferdoc(String transferdoc) {
        this.transferdoc = transferdoc;
    }

    public void setTransfer_src(String transfer_src) {
        this.transfer_src = transfer_src;
    }

    public void setTransfer_rate(double transfer_rate) {
        this.transfer_rate = transfer_rate;
    }

    public void setTransfer_notes(String transfer_notes) {
        this.transfer_notes = transfer_notes;
    }

    public void setTransfer_dest(String transfer_dest) {
        this.transfer_dest = transfer_dest;
    }

    public void setTransfer_date(String transfer_date) {
        this.transfer_date = transfer_date;
    }

    public void setTransfer_isdone(String transfer_isdone) {
        this.transfer_isdone = transfer_isdone;
    }

    public void setTransfer_createdate(String transfer_createdate) {
        this.transfer_createdate = transfer_createdate;
    }

    public void setTransfer_amount(double transfer_amount) {
        this.transfer_amount = transfer_amount;
    }

    public String getTransfer_isdone() {
        return transfer_isdone;
    }


    @Override
    public int compareTo(transfer o) {
        if (getTransfer_date() == null ||o.getTransfer_date()  == null)
            return 0;
        else {
            String string1 = getTransfer_date();
            String string = o.getTransfer_date();
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
