package com.example.lab1;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;


public class ConvertFragment extends Fragment {

    MainViewModel mainViewModel;
    ClipboardManager clipboardManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_convert, container, false);
        EditText editInput = layout.findViewById(R.id.inputField);
        EditText editOutput = layout.findViewById(R.id.outputField);
        Spinner spinnerInput = layout.findViewById(R.id.inputSpinner);
        Spinner spinnerOutput = layout.findViewById(R.id.outputSpinner);

        spinnerInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainViewModel.setFirst_field_data(spinnerInput.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerOutput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainViewModel.setSecond_field_data(spinnerOutput.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mainViewModel.getFirst_field_data().observe(requireActivity(), value -> editInput.setText(value));
        mainViewModel.getSecond_field_data().observe(requireActivity(), value -> editOutput.setText(value));
        clipboardManager = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        layout.findViewById(R.id.exchange_button).setOnClickListener(item -> mainViewModel.ReplaceValues());
        layout.findViewById(R.id.convert_button).setOnClickListener(item -> mainViewModel.convert(editInput.getText().toString()));
        layout.findViewById(R.id.save_input_button).setOnClickListener(item -> mainViewModel.PasteToBuffer(CopyOrder.INPUT, clipboardManager));
        layout.findViewById(R.id.save_output_button).setOnClickListener(item -> mainViewModel.PasteToBuffer(CopyOrder.OUTPUT, clipboardManager));
        return layout;
    }
}