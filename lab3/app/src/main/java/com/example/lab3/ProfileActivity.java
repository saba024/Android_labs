package com.example.lab3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;
    private ProfileViewModel profileViewModel;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
            profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

            final TextView text = findViewById(R.id.text_username);
            profileViewModel.getUserNameLiveData().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    text.setText(s);
                }
            });

            ImageView image = findViewById(R.id.image_avatar);
            Picasso.with(getApplicationContext())
                    .load(profileViewModel.getImageUri())
                    .error(R.drawable.board_white)
                    .into(image);

            Button btn = findViewById(R.id.btn_change_username);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });

            btn = findViewById(R.id.btn_change_avatar);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
            super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

            ImageView imgview = (ImageView) findViewById(R.id.image_avatar);

            if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
                Storage.SaveAvatar(imageReturnedIntent.getData());
                Picasso.with(getApplicationContext())
                        .load(profileViewModel.getImageUri())
                        .error(R.drawable.user)
                        .into(imgview);
            }
        }

        public void showDialog(){
            View promptView = getLayoutInflater().inflate(R.layout.prompt, null);

            AlertDialog.Builder mdialogbuilder = new AlertDialog.Builder(this);
            mdialogbuilder.setView(promptView);

            final EditText input = (EditText) promptView.findViewById(R.id.input_text);

            mdialogbuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    profileViewModel.setUserName(input.getText().toString());
                                }
                            })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            mdialogbuilder.create();
            mdialogbuilder.show();
        }
}