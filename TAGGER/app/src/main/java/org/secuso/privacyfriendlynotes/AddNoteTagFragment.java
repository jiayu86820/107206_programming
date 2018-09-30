package org.secuso.privacyfriendlynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class AddNoteTagFragment extends Fragment implements View.OnClickListener {

    Button btnText;
    private Editable StrText;
    private String CText;
    private EditText editTextView;


    public AddNoteTagFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addnotetag, container, false);
        // Inflate the layout for this fragment
        btnText = (Button) view.findViewById(R.id.btnText);
        editTextView=(EditText)  view.findViewById(R.id.editText2);


        //btnText.setOnClickListener(this);
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FindTagFragment.class);
                StrText= editTextView.getEditableText();
                CText =StrText.toString().trim();
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putString("detectedText", CText);

                //將Bundle物件assign給intent
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
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
            //case R.id.button:
            //   startActivity(new Intent(getActivity(), TextNoteActivity.class));
            //  getActivity().finish();
        }
    }
}