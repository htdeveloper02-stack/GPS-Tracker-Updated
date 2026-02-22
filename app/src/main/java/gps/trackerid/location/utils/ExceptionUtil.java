package gps.trackerid.location.utils;

public final class ExceptionUtil {

    private ExceptionUtil() {
        // no instance
    }

    public interface ErrorCallback {
        void onError(String message);
    }

    public interface SafeBlock {
        void run() throws Exception;
    }

    public static void safe(ErrorCallback errorCallback, SafeBlock block) {
        try {
            block.run();
        } catch (Exception e) {
            if (errorCallback != null) {
                String msg = e.getMessage();
                if (msg == null || msg.isEmpty()) {
                    msg = "An unknown error occurred";
                }
                errorCallback.onError(msg);
            }
        }
    }
}
