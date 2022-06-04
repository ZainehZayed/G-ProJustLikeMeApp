package com.example.g_pro_justlikemeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.g_pro_justlikemeapp.CommunityChatActivity;
import com.example.g_pro_justlikemeapp.Model.CommunityChat;
import com.example.g_pro_justlikemeapp.R;


import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.g_pro_justlikemeapp.CommunityChatActivity;
import com.example.g_pro_justlikemeapp.Model.CommunityChat;
import com.example.g_pro_justlikemeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CommunityChatAdapter extends RecyclerView.Adapter<CommunityChatAdapter.HolderCommunityChat> {
    private Context context;
    private ArrayList<CommunityChat> CommunityChatList;

    public CommunityChatAdapter(Context context,ArrayList<CommunityChat> CommunityChatList) {
        this.context = context;
        this.CommunityChatList=CommunityChatList;
    }

    @NonNull
    @Override
    public HolderCommunityChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_groupchats_list,parent,false);
        return new HolderCommunityChat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCommunityChat holder, int position) {
        CommunityChat model= CommunityChatList.get(position);
        String groupId=model.getGroupId();
        String groupIcon=model.getGroupIcon();
        String groupTitle=model.getGroupTitle();
        holder.nameTv.setText("")
        ;
        holder.timeTv.setText("");
        holder.messageTv.setText("");



        loadlastMessage(model,holder);
        holder.groupTitleTv.setText(groupTitle);

        try{
            //Glide.with(getItemViewType()).load(groupIcon).placeholder(R.drawable.ic_group_primary));
            //Glide.with(getApplicationContext()).load(users.getImageURL()).into(imageView);
            Glide.with(context).load(groupIcon).placeholder(R.drawable.ic_group_primary).into(holder.groupIconIv);}
        catch(Exception e){
            holder.groupIconIv.setImageResource(R.drawable.ic_group_primary);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //later
                Intent intent=new Intent(context, CommunityChatActivity.class);
                intent.putExtra("groupId",groupId);
                context.startActivity(intent);
            }
        });
    }

    private void loadlastMessage(CommunityChat model, HolderCommunityChat holder) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("V_Comm");
        ref.child(model.getGroupId()).child("Messages").limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren()
                        ) {

                            String message =""+ds.child("message").getValue();
                            String timestamp =""+ds.child("Timestamp").getValue();
                            String sendr =""+ds.child("sender").getValue();
                            Calendar cal =Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(Long.parseLong(timestamp));
                            String datatime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();
                            holder.messageTv.setText(message);
                            holder.timeTv.setText(datatime);
                            DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Users");
                            ref.orderByChild("id").equalTo(sendr)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds: snapshot.getChildren()){
                                                String name = ""+ds.child("username").getValue();
                                                holder.nameTv.setText(name);
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
    public int getItemCount() {
        return CommunityChatList.size();
    }

    class HolderCommunityChat extends RecyclerView.ViewHolder {
        private ImageView groupIconIv;
        private TextView groupTitleTv,nameTv,messageTv,timeTv;
        public HolderCommunityChat(@NonNull View itemView) {
            super(itemView);
            groupIconIv=itemView.findViewById(R.id.groupIconIv);
            groupTitleTv=itemView.findViewById(R.id.groupTitleTv);
            nameTv=itemView.findViewById(R.id.nameTv);
            messageTv=itemView.findViewById(R.id.messageTv);
            timeTv=itemView.findViewById(R.id.timeTv);

        }
    }
}
