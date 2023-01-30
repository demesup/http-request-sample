package httprequests.sender;

import httprequests.model.Activity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static httprequests.savetodb.HibernateUtil.closeSessionFactoryIfOpened;
import static httprequests.savetodb.HibernateUtil.session;


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
public class HttpClientSample implements Sender {
    static Marker activityMarker = MarkerFactory.getMarker("ACTIVITY");

    @Override
    public Runnable workToDo() {
        return () -> getActivityJson().thenAccept(s -> save(
                getActivity(s)
        )).join();
    }

    public static CompletableFuture<String> getActivityJson() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.boredapi.com/api/activity")).build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    private static Activity getActivity(String str) {
        return Activity.createActivity(str);
    }

    private static void save(Activity activity) {
        try (Session session = session()) {
            session.beginTransaction();
            session.persist(activity);
            session.getTransaction().commit();
            log.info(activityMarker, "{} is saved", activity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void finish() {
        closeSessionFactoryIfOpened();
    }
}