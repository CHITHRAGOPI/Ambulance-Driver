package com.example.ambulance.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ambulance.R;
import com.example.ambulance.model.trip.TripItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private ArrayList<TripItem> mTripItemArrayList;
    private Context mContext;

    private OnTripClickedListener mOnTripClickedListener;


    public TripAdapter(ArrayList<TripItem> mTripItemArrayList, Context mContext) {
        this.mTripItemArrayList = mTripItemArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trip_list,parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
         TripItem mTripItem=mTripItemArrayList.get(position);

        holder.mAddressRequestTextView.setText(getCompleteAddressString(Double.parseDouble(mTripItem.getLatitude()),Double.parseDouble(mTripItem.getLongitude())).trim());
        holder.mVehicleRequestTextView.setText(mTripItem.getVehicleNo());
//        holder.mTimeRequestTextView.setText(mTripItem.getTimestamp());
        holder.itemView.setOnClickListener(v -> mOnTripClickedListener.onClicked(position));
        if(mTripItem.getStatus().equals("0")){
            holder.mStatusRequestTextView.setText("Pending");
        }
        else if(mTripItem.getStatus().equals("2")){
            holder.mStatusRequestTextView.setText("Cancelled");

        }
        else{
            holder.mStatusRequestTextView.setText("Completed");
            holder.mStatusRequestTextView.setTextColor(mContext.getResources().getColor(R.color.colorLightgreen));
        }

    }


    private String getCompleteAddressString(double lat, double lng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat,lng, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }



    @Override
    public int getItemCount() {
        return mTripItemArrayList.size();
    }

    public class TripViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tripAddress_item_text)
        TextView mAddressRequestTextView;

        @BindView(R.id.tripVehicleNo_item_text)
        TextView mVehicleRequestTextView;



        @BindView(R.id.tripStatus_item_text)
        TextView mStatusRequestTextView;
//
//        @BindView(R.id.tripTime_item_text)
//        TextView mTimeRequestTextView;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);        }
    }

  public   interface  OnTripClickedListener{
        void onClicked(int position);
    }

    public void setOnTripClickedListener(OnTripClickedListener mOnTripClickedListener){
        this.mOnTripClickedListener=mOnTripClickedListener;
    }
}
