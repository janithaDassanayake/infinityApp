package com.example.infinityapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userEditProfile extends AppCompatActivity {

    private EditText changeFname,changeLname,changePhone;

   private EditText displayEmail;

    private Button changePassword,update,deleteAcc;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;

    private FirebaseUser firebaseUser;

    private DatabaseReference mPostReference;
    String mydata="";
    String data[ ];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);

        Log.d("mydata","mama malidi");
        changeFname=findViewById(R.id.etchangefname);
        changeLname=findViewById(R.id.etchangelname);
        changePhone=findViewById(R.id.etchangephone);
        displayEmail=findViewById(R.id.tvemaildis);

        changePassword=findViewById(R.id.btnchangePassword);
        update=findViewById(R.id.btnupdateuser);
        deleteAcc=findViewById(R.id.btndeleteuser);



      firebaseAuth=FirebaseAuth.getInstance();



        firebaseDatabase= FirebaseDatabase.getInstance();

        firebaseUser=firebaseAuth.getCurrentUser();


      //  final DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());

       // databaseReference.addValueEventListener(new ValueEventListener() {



        try {


DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
DatabaseReference databaseReference1=databaseReference.child("users");

//String id="0xjtiCfdavPTCkUwJqTDCPypAv72";
            String id=firebaseAuth.getUid();



databaseReference1.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               // User user=dataSnapshot.getValue(User.class);

                for (DataSnapshot user :dataSnapshot.getChildren() ){

                    mydata+=user.getValue().toString()+"/";

                    Log.d("mydata",user.getValue().toString());
                }
                data=mydata.split("/");

                changeFname.setText(data[0]);
                changeLname.setText(data[1]);
                displayEmail.setText(data[2]);
                changePhone.setText(data[3]);

/*
                    //
                    //  displayEmail.setText(user.getEmail());

*/


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(userEditProfile.this,databaseError.getCode(),Toast.LENGTH_SHORT);

            }
        });

        }catch (Exception e){
            Log.d("mydata","firebase error"+e);
        }


/*
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                DatabaseReference databaseReference1=databaseReference.child("users");

              String firstName=changeFname.getText().toString();
                String lastName=changeLname.getText().toString();
                String Email=displayEmail.getText().toString();
                String Phone=changePhone.getText().toString();

                User user=new User(firstName,lastName,Email,Phone);

                databaseReference1.setValue(user);

                finish();
            }
        });*/


    }












    @Override
    protected void onResume() {
        super.onResume();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(userEditProfile.this,mforgotpassword.class);

                startActivity(i);
            }
        });

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog=new AlertDialog.Builder(userEditProfile.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your account from the system");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(userEditProfile.this,"Account Deleted successfully",Toast.LENGTH_LONG).show();

                                Intent i=new Intent(userEditProfile.this,Admin_logn.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }else{

                                Toast.makeText(userEditProfile.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog=dialog.create();
                alertDialog.show();
            }
        });
    }
}
