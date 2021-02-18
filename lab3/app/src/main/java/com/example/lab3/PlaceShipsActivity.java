package com.example.lab3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;



public class PlaceShipsActivity extends AppCompatActivity {


    int count = 0;
    boolean flag = false;
    ShipType selectedShip;
    String playerSession = "";
    String userName = "";
    String otherPlayer = "";
    String loginUID = "";
    String requestType = "";

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://lab3-dfb82-default-rtdb.firebaseio.com/");
    DatabaseReference myRef = database.getReference("playing");


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_ships);

        userName = getIntent().getExtras().get("user_name").toString();
        loginUID = getIntent().getExtras().get("login_uid").toString();
        otherPlayer = getIntent().getExtras().get("other_player").toString();
        requestType = getIntent().getExtras().get("request_type").toString();
        playerSession = getIntent().getExtras().get("player_session").toString();

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            setButton(btn, ShipType.Carrier);
        });

        Button btn1 = findViewById(R.id.button2);
        btn1.setOnClickListener(v -> {
            setButton(btn1, ShipType.Battleship);
        });

        Button btn2 = findViewById(R.id.button3);
        btn2.setOnClickListener(v -> {
            setButton(btn2, ShipType.Submarine);
        });

        Button btn3 = findViewById(R.id.button4);
        btn3.setOnClickListener(v -> {
            setButton(btn, ShipType.Destroyer);
        });

        Button btnstartgame = findViewById(R.id.button5);
        btnstartgame.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("player_session", playerSession);
            intent.putExtra("user_name", userName);
            intent.putExtra("other_player", otherPlayer);
            intent.putExtra("login_uid", playerSession);
            intent.putExtra("request_type", requestType);
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ResourceType")
    public void GameBoardClick(View view){
        ImageView selectedImage = (ImageView) view;
        ImageView imageView = (ImageView) view;

        int n = setCount();
        int selectedBlock = 0;

        for (int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j ++)
            {
                String temp = "iv_" + i + j;
                int ident = getResources().getIdentifier(temp, "id", getPackageName());
                if(selectedImage.getId() == findViewById(ident).getId())
                {
                    selectedBlock = i * 10 + j;
                }
            }
        }
        System.out.println(selectedBlock);

        if (flag) {
            for (int i = 0; i < n; i++) {
                imageView = findViewById(selectedImage.getId() + i);
                imageView.setImageResource(R.drawable.yellow_cell);
                String selBlock = String.valueOf(selectedBlock + i);
                //list.add(selectedBlock + i);
                myRef.child(playerSession).child(userName).child("field").child(selBlock).setValue(true);
            }
            count--;
        }

        if (count == 0) {
            flag = false;
        }
    }

    public int setCount()
    {
        int n = 0;
        if(selectedShip == ShipType.Carrier)
        {
            n = 4;
        }

        else if(selectedShip == ShipType.Battleship)
        {
            n = 3;
        }

        else if (selectedShip == ShipType.Submarine)
        {
            n = 2;
        }

        else if(selectedShip == ShipType.Destroyer)
        {
            n = 1;
        }

        return n;
    }

    @SuppressLint("ResourceAsColor")
    public void setButton(Button btn, ShipType shipType)
    {
        selectedShip = shipType;
        flag = true;
        count = 1;
        btn.setClickable(false);
        btn.setBackgroundColor(R.color.grey);
    }

}