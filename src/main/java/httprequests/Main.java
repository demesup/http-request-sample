package httprequests;

import httprequests.sender.HttpClientSample;
import httprequests.sender.HttpUrlConnectionSample;
import httprequests.sender.Sender;
import lombok.extern.slf4j.Slf4j;
import org.utils.Read;

@Slf4j
public class Main {
    enum RequestSender {
        JOKE(new HttpUrlConnectionSample()),

        ACTIVITY(new HttpClientSample());

        final Sender sender;

        RequestSender(Sender sender) {
            this.sender = sender;
        }
    }

    public static void main(String[] args) {
        try {
            Sender sender = Read.readEnumValue(RequestSender.values()).sender;
            Thread thread = getThread(sender);
            thread.start();
            log.debug("To stop sending requests enter anything");
            Read.read();
            thread.interrupt();
            sender.finish();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static Thread getThread(Sender sender) {
        String senderName = sender.getClass().getSimpleName();
        return new Thread(() -> {
            log.info("Start " + senderName);
            Thread current = Thread.currentThread();
            while (!current.isInterrupted()) {
                try {
                    sender.workToDo().run();
                    Thread.sleep(1000 * 10);
                } catch (Exception e) {
                    if (!e.getClass().equals(InterruptedException.class)) log.error(e.getMessage());
                    log.info("End " + senderName);
                    return;
                }
            }
        });
    }
}
