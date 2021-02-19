package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    List<Integer> mylist = new ArrayList<Integer>();
    List<Integer> enemylist = new ArrayList<Integer>();
    ImageView imageView;
    ViewFlipper vf;
    boolean screen;
    boolean player1 = true;
    boolean player2 = false;
    String playerSession = "";
    String userName = "";
    String otherPlayer = "";
    String loginUID = "";
    String requestType = "";

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://lab3-dfb82-default-rtdb.firebaseio.com//");
    DatabaseReference myRef = database.getReference();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setBoard();
        userName = getIntent().getExtras().get("user_name").toString();
        loginUID = getIntent().getExtras().get("login_uid").toString();
        otherPlayer = getIntent().getExtras().get("other_player").toString();
        requestType = getIntent().getExtras().get("request_type").toString();
        playerSession = getIntent().getExtras().get("player_session").toString();
        vf = (ViewFlipper) findViewById( R.id.viewFlipper );

        if(requestType.equals("From")){
            myRef.child("playing").child(playerSession).child("turn").setValue(userName);
            screen = true;

        }else{
            myRef.child("playing").child(playerSession).child("turn").setValue(otherPlayer);
            vf.showNext();
        }



        myRef.child("playing").child(playerSession).child("game")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if(map != null){
                                String value = "";
                                String firstPlayer = userName;
                                for(String key:map.keySet()){
                                    value = (String) map.get(key);
                                    firstPlayer = value;
                                    String[] splitID = key.split(":");
                                    OtherPlayer(Integer.parseInt(splitID[1]));
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }

    public void PlayGame(List<Integer> exlist, int selectedBlock, ImageView selectedImage)
    {
        if (exlist.contains(selectedBlock))
        {
            selectedImage.setImageResource(R.drawable.cell_green);
            int position = exlist.indexOf(selectedBlock);;
            exlist.remove(position);
        }

        vf.showPrevious();
        selectedImage.setEnabled(false);
        checkWin();
    }

    public void BoardClick(View view)
    {
        ImageView setImageView = (ImageView) view;
        int selectedBlock = 0;
        for (int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                String temp = "btn_" + i + j;
                int ident = getResources().getIdentifier(temp, "id", getPackageName());
                if(setImageView.getId() == findViewById(ident).getId())
                {
                    selectedBlock = i * 10 + j;
                }
            }
        }


        myRef.child("playing").child(playerSession).child("game").child("block:"+ selectedBlock).setValue(userName);
        myRef.child("playing").child(playerSession).child("turn").setValue(otherPlayer);

        PlayGame(enemylist, selectedBlock, setImageView);

        vf.showPrevious();

    }


    public void getShips()
    {
        DatabaseReference reference = myRef.child(playerSession).child(userName).child("field");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren())
                {
                    int selectedblock = Integer.parseInt(snap.getKey());
                    mylist.add(selectedblock);
                }
                snapshot.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setBoard()
    {
        getShips();
        System.out.println(mylist);
        int len = mylist.size();
        for (int i = 0; i < len; i++)
        {
            String temp;
            if (mylist.get(i) - 10 < 0) {
                temp = "iv_" + "0" + mylist.get(i);
            }

            else {
                temp = "iv_" + mylist.get(i);
            }

            int ident = getResources().getIdentifier(temp, "id", getPackageName());
            imageView = findViewById(ident);
            imageView.setImageResource(R.drawable.yellow_cell);
        }
    }

    void OtherPlayer(int selectedBlock)
    {
        String temp;
        if (selectedBlock - 10 < 0) {
            temp = "iv_" + "0" + selectedBlock;
        }

        else {
            temp = "iv_" + selectedBlock;
        }

        int ident = getResources().getIdentifier(temp, "id", getPackageName());
        imageView = findViewById(ident);
        PlayGame(mylist, selectedBlock, imageView);
    }

    public void checkWin()
    {
        if(enemylist.isEmpty())
        {
            ShowAlert("You" +" are winner");
        }

        else if (mylist.isEmpty())
        {
            ShowAlert(otherPlayer +" is winner");
        }
    }

    void ShowAlert(String Title){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(Title)
                .setMessage("Start a new game?")
                .setNegativeButton("Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}