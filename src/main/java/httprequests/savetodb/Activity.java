package httprequests.savetodb;

import jakarta.persistence.*;
import lombok.ToString;
import org.json.JSONObject;

@Entity
@ToString
@Table(name = "activity")
public class Activity {
    @Column(name = "type")
    String type;
    @Column(name = "activity")
    String activity;
    @Column(name = "participants" )
    int participants;
    @Column(name = "price", precision = 2)
    float price;
    @Column(name = "link")
    String link;
    @Id
    @Column(name = "key_num")
    int key;
    @Column(name = "accessibility", precision = 2)
    float accessibility;

    private Activity(String type, String activity, int participants, float price, String link, int key, float accessibility) {
        this.type = type;
        this.activity = activity;
        this.participants = participants;
        this.price = price;
        this.link = link;
        this.key = key;
        this.accessibility = accessibility;
    }

    public Activity() {
    }

    public static Activity createActivity(String str) {
        JSONObject object = new JSONObject(str);
        if (object.keySet().size() != 7) throw new RuntimeException("Wrong response parameter count");
        try {
            String type = object.getString("type");
            String activity = object.getString("activity");
            int participants = object.getInt("participants");
            float price = object.getFloat("price");
            String link = object.getString("link");
            int key = object.getInt("key");
            float accessibility = object.getFloat("accessibility");
            return new Activity(type, activity, participants, price, link, key, accessibility);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}