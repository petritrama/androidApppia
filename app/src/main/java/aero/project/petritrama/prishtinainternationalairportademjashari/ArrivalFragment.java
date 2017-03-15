package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ArrivalFragment extends Fragment
{
    RecyclerView arrivalRecycleView;
    SwipeRefreshLayout swipeRefreshArrival;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_arrival_info, container, false);

        swipeRefreshArrival = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh_arrival);
        swipeRefreshArrival.setOnRefreshListener(srlArrival_listener);
        swipeRefreshArrival.setColorSchemeResources(R.color.flightInfo, R.color.arrivalColor);

        arrivalRecycleView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_arrival_fragment);
        arrivalRecycleView.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        arrivalRecycleView.setLayoutManager(layout);

        new retrieveArrivalInfo(getActivity(), arrivalRecycleView).execute();

        return rootView;
    }


    public class retrieveArrivalInfo extends AsyncTask<Void, Integer, Void>
    {
        Context mContext;
        RecyclerView mRecyclerView;
        ProgressDialog pDialog;
        ArrayList<HashMap<String, String>> arrivalInfo = null;

        public retrieveArrivalInfo(Context mContext, RecyclerView mRecyclerView)
        {
            this.mContext = mContext;
            this.mRecyclerView = mRecyclerView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(getText(R.string.wait_msg));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStreamReader is;
            BufferedReader br;

            JSONparser parser = new JSONparser();
            String info;

            try {
                URL url = new URL("http://46.99.150.6/enter/json/a.php");

                is = new InputStreamReader(url.openStream());
                br = new BufferedReader(is);

                info = br.readLine();

                is.close();
                br.close();

                this.arrivalInfo = parser.JSONinfo(info, true);
            } catch (MalformedURLException me) {
                Log.e("URL_Error", me.getMessage());
            } catch (IOException io) {
                Log.e("IO_Error", io.getMessage());
            } catch (Exception e) {
                Log.e("E_Error", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();

            FlightInfoAdapter arrayAdapter = new FlightInfoAdapter(getActivity(), arrivalInfo, true);
            mRecyclerView.setAdapter(arrayAdapter);
        }
    }

    SwipeRefreshLayout.OnRefreshListener srlArrival_listener = new SwipeRefreshLayout.OnRefreshListener()
    {
        @Override
        public void onRefresh()
        {
            new retrieveArrivalInfo(getActivity(), arrivalRecycleView).execute();
            swipeRefreshArrival.setRefreshing(false);
        }
    };
}