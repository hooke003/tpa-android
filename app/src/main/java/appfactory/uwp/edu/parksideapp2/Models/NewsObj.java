package appfactory.uwp.edu.parksideapp2.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kyluong09 on 4/13/18.
 */

public class NewsObj extends RealmObject {
    @PrimaryKey
    private String _id;
    private String pubDate;
    private String title;
    private String headLine;
    private String summary;
    private String articleText;
    private String bannerImage;
    private String thumbNail;
    private String thumbNail2;
    private String audioFile;
    private String url;

    public NewsObj(){

    }

    public NewsObj(String _id,String url, String pubDate,String title, String headLine, String summary, String articleText, String bannerImage, String thumbNail, String thumbNail2, String audioFile) {
        this._id = _id;
        this.url = url;
        this.pubDate = pubDate;
        this.title = title;
        this.headLine = headLine;
        this.summary = summary;
        this.articleText = articleText;
        this.bannerImage = bannerImage;
        this.thumbNail = thumbNail;
        this.thumbNail2 = thumbNail2;
        this.audioFile = audioFile;
    }

    public String get_id() {
        return _id;
    }


    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getThumbNail2() {
        return thumbNail2;
    }

    public void setThumbNail2(String thumbNail2) {
        this.thumbNail2 = thumbNail2;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
