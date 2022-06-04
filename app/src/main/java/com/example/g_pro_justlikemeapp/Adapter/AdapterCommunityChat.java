package com.example.g_pro_justlikemeapp.Adapter;

import android.content.Context;
//import android.icu.util.Calendar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g_pro_justlikemeapp.Model.ModelCommunityChat;
import com.example.g_pro_justlikemeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//import java.text.Format;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Calendar;
import java.util.Locale;
import android.text.format.DateFormat;

public class AdapterCommunityChat extends RecyclerView.Adapter<AdapterCommunityChat.HolderCommunityChat> {
    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    private Context context;
    private ArrayList<ModelCommunityChat> modelCommunityChats;
    private FirebaseAuth firebaseAuth;

    public AdapterCommunityChat(Context context, ArrayList<ModelCommunityChat> modelCommunityChats) {
        this.context = context;
        this.modelCommunityChats = modelCommunityChats;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderCommunityChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT)
        {View view= LayoutInflater.from(context).inflate(R.layout.row_groupchat_right,parent,false);
            return new HolderCommunityChat(view);

        }
        else{View view= LayoutInflater.from(context).inflate(R.layout.row_groupchat_left,parent,false);
            return new HolderCommunityChat(view);}
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCommunityChat holder, int position) {
        ModelCommunityChat model=modelCommunityChats.get(position);
        String Timestamp=model.getTimestamp();


        String message=model.getMessage();
        String senderUid=model.getSender();

     /*  Calendar cal =Calendar.getInstance(Locale.ENGLISH);
       cal.setTimeInMillis(Long.parseLong(Timestamp));
        */
        //Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        //cal.setTimeInMillis(Long.parseLong(Timestamp));
        //هاي ما زبطت زي الفيديو
        //String dateTime=DateFormat.getDateTimeInstance().format("dd/MM/yyyy").toString();
        //String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();


        holder.messageTv.setText(message);
        //holder.timeTv.setText(datetime);
        setUserName(model,holder);

    }

    private void setUserName(ModelCommunityChat model, HolderCommunityChat holder) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("id").equalTo(model.getSender()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {String name=""+ds.child("username").getValue();
                    holder.nameTv.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelCommunityChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(modelCommunityChats.get(position).getSender().equals(firebaseAuth.getUid()))
        {return MSG_TYPE_RIGHT;}
        else {return MSG_TYPE_LEFT;}
    }

    class HolderCommunityChat extends RecyclerView.ViewHolder{
        private TextView nameTv,messageTv,timeTv;

        public HolderCommunityChat(@NonNull View itemView) {
            super(itemView);
            nameTv=itemView.findViewById(R.id.nameTv);
            messageTv=itemView.findViewById(R.id.messageTv);
            timeTv=itemView.findViewById(R.id.timeTv);


        }
    }
}
