package com.clinicapp.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.clinicapp.R;
import com.clinicapp.ui.common.WifiDialogFragment;

public abstract class BaseActivity extends AppCompatActivity {

    public BroadcastReceiver batteryBroadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

            updateBatteryPercent(level);
        }
    };



    public void setViewState(boolean isEnabled, View...views){
        for (View view:views) {
            view.setEnabled(isEnabled);
        }
    }

    public void onClickShowWifi(View v){
        new WifiDialogFragment().show(getSupportFragmentManager(),null);
    }

    public abstract void updateBatteryPercent(int battery);

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(batteryBroadcastReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(batteryBroadcastReceiver);
    }

    public void onClickSetSelected(View v){
        CheckBox checkBox;
        switch(v.getId()){
            case R.id.right_head_container: checkBox=v.findViewById(R.id.right_side);
                    checkBox.setChecked(!checkBox.isChecked());
                    break;
            case R.id.front_head_container: checkBox=v.findViewById(R.id.front_side);
                    checkBox.setChecked(!checkBox.isChecked());
                    break;
            case R.id.left_head_container: checkBox=v.findViewById(R.id.left_side);
                    checkBox.setChecked(!checkBox.isChecked());
                    break;
            case R.id.top_head_container: checkBox=v.findViewById(R.id.top_side);
                    checkBox.setChecked(!checkBox.isChecked());
                    break;
            case R.id.back_head_container: checkBox=v.findViewById(R.id.back_side);
                    checkBox.setChecked(!checkBox.isChecked());
                    break;
        }
    }
}
