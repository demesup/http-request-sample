package httprequests.savetodb;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


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
@Log4j2
public class HttpClientSample {

    public static Thread start() {
        log.debug("Start http-client");
        Thread thread = new Thread(() -> {
            Thread current = Thread.currentThread();
            while (!current.isInterrupted()) {
                try {
                    httpClient();
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    log.debug("End http-client");
                    return;
                }
            }
        });
        thread.start();
        return thread;
    }

    private static void httpClient() {
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
        try (Session session = HibernateUtil.session()) {
            session.beginTransaction();
            session.persist(activity);
            session.getTransaction().commit();
            log.info(activity + " is saved");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}