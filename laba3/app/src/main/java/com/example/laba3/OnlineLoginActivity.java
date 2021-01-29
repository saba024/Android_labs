package com.example.laba3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class OnlineLoginActivity extends AppCompatActivity {

    ListView lv_login_users;
    ArrayList<String> list_loggingUsers;
    ArrayAdapter adpt;

    ListView lv_requestedUsers;
    ArrayList<String> list_requestedUsers;
    ArrayAdapter reqUsersAdapter;

    TextView tvUserId, tvSendRequest, tvAcceptRequest;
    String LoginUserID, UserName, LoginUID;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_login);


        tvSendRequest = (TextView) findViewById(R.id.tvSendRequest);
        tvAcceptRequest = (TextView) findViewById(R.id.tvAcceptRequest);

        tvSendRequest.setText("Please wait...");
        tvAcceptRequest.setText("Please wait...");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

        lv_login_users = (ListView) findViewById(R.id.lv_loginUsers);
        adpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        lv_login_users.setAdapter(adpt);

        lv_requestedUsers = (ListView) findViewById(R.id.lv_requestedUsers);
        reqUsersAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_requestedUsers);
        lv_requestedUsers.setAdapter(reqUsersAdapter);
        tvUserId = (TextView) findViewById(R.id.tvLoginUser);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    LoginUID = user.getUid();
                    //Log.d("Auth", "onAuthStateChanged:signed_in:" + LoginUID);
                    LoginUserID = user.getEmail();
                    tvUserId.setText(LoginUserID);
                    UserName = convertEmailToString(LoginUserID);
                    UserName = UserName.replace(".", "");
                    myRef.child("users").child(UserName).child("request").setValue(LoginUID);
                    reqUsersAdapter.clear();
                    AcceptIncommingRequests();
                } else {
                    // User is signed out
                    //Log.d("Auth", "onAuthStateChanged:signed_out");
                    JoinOnlineGame();
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

        lv_login_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String requestToUser = ((TextView)view).getText().toString();
                confirmRequest(requestToUser, "To");
            }
        });

        lv_requestedUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String requestFromUser = ((TextView)view).getText().toString();
                confirmRequest(requestFromUser, "From");
            }
        });
    }

    void confirmRequest(final String OtherPlayer, final String reqType){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.connect_player_dialog, null);
        builder.setView(dialogView);

        builder.setTitle("Start Game?");
        builder.setMessage("Connect with " + OtherPlayer);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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

        adpt.clear();
        adpt.addAll(set);
        adpt.notifyDataSetChanged();
        tvSendRequest.setText("Send request to");
        tvAcceptRequest.setText("Accept request from");
    }

    private String convertEmailToString(String email)
    {
        String value = email.substring(0, email.indexOf('@'));
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
                                    reqUsersAdapter.add(convertEmailToString(value));
                                    reqUsersAdapter.notifyDataSetChanged();
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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void RegisterUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void JoinOnlineGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.login_dialog, null);
        builder.setView(dialogView);

        final EditText etEmail = (EditText) dialogView.findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) dialogView.findViewById(R.id.etPassword);

        builder.setTitle("Please register");
        builder.setMessage("Enter you email and password for registration");
        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RegisterUser(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    void StartGame(String PlayerGameID, String OtherPlayer, String requestType){
        resetShipIcons();
        myRef.child("playing").child(PlayerGameID).removeValue();
        Intent intent = new Intent(getApplicationContext(), ShipRotateActivity.class);
        intent.putExtra("player_session", PlayerGameID);
        intent.putExtra("user_name", UserName);
        intent.putExtra("other_player", OtherPlayer);
        intent.putExtra("login_uid", LoginUID);
        intent.putExtra("request_type", requestType);
        startActivity(intent);
    }

    private void resetShipIcons()
    {
        ((ImageView)findViewById(R.id.image_my_carrier)).setImageResource(R.drawable.carrier);
        ((ImageView)findViewById(R.id.image_my_battleship)).setImageResource(R.drawable.battleship);
        ((ImageView)findViewById(R.id.image_my_cruiser)).setImageResource(R.drawable.cruiser);
        ((ImageView)findViewById(R.id.image_my_submarine)).setImageResource(R.drawable.submarine);
        ((ImageView)findViewById(R.id.image_my_destroyer)).setImageResource(R.drawable.destroyer);

        ((ImageView)findViewById(R.id.image_target_carrier)).setImageResource(R.drawable.carrier);
        ((ImageView)findViewById(R.id.image_target_battleship)).setImageResource(R.drawable.battleship);
        ((ImageView)findViewById(R.id.image_target_cruiser)).setImageResource(R.drawable.cruiser);
        ((ImageView)findViewById(R.id.image_target_submarine)).setImageResource(R.drawable.submarine);
        ((ImageView)findViewById(R.id.image_target_destroyer)).setImageResource(R.drawable.destroyer);
    }

    public void StartOnlineGame(View view)
    {
        Intent intent = new Intent(this, OnlineLoginActivity.class);
        startActivity(intent);
    }

}