package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import kr.ac.jbnu.se.mobile.sketchvoca.MusicService;
import kr.ac.jbnu.se.mobile.sketchvoca.PreferenceManager;
import kr.ac.jbnu.se.mobile.sketchvoca.R;


public class child_fragBGMSetting extends Fragment {
    final static private int MUSIC_ON_STATE = 1;
    final static private int MUSIC_OFF_STATE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_child_fragbgmsetting, container, false);
        Intent intent = new Intent(getContext(), MusicService.class);
        ToggleButton toggleButton = (ToggleButton) v.findViewById(R.id.toggleButton);
        if(PreferenceManager.getInt(getContext(), "music") == MUSIC_ON_STATE){
            toggleButton.setChecked(true);
        } else if(PreferenceManager.getInt(getContext(), "music") == MUSIC_OFF_STATE){
            toggleButton.setChecked(false);
        }
        toggleButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            //음악재생
                            getActivity().startService(intent);
                            PreferenceManager.setInt(getContext(), "music", MUSIC_ON_STATE);
                        }
                        else {
                            //음악중지
                            getActivity().stopService(intent);
                            PreferenceManager.setInt(getContext(), "music", MUSIC_OFF_STATE);
                        }
                    }
                }
        );
        return v;
    }
}