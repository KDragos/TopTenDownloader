package com.kristindragos.toptendownloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by kristin on 5/4/16.
 */
public class Parser {
    private String xmlData;
    private ArrayList<Application> applications;

    public Parser(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<Application>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public boolean process (){
        boolean status = true;
        Application currentRecord = null;
        boolean inentry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch(eventType) {
                    case XmlPullParser.START_TAG:
//                        Log.d("Parser", "Starting tag for "+ tagName);
                        if(tagName.equalsIgnoreCase("entry")){
                            inentry = true;
                            currentRecord = new Application();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("entry")) {
                            applications.add(currentRecord);
                            inentry = false;
                        } else if (tagName.equalsIgnoreCase("name")) {
                            if(currentRecord != null) {
                                currentRecord.setName(textValue);
                            }
                        } else if (tagName.equalsIgnoreCase("artist")) {
                        currentRecord.setArtist(textValue);
                        } else if (tagName.equalsIgnoreCase("releaseDate")) {
                            currentRecord.setReleaseDate(textValue);
                        }
//                        Log.d("Parser", "Ending tag for "+ tagName);
                        break;

                    default:
                        //Nothing yet
                }
                eventType = xpp.next();
            }


        }catch (Exception e){
            status = false;
            e.printStackTrace();
        }

        for (Application app: applications) {
            Log.d("Parser", "*******" );
            Log.d("Parser", "Name: " + app.getName() );
            Log.d("Parser", "Artist: " + app.getArtist() );
            Log.d("Parser", "Release Date: " + app.getReleaseDate() );


        }

        return true;
    }
}
