package httprequests.sender;

import httprequests.model.Joke;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class HttpUrlConnectionSample implements Sender {
    static Marker jokeMarker = MarkerFactory.getMarker("JOKE");
    static URL url;

    static {
        try {
            url = new URL("https://official-joke-api.appspot.com/random_joke");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Override
    public Runnable workToDo() {
        return () -> {
            Joke joke = getJoke();
            log.info(jokeMarker, "{} is saved", joke);
        };
    }

    @SneakyThrows
    private Joke getJoke() {
        HttpURLConnection connection = getHttpURLConnection(url);
        int status = connection.getResponseCode();
        String responseContent = getResponseContent(connection, status);
        connection.disconnect();
        return Joke.createJoke(responseContent);
    }

    private static String getResponseContent(HttpURLConnection connection, int status) throws IOException {
        String responseContent;
        if (status > 299) {
            responseContent = readInput(connection.getErrorStream());
        } else {
            responseContent = readInput(connection.getInputStream());
        }
        return responseContent;
    }

    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection;
    }

    private static String readInput(InputStream stream) throws IOException {
        StringBuilder responseContent = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }
        reader.close();
        return responseContent.toString();
    }
}