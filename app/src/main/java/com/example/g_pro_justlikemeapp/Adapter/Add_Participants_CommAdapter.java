package com.example.g_pro_justlikemeapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.g_pro_justlikemeapp.Model.Users;
import com.example.g_pro_justlikemeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Add_Participants_CommAdapter extends RecyclerView.Adapter<Add_Participants_CommAdapter.HolderParticipantAdd> {

    private Context context;
    private ArrayList<Users> userslist;
    private   String  groupId,mygrouprole;

    public Add_Participants_CommAdapter(Context context, ArrayList<Users> userslist, String groupId, String mygrouprole) {
        this.context = context;
        this.userslist = userslist;
        this.groupId = groupId;
        this.mygrouprole = mygrouprole;
    }

    @NonNull
    @Override
    public HolderParticipantAdd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_participent_list,parent,false);
        return new HolderParticipantAdd(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderParticipantAdd holder, int position) {
        Users users =userslist.get(position);
//get data
        String name=users.getUsername();
        String email=users.getEmail();
        String Image=users.getImageURL();
        final String uid=users.getId();
        //set data
        holder.nametv.setText(name);
        holder.emailtv.setText(email);
        try {
            Glide.with(context).load(Image).placeholder(R.drawable.ic_default).into(holder.avataeTv);
            //Picasso.with(context).load(Image).placeholder(R.drawable.ic_default).into(holder.avataeTv);
        }catch (Exception e){
            holder.avataeTv.setImageResource(R.drawable.ic_default);

        }

        chrckIAlreadyExists(users,holder);

        //handle clicke
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("V_Comm");
                ref.child(groupId).child("participant").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            //user exists
                            String hispreviousRole=""+snapshot.child("role").getValue();
                            String[] options;
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setTitle("choose Option");
                            if(mygrouprole.equals("creator")){
                                if (hispreviousRole.equals("admin")){
                                    options=new String[]{"Remove admin","Remove user"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(i==0){
                                                //remova admin
                                                Removaadmin(users);
                                            }else {
                                                //remova user
                                                Rmoveparticipants(users);
                                            }

                                        }
                                    }).show();



                                }
                                else  if (hispreviousRole.equals("participant")){
                                    options=new String[]{"Make admin","Remove user"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(i==0){
                                                //remova admin
                                                Makeadmain(users);
                                            }else {
                                                //remova user
                                                Rmoveparticipants(users);
                                            }

                                        }
                                    }).show();





                                }
                            }
                            else  if(mygrouprole.equals("admin")){
                                //im admin, he creat
                                // Toast.makeText(context, "Creat  group ...", Toast.LENGTH_SHORT).show();
                                if (hispreviousRole.equals("creator")){
                                    Toast.makeText(context, "Creat  group ...", Toast.LENGTH_SHORT).show();
                                } else if (hispreviousRole.equals("admin")){
                                    options=new String[]{"Remove Admin ","Remove User "};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i==0){
                                                Removaadmin(users);

                                            }
                                            else { Rmoveparticipants(users);}
                                        }
                                    }).show();


                                }
                                else if (hispreviousRole.equals("participant")){
                                    options=new String[]{"Make Admin ","Remove User "};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i==0){
                                                Makeadmain(users);

                                            }
                                            else { Rmoveparticipants(users);}
                                        }
                                    }).show();


                                }

                            }

                        }
                        else {
                            AlertDialog .Builder builder=new AlertDialog.Builder(context);
                            builder.setTitle("Add participant")
                                    .setMessage("Add this user in this group ?")
                                    .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            addparticipant(users);
                                        }
                                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });



    }

    private void Makeadmain(Users users) {
        String Timestamp=""+System.currentTimeMillis();
        HashMap <String,Object>hashMap=new HashMap<>();
        hashMap.put("role","admin");
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("V_Comm");
        reference.child(groupId).child("participant").child(users.getId()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "is User Admain", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void Removaadmin(Users users) {

        HashMap <String,Object>hashMap=new HashMap<>();
        hashMap.put("role","admin");
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("V_Comm");
        reference.child(groupId).child("participant").child(users.getId()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "the user longer admin", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void Rmoveparticipants(Users users) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("V_Comm");
        reference.child(groupId).child("participant").child(users.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Toast.makeText(context, "the user longer admin", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void addparticipant(Users users) {
        String Timestamp=""+System.currentTimeMillis();
        HashMap <String,String>hashMap=new HashMap<>();
        hashMap.put("id",users.getId());
        hashMap.put("role","participant");
        hashMap.put("Timestamp",Timestamp);
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("V_Comm");
        ref.child(groupId).child("participant").child(users.getId()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Added succefully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void chrckIAlreadyExists(Users users,final HolderParticipantAdd holder) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("V_Comm");
        ref.child(groupId).child("participant").child(users.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot .exists()){
                    //his already exists
                    String hisRole=""+snapshot.child("role").getValue();
                    holder.statusTv.setText(hisRole);
                }else {
                    //dosnt already exists
                    holder.statusTv.setText("");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userslist.size() ;
    }

    class HolderParticipantAdd extends RecyclerView.ViewHolder{
        private ImageView avataeTv;
        private TextView nametv,emailtv,statusTv;

        public HolderParticipantAdd(@NonNull View itemView) {
            super(itemView);
            avataeTv=itemView.findViewById(R.id.groupIconIv);
            nametv=itemView.findViewById(R.id.nameTv);
            emailtv=itemView.findViewById(R.id.emailtv);
            statusTv=itemView.findViewById(R.id.statustv);

        }
    }
}