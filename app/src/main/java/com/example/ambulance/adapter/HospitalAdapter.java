package com.example.ambulance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ambulance.R;
import com.example.ambulance.model.hospital.HospitaItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    private ArrayList<HospitaItem> mHospitalList;
    private Context mContext;

    private OnHospitalClickedListener mOnHospitalClickedListener;


    private static final String TAG = "HospitalAdapter";

    public HospitalAdapter(ArrayList<HospitaItem> mHospitalList, Context mContext) {
        this.mHospitalList = mHospitalList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hospital_list,parent, false);
        return new HospitalViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
       holder.mNameHospitalTextView.setText(mHospitalList.get(position).getName());
       holder.mAddressHospitalTextView.setText(mHospitalList.get(position).getAddress());
       holder.itemView.setOnClickListener(v -> mOnHospitalClickedListener.onClicked(position));

    }

    @Override
    public int getItemCount() {
        return mHospitalList.size();
    }

    public class HospitalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.hospitalName_item_text)
        TextView mNameHospitalTextView;

        @BindView(R.id.hospitalAddress_item_text)
        TextView mAddressHospitalTextView;

        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnHospitalClickedListener{
        void onClicked(int position);
    }

    public void setOnHospitalClickedListener(OnHospitalClickedListener mOnHospitalClickedListener){
        this.mOnHospitalClickedListener=mOnHospitalClickedListener;
    }
}
