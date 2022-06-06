package com.example.g_pro_justlikemeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.g_pro_justlikemeapp.Adapter.UserAdapter;
import com.example.g_pro_justlikemeapp.Model.Users;
import com.example.g_pro_justlikemeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {


    EditText search_users;

    RecyclerView recyclerView;
    List<Users> usersList;
    UserAdapter mAdapter;
    FirebaseUser firebaseUser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersList = new ArrayList<>();
        displayusers();
        search_users=view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_users(charSequence.toString().toLowerCase());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                search_users(charSequence.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search_users(editable.toString().toLowerCase());


            }
        });

        return view;
    }

    private void search_users(String s) {
        final FirebaseUser fUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot  dataSnapshot:snapshot.getChildren() ){
                    Users users=snapshot.getValue((Users.class));
                    assert users !=null;
                    assert  fUser!=null;
                    try {


                        if(!users.getId().equals( fUser.getUid())){
                            usersList.add(users);


                        }} catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }


                }

                mAdapter  = new UserAdapter(getContext(), usersList, false);
                recyclerView.setAdapter(mAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void displayusers() {

        usersList = new ArrayList<>();

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (search_users.getText().toString().equals("")){
                    usersList.clear();

                    for (DataSnapshot ds: snapshot.getChildren()) {

                        Users users = ds.getValue(Users.class);

                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    /*if (!users.getId().equals(firebaseUser.getUid())) {


                        usersList.add(users);

                    }*/
                        assert users !=null;
                        assert firebaseUser!=null;

                        if(!users.getId().equals(firebaseUser.getUid()))
                        {
                            usersList.add(users);
                        }



                        mAdapter  = new UserAdapter(getContext(), usersList, false);
                        recyclerView.setAdapter(mAdapter);








                    }

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

}