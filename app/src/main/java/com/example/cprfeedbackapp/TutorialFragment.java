package com.example.cprfeedbackapp;


//import static android.support.v4.media.session.MediaControllerCompatApi21.getPackageName;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TutorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    VideoView CprVideoView;

    public TutorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tutorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialFragment newInstance(String param1, String param2) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tutorial_navigation_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.InstructionManual:
                // navigate to instruction manual screen
            case R.id.CprVideo:
                // navigate to cpr videos
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        CprVideoView = fragmentView.findViewById(R.id.videoView);
//        CprVideoView.setVideoURI(Uri.parse("https://youtu.be/-NodDRTsV88"));
//        CprVideoView.setVideoURI(Uri.parse("https://youtu.be/A5PnI4I-vd8"));
        CprVideoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.howtocpr));
//        CprVideoView.setOnPreparedListener(mediaPlayer -> CprVideoView.start());
//        CprVideoView.start();

        MediaController videoMediaController = new MediaController(this.CprVideoView.getContext());
        videoMediaController.setMediaPlayer(CprVideoView);
        CprVideoView.setMediaController(videoMediaController);

        return fragmentView;
    }
}