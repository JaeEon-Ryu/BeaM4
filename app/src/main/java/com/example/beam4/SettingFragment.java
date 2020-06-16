package com.example.beam4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;

public class SettingFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        final SeekBar seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new similarityChangeListener());

        return view;
    }

    private class similarityChangeListener implements SeekBar.OnSeekBarChangeListener{
        String tag = "Similarity";
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            switch (progress){
                case 0:
                    SimilaritySetter.setSimilarity(5);
                    break;
                case 1:
                    SimilaritySetter.setSimilarity(10);
                    break;
                case 2:
                    SimilaritySetter.setSimilarity(20);
                    break;
                case 3:
                    SimilaritySetter.setSimilarity(40);
                    break;
                case 4:
                    SimilaritySetter.setSimilarity(80);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d(tag, "onProgressChanged: " + SimilaritySetter.getSimilarity());
        }
    }
}