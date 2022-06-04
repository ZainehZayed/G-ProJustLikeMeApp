package com.example.g_pro_justlikemeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;
//import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
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

public class LogInActivity extends AppCompatActivity {
 /*ListView userListView;
 ArrayAdapter arrayAdapter;
 ArrayList<String> user=new ArrayList<>();
 FirebaseAuth mAuth;
 DatabaseReference databaseReference;
 Button singOut;
 //Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //toolbar=findViewById(R.id.);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Register");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        userListView=findViewById(R.id.userListView);
        singOut=findViewById(R.id.signOut);
        databaseReference.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists())
               {for(DataSnapshot dataSnapshot : snapshot.getChildren())
               {if(!dataSnapshot.child("Email").getValue().toString().equals(mAuth.getCurrentUser().getEmail()))
               {
                   user.add(dataSnapshot.child("Email").getValue().toString());
               }
               }
                arrayAdapter=new ArrayAdapter(LogInActivity.this, android.R.layout.simple_list_item_1,user);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogInActivity.this, "Failed to Load Users", Toast.LENGTH_SHORT).show();
            }
        });
        singOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent=new Intent(LogInActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(LogInActivity.this, RegisterActivity.class);
                intent.putExtra("Email",user.get(position));
                startActivity(intent);
            }
        });
    }*/
 TextInputEditText et_username, et_password, et_email;
    Button loginBtn;
    //Toolbar toolbar;
    String email, password;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    AuthResult authResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_email = findViewById(R.id.log_email);
        et_password = findViewById(R.id.log_pass2);
        loginBtn = findViewById(R.id.login_bet);
        mAuth = FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et_email.getText().toString();
                password = et_password.getText().toString();


                if (TextUtils.isEmpty(email)) {

                    et_email.setError("Required");

                } else if (TextUtils.isEmpty(password)) {

                    et_password.setError("Required");
                } else {
                    LoginMeIn(email, password);
                }



            }
        });








    }

    private void LoginMeIn(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {



                if (task.isSuccessful()) {

                    //CheckAccessUserLevel(mAuth.getUid());

                   CheckAccessUserLevel();


                    /*Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    // Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                   /* Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LogInActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();*/

                }

            }
        });



    }

    private void CheckAccessUserLevel() {



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String RegisteredUserID = currentUser.getUid();
        DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
        jLoginDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ISadmin = dataSnapshot.child("isAdmin").getValue().toString();
                if(ISadmin.equals("false")){
                    Intent intentResident = new Intent(LogInActivity.this, MainActivity.class);
                    intentResident.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentResident);
                    finish();
                }else if(ISadmin.equals("true")){
                    Intent intentMain = new Intent(LogInActivity.this, AdminActivity.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMain);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }}


   /* private void CheckAccessUserLevel() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //   Log.d("TAG","onSuccess: ",dataSnapshot.getValue(Users.));
                if (dataSnapshot.child("isAdmin").getValue().toString().equals("false")) {
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                   // finish();
                    Toast.makeText(LogInActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LogInActivity.this, AdminActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                   // finish();
                    Toast.makeText(LogInActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                }
                // if (dataSnapshot.getString("isAdmin")
            }
        });
    }}*/
        /*reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
    @Override
    public void onSuccess(DataSnapshot dataSnapshot) {
        mAuth=FirebaseAuth.getInstance();*/

       /*// Log.d("TAG","onSuccess: ",dataSnapshot.child().;
        if()
        {

            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }
        else{   Intent intent = new Intent(LogInActivity.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        }*/

//});


/* mAuth = FirebaseAuth.getInstance();
         mFirebaseDatabase = FirebaseDatabase.getInstance();
final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        myRef = mFirebaseDatabase.getReference();

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        query = myRef.orderByChild("medical").equalTo("Diabetes");

        }

private void submit(){
        query.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){

        for (DataSnapshot issue : dataSnapshot.getChildren()){
        UserInformation uInfo = issue.getValue(UserInformation.class);
        if (uInfo.getMedical().equals("Diabetes")){
        startActivity(new Intent(getApplicationContext(),Medicine.class));

        }else{
        myRef.child("Medicines").child("Pain and Fever").child(userID).setValue("Acetaminophen");
        startActivity(new Intent(getApplicationContext(),Medicine.class));
        }
        }
        }
        }

@Override
public void onCancelled(DatabaseError databaseError) {

        }
        });

        }


@Override
public void onClick(View view) {
        if (view == btnSubmit){
        submit();
        }
        if (view == btnCancel){
        startActivity(new Intent(this,Medicine.class));
        }
        }*/




