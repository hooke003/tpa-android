package appfactory.uwp.edu.parksideapp2.ranger_restart.updates;

/**
 * The class that contains all the recent update card attributes.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RecentUpdateCard {
    private String title;
    private String body;
    private String date;

    /**
     * Initializes ths cards title, body, and date.
     *
     * @param title of the card.
     * @param body  of the card.
     * @param date  of the card.
     */
    public RecentUpdateCard(String title, String body, String date) {
        this.title = title;
        this.body = body;
        this.date = date;
    }

    /**
     * @return title of the card.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return body of this card.
     */
    public String getBody() {
        return body;
    }

    /**
     * @return date of this card
     */
    public String getDate() {
        return date;
    }
}
