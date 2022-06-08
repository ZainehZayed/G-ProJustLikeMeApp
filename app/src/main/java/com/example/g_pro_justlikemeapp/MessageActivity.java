package com.example.g_pro_justlikemeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.g_pro_justlikemeapp.Adapter.MessageAdapter;
import com.example.g_pro_justlikemeapp.notifications.APIService;
import com.example.g_pro_justlikemeapp.Model.Chats;
import com.example.g_pro_justlikemeapp.Model.Users;
import com.example.g_pro_justlikemeapp.notifications.Client;
import com.example.g_pro_justlikemeapp.notifications.Data;
import com.example.g_pro_justlikemeapp.notifications.Response;
import com.example.g_pro_justlikemeapp.notifications.Sender;
import com.example.g_pro_justlikemeapp.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class MessageActivity extends AppCompatActivity {


    String friendid, message, myid;
    CircleImageView imageViewOnToolbar;
    TextView usernameonToolbar;
    //Toolbar toolbar;
    FirebaseUser firebaseUser;

    EditText et_message;
    Button send;

    DatabaseReference reference;

    List<Chats> chatsList;

    ArrayList<Chats> chatsList2;


    MessageAdapter messageAdapter;
    RecyclerView recyclerView;
    ValueEventListener seenlistener;

    String Iduser;
    APIService apiService;
    boolean notify=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //toolbar = findViewById(R.id.toolbar_message);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewOnToolbar = findViewById(R.id.profile_image_toolbar_message);
        usernameonToolbar = findViewById(R.id.username_ontoolbar_message);


        recyclerView = findViewById(R.id.recyclerview_messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        send = findViewById(R.id.send_messsage_btn);
        et_message = findViewById(R.id.edit_message_text);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myid = firebaseUser.getUid(); // my id or the one who is loggedin
        apiService= Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        friendid = getIntent().getStringExtra("friendid"); // retreive the friendid when we click on the item

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent=getIntent();
        Users user=new Users();
        Iduser=intent.getStringExtra(user.getId());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(friendid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);

                usernameonToolbar.setText(users.getUsername()); // set the text of the user on textivew in toolbar

                if (users.getImageURL().equals("default")) {

                    imageViewOnToolbar.setImageResource(R.drawable.user);
                } else {

                    Glide.with(getApplicationContext()).load(users.getImageURL()).into(imageViewOnToolbar);
                }

                readMessages(myid, friendid, users.getImageURL());
               // loadGroupMessage();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        seenMessage(friendid);






        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.toString().length() > 0) {

                    send.setEnabled(true);

                } else {

                    send.setEnabled(false);


                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = et_message.getText().toString();

                if (!text.startsWith(" ")) {
                    et_message.getText().insert(0, " ");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               notify=true;
                message = et_message.getText().toString();

                sendMessage(myid, friendid, message);

                et_message.setText(" ");



            }
        });


        et_message.setText(" ");


    }



    private void seenMessage(final String friendid) {

        reference = FirebaseDatabase.getInstance().getReference("Chats");


        seenlistener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);

                    if (chats.getReciever().equals(myid) && chats.getSender().equals(friendid)) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        ds.getRef().updateChildren(hashMap);

                    }




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    private void readMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);

                    if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                            chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                        chatsList.add(chats);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, chatsList, imageURL);
                    recyclerView.setAdapter(messageAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(final String myid, final String friendid, final String message) {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();



        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myid);
        hashMap.put("reciever", friendid);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);
       final String msg=message;
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               Users user=snapshot.getValue(Users.class);
               if(notify){
               sendNotification(friendid,user.getUsername(),msg);}
               notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chatslist").child(myid).child(friendid);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (!snapshot.exists()) {


                    reference1.child("id").setValue(friendid);
                }
               /* mUID=firebaseUser.getUid();
                SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
                SharedPreferences.Editor editor= sp.edit();
                editor.putString("Current_USERID",mUID);
                editor.apply();
*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotification(String friendid, String username, String message) {
        DatabaseReference allTokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=allTokens.orderByKey().equalTo(friendid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Token token=ds.getValue(Token.class);
                    Data data=new Data(firebaseUser.getUid(),username+":"+message,"New Message",friendid,R.drawable.ic_default);
                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            if(response.code()==200){
                                if(response.body().success!=1){}
                            Toast.makeText(MessageActivity.this,""+response.message(),Toast.LENGTH_SHORT).show();}
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void Status (final String status) {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);



    }

    @Override
    protected void onResume() {
        super.onResume();
        Status("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Status("offline");
        reference.removeEventListener(seenlistener);
    }

   /* private void loadGroupMessage() {
        chatsList2=new ArrayList<>();
   DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.child(Iduser).child("Inbox").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Chats model=ds.getValue(Chats.class);
                    chatsList2.add(model);
                }
                // messageAdapter=new AdapterCommunityChat(MessageActivity.this,);
//                chatRv.setAdapter(messageAdapter);
            }*/

          /*  @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}





