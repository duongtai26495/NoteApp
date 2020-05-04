package com.kaiapp.simplenote;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends Activity {
    EditText edtUsername,edtPassword;
    Button btnDangNhap, btnDangNhapBangFB;
    TextView tvDangKy;
    Switch swSave;
    SharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        AnhXa();
        sharedPreferences = getSharedPreferences("dalaLogin",MODE_PRIVATE);
        edtUsername.setText(sharedPreferences.getString("userName",""));
        edtPassword.setText(sharedPreferences.getString("passWord",""));
        swSave.setChecked(sharedPreferences.getBoolean("active",false));
        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangNhap();
            }
        });

    }
    private void DangNhap(){
        final String userName = edtUsername.getText().toString();
        final String passWord = edtPassword.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = firebaseDatabase.getReference("USERS").child(userName);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    if(dataSnapshot.getValue()==null){
                        Toast.makeText(SignIn.this,"Tài khoản không tồn tại",Toast.LENGTH_SHORT).show();
                    }else{
                        User user = dataSnapshot.getValue(User.class);
                        if (user.passWord.equals(passWord)){
                            Toast.makeText(SignIn.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            if(swSave.isChecked()){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userName",userName);
                                editor.putString("passWord",passWord);
                                editor.putBoolean("active",true);
                                editor.commit();
                                startActivity(new Intent(SignIn.this,Home.class));
                                finish();
                            }else{
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("userName");
                                editor.remove("passWord");
                                editor.remove("active");
                                editor.commit();
                            }
                            Intent intent = new Intent(SignIn.this,Home.class);
                            intent.putExtra("1",user.name);
                            startActivity(intent);
                            finish();
                        }
                    }

                }catch (Exception e){
                    Toast.makeText(SignIn.this,"Đã có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void AnhXa(){
        edtUsername = (EditText) findViewById(R.id.edtUsernameLog);
        edtPassword = (EditText) findViewById(R.id.edtPasswordLog);
        btnDangNhap = (Button) findViewById(R.id.btnLogin);
        btnDangNhapBangFB = (Button) findViewById(R.id.btnLoginByFb);
        tvDangKy = (TextView) findViewById(R.id.tvDangKy);
        swSave = (Switch) findViewById(R.id.swSave);
    }
}
