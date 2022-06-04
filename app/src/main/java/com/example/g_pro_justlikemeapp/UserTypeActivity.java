package com.example.g_pro_justlikemeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class UserTypeActivity extends AppCompatActivity {

    Button user,org;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        user=findViewById(R.id.user);
        org=findViewById(R.id.org);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Useruser();
                startActivity(new Intent(UserTypeActivity.this,
                        RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ));

            }
        });
        org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Userorg();
                startActivity(new Intent(UserTypeActivity.this,
                        orgActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ));

            }
        });

    }

   /* private void Useruser() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("UserType", "user");
        reference.updateChildren(hashMap);
        Toast.makeText(UserTypeActivity.this, "HI SIR .....WELCOM TO COMMUNITY", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UserTypeActivity.this,
                MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ));


    }

    private void Userorg() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("UserType", "org");
        reference.updateChildren(hashMap);
        Toast.makeText(UserTypeActivity.this, "HI SIR .....WELCOM TO COMMUNITY", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UserTypeActivity.this,
                MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ));


    }*/
}