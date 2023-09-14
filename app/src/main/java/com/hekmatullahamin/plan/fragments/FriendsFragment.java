package com.hekmatullahamin.plan.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.FriendsRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Friend;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {

    private View fragmentFriends;
    private EditText searchFriendET;
    private FloatingActionButton addFriendFAB;
    private RecyclerView recyclerView;
    private FriendsRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Friend> friends;
    private DatabaseHandler databaseHandler;
    private BottomSheetDialog bottomSheetDialog;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentFriends = inflater.inflate(R.layout.fragment_friends, container, false);

        fieldsInitialization();

        searchFriendET.addTextChangedListener(new TextWatcher() {
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

        addFriendFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTaskBottomSheetDialog();
            }
        });

        return fragmentFriends;
    }

    private void fieldsInitialization() {
        searchFriendET = fragmentFriends.findViewById(R.id.friendsFragmentSearchPersonEditTextId);
        addFriendFAB = fragmentFriends.findViewById(R.id.friendsFragmentAddPersonFABId);
        recyclerView = fragmentFriends.findViewById(R.id.friendsFragmentRecyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseHandler = new DatabaseHandler(getContext());
    }

    private void filterRecyclerView(String personName) {
        ArrayList<Friend> filteredList = new ArrayList<>();
        for (Friend friend : friends) {
            if (friend.getFriendName().toLowerCase().contains(personName)) {
                filteredList.add(friend);
            }
        }
        recyclerViewAdapter.filterRecyclerView(filteredList);
    }

    private void refreshRecyclerView() {
//        for refreshing of recycler view
        friends = databaseHandler.getAllFriends();
        recyclerViewAdapter = new FriendsRecyclerViewAdapter(getContext(), friends);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void openAddTaskBottomSheetDialog() {
        View customBottomSheetDialog = LayoutInflater.from(getContext()).inflate(R.layout.custom_add_friend_bottom_sheet_dialog, null);

        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(customBottomSheetDialog);

        Button addFriendButton = customBottomSheetDialog.findViewById(R.id.customAddFriendBottomSheetDialogAddFriendButtonId);
        EditText typedFriendNameET = customBottomSheetDialog.findViewById(R.id.customAddFriendBottomSheetDialogTypeFriendEditTextId);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendName = typedFriendNameET.getText().toString().trim();
                if (!TextUtils.isEmpty(friendName)) {
//                    Line No. 128
                    addFriendToDatabase(friendName);
                } else {
                    Toast.makeText(getContext(), "Friend name can not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void addFriendToDatabase(String personName) {
        Friend friend = new Friend();
        friend.setFriendName(personName);
        databaseHandler.addFriend(friend);
        refreshRecyclerView();
        int lastPersonAddedPosition = friends.size() - 1;
        recyclerView.smoothScrollToPosition(lastPersonAddedPosition);
        bottomSheetDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecyclerView();
    }
}