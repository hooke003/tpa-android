package appfactory.uwp.edu.parksideapp2.ComputerLab;

import android.os.AsyncTask;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import appfactory.uwp.edu.parksideapp2.HttpRequest.HttpRequest;
import appfactory.uwp.edu.parksideapp2.Models.EquipmentObj;
import appfactory.uwp.edu.parksideapp2.Models.LabObj;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by kyluong09 on 6/1/18.
 */

public class AsyncTaskLab extends AsyncTask<String, Void, Void> {
    // Vars
    private SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    private SAXParser saxParser = saxParserFactory.newSAXParser();
    private LabSaxHandler labHandler = new LabSaxHandler();
    // Lab List
    private ArrayList<LabObj> labList = new ArrayList<>();

    private EquipmentSaxHandler equipHandler = new EquipmentSaxHandler();
    // Equipment List
    private ArrayList<EquipmentObj> equipmentList = new ArrayList<>();
    private HashMap<String, String> equipmentMap = new HashMap<>();

    // Realm Database
    Realm mRealm;

    public AsyncTaskLab() throws ParserConfigurationException, SAXException {
    }

    @Override
    protected Void doInBackground(String... strings) {
        mRealm = Realm.getDefaultInstance();
        String labUrl = strings[0];
        String equipmentUrl = strings[1];

        // Connection http and fetch data back from API
        try {
            // Get data from Lab API
            String labData = new HttpRequest().getUrlWithoutParameter(labUrl);
            // Parse XML API
            saxParser.parse(new InputSource(new StringReader(labData)), labHandler);
            // .addAll means copy everything from labHandler to the new labList
            labList.addAll(labHandler.returnData());

            // Get data from Equipment API
            String equipmentData = new HttpRequest().getUrlWithoutParameter(equipmentUrl);
            saxParser.parse(new InputSource(new StringReader(equipmentData)), equipHandler);
            equipmentList.addAll(equipHandler.returnData());

            for (int i = 0; i < equipmentList.size(); i++) {
                int id = equipmentList.get(i).getId();
                String name = equipmentList.get(i).getName();
                equipmentMap.put(Integer.toString(id), name);
            }

            for (int i = 0; i < labList.size(); i++) {
                RealmList<String> equipList = labList.get(i).getEquipmentNewList();
                ArrayList<String> temp = new ArrayList<>();
                int size = equipList.size();

                for (int j = 0; j < size; j++) {
                    String id = equipList.get(j);
                    String name = equipmentMap.get(id);
                    temp.add(name);
                }
                equipList.clear();
                equipList.addAll(temp);
                labList.get(i).setEquipmentNewList(equipList);
            }

            mRealm.executeTransactionAsync(realm -> {
                for (LabObj lab : labList) {
                    realm.copyToRealmOrUpdate(lab);
                }
            });

            mRealm.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
