package httprequests.savetodb;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static httprequests.Main.UTILS;


/*
create database http_request_db;
create table activity
(
    id            int primary key auto_increment,
    type          varchar(255),
    activity      varchar(255),
    participants  int,
    price         decimal(8,2),
    link          varchar(255),
    key_num       int,
    accessibility decimal
)*/
@Slf4j
public class HttpClientSample {
    static Marker activityMarker = MarkerFactory.getMarker("ACTIVITY");

    public static Thread start() {
        log.info("Start http-client");
        Thread thread = new Thread(() -> {
            Thread current = Thread.currentThread();
            while (!current.isInterrupted()) {
                try {
                    saveResponse();
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    log.info("End http-client");
                    return;
                }
            }
        });
        thread.start();
        return thread;
    }

    private static void saveResponse() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.boredapi.com/api/activity")).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(HttpClientSample::sendToDatabase).join();
    }

    private static void sendToDatabase(String str) {
        Activity activity = Activity.createActivity(str);
        save(activity);
    }

    private static void save(Activity activity) {
        try (Session session = UTILS.session()) {
            session.beginTransaction();
            session.persist(activity);
            session.getTransaction().commit();
            log.info(activityMarker, "{} is saved", activity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}