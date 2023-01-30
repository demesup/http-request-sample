package httprequests.model;

import lombok.*;
import org.json.JSONObject;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Joke {
    int id;
    String type;
    String setup;
    String punchline;

    public static Joke createJoke(String str) {
        JSONObject object = new JSONObject(str);
        try {
            int id = object.getInt("id");
            String type = object.getString("type");
            String setup = object.getString("setup");
            String punchline = object.getString("punchline");
            return new Joke(id, type, setup, punchline);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
