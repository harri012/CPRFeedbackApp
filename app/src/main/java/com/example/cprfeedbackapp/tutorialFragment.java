package com.example.cprfeedbackapp;


//import static android.support.v4.media.session.MediaControllerCompatApi21.getPackageName;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tutorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tutorialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    VideoView CprVideoView;

    public tutorialFragment() {
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
    public static tutorialFragment newInstance(String param1, String param2) {
        tutorialFragment fragment = new tutorialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        CprVideoView = fragmentView.findViewById(R.id.videoView);
        CprVideoView.setVideoURI(Uri.parse("https://youtu.be/-NodDRTsV88"));
//        CprVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.howtocpr));
//        CprVideoView.setOnPreparedListener(mediaPlayer -> CprVideoView.start());
//        CprVideoView.start();

        MediaController videoMediaController = new MediaController(this.getContext());
        videoMediaController.setMediaPlayer(CprVideoView);
        CprVideoView.setMediaController(videoMediaController);

        return fragmentView;
    }
}