package com.kaiapp.simplenote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends Activity {
    EditText edtFullName,edtPhone,edtUsernameReg,edtPassword,edtRepassword;
    Button btnDangKy,btnHuyBo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        AnhXa();
        btnHuyBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, SignIn.class));
                finish();
            }
        });
    btnDangKy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SigUp();
        }
    });
    }
    private void SigUp(){
        final String fullName = edtFullName.getText().toString().trim();
        final String userName = edtUsernameReg.getText().toString().trim();
        final String phoneNum = edtPhone.getText().toString().trim();
        final String passWord = edtPassword.getText().toString().trim();
        final String rePassWord = edtRepassword.getText().toString().trim();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dataRef = database.getReference("USERS").child(userName);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){
                    User user = new User();
                    user.name = fullName;
                    user.phone = phoneNum;
                    user.userName = userName;
                    user.passWord = passWord;
                    if(passWord.equals(rePassWord)==false){
                        Toast.makeText(SignUp.this, "Hãy nhập mật khẩu giống nhau ở 2 ô", Toast.LENGTH_SHORT).show();
                    }else{
                        dataRef.setValue(user, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                Toast.makeText(SignUp.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, SignIn.class);
                                intent.putExtra("1",edtFullName.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void AnhXa(){
        edtFullName = (EditText) findViewById(R.id.edtFullName);
        edtPhone = (EditText) findViewById(R.id.editPhoneNum);
        edtUsernameReg = (EditText) findViewById(R.id.edtUsernameReg);
        edtPassword = (EditText) findViewById(R.id.edtPasswordReg);
        edtRepassword = (EditText) findViewById(R.id.edtRePasswordReg);
        btnHuyBo = (Button) findViewById(R.id.btnHuy);
        btnDangKy = (Button) findViewById(R.id.btnXacNhanDangKy);
     }
}
