package appfactory.uwp.edu.parksideapp2.tpa2;

/**
 * This class contains the attributes for the latest news item.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2NotificationCard implements Comparable<TPA2NotificationCard> {
    private String title;
    private String body;
    private String timeStamp;
    private int icon;
    private long longTime;

    /**
     * @param title of this notification card.
     * @param body of this notification card.
     * @param date of this notification card
     */
    public TPA2NotificationCard(String title, String body, String date, int icon, long longTime) {
        this.title = title;
        this.body = body;
        this.timeStamp = date;
        this.icon = icon;
        this.longTime = longTime;
    }

    /**
     * @return title of this notification card.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title of this notification card.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return body of this notification card.
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body of this notification card.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return date of this notification card.
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp of this notification card.
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getLongTime() {
        return longTime;
    }

    @Override
    public int compareTo(TPA2NotificationCard other) {
        return (Long.compare(this.getLongTime(), other.getLongTime()));
    }
}
