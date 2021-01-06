package com.example.lab1;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.Toast;



public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<String> first_field_data;
    private MutableLiveData<String> second_field_data;
    private MutableLiveData<Item> first_field_spinner;
    private MutableLiveData<Item> second_filed_spinner;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getSecond_field_data(){
        if(second_field_data == null){
            second_field_data = new MutableLiveData<String>();
            second_field_data.setValue("");
        }
        return second_field_data;
    }

    public MutableLiveData<Item> getInputSpinner(){
        if(first_field_spinner == null){
            first_field_spinner = new MutableLiveData<Item>();
            first_field_spinner.setValue(Item.METERS);
        }
        return first_field_spinner;
    }

    public void setSecond_field_data(String str){
        if(second_field_data == null){
            second_field_data = new MutableLiveData<String>();
        }
        second_field_data.setValue(str);
    }

    public void setFirst_field_data(String str){
        if(first_field_data == null){
            first_field_data = new MutableLiveData<String>();
        }
        first_field_data.setValue(str);
    }

    public MutableLiveData<String> getFirst_field_data(){
        if(first_field_data == null){
            first_field_data = new MutableLiveData<String>();
            first_field_data.setValue("");
        }
        return first_field_data;
    }

    public MutableLiveData<Item> getSecond_filed_spinner(){
        if(second_filed_spinner == null){
            second_filed_spinner = new MutableLiveData<Item>();
            second_filed_spinner.setValue(Item.METERS);
        }
        return second_filed_spinner;
    }

    public MutableLiveData<Item> getFirst_field_spinner(){
        if(first_field_spinner == null){
            first_field_spinner = new MutableLiveData<Item>();
            first_field_spinner.setValue(Item.METERS);
        }
        return first_field_spinner;
    }


    public String convert(String data){
        String str = Translator.convert(data, getFirst_field_spinner().getValue(), getSecond_filed_spinner().getValue());
        setSecond_field_data(str);
        return second_field_data.getValue();
    }

    public String convert(Item data){
        String str = Translator.convert(data, getFirst_field_spinner().getValue(), getSecond_filed_spinner().getValue());
        setSecond_field_data(str);
        return second_field_data.getValue();
    }


    public void PasteToBuffer(CopyOrder type, ClipboardManager clipManager){
        ClipData clipboard;
        if(type == CopyOrder.INPUT){
            clipboard = ClipData.newPlainText("text", getFirst_field_data().getValue());
        }
        else{
            clipboard = ClipData.newPlainText("text", getSecond_field_data().getValue());
        }
        clipManager.setPrimaryClip(clipboard);
        Toast toast = Toast.makeText(getApplication(), clipboard.toString(), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void ReplaceValues()
    {
        String temp = first_field_data.getValue();
        first_field_data.setValue(second_field_data.getValue());
        second_field_data.setValue(temp);
        Item temporary;
        temporary = first_field_spinner.getValue();
        first_field_spinner.setValue(second_filed_spinner.getValue());
        second_filed_spinner.setValue(temporary);
    }
}