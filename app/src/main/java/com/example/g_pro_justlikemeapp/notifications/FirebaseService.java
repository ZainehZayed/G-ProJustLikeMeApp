package com.example.g_pro_justlikemeapp.notifications;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;

//هون في اشياء قديمه استخدمت اشي جديد
public class FirebaseService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null)
        {updateToken(token);}
    }

    private void updateToken(String token) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
        Token Refreshtoken=new Token(token);
        ref.child(user.getUid()).setValue(Refreshtoken);
    }
}
