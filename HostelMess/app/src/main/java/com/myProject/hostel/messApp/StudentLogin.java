package com.myProject.hostel.messApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentLogin extends AppCompatActivity {
    public Button loginStudent,signUpStudent;
    public EditText emailStudent,passwordStudent;
   private  FirebaseAuth firebaseAuth;
    public  FirebaseUser firebaseUser;
    private ProgressDialog progressDi`1 alog;
    String name;
   String stat;
    String testString;
    Boolean emailflag,flag;
    int y;
    Student student;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        loginStudent = (Button)findViewById(R.id.loginStudent);
        signUpStudent = (Button)findViewById(R.id.signUpStudent);
        emailStudent = (EditText)findViewById(R.id.emailStudent);
        passwordStudent = (EditText)findViewById(R.id.passwordStudent);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(StudentLogin.this);
        loginStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate1()){
                    validate(emailStudent.getText().toString(),passwordStudent.getText().toString());
                }
            }
        });
        signUpStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(StudentLogin.this,StudentSignUp.class);
                startActivity(intent);
            }
        });
    }




    public Boolean validate1(){
        Boolean result = false;
        String name = emailStudent.getText().toString();
        String passWord = passwordStudent.getText().toString();
        // String userEmail = user_name.getText().toString();
        if(name.isEmpty()){
            Toast.makeText(this,"Please Enter Email Id",Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(name).matches()){
            Toast.makeText(this,"Please Enter Valid Email Id",Toast.LENGTH_SHORT).show();
        }else if(passWord.isEmpty()){
            Toast.makeText(this,"Please Enter Student Password",Toast.LENGTH_SHORT).show();
        }else if(passWord.length()<6){
            Toast.makeText(this,"Password length must be greater than 6 character",Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return result;
    }
    private void validate(String userEmail,String userPassword){

        progressDialog.setMessage("Checking your credintials!!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //Toast.makeText(MainActivity.this, "Login Successful   !!", Toast.LENGTH_SHORT).show();
                    // Intent intent = new Intent(MainActivity.this,Second.class);
                    //   startActivity(intent);
                    checkEmailVerification();


                }
                else{

                    Toast.makeText(StudentLogin.this, "Please Enter correct credentials !!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

    }


    private void checkEmailVerification(){
        Log.d("res", "in check email verify");
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        emailflag = firebaseUser.isEmailVerified();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);
//                if(student.getStatus().compareTo("confirm")==0&&emailflag)
//                {

                    Toast.makeText(StudentLogin.this, "Login successfull!!", Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();
                    emailStudent.setText("");
                    passwordStudent.setText("");
                    Intent intent = new Intent(StudentLogin.this, StudentHome.class);
                    String mealId = student.getMealId();
                    intent.putExtra("mealId",mealId);
                    intent.putExtra("user_name",student.getName());
                    startActivity(intent);
                    finish();
//                }
//                else{
//                    Toast.makeText(StudentLogin.this,"Please get you email verified!!",Toast.LENGTH_SHORT).show();
//                    emailStudent.setText("");
//                    passwordStudent.setText("");
//                    firebaseAuth.signOut();
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(StudentLogin.this, " data base error "+ databaseError , Toast.LENGTH_SHORT).show();
            }
        });
        //Toast.makeText(this,name,Toast.LENGTH_SHORT).show();

        y=0;
        progressDialog.dismiss();





    }



}
