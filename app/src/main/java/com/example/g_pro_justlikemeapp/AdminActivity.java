package com.example.g_pro_justlikemeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;


public class AdminActivity extends AppCompatActivity {
    ImageView imageView;

    FirebaseAuth mAuth;
    EditText grouptitel,groupDescription1;
    ProgressDialog progressDialog;

    CircleImageView gruopicon;
    public static final int STORAGE_REQUEST_CODE = 100;
    public static final int CAMERA_REQUEST_CODE = 200;
    // public static final int GALLERY_REQUEST_CODE = 100;
    public static final int IMAGE_PICK_CAMERA_CODE = 300;
    public static final int IMAGE_PICK_GALLERY_CODE = 400;

    private String[]cameraPermission;
    private  String[] storagePermission;
    private Uri image_url=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        grouptitel=findViewById(R.id.groupTitleEt);
        groupDescription1=findViewById(R.id.groupDescription);
        imageView= findViewById(R.id.imageView2);

        gruopicon=findViewById(R.id.group_image);
        cameraPermission= new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        mAuth=FirebaseAuth.getInstance();
        gruopicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showimagePike();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                StartCreatgroup();
//                        String  g_limestam=""+System.currentTimeMillis();
//        String groupTitle=""+System.currentTimeMillis();
//      String  groupDescription=""+System.currentTimeMillis();
//        String groupIcon=""+System.currentTimeMillis();
//                                     creatGroup(""+g_limestam,""+groupTitle,""+groupDescription,"");



            }
        });

    }

    private void StartCreatgroup() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("CREATE VIRTUAL COMMUNITY");
        String groupTitle=grouptitel.getText().toString().trim();
        String groupDescription=groupDescription1.getText().toString().trim();
        if(TextUtils.isEmpty(groupTitle)){
            Toast.makeText(this, "PLEASE ENTER TITLE", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        String  g_limestam=""+System.currentTimeMillis();
        //String groupTitle=""+System.currentTimeMillis();
        // String  groupDescription=""+System.currentTimeMillis();
        // String groupIcon=""+System.currentTimeMillis();

        if(image_url==null){

            creatGroup(""+g_limestam,""+groupTitle,""+groupDescription,"");
        }
        else {
            String filenamePath="Group_Imgs/"+"imge"+g_limestam;
            StorageReference storageReference= FirebaseStorage.getInstance().getReference(filenamePath);
            storageReference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> p_uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while (!p_uriTask.isSuccessful());
                    Uri P_downloadUri=p_uriTask.getResult();
                    if (p_uriTask.isSuccessful()){
                        creatGroup(""+g_limestam,""+groupTitle,""+groupDescription,""+P_downloadUri);


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void creatGroup( String  g_limestam,String groupTitle,String groupDescription ,String groupIcon) {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("groupId",""+g_limestam);
        hashMap.put("groupTitle",""+groupTitle);
        hashMap.put("groupDescription",""+groupDescription);
        hashMap.put("groupIcon",""+groupIcon);
        hashMap.put("Timestamp",""+g_limestam);
        hashMap.put("creatBY",""+mAuth.getUid());
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("V_Comm");
        ref.child(g_limestam).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                HashMap<String,String> hashMap2=new HashMap<>();
                hashMap2.put("id",mAuth.getUid());
                hashMap2.put("role","creator");
                hashMap2.put("Timestamp",""+g_limestam);
                DatabaseReference ref2= FirebaseDatabase.getInstance().getReference("V_Comm");
                ref2.child(g_limestam).child("participant").child(mAuth.getUid()).setValue(hashMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminActivity.this, "Virtual Community Created created.....", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AdminActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void showimagePike() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image :");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {


                if (i==0) {

                    //CameraClick();
                    if(!checkCmeraePermissions()){
                        requstCameraPermissions();
                    }else {pickfromCamera();}

                }

                else {
                    if(!checkstorgePermissions()){
                        regustStoragePermissions();
                    }else {pickfromGallary();}

                    //GalleryClick();
                }
            }
        }).show();
    }
    private  void pickfromGallary(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }
    private  void pickfromCamera(){
        ContentValues cv=new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Group Image Icon Title");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Group Image Icon DESCRIPTION");
        image_url=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_url);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);

    }
    private boolean checkstorgePermissions(){
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private  void regustStoragePermissions(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }
    private boolean checkCmeraePermissions(){
        boolean result=ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return  result && result1;
    }
    private  void requstCameraPermissions(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
    }

    //
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {

            mAuth.signOut();
            finish();
            return  true;
        }
        // else if (item.getItemId()==R.id.Community)
        // {         /*AdminActivity. viewPagerAdapter = new AdminActivity().ViewPagerAdapter(getSupportFragmentManager());

        // viewPagerAdapter.addFragment(new CommunityFragment(), "Community");*/

        // }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storgeAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storgeAccept) {
                        pickfromCamera();

                    } else {
                        Toast.makeText(this, "Camera & Storge permission are requied", Toast.LENGTH_SHORT).show();
                    }


                }

            }
            break;
            case STORAGE_REQUEST_CODE :{
                if (grantResults.length>0){
                    boolean storgeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storgeAccepted){
                        pickfromGallary();
                    }else {
                        Toast.makeText(this, "Storge permission are requied", Toast.LENGTH_SHORT).show();
                    }


                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==RESULT_OK){
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                image_url=data.getData();
                gruopicon.setImageURI(image_url);
            }
            else if(requestCode==IMAGE_PICK_CAMERA_CODE){
                gruopicon.setImageURI(image_url);
            }



        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


//View.OnClickListener(new)
