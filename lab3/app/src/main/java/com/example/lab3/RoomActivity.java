package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RoomActivity extends AppCompatActivity {

    ListView userlist;
    ArrayList<String> list_loginUsers = new ArrayList<String>();
    ArrayAdapter adapter;

    ListView listviewv_requstedUsers;
    ArrayList<String> list_requestedUsers = new ArrayList<String>();
    ArrayAdapter reqUsersAdpt;

    TextView tvUserID, tvSendRequest, tvAcceptRequest;
    String LoginUserID, UserName, LoginUID;

    User player;

    private FirebaseAuth fauth;
    private FirebaseAuth.AuthStateListener fAuthListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://lab3-dfb82-default-rtdb.firebaseio.com/");
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        tvSendRequest = (TextView) findViewById(R.id.tvSendRequest);
        tvAcceptRequest = (TextView) findViewById(R.id.tvAcceptRequest);

        tvSendRequest.setText("Please wait...");
        tvAcceptRequest.setText("Please wait...");

        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        fauth = FirebaseAuth.getInstance();

        userlist = (ListView) findViewById(R.id.lv_loginUsers);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_loginUsers);
        userlist.setAdapter(adapter);


        listviewv_requstedUsers = (ListView) findViewById(R.id.lv_requestedUsers);
        reqUsersAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_requestedUsers);
        listviewv_requstedUsers.setAdapter(reqUsersAdpt);


        tvUserID = (TextView) findViewById(R.id.tvLoginUser);




        fAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    LoginUID = user.getUid();
                    LoginUserID = user.getEmail();
                    tvUserID.setText(LoginUserID);
                    UserName = convertEmailToString(LoginUserID);
                    UserName = UserName.replace(".", "");
                    myRef.child("users").child(UserName).child("request").setValue(LoginUID);
                    reqUsersAdpt.clear();
                    AcceptIncommingRequests();
                }
            }
        };



        myRef.getRoot().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateLoginUsers(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String requestToUser = ((TextView)view).getText().toString();
                confirmRequest(requestToUser, "To");
            }
        });



        listviewv_requstedUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String requestFromUser = ((TextView)view).getText().toString();
                confirmRequest(requestFromUser, "From");
            }
        });
    }

    void confirmRequest(final String OtherPlayer, final String reqType){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        //final View dialogView = inflater.inflate(R.layout.connect_player_dialog, null);
        //b.setView(dialogView);

        b.setTitle("Start Game?");
        b.setMessage("Connect with " + OtherPlayer);
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myRef.child("users")
                        .child(OtherPlayer).child("request").push().setValue(LoginUserID);
                if(reqType.equalsIgnoreCase("From")) {
                    StartGame(OtherPlayer + ":" + UserName, OtherPlayer, "From");
                }else{
                    StartGame(UserName + ":" + OtherPlayer, OtherPlayer, "To");
                }
            }
        });
        b.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.show();



    }


    void StartGame(String PlayerGameID, String OtherPlayer, String requestType){
        myRef.child("playing").child(PlayerGameID).removeValue();
        player.setLoginUID(LoginUID);
        player.setOtherPlayer(OtherPlayer);
        player.setPlayerSession(PlayerGameID);
        player.setRequestType(requestType);
        player.setUserName(UserName);
        Intent intent = new Intent(getApplicationContext(), PlaceShipsActivity.class);
        intent.putExtra("player_session", PlayerGameID);
        intent.putExtra("user_name", UserName);
        intent.putExtra("other_player", OtherPlayer);
        intent.putExtra("login_uid", LoginUID);
        intent.putExtra("request_type", requestType);
        startActivity(intent);
    }

    private String convertEmailToString(String Email){
        String value = Email.substring(0, Email.indexOf('@'));
        value = value.replace(".", "");
        return value;
    }


    void AcceptIncommingRequests(){
        myRef.child("users").child(UserName).child("request")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if(map != null){
                                String value = "";
                                for(String key:map.keySet()){
                                    value = (String) map.get(key);
                                    reqUsersAdpt.add(convertEmailToString(value));
                                    reqUsersAdpt.notifyDataSetChanged();
                                    myRef.child("users").child(UserName).child("request").setValue(LoginUID);
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

    public void updateLoginUsers(DataSnapshot dataSnapshot){
        String key = "";
        Set<String> set = new HashSet<String>();
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext()){
            key = ((DataSnapshot) iterator.next()).getKey();
            if(!key.equalsIgnoreCase(UserName)) {
                set.add(key);
            }
        }

        adapter.clear();
        adapter.addAll(set);
        adapter.notifyDataSetChanged();
        tvSendRequest.setText("Send request to");
        tvAcceptRequest.setText("Accept request from");
    }

    @Override
    public void onStart() {
        super.onStart();
        fauth.addAuthStateListener(fAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (fAuthListener != null) {
            fauth.removeAuthStateListener(fAuthListener);
        }
    }
}