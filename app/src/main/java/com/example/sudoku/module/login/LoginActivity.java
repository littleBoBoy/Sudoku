package com.example.sudoku.module.login;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.base.BaseActivity;
import com.example.sudoku.base.BaseBean;
import com.example.sudoku.common.ActivityController;
import com.example.sudoku.http.RequestCallback;
import com.example.sudoku.http.RetrofitManager;
import com.example.sudoku.http.RxSchedulers;
import com.example.sudoku.http.service.ApiService;
import com.example.sudoku.util.AppUtil;

import java.util.HashMap;
import java.util.Map;

import static com.example.sudoku.common.Constant.HINT_SIZE;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    EditText editTextAccount;
    EditText editTextPassword;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //设置edittext的hint的大小
        editTextAccount = findViewById(R.id.et_account_login);
        editTextPassword = findViewById(R.id.et_password_login);

        setHint(editTextAccount, HINT_SIZE);
        setHint(editTextPassword, HINT_SIZE);

        Button btnRegister = findViewById(R.id.btn_go_register);
        Button btnLogin = findViewById(R.id.btn_login);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void initData() {
        String id = AppUtil.readId();
        if (id.length() == 18)
            ActivityController.skipActivityAndFinish(LoginActivity.this, MainActivity.class);

    }

    private void setHint(EditText editText, int size) {
        SpannableString accountSs = new SpannableString(editText.getHint().toString());
        AbsoluteSizeSpan accountAss = new AbsoluteSizeSpan(size, true);
        accountSs.setSpan(accountAss, 0, accountSs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannableString(accountSs));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_register:
                ActivityController.skipActivity(this, RegisterActivity.class);
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
        String userName = editTextAccount.getText().toString();
        String password = AppUtil.md5(editTextPassword.getText().toString());

        if (userName.length() != 11) {
            Toast.makeText(this, "请正确填写手机号", Toast.LENGTH_SHORT).show();
        }
        Map<String, String> map = new HashMap<>();
        map.put("phone", userName);
        map.put("password", password);

        RetrofitManager
                .getRetrofit()
                .create(ApiService.class)
                .login(map)
                .compose(RxSchedulers.applySchedulers())
                .as(bindAutoDispose())
                .subscribe(new RequestCallback<BaseBean<String>>() {
                    @Override
                    protected void onSuccess(Object object) {
                        AppUtil.saveId((String) object);
                        AppUtil.ToastShort("登录成功");
                        ActivityController.skipActivityAndFinish(LoginActivity.this, MainActivity.class);
                    }

                    @Override
                    protected void onFailer(String errStr) {
                    }
                });
    }
}


