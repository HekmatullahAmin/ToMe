package com.hekmatullahamin.plan.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.ItemRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ArchivedActivity extends AppCompatActivity implements View.OnClickListener {

    public static List<Item> itemsFromAdapter = new ArrayList<>();
    private Button exportButton;
    private TextView noItemArchivedTV;
    private RecyclerView allArchivedItemsRecyclerView;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;

    private DatabaseHandler databaseHandler;
    private List<Item> archivedItems;

    private Bundle bundle;
    private int personId;
    private static final String PERSON_ID = "PERSON_ID";

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
        personId = bundle.getInt(PERSON_ID);
        exportButton = findViewById(R.id.archivedActivityExportEntriesButtonId);
        noItemArchivedTV = findViewById(R.id.archivedActivityNoItemArchivedTextViewId);
        allArchivedItemsRecyclerView = findViewById(R.id.archivedActivityRecyclerViewId);
        allArchivedItemsRecyclerView.setHasFixedSize(true);
        allArchivedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ArchivedActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider_item_decoration));
        allArchivedItemsRecyclerView.addItemDecoration(dividerItemDecoration);

        databaseHandler = new DatabaseHandler(this);
    }

    private void refreshRecyclerView() {
        archivedItems = databaseHandler.getAllArchivedItemsOfSpecificPerson(personId);
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(ArchivedActivity.this, archivedItems, exportButton);
        allArchivedItemsRecyclerView.setAdapter(itemRecyclerViewAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.archivedActivityExportEntriesButtonId) {
            itemsFromAdapter = itemRecyclerViewAdapter.getExportedItems();
            for (Item item : itemsFromAdapter) {
                item.setItemState(0);
                databaseHandler.updateItem(item);
            }
            finish();
        }
    }
}