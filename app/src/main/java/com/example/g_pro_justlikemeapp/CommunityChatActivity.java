package com.example.g_pro_justlikemeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.g_pro_justlikemeapp.Adapter.AdapterCommunityChat;
import com.example.g_pro_justlikemeapp.Model.ModelCommunityChat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CommunityChatActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private String groupId,myGroupRole="";

    //private Toolbar toolbar;
    private ImageView groupIconIv,imageview3;
    private ImageButton attachBtn,sendBtn,addParticipent;
    private TextView groupTitleTv;
    private EditText messageEt;
    private RecyclerView chatRv;
    private ArrayList<ModelCommunityChat> CommunityChatList;
    private AdapterCommunityChat adapterCommunityChat;
    ImageView Post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chat);

        //toolbar=findViewById(R.id.toolbar);
        groupIconIv=findViewById(R.id.groupIconIv);
        groupTitleTv=findViewById(R.id.groupTitleTv);
        attachBtn=findViewById(R.id.attachBtn);
        messageEt=findViewById(R.id.messageEt);
        sendBtn=findViewById(R.id.sendBtn);
        chatRv=findViewById(R.id.chatRv);
        addParticipent=findViewById(R.id.AddParticipent);

        Intent intent=getIntent();
        groupId=intent.getStringExtra("groupId");
        firebaseAuth=FirebaseAuth.getInstance();
        loadGroupInfo();
        loadMyGroupRole();
        loadGroupMessage();
        Post=findViewById(R.id.imageView3);
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToPost();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=messageEt.getText().toString().trim();
                if(TextUtils.isEmpty(message))
                {
                    Toast.makeText(CommunityChatActivity.this, "Can't send empty message ", Toast.LENGTH_SHORT).show();
                }
                else {sendMessage(message);}
            }
        });
        // imageview3.setOnClickListener(new O);

        addParticipent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivites();
//Intent intent1=new Intent(this,Community_participantAdd_Activity.class);
//Intent intent1=new Intent(this,Community_participantAdd_Activity.class);

                //startActivity(new Intent(CommunityChatActivity.this,
                //Community_participantAdd_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ));
            }
        });
    }
    private void switchActivites()

    {
        Intent intent1=new Intent(this,Community_participantAdd_Activity.class);
        intent1.putExtra("groupId",groupId);
        startActivity(intent1);
    }
    private void switchToPost()

    {
        Intent intent1=new Intent(this,AddPostActivity.class);
        intent1.putExtra("groupId",groupId);
        startActivity(intent1);
    }
    private void loadMyGroupRole() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("V_Comm");
        ref.orderByChild("id").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()
                        ) {
                            myGroupRole=""+ds.child("role").getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadGroupMessage() {
        CommunityChatList=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("V_Comm");
        ref.child(groupId).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CommunityChatList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {ModelCommunityChat model=ds.getValue(ModelCommunityChat.class);
                    CommunityChatList.add(model);
                }
                adapterCommunityChat=new AdapterCommunityChat(CommunityChatActivity.this,CommunityChatList);
                chatRv.setAdapter(adapterCommunityChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {
        String Timestamp=""+System.currentTimeMillis();
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("sender","" + firebaseAuth.getUid());
        hashMap.put("message","" +message);
        hashMap.put("Timestamp","" +Timestamp);
        hashMap.put("type","text");
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("V_Comm");
        ref.child(groupId).child("Messages").child(Timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                messageEt.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CommunityChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGroupInfo() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("V_Comm");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {  /*hashMap.put("groupId",""+g_limestam);
                  hashMap.put("groupTitle",""+groupTitle);
                  hashMap.put("groupDescription",""+groupDescription);
                  hashMap.put("groupIcon",""+groupIcon);
                  hashMap.put("Timestamp",""+g_limestam);
                  hashMap.put("creatBY",""+mAuth.getUid());*/
                    String groupTitle=""+ds.child("groupTitle").getValue();
                    String groupDescription=""+ds.child("groupDescription").getValue();
                    String groupIcon=""+ds.child("groupIcon").getValue();
                    String Timestamp=""+ds.child("Timestamp").getValue();
                    String creatBy=""+ds.child("creatBy").getValue();
                    groupTitleTv.setText(groupTitle);
                    try{
                        // Glide.with().load(groupIcon).placeholder(R.drawable.ic_group_primary).into(holder.groupIconIv);
                        Glide.with(CommunityChatActivity.this).load(groupIcon).placeholder(R.drawable.ic_group_primary).into(groupIconIv);}
                    catch(Exception e){groupIconIv.setImageResource(R.drawable.ic_group_primary);}


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



     if (item.getItemId() == R.id.adduser){
            Intent intent=new Intent(this,Community_participantAdd_Activity.class);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
            }


        return super.onOptionsItemSelected(item);

    }
*/
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.findItem(R.id.adduser).setVisible(false);
        if (myGroupRole.equals("creator")||myGroupRole.equals("admin")){
            menu.findItem(R.id.adduser).setVisible(true);
        }else{
            menu.findItem(R.id.adduser).setVisible(false);}



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.adduser){
            Intent intent=new Intent(this,Community_participantAdd_Activity.class);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }*/
}