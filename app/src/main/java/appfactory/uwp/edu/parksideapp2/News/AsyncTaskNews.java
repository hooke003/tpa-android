package appfactory.uwp.edu.parksideapp2.News;

import android.os.AsyncTask;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import appfactory.uwp.edu.parksideapp2.HttpRequest.HttpRequest;
import appfactory.uwp.edu.parksideapp2.Models.NewsObj;
import io.realm.Realm;

/**
 * Created by kyluong09 on 4/13/18.
 */

public class AsyncTaskNews extends AsyncTask<String, Void, Void> {
    //
    Realm mRealm;
    //
    SAXParserFactory saxFac = SAXParserFactory.newInstance();
    SAXParser saxParser = saxFac.newSAXParser();
    NewsSAXHandler handler = new NewsSAXHandler();

    public AsyncTaskNews() throws ParserConfigurationException, SAXException {
    }

    @Override
    protected Void doInBackground(String... params) {
        // Get realm instance
        mRealm = Realm.getDefaultInstance();
        // URL
        String url = params[0];

        try {
            String data = new HttpRequest().getUrlWithoutParameter(url);
            saxParser.parse(new InputSource(new StringReader(data)), handler);
            final ArrayList<NewsObj> newsList = handler.returnDarta();

            mRealm.executeTransactionAsync(realm -> {
                for (NewsObj news : newsList) {
                    realm.copyToRealmOrUpdate(news);
                }
            });
            mRealm.close();
        } catch (Exception ignored) {
        }
        return null;
    }
}
