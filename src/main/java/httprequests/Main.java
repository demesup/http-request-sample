package httprequests;

import httprequests.savetodb.HibernateUtil;
import httprequests.savetodb.HttpClientSample;
import httprequests.savetojson.HttpUrlConnectionSample;
import lombok.extern.slf4j.Slf4j;
import org.utils.Read;

import java.io.IOException;

@Slf4j
public class Main {
    public static HibernateUtil UTILS = new HibernateUtil();
    public static void main(String[] args) {
        try {
            Thread thread = startThread();
            log.debug("To stop sending requests enter anything");
            Read.read();
            thread.interrupt();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            UTILS.closeSessionFactoryIfOpened();
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
