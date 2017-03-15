package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONparser
{
    String FLIGHT_NUMBER = "FLIGHT NUMBER";
    String AIRLINE = "AIRLINE";
    String TO = "TO";
    String FROM = "FROM";
    String SCHEDULE = "SCHEDULE";
    String EST = "EST";
    String STATUS = "STATUS";

    public ArrayList<HashMap<String,String>> JSONinfo(String jsonString, boolean isArrival)
    {
        ArrayList<HashMap<String,String>> flightInfo = new ArrayList<>();

        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i=0; i<=jsonArray.length()-1; i++)
            {
                JSONObject f = jsonArray.getJSONObject(i);

                String flightNr = f.getString(FLIGHT_NUMBER);
                String airline = f.getString(AIRLINE);
                String schedule = f.getString(SCHEDULE);
                String est = f.getString(EST);
                String status = f.getString(STATUS);
                String from = null, to = null;
                if(isArrival)
                    from = f.getString(FROM);
                else
                    to = f.getString(TO);

                HashMap<String, String> info = new HashMap<>();

                info.put(FLIGHT_NUMBER,flightNr);
                info.put(AIRLINE,airline);
                info.put(SCHEDULE,schedule);
                info.put(EST,est);
                info.put(STATUS,status);
                if(isArrival)
                    info.put(FROM,from);
                else
                    info.put(TO,to);

                flightInfo.add(info);
            }
        }
        catch (JSONException e)
        {
            Log.d("JSON_ERROR", e.getMessage());
        }

        return flightInfo;
    }
}
