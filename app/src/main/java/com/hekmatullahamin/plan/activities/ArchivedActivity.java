package com.hekmatullahamin.plan.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.ArchivedRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Item;

import java.util.ArrayList;

public class ArchivedActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<Item> itemsFromAdapter = new ArrayList<>();
    private Button exportButton;
    private TextView noItemArchivedTV;
    private RecyclerView allArchivedItemsRecyclerView;
    private ArchivedRecyclerViewAdapter archivedRecyclerViewAdapter;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private DatabaseHandler databaseHandler;
    private ArrayList<Item> archivedItems;

    private Bundle bundle;
    private int personId;
    private static final String FRIEND_ID = "FRIEND_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

//        line No.46
        fieldsInitialization();

//        line No.60
        refreshRecyclerView();

        if (archivedItems.size() > 0) {
            noItemArchivedTV.setVisibility(View.GONE);
        } else {
            noItemArchivedTV.setVisibility(View.VISIBLE);
        }
    }

    private void fieldsInitialization() {
        bundle = getIntent().getExtras();
        personId = bundle.getInt(FRIEND_ID);
        exportButton = findViewById(R.id.archivedActivityExportEntriesButtonId);
        noItemArchivedTV = findViewById(R.id.archivedActivityNoItemArchivedTextViewId);
        allArchivedItemsRecyclerView = findViewById(R.id.archivedActivityRecyclerViewId);
        allArchivedItemsRecyclerView.setHasFixedSize(true);
        allArchivedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        collapsingToolbarLayout = findViewById(R.id.archivedActivityCollapsingToolbarLayoutId);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white, null));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white, null));
        databaseHandler = new DatabaseHandler(this);
    }

    private void refreshRecyclerView() {
        archivedItems = databaseHandler.getAllArchivedItemsOfSpecificPerson(personId);
        archivedRecyclerViewAdapter = new ArchivedRecyclerViewAdapter(ArchivedActivity.this, archivedItems, exportButton);
        allArchivedItemsRecyclerView.setAdapter(archivedRecyclerViewAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.archivedActivityExportEntriesButtonId) {
            itemsFromAdapter = archivedRecyclerViewAdapter.getExportedItems();
            for (Item item : itemsFromAdapter) {
                item.setItemState(0);
                databaseHandler.updateItem(item);
            }
            finish();
        }
    }
}