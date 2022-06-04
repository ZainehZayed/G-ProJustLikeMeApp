package com.example.g_pro_justlikemeapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g_pro_justlikemeapp.Adapter.Add_Participants_CommAdapter;
import com.example.g_pro_justlikemeapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Community_participantAdd_Activity extends AppCompatActivity {
    private RecyclerView uerRv;
    private FirebaseAuth firebaseAuth;
    private String groupId;
    private  String   myGroupRole;
    private ActionBar actionBar;
    private ArrayList<Users> usersList;
    private Add_Participants_CommAdapter add_participants_groupAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_participant_add);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Add participants");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        uerRv=findViewById(R.id.group);
        firebaseAuth=FirebaseAuth.getInstance();
        groupId=getIntent().getStringExtra("groupId");
        loadGroupInfo();
        getAllUers();



    }

    private void getAllUers() {
        usersList=new ArrayList<>();
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Users users=ds.getValue(Users.class);
                    if(!firebaseAuth.getUid().equals(users.getId())){
                        usersList.add(users);
                    }
                }
                add_participants_groupAdapter=new Add_Participants_CommAdapter(Community_participantAdd_Activity.this,usersList,""+groupId,""+myGroupRole);
                uerRv.setAdapter(add_participants_groupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadGroupInfo() {
        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("V_Comm");

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("V_Comm");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    String groupId=""+ds.child("groupId").getValue();
                    String groupTitle=""+ds.child("groupTitle").getValue();
                    String groupDescription=""+ds.child("groupDescription").getValue();
                    String groupIcon=""+ds.child("groupIcon").getValue();
                    String creatBy=""+ds.child("creatBy").getValue();
                    String timestamp=""+ds.child("Timestamp").getValue();
                    //actionBar.setTitle("Add participants");
                    ref1.child(groupId).child("participant").child(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        myGroupRole=""+snapshot.child("role").getValue();
                                        actionBar.setTitle(groupTitle+"("+myGroupRole+")");
                                        getAllUers();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}