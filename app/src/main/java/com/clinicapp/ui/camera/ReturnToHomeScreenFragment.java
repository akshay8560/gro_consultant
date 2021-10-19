package com.clinicapp.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.clinicapp.R;
import com.clinicapp.databinding.FragmentReturnOrShootHairBinding;

public class ReturnToHomeScreenFragment extends Fragment {
    private FragmentReturnOrShootHairBinding views;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        views = FragmentReturnOrShootHairBinding.inflate(getLayoutInflater(),container,false);

        if(!getArguments().getBoolean("isPortrait")){
            views.headPositions.setVisibility(View.GONE);
            views.btHome.setText(R.string.finished);
        }
        initViews();

        return views.getRoot();
    }

    private void initViews(){
        views.headPositions.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(R.id.action_returnToHomeScreen_to_selectHairPosition, getArguments()));
        views.btHome.setOnClickListener(view ->
                {
                    if (views.btHome.getText().toString().equals("Finished")){
                        views.btHome.setText(R.string.gro_scale);
                        views.btHome.setBackgroundColor(getActivity().getColor(R.color.groscale_color));
                        views.headPositions.setVisibility(View.VISIBLE);
                        views.headPositions.setText(R.string.gro_score);
                        views.btHome.setBackgroundColor(getActivity().getColor(R.color.groscore_color));

                    }else {

                    if (views.btHome.getText().toString().equals("Finished")){
                        views.btHome.setText(R.string.processing);
                        views.btHome.setBackgroundColor(getActivity().getColor(R.color.error));
                   }else {
                        Navigation.findNavController(view)
                                .navigate(R.id.action_returnToHomeScreen_to_homeFragment);
                    }

                }});
    }
}