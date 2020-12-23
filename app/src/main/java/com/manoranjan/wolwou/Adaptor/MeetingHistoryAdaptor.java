package com.manoranjan.wolwou.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.manoranjan.wolwou.Model.MeetingModel;
import com.manoranjan.wolwou.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class MeetingHistoryAdaptor extends RecyclerView.Adapter<MeetingHistoryAdaptor.ImageViewHolder> {
private Context mContext;
private List<MeetingModel> mUploads;

    private  OnitemClickListner mlistner;

    public interface  OnitemClickListner{

        void OnItemCLiCK(int position);
        void Joinmeetinh(int position);

    }

    public  void setonItemClickListner(OnitemClickListner listner){
        mlistner=listner;

    }
public MeetingHistoryAdaptor(Context mContext, List<MeetingModel> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        }
@Override
public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_meeting, parent, false);
        return new ImageViewHolder(v,mlistner);
        }

@Override
public void onBindViewHolder(final ImageViewHolder holder, int position) {

        final MeetingModel uploadCurrent = mUploads.get(position);

           /* final String imgurl= ApiLinks.baseimgurl+uploadCurrent.getImage();
                Picasso.with(mContext)
                .load(imgurl)
                .placeholder(R.drawable.ic_launcher_background)
                .fit()
                .into(holder.hotelimg);*/
                holder.meetingid.setText(uploadCurrent.getMeetingcode());

    String d = uploadCurrent.getCurrenttime();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    try {

        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(d));
        holder.joindate.setText(c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DATE)
                +" at "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE));
        Log.d("DEBUG", "year:" + c.get(Calendar.YEAR));
        //...
    } catch (ParseException e) {
    }

    }

@Override
public int getItemCount() {
        return mUploads.size();
        }

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView meetingid;
    TextView join,joindate;

    public ImageViewHolder(View itemView, final OnitemClickListner listner) {
        super(itemView);
        meetingid=itemView.findViewById(R.id.meetingid);
        join=itemView.findViewById(R.id.join);
        joindate=itemView.findViewById(R.id.joiningdate);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.Joinmeetinh(position);
                    }
                }
            }
        });




    }


}


}