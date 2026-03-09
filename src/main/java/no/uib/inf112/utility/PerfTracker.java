package no.uib.inf112.utility;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;


//AI assisted creating this as this, used for debugging, probably gone b4 release.
public class PerfTracker {
    public static double fps = 0;
    public static double ups = 0;
    public static double lastFrameTime = 0;
    public static final Map<String, Double> taskMs = new ConcurrentHashMap<>();
    private static final Map<String, Long> startTimes = new ConcurrentHashMap<>();
    private static long lastSnapshot = System.nanoTime();
    private static int frames, updates;
    public static void start(String task) {
        startTimes.put(task, System.nanoTime());
    }

    public static void stop(String task) {
        Long start = startTimes.get(task);
        if (start != null) {
            double duration = (System.nanoTime() - start) / 1_000_000.0;
            taskMs.put(task, taskMs.getOrDefault(task, duration) * 0.9 + duration * 0.1);
        }
    }

    public static void tick(boolean isFrame) {
        if (isFrame) {
            frames++;
        } else {
            updates++;
        }

        long now = System.nanoTime();
        if (now - lastSnapshot >= 1_000_000_000L) {
            fps = frames;
            ups = updates;
            frames = 0;
            updates = 0;
            lastSnapshot = now;
        }
    }
}