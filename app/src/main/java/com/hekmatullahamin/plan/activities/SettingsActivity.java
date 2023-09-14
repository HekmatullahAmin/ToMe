package com.hekmatullahamin.plan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.utils.Constants;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    //    CIV stands for circular image view, IV stands for image view, ET stand for edit text
    private CircleImageView profilePictureCIV;
    private ImageView chooseProfilePictureIV;
    private EditText enteredNameET, enteredCurrencyET;
    private Button saveButton;

    private Toolbar toolbar;
    private Uri croppedUri;
    private SharedPreferences mySettingPreferences;

    private static final int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fieldsInitialization();

        populateSettingsNameProfileCurrency();

        chooseProfilePictureIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToPreferences();
            }
        });
    }

    private void fieldsInitialization() {
        profilePictureCIV = findViewById(R.id.settingsActivityProfilePictureCircleImageViewId);
        chooseProfilePictureIV = findViewById(R.id.settingsActivityChooseProfilePictureImageViewId);
        enteredNameET = findViewById(R.id.settingsActivityEnterNameEditTextId);
        enteredCurrencyET = findViewById(R.id.settingsActivityEnterCurrencyEditTextId);
        saveButton = findViewById(R.id.settingsActivitySaveSettingsButtonId);
        mySettingPreferences = getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, MODE_PRIVATE);

        toolbar = findViewById(R.id.settingsActivityToolbarId);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_white_24, null);
        upArrow.setColorFilter(getResources().getColor(R.color.white, null), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void populateSettingsNameProfileCurrency() {
        SharedPreferences preferences = getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, MODE_PRIVATE);
        String name = preferences.getString(Constants.MY_SETTING_USER_NAME, null);
        String profilePicture = preferences.getString(Constants.MY_SETTING_PROFILE_PICTURE, null);
        String currency = preferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        if (profilePicture != null) {
            profilePictureCIV.setImageURI(Uri.parse(profilePicture));
        }
        enteredCurrencyET.setText(currency);
        enteredNameET.setText(name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .start(this);
        } else if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            croppedUri = result.getUri();
            if (croppedUri != null) {
                profilePictureCIV.setImageURI(croppedUri);
            }
        }
    }

    private void saveToPreferences() {
        String userName = enteredNameET.getText().toString().trim();
        String currency = enteredCurrencyET.getText().toString().trim();
        if (!TextUtils.isEmpty(userName)) {
            SharedPreferences.Editor editor = mySettingPreferences.edit();
            if (croppedUri != null) {
                String croppedImageUriString = croppedUri.toString();
                editor.putString(Constants.MY_SETTING_PROFILE_PICTURE, croppedImageUriString);
            }
            editor.putString(Constants.MY_SETTING_USER_NAME, userName);
            editor.putString(Constants.MY_SETTING_CURRENCY_SYMBOL, currency);
            editor.commit();
            finish();
        } else {
            Toast.makeText(SettingsActivity.this, "Please type your name", Toast.LENGTH_SHORT).show();
        }
    }
}