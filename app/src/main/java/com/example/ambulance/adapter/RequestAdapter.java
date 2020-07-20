package com.example.ambulance.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ambulance.R;
import com.example.ambulance.model.requests.UserRequestItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private ArrayList<UserRequestItem> mUserRequestItems;
    private Context mContext;

    private OnRequestClickedListener mOnRequestClickedListener;

    public RequestAdapter(ArrayList<UserRequestItem> mUserRequestItems, Context mContext) {
        this.mUserRequestItems = mUserRequestItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_request_item,parent, false);
        return new RequestAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        UserRequestItem mUserRequestItem=mUserRequestItems.get(position);
        holder.mAddressRequestTextView.setText(getCompleteAddressString(Double.parseDouble(mUserRequestItem.getLatitude()),Double.parseDouble(mUserRequestItem.getLongitude())).trim());
        holder.mVehicleRequestTextView.setText(mUserRequestItem.getVehicleNo());
        holder.mTimeRequestTextView.setText(mUserRequestItem.getTimestamp());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRequestClickedListener.onClicked(position);
            }
        });

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
        return mUserRequestItems.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.requestAddress_item_text)
        TextView mAddressRequestTextView;

        @BindView(R.id.requestVehicleNo_item_text)
        TextView mVehicleRequestTextView;

        @BindView(R.id.requestTime_item_text)
        TextView mTimeRequestTextView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnRequestClickedListener{
        void onClicked(int position);
    }
    public void setOnRequestClickedListener(OnRequestClickedListener mOnRequestClickedListener){
        this.mOnRequestClickedListener=mOnRequestClickedListener;
    }
}
