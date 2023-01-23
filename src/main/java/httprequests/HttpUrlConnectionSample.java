package httprequests;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Log4j2
public class HttpUrlConnectionSample {
    public static Thread start() {
        log.debug("Start http-url-connection");
        Thread thread = new Thread(() -> {
            Thread current = Thread.currentThread();
            while (!current.isInterrupted()) {
                try {
                    log.info(httpUrlConnection());
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException | IOException e) {
                    log.debug("End http-url-connection");
                    return;
                }
            }
        });
        thread.start();
        return thread;
    }

    private static String httpUrlConnection() throws IOException {
        URL url = new URL("https://official-joke-api.appspot.com/random_joke");
        HttpURLConnection connection = getHttpURLConnection(url);
        int status = connection.getResponseCode();
        String responseContent = getResponseContent(connection, status);
        connection.disconnect();
        return responseContent;
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