/**
* Luke Pringle
* S1624789
* MPD 2019-2020
**/

package mpdproject.gcu.me.org.mpdcwlukepringle;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewAdapter extends BaseAdapter implements Filterable
{
    private List<CurrentIncidents> originalData = null;
    private List<CurrentIncidents> filteredData = null;
    private LayoutInflater mInflater;
    Context context;
    int layoutId;

    ValueFilter filter;

    public ListViewAdapter (Context context, int viewId, List<CurrentIncidents> currentIncidentsList)
    {
        this.context = context;
        this.layoutId = viewId;
        this.filteredData = currentIncidentsList;
        this.originalData = currentIncidentsList;
    }
    @Override
    public int getCount()
    {
        return filteredData.size();
    }

    @Override
    public CurrentIncidents getItem(int position)
    {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = View.inflate(context, layoutId, null);

            holder = new ViewHolder();
            holder.text1 = (TextView) convertView.findViewById(R.id.locationText);
            holder.imageView = (ImageView) convertView.findViewById(R.id.itemIcon);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        int duration = filteredData.get(position).getDurationInDays();

        if (filteredData == null)
        {
            holder.text1.setText("There is currently no information availabe");
        }
        else {
            holder.text1.setText(filteredData.get(position).getTitle());
        }


        if (MainActivity.getIsCI() == true)
        {
            holder.imageView.setImageResource(R.drawable.currentincidentsicon);
        }
        else
        {
            if (duration >= 0 && duration <= 3)
            {
                holder.imageView.setImageResource(R.drawable.roadworksgreen1);
            }
            else if (duration > 3 && duration <= 9)
            {
                holder.imageView.setImageResource(R.drawable.roadworksorange);
            }
            else if ( duration >= 9 )
            {
                holder.imageView.setImageResource(R.drawable.roadworksred1);
            }
        }

        return convertView;

    }

    static class ViewHolder
    {
        TextView text1;
        TextView text2;
        ImageView imageView;
    }

    @Override
    public Filter getFilter()
    {
        if (filter == null)
        {
            filter = new ValueFilter();
        }

        return filter;
    }


    private class ValueFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() >0)
            {
                List<CurrentIncidents> filteredList = new ArrayList<>();
                for (int i = 0; i < filteredData.size();i++)
                {
                    if (filteredData.get(i).getTitle().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        CurrentIncidents item = filteredData.get(i);

                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            else
            {
                results.count = filteredData.size();
                results.values = filteredData;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            filteredData = (ArrayList<CurrentIncidents>)results.values;
            notifyDataSetChanged();
        }
    }
}




















