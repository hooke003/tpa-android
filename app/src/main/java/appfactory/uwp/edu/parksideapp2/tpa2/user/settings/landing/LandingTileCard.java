package appfactory.uwp.edu.parksideapp2.tpa2.user.settings.landing;

public class LandingTileCard {
    private String tileCardTextView;
    private int position;
    private int iconRes;

    public LandingTileCard(String tileCardTextView, int position, int iconRes) {
        this.tileCardTextView = tileCardTextView;
        this.position = position;
        this.iconRes = iconRes;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTileCardTextView() {
        return tileCardTextView;
    }

    public int getIconRes() {
        return iconRes;
    }

}
