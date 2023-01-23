package httprequests;

import httprequests.savetodb.HttpClientSample;
import lombok.extern.log4j.Log4j2;
import org.utils.Read;

import java.io.IOException;

@Log4j2
public class Main {
    public static void main(String[] args) {
        try {
            Thread thread = startThread();
            log.debug("To stop sending requests enter anything");
            Read.read();
            thread.interrupt();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static Thread startThread() throws IOException {
        switch (Read.readInt(2, """
                Enter:\s
                \t0 - for activities
                \t1 - for jokes""")) {
            case 0 -> {
                return HttpClientSample.start();
            }
            case 1 -> {
                return HttpUrlConnectionSample.start();
            }
            default -> throw new RuntimeException("Wrong input");
        }
    }
}
