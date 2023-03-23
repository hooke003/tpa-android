package appfactory.uwp.edu.parksideapp2.Event;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import appfactory.uwp.edu.parksideapp2.HttpRequest.HttpRequest;
import appfactory.uwp.edu.parksideapp2.Models.EventObj;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by kyluong09 on 3/27/18.
 */

public class AsyncTaskEvent extends AsyncTask<String,Void,Void> {
    // Realm
    Realm mRealm;
    // SAX Parser, handler
    SAXParserFactory saxFac = SAXParserFactory.newInstance();
    SAXParser saxParser = saxFac.newSAXParser();
    SAXHandler handler = new SAXHandler();

    private ArrayList<EventObj> mEventList = new ArrayList<>();

    // Calendar
    Calendar calendar;

    public AsyncTaskEvent() throws ParserConfigurationException, SAXException {
    }



    @SuppressLint("BinaryOperationInTimber")
    @Override
    protected Void doInBackground(String... params) {
        String url = params[0];
        // Set calendar day and month to 01/01
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -5);

        Date startDate = calendar.getTime();
        // Set endDate 1 year ahead
        calendar.add(Calendar.MONTH, 10);
        Date endDate = calendar.getTime();
        // Set date in format yyyy-MM-dd
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Timber.d(" startDate:" + format.format(startDate));
        Timber.d(" endDate: " + format.format(endDate));

        // Pull data back from API
        // Parse and store into Arraylist -> RealmDB
        try {
            String startDateString = format.format(startDate);
            String endDateString = format.format(endDate);
            String data = new HttpRequest().getUrlEvent(url,startDateString,endDateString);
            saxParser.parse(new InputSource(new StringReader(data)),handler);
            mEventList.addAll(handler.returnData());

            Timber.d("Event list size = " + mEventList.size());

            // Insert into realmDatabase
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(realm -> {
                realm.deleteAll();
                for(final EventObj event:mEventList){
                    realm.copyToRealm(event);
                }
            });
            mRealm.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
