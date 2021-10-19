package com.clinicapp.adapters;

import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clinicapp.databinding.ItemWifiBinding;
import com.clinicapp.models.WifiEntry;

import java.util.ArrayList;
import java.util.List;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.WifiViewHolder> {

    private List<WifiEntry> entries;
    private ItemClickListener listener;

    public WifiAdapter(ItemClickListener listener) {
        this.listener = listener;
        entries = new ArrayList<>();
    }

    public void setData(List<WifiEntry> entries){
        this.entries = entries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WifiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemWifiBinding views = ItemWifiBinding.inflate(layoutInflater, parent, false);
        return new WifiViewHolder(views, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull WifiViewHolder holder, int position) {
        holder.populate(entries.get(position));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class WifiViewHolder extends RecyclerView.ViewHolder {
        private ItemWifiBinding views;
        private ItemClickListener listener;

        public WifiViewHolder(@NonNull ItemWifiBinding views, ItemClickListener listener) {
            super(views.getRoot());
            this.views = views;
            this.listener = listener;
        }

        public void populate(WifiEntry wifiEntry){
            ScanResult scanResult = wifiEntry.getDetails();
            views.txtName.setText(scanResult.SSID);
            views.imgConnected.setVisibility(wifiEntry.isConnected()?View.VISIBLE:View.INVISIBLE);
            views.getRoot().setOnClickListener(v->listener.onClickItem(wifiEntry));
        }
    }

    public interface ItemClickListener {
        void onClickItem(WifiEntry wifiEntry);
    }
}
