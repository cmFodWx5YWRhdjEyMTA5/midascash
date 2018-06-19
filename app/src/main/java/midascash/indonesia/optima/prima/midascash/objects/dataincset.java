package midascash.indonesia.optima.prima.midascash.objects;

import io.realm.RealmObject;

public class dataincset extends RealmObject {
    private String dateformatted;
    private float firebaseincome;
    private float ranges;

    public dataincset(){

    }

    public dataincset(float fire,float rage,String dates){
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