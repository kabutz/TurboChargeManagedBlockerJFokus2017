package eu.javaspecialists.performance.managedblocker.issue223;

public class Interruptions {
    public static void saveForLater(InterruptibleAction action) {
        saveForLaterTask(() -> {
            action.run();
            return null;
        });
    }

    public static <E> E saveForLaterTask(
        InterruptibleTask<E> task) {
        boolean interrupted = Thread.interrupted(); // clears flag
        try {
            while (true) {
                try {
                    return task.run();
                } catch (InterruptedException e) {
                    // flag would be cleared at this point too
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) Thread.currentThread().interrupt();
        }
    }

    @FunctionalInterface
    public interface InterruptibleAction {
        void run() throws InterruptedException;
    }

    @FunctionalInterface
    public interface InterruptibleTask<E> {
        E run() throws InterruptedException;
    }
}
