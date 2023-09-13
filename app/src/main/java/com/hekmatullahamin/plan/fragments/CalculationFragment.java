package com.hekmatullahamin.plan.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.AllTransactionsActivity;
import com.hekmatullahamin.plan.adapters.CalculationRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Person;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;

public class CalculationFragment extends Fragment {

    //    TV stands for text view
//    ET stands for edit text
    private View calculationFragmentView;
    private TextView totalIncomeTV, totalLostTV, incomeCurrencySymbol, lossCurrencySymbol;
    private EditText searchPersonEditText;
    private CardView nativeAdCardViewLayout;
    private FloatingActionButton addPersonFAB;
    private RecyclerView peopleRecyclerView;
    private CalculationRecyclerViewAdapter recyclerViewAdapter;
    private Toolbar calculationToolbar;

    private BottomSheetDialog bottomSheetDialog;

    private DatabaseHandler databaseHandler;
    private ArrayList<Person> people;

    private SharedPreferences mySettingPreferences;

    private NativeAd nativeAd;
    private Thread adThread;

    private static final String LIST_STATE_KEY = "LIST_STATE";
    private Parcelable listState = null;
    private LinearLayoutManager linearLayoutManager;

    public CalculationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("aaaoncreateView", "calculation");
        // Inflate the layout for this fragment
        calculationFragmentView = inflater.inflate(R.layout.fragment_calculation, container, false);

        fieldsInitialization();

        advertiseThread();

        searchPersonEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String personName = s.toString().toLowerCase().trim();
                if (personName != null) {
                    filterRecyclerView(personName);
                }
            }
        });

        addPersonFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTaskBottomSheetDialog();
            }
        });
        return calculationFragmentView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        listState = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    private void fieldsInitialization() {
        incomeCurrencySymbol = calculationFragmentView.findViewById(R.id.calculationFragmentReceiveCurrencySymbolTextViewId);
        lossCurrencySymbol = calculationFragmentView.findViewById(R.id.calculationFragmentLossCurrencySymbolTextViewId);
        totalIncomeTV = calculationFragmentView.findViewById(R.id.calculationFragmentTotalIncomeTextViewId);
        totalLostTV = calculationFragmentView.findViewById(R.id.calculationFragmentTotalLossTextViewId);
        searchPersonEditText = calculationFragmentView.findViewById(R.id.calculationFragmentSearchByPersonNameEditTextId);
        addPersonFAB = calculationFragmentView.findViewById(R.id.calculationFragmentAddPersonFABId);

        peopleRecyclerView = calculationFragmentView.findViewById(R.id.calculationFragmentCalculationsRecyclerViewId);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider_item_decoration));
        peopleRecyclerView.addItemDecoration(dividerItemDecoration);
        peopleRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        peopleRecyclerView.setLayoutManager(linearLayoutManager);

        calculationToolbar = calculationFragmentView.findViewById(R.id.calculationFragmentToolbarId);
        calculationToolbar.setTitle("Budget Control");
        calculationToolbar.inflateMenu(R.menu.custom_budget_control_menu);
        calculationToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.customBudgetControlMenuAllTransactionsItemId) {
                    Intent allTransactionsActivityIntent = new Intent(getContext(), AllTransactionsActivity.class);
                    startActivity(allTransactionsActivityIntent);
                }
                return true;
            }
        });

        people = new ArrayList<>();
        databaseHandler = new DatabaseHandler(getContext());
    }

    private void filterRecyclerView(String personName) {
        ArrayList<Person> filteredList = new ArrayList<>();
        for (Person person : people) {
            if (person.getPersonName().toLowerCase().contains(personName)) {
                filteredList.add(person);
            }
        }
        recyclerViewAdapter.filterRecyclerView(filteredList);
    }

    private void refreshRecyclerView() {
//        for refreshing of recycler view
        people = databaseHandler.getAllPeople();
        recyclerViewAdapter = new CalculationRecyclerViewAdapter(getContext(), people);
        peopleRecyclerView.setAdapter(recyclerViewAdapter);

//        for refreshing total income and loss of text view
        populateTotalIncomeAndLoss(people);
    }

    private void openAddTaskBottomSheetDialog() {
        View customBottomSheetDialog = LayoutInflater.from(getContext()).inflate(R.layout.custom_add_task_bottom_sheet_dialog, null);

        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(customBottomSheetDialog);

        TextView title = (TextView) customBottomSheetDialog.findViewById(R.id.customAddTaskBottomSheetDialogTitleTextViewId);
        Button addPersonButton = (Button) customBottomSheetDialog.findViewById(R.id.customAddTaskBottomSheetDialogAddButtonId);
        EditText typedPersonNameET = (EditText) customBottomSheetDialog.findViewById(R.id.customAddTaskBottomSheetDialogTypeEditTextId);
        title.setText("Person");
        typedPersonNameET.setHint("Type person name here");
        addPersonButton.setText("ADD PERSON");

        addPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personName = typedPersonNameET.getText().toString().trim();
                if (!TextUtils.isEmpty(personName)) {
//                    Line No. 128
                    addPersonToDatabase(personName);
                } else {
                    Toast.makeText(getContext(), "Please write something", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void addPersonToDatabase(String personName) {
        Person person = new Person();
        person.setPersonName(personName);
        databaseHandler.addPerson(person);
        refreshRecyclerView();
        int lastPersonAddedPosition = people.size() - 1;
        peopleRecyclerView.smoothScrollToPosition(lastPersonAddedPosition);
        bottomSheetDialog.dismiss();
    }

    private void populateTotalIncomeAndLoss(ArrayList<Person> people) {
        double totalIncome = 0.0, totalLoss = 0.0;
        for (Person person : people) {
            if (person.getPersonMoneyGainOrLoss().equals(Constants.TYPE_GAIN)) {
                totalIncome += person.getPersonMoneyAmount();
            } else {
                totalLoss += person.getPersonMoneyAmount();
            }
        }

        String totalIncomeString = Utils.formatMoney(totalIncome);
        String totalLossString = Utils.formatMoney(totalLoss);

        totalIncomeTV.setText(totalIncomeString);
        totalLostTV.setText(totalLossString);
    }

    private void loadAdvertise() {
        AdLoader adLoader = new AdLoader.Builder(getContext(), "ca-app-pub-7968324539066079/7376944210")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAdvertise) {
                        // Show the ad.
                        if (nativeAd == null) {
                            nativeAd = nativeAdvertise;
                        }

                        NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(getContext())
                                .inflate(R.layout.advertise_partial_native_layout, null);
                        mapNativeAdToLayout(nativeAdvertise, nativeAdView);

                        nativeAdCardViewLayout = calculationFragmentView.findViewById(R.id.calculationFragmentAdvertiseCardViewId);
                        nativeAdCardViewLayout.removeAllViews();
                        nativeAdCardViewLayout.addView(nativeAdView);

                        ImageView closeAdImageView = (ImageView) nativeAdView.findViewById(R.id.advertisePartialNativeLayoutCloseAdvertiseImageViewId);
                        closeAdImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nativeAdCardViewLayout.setVisibility(View.GONE);
                            }
                        });

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
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.advertisePartialNativeLayoutIconImageViewId));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.advertisePartialNativeLayoutHeadlineTextViewId));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.advertisePartialNativeLayoutAdvertiserTextViewId));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.advertisePartialNativeLayoutCallToActionButtonId));

        ((TextView) nativeAdView.getHeadlineView()).setText(advertiseFromGoogleMob.getHeadline());

        if (advertiseFromGoogleMob.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(advertiseFromGoogleMob.getIcon().getDrawable());
        }

        if (advertiseFromGoogleMob.getAdvertiser() == null) {
            nativeAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getAdvertiserView()).setText(advertiseFromGoogleMob.getAdvertiser());
        }

        if (advertiseFromGoogleMob.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(advertiseFromGoogleMob.getCallToAction());
        }

        nativeAdView.setNativeAd(advertiseFromGoogleMob);
    }

    private void advertiseThread() {
        adThread = new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadAdvertise();
                    }
                });
            }
        });
        adThread.start();
    }

    private void setCurrencySymbol() {
        //        for adding currency symbol which is chose from currency picker activity
        mySettingPreferences = getContext().getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        incomeCurrencySymbol.setText(currency);
        lossCurrencySymbol.setText(currency);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrencySymbol();
        refreshRecyclerView();
        if (listState != null) {
            linearLayoutManager.onRestoreInstanceState(listState);
            listState = null;
        }
    }

    @Override
    public void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
            nativeAd = null;
        }
        adThread.interrupt();
        adThread = null;
        super.onDestroy();
    }
}