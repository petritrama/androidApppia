package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomListAdapter extends ArrayAdapter
{
    private Context mContext;
    private int id;
    private String[] items;
    int mSelectedItem;
    private Typeface custom_typeface;
    private Typeface custom_typeface_bold;

    public CustomListAdapter(Context context, int textViewResourceId , String[] list, Typeface tf, Typeface tf_bold)
    {
        super(context, textViewResourceId, list);

        mContext = context;
        id = textViewResourceId;
        items = list;
        custom_typeface = tf;
        custom_typeface_bold = tf_bold;
    }

    public void setSelectedItem(int selectedItem) { mSelectedItem = selectedItem; }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View view = super.getView(position, v, parent);

        TextView tv = (TextView) view.findViewById(R.id.list_item);
        tv.setTypeface(custom_typeface);
        tv.setText(items[position]);

        if (position == mSelectedItem)
        {
            tv.setTypeface(custom_typeface_bold);
            tv.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
        }
        else
        {
            tv.setTypeface(custom_typeface);
            tv.setBackground(getContext().getResources().getDrawable(R.drawable.navdraw_listitem_ripple));
        }
        return tv;
    }
}
