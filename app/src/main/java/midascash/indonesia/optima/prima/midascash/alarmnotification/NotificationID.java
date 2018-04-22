package midascash.indonesia.optima.prima.midascash.alarmnotification;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rwina on 4/22/2018.
 */

public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}