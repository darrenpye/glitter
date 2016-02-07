package com.darrenpye.ui.litterlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.darrenpye.R;
import com.darrenpye.ui.com.darrenpye.api.Litter;

import java.util.List;

/**
 * Created by darrenpye on 16-02-07.
 */
public class LitterAdapter extends ArrayAdapter<Litter> {
    Context mContext;
    int resourceId;

    public LitterAdapter(Context context, int resourceId, List<Litter> objects) {
        super(context, resourceId, objects);

        this.mContext = context;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LitterHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);

            holder = new LitterHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtMessage = (TextView)row.findViewById(R.id.txtMessage);

            row.setTag(holder);
        }
        else
        {
            holder = (LitterHolder)row.getTag();
        }

        Litter litter = getItem(position);

        holder.txtMessage.setText(litter.getMessage());
        holder.imgIcon.setImageResource(litter.getUserImageResource());

        return row;
    }

    static class LitterHolder
    {
        ImageView imgIcon;
        TextView txtMessage;
    }
}
