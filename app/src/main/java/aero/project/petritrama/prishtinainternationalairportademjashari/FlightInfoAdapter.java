package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;


public class FlightInfoAdapter extends RecyclerView.Adapter<FlightInfoAdapter.FlightViewHolder>
{
    private ArrayList<HashMap<String, String>> flightItemList;
    private Context mContext;
    private boolean isArrival;

    public FlightInfoAdapter(Context context, ArrayList<HashMap<String, String>> ItemList, boolean isArrival)
    {
        this.mContext = context;
        this.flightItemList = ItemList;
        this.isArrival = isArrival;
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder
    {
        CardView cvLogo;
        TextView tvAirline;
        TextView tvFrom;
        TextView tvTo;
        TextView tvFlightNr;
        TextView tvStatus1;
        TextView tvStatus2;
        TextView tvScheduled;

        public FlightViewHolder(View itemView, boolean isArrival)
        {
            super(itemView);

            Typeface custom_font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/UnicodSans.ttf");
            Typeface custom_font_light = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/UnicodSans-Light.ttf");
            Typeface custom_font_bold = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/UnicodSans-Bold.ttf");
            Typeface custom_italic = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/UnicodSans-Italic.ttf");

            try
            {
                cvLogo = (CardView) itemView.findViewById(R.id.CardView_departure);

                tvAirline = (TextView) itemView.findViewById(R.id.tvAirline);
                tvAirline.setTypeface(custom_font_bold);

                tvFrom = (TextView) itemView.findViewById(R.id.tvFrom);
                tvTo = (TextView) itemView.findViewById(R.id.tvTo);

                tvFlightNr = (TextView) itemView.findViewById(R.id.tvFlightNr);
                tvFlightNr.setTypeface(custom_font_light);

                tvStatus1 = (TextView) itemView.findViewById(R.id.tvStatus1);
                tvStatus1.setTypeface(custom_font);

                tvStatus2 = (TextView) itemView.findViewById(R.id.tvStatus2);
                tvStatus2.setTypeface(custom_font);

                tvScheduled = (TextView) itemView.findViewById(R.id.tvSchedule);
                tvScheduled.setTypeface(custom_italic);

                if(isArrival)
                    tvFrom.setTypeface(custom_font);
                else
                    tvTo.setTypeface(custom_font);
            }
            catch (Exception e)
            {
                Log.d("ADAPTER_ERROR", e.getMessage());
            }
        }
    }

    @Override
    public FlightInfoAdapter.FlightViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view;
        if(isArrival)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_row_arrival, viewGroup, false);
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_row_departure, viewGroup, false);

        FlightViewHolder viewHolder = new FlightViewHolder(view, isArrival);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FlightInfoAdapter.FlightViewHolder customViewHolder, int i)
    {
        try
        {
            String[] status = flightItemList.get(i).get("STATUS").split("  ");

            customViewHolder.tvAirline.setText(flightItemList.get(i).get("AIRLINE"));
            customViewHolder.tvFlightNr.setText(flightItemList.get(i).get("FLIGHT NUMBER"));
            customViewHolder.tvScheduled.setText(flightItemList.get(i).get("SCHEDULE"));

            if (isArrival)
                customViewHolder.tvFrom.setText(flightItemList.get(i).get("FROM").toUpperCase());
            else
                customViewHolder.tvTo.setText(flightItemList.get(i).get("TO").toUpperCase());

            customViewHolder.tvStatus1.setText(status[0]);
            customViewHolder.tvStatus2.setText(status[1]);
        }
        catch (Exception e)
        {
            Log.d("ADAPTER_ERROR_BIND", e.getMessage());
        }
    }

    @Override
    public int getItemCount()
    {
        return (null != flightItemList ? flightItemList.size() : 0);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}