package appfactory.uwp.edu.parksideapp2.News;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import appfactory.uwp.edu.parksideapp2.Models.NewsObj;

/**
 * Created by kyluong09 on 4/13/18.
 */

public class NewsSAXHandler extends DefaultHandler{
    private NewsObj newsObj;
    private String tempChar;
    private StringBuilder stringBuilder;
    private ArrayList<NewsObj> newsObjsList = new ArrayList<>();
    // Boolean
    private boolean bId;
    private boolean bUrl;
    private boolean bPubDate;
    private boolean bTitle;
    private boolean bHeadLine;
    private boolean bSummary;
    private boolean bBannerImage;
    private boolean bThumbNail;
    private boolean bThumbNail2;
    private boolean bAudioFile;
    private boolean bArticleText;

    public NewsSAXHandler() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equalsIgnoreCase("article")){
            newsObj = new NewsObj();
        }else if(qName.equalsIgnoreCase("pageid")){
            tempChar = "";
            bId = true;
        }else if(qName.equalsIgnoreCase("pubDate")){
            tempChar = "";
            bPubDate = true;
        }else if(qName.equalsIgnoreCase("title")){
            tempChar = "";
            bTitle = true;
        }else if(qName.equalsIgnoreCase("headline")){
            tempChar = "";
            bHeadLine = true;
        }else if(qName.equalsIgnoreCase("summary")){
            tempChar = "";
            bSummary = true;
        }else if(qName.equalsIgnoreCase("bannerimage")){
            tempChar = "";
            bBannerImage = true;
        }else if(qName.equalsIgnoreCase("thumbnail")){
            tempChar = "";
            bThumbNail = true;
        }else if(qName.equalsIgnoreCase("thumbnail2")){
            tempChar = "";
            bThumbNail2= true;
        }else if(qName.equalsIgnoreCase("articletext")){
            stringBuilder = new StringBuilder();
            bArticleText = true;
        }else if(qName.equalsIgnoreCase("audiofile")){
            tempChar = "";
            bAudioFile = true;
        }else if(qName.equalsIgnoreCase("url")){
            tempChar = "";
            bUrl = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase("article")){
            newsObjsList.add(newsObj);
        }else if(qName.equalsIgnoreCase("pageid")){
            newsObj.set_id(tempChar);
            bId = false;
        }else if(qName.equalsIgnoreCase("pubDate")){
            newsObj.setPubDate(tempChar);
            bPubDate = false;
        }else if(qName.equalsIgnoreCase("title")){
            newsObj.setTitle(tempChar);
            bTitle = false;
        }else if(qName.equalsIgnoreCase("headline")){
            newsObj.setHeadLine(tempChar);
            bHeadLine= false;
        }else if(qName.equalsIgnoreCase("summary")){
            newsObj.setSummary(tempChar);
            bSummary = false;
        }else if(qName.equalsIgnoreCase("bannerimage")){
            newsObj.setBannerImage(tempChar);
            bBannerImage = false;
        }else if(qName.equalsIgnoreCase("thumbnail")){
            newsObj.setThumbNail(tempChar);
            bThumbNail = false;
        }else if(qName.equalsIgnoreCase("thumbnail2")){
            newsObj.setThumbNail2(tempChar);
            bThumbNail2= false;
        }else if(qName.equalsIgnoreCase("articletext")){
            newsObj.setArticleText(stringBuilder.toString());
            bArticleText = false;
        }else if(qName.equalsIgnoreCase("audiofile")){
            newsObj.setAudioFile(tempChar);
            bAudioFile = false;
        }else if(qName.equalsIgnoreCase("url")){
            newsObj.setUrl(tempChar);
            bUrl = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(bId){
            tempChar = new String(ch,start,length);
        }
        else if(bPubDate){
            tempChar = new String(ch,start,length);
        }
        else if(bTitle){
            tempChar = new String(ch,start,length);
        }
        else if(bHeadLine){
            tempChar = new String(ch,start,length);
        }else if(bSummary){
            tempChar = new String(ch,start,length);
        }else if(bBannerImage){
            tempChar = new String(ch,start,length);
        }else if(bThumbNail){
            tempChar = new String(ch,start,length);
        }else if(bThumbNail2){
            tempChar = new String(ch,start,length);
        }else if(bAudioFile){
            tempChar = new String(ch,start,length);
        }else if(bArticleText){
            stringBuilder.append(new String(ch,start,length));
        }else if(bUrl){
            tempChar = new String(ch,start,length);
        }
    }

    public ArrayList<NewsObj> returnDarta(){
        return this.newsObjsList;
    }
}
