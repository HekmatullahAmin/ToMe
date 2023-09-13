package com.hekmatullahamin.plan.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.captaindroid.tvg.Tvg;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.SpinnerDayOfWeekAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.MyActivity;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity {

    private TextView dayOfTheWeekTV, fromTimeTV, toTimeTV;
    private Spinner daysOfTheWeekSpinner;
    private EditText scheduleActivity;
    private Button addActivity;
    private CardView nativeAdCardViewLayout;

    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

//        line No.94
        fieldsInitialization();

//        line No.160
        loadAdvertise();

        Tvg.change(dayOfTheWeekTV, new int[]{
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.orange),
                ContextCompat.getColor(this, R.color.purple),
                ContextCompat.getColor(this, R.color.pink),
                ContextCompat.getColor(this, R.color.green),
                ContextCompat.getColor(this, R.color.blue),
        });

        fromTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                line No.116
                pickTime("left");
            }
        });

        toTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                line No.116
                pickTime("right");
            }
        });

        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                line No.135
                addActivityToDatabase();
            }
        });

    }

    private void fieldsInitialization() {
        dayOfTheWeekTV = findViewById(R.id.addScheduleActivitySelectDayOfTheWeekTextViewId);
        daysOfTheWeekSpinner = findViewById(R.id.addScheduleActivitySelectDayOfTheWeekSpinnerId);
        fromTimeTV = findViewById(R.id.addScheduleActivityFromTimTextViewId);
        toTimeTV = findViewById(R.id.addScheduleActivityToTimeTextViewId);
        addActivity = findViewById(R.id.addScheduleActivityAddTimeAndTaskButtonId);
        scheduleActivity = findViewById(R.id.addScheduleActivityWriteYourTaskEditTextId);

        List<String> daysOfTheWeek = new ArrayList<>();
        daysOfTheWeek.add("Saturday");
        daysOfTheWeek.add("Sunday");
        daysOfTheWeek.add("Monday");
        daysOfTheWeek.add("Tuesday");
        daysOfTheWeek.add("Wednesday");
        daysOfTheWeek.add("Thursday");
        daysOfTheWeek.add("Friday");
        SpinnerDayOfWeekAdapter spinnerAdapter = new SpinnerDayOfWeekAdapter(this, R.layout.custom_spinner_text_view_row, daysOfTheWeek);
        daysOfTheWeekSpinner.setAdapter(spinnerAdapter);

        databaseHandler = new DatabaseHandler(this);
    }

    private void pickTime(String timePickerPosition) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
                if (timePickerPosition.equals("left")) {
                    fromTimeTV.setText(time);
                } else {
                    toTimeTV.setText(time);
                }
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private void addActivityToDatabase() {
        MyActivity myActivity = new MyActivity();
        String fromChooseTime = fromTimeTV.getText().toString();
        String toChooseTime = toTimeTV.getText().toString();
        String activity = scheduleActivity.getText().toString();
        String dayOfTheWeek = daysOfTheWeekSpinner.getSelectedItem().toString().toUpperCase();

        if (!TextUtils.isEmpty(fromChooseTime) && !TextUtils.isEmpty(toChooseTime) && !TextUtils.isEmpty(activity)) {
            myActivity.setActivityName(activity);
            myActivity.setActivityFromTime(fromChooseTime);
            myActivity.setActivityToTime(toChooseTime);
            myActivity.setActivityDayOfTheWeek(dayOfTheWeek);
            databaseHandler.addActivity(myActivity);

            Toast.makeText(this, "Activity " + myActivity.getActivityName() + " added successfully", Toast.LENGTH_SHORT).show();

            fromTimeTV.setText("");
            toTimeTV.setText("");
            scheduleActivity.setText("");

            Utils.hideSoftKeyboard(this);

        } else {
            Toast.makeText(AddScheduleActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAdvertise() {
        AdLoader adLoader = new AdLoader.Builder(AddScheduleActivity.this, "ca-app-pub-7968324539066079/1581745956")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAdvertise) {
                        // Show the ad.
                        NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(AddScheduleActivity.this)
                                .inflate(R.layout.advertise_full_native_layout, null);
                        mapNativeAdToLayout(nativeAdvertise, nativeAdView);

                        nativeAdCardViewLayout = findViewById(R.id.addScheduleActivityAdvertiseCardViewId);
                        nativeAdCardViewLayout.removeAllViews();
                        nativeAdCardViewLayout.addView(nativeAdView);

                        ImageView closeAdImageView = (ImageView) nativeAdView.findViewById(R.id.advertiseFullNativeLayoutCloseAdvertiseImageViewId);
                        closeAdImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nativeAdCardViewLayout.setVisibility(View.GONE);
                            }
                        });

                        if (isDestroyed()) {
                            nativeAdvertise.destroy();
                        }

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void mapNativeAdToLayout(NativeAd advertiseFromGoogleMob, NativeAdView nativeAdView) {
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.advertiseFullNativeLayoutIconImageViewId));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.advertiseFullNativeLayoutHeadlineTextViewId));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.advertiseFullNativeLayoutRatingBarId));
        nativeAdView.setMediaView(nativeAdView.findViewById(R.id.advertiseFullNativeLayoutMediaViewId));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.advertiseFullNativeLayoutCallToActionButtonId));

        ((TextView) nativeAdView.getHeadlineView()).setText(advertiseFromGoogleMob.getHeadline());

        if (advertiseFromGoogleMob.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(advertiseFromGoogleMob.getIcon().getDrawable());
        }

        if (advertiseFromGoogleMob.getStarRating() == null) {
            nativeAdView.getStarRatingView().setVisibility(View.GONE);
        } else {
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
            ((RatingBar) nativeAdView.getStarRatingView()).setRating(advertiseFromGoogleMob.getStarRating().floatValue());
        }

        if (advertiseFromGoogleMob.getMediaContent() == null) {
            nativeAdView.getMediaView().setVisibility(View.GONE);
        } else {
            nativeAdView.getMediaView().setVisibility(View.VISIBLE);
            ((MediaView) nativeAdView.getMediaView()).setMediaContent(advertiseFromGoogleMob.getMediaContent());
        }

        if (advertiseFromGoogleMob.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(advertiseFromGoogleMob.getCallToAction());
        }

        nativeAdView.setNativeAd(advertiseFromGoogleMob);
    }
}