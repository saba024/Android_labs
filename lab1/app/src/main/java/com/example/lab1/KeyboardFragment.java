package com.example.lab1;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;


public class KeyboardFragment extends Fragment implements View.OnClickListener {

    private MainViewModel mainviewmodel;
    private String input;
    public static KeyboardFragment newInstance() {
        return new KeyboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_keyboard, container, false);
        mainviewmodel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        Button button = (Button) view.findViewById(R.id.button1);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button3);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button4);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button5);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button6);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button7);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button8);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button9);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button0);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.buttonDel);
        button.setOnClickListener(item -> mainviewmodel.getFirst_field_data().setValue(""));
        button = (Button) view.findViewById(R.id.buttonDot);
        button.setOnClickListener(dotInput);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button)view;
        mainviewmodel.getFirst_field_data().setValue(mainviewmodel.getFirst_field_data().getValue() + btn.getText());
    }

    private View.OnClickListener dotInput = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            input = mainviewmodel.getFirst_field_data().getValue();
            if(input.contains(".") == false && input.equals("") == false)
            {
                mainviewmodel.getFirst_field_data().setValue(mainviewmodel.getFirst_field_data().getValue() + ".");
            }
        }
    };
}