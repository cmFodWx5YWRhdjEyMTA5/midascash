package prima.optimasi.indonesia.primacash.objects;

import io.realm.RealmObject;

public class dataexpset extends RealmObject {
    private String dateformatted;
    private float firebaseincome;
    private float ranges;

    public dataexpset(){

    }

    public dataexpset(float fire, float rage, String dates){
        dateformatted=dates;
        firebaseincome=fire;
        ranges=rage;
    }

    public float getFirebaseincome() {
        return firebaseincome;
    }

    public float getRanges() {
        return ranges;
    }

    public String getDateformatted() {
        return dateformatted;
    }

    public void setDateformatted(String dateformatted) {
        this.dateformatted = dateformatted;
    }

    public void setFirebaseincome(float firebaseincome) {
        this.firebaseincome = firebaseincome;
    }

    public void setRanges(float ranges) {
        this.ranges = ranges;
    }
}