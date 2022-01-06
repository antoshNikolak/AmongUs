package Utils;

public class ConcurrentUtils {

    public static Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }

    public static boolean isMonitorOwner(Object obj) {
        return (Thread.holdsLock(obj));
    }

}
