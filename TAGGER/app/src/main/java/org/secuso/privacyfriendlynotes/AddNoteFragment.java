package org.secuso.privacyfriendlynotes;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.secuso.privacyfriendlynotes.code_old.TextNoteActivity;


public class AddNoteFragment extends Fragment implements View.OnClickListener {

  Button btnText;

    public AddNoteFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addnote, container, false);
        // Inflate the layout for this fragment
        btnText = (Button) view.findViewById(R.id.button);
       btnText.setOnClickListener(this);

        return view;
    }

    /*

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_text:
                    startActivity(new Intent(getActivity().getApplication(),TextNoteActivity.class));
                  getActivity().finish();
            }
        }*/


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                startActivity(new Intent(getActivity(), TextNoteActivity.class));
                getActivity().finish();
        }
    }
}
