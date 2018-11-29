package org.secuso.privacyfriendlynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TakePhotoFragment extends Fragment implements View.OnClickListener{
    Button btnGo;
    public TakePhotoFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_takephoto, container, false);
        // Inflate the layout for this fragment
        btnGo = (Button) view.findViewById(R.id.button2);
        btnGo.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button2:
                //startActivity(new Intent(getActivity(), MainActivity33.class));
                //getActivity().finish();
                startActivity(new Intent(getActivity(), PhotoNoteActivity.class));
                getActivity().finish();
        }
    }
}