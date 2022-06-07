package com.example.g_pro_justlikemeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.g_pro_justlikemeapp.Adapter.CommunityAdapter;
import com.example.g_pro_justlikemeapp.Model.CommunityChat;
import com.example.g_pro_justlikemeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CommunityFragment extends Fragment {

    private RecyclerView groupsRv;
    private FirebaseAuth firebaseAuth;
    Button Add;
    private ArrayList<CommunityChat> CommunityChatList;
    private CommunityAdapter communityAdapter;
    String myGroupRole,groupId;
    public CommunityFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_community, container, false);
        groupsRv=view.findViewById(R.id.groupsRv);
        firebaseAuth=FirebaseAuth.getInstance();
        loadCommunityChat();
        return view;
    }

    private void loadCommunityChat() {
        CommunityChatList=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("V_Comm");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CommunityChatList.size();
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds.child("participant").child(firebaseAuth.getUid()).exists())
                    { CommunityChat model=ds.getValue(CommunityChat.class);
                        CommunityChatList.add(model);



                    }
                    else {CommunityChat model=ds.getValue(CommunityChat.class);
                        CommunityChatList.add(model);}
                }
                communityAdapter =new CommunityAdapter(getActivity(),CommunityChatList);
                groupsRv.setAdapter(communityAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void searchCommunityChat(String query) {
        CommunityChatList=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("V_Comm");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CommunityChatList.size();
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds.child("participant").child(firebaseAuth.getUid()).exists())
                    { if(ds.child("groupTitle").toString().toLowerCase().contains(query.toLowerCase())) {
                        CommunityChat model = ds.getValue(CommunityChat.class);
                        CommunityChatList.add(model);

                    }

                    }
                }
                communityAdapter =new CommunityAdapter(getActivity(),CommunityChatList);
                groupsRv.setAdapter(communityAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}