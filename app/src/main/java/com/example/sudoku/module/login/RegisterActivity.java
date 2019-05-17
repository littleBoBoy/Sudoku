package com.example.sudoku.module.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.base.BaseActivity;
import com.example.sudoku.base.BaseBean;
import com.example.sudoku.bean.User;
import com.example.sudoku.common.ActivityController;
import com.example.sudoku.common.Constant;
import com.example.sudoku.http.RequestCallback;
import com.example.sudoku.http.RetrofitManager;
import com.example.sudoku.http.RxSchedulers;
import com.example.sudoku.http.service.ApiService;
import com.example.sudoku.util.AppUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    private User user;
    private EditText editName;
    private EditText editPhoneNum;
    private EditText editCode;
    private EditText editPassword;
    private EditText editRePassword;

    Button getCode;
    Button register;

    CountDownTimer timer;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        editName = findViewById(R.id.et_name_register);
        editPhoneNum = findViewById(R.id.et_phone_num_register);
        editCode = findViewById(R.id.et_code_register);
        editPassword = findViewById(R.id.et_password_register);
        editRePassword = findViewById(R.id.et_re_password_register);
        //设置提示字体
        setHint(editName, Constant.HINT_SIZE);
        setHint(editPhoneNum, Constant.HINT_SIZE);
        setHint(editCode, Constant.HINT_SIZE);
        setHint(editPassword, Constant.HINT_SIZE);
        setHint(editRePassword, Constant.HINT_SIZE);

        ImageView back = findViewById(R.id.back);
        register = findViewById(R.id.btn_register);
        getCode = findViewById(R.id.get_code_register);

        back.setOnClickListener(this);
        register.setOnClickListener(this);
        getCode.setOnClickListener(this);

    }

    @Override
    public void initData() {
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
            case R.id.back:
                finish();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.get_code_register:
                getCode();
                break;
        }
    }

    private void register() {
        if (this.editName.getText().toString().length() == 0) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        } else if (!AppUtil.isChinaPhoneLegal(this.editPhoneNum.getText().toString())) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        } else if (this.editCode.getText().toString().length() != 4) {
            Toast.makeText(this, "请输入四位验证码", Toast.LENGTH_SHORT).show();
            return;
        } else if (!this.editPassword.getText().toString().equals(this.editRePassword.getText().toString())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        user = new User();
        user.setName(this.editName.getText().toString());
        user.setPassword(md5(this.editPassword.getText().toString()));
        user.setPhone(this.editPhoneNum.getText().toString());
        user.setCode(this.editCode.getText().toString());

        RetrofitManager
                .getRetrofit()
                .create(ApiService.class)
                .register(user)
                .compose(RxSchedulers.applySchedulers())
                .as(bindAutoDispose())
                .subscribe(new RequestCallback<BaseBean<String>>() {
                    @Override
                    protected void onSuccess(Object object) {
                        AppUtil.saveId((String) object);
                        Toast.makeText(RegisterActivity.this, "注册成功，正在登录", Toast.LENGTH_SHORT).show();
                        ActivityController.skipActivityAndFinish(RegisterActivity.this, MainActivity.class);
                    }

                    @Override
                    protected void onFailer(String errStr) {
                        Toast.makeText(RegisterActivity.this, errStr, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void getCode() {
        String phoneNum = editPhoneNum.getText().toString();

        if (!AppUtil.isChinaPhoneLegal(phoneNum)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", phoneNum);
        RetrofitManager
                .getRetrofit()
                .create(ApiService.class)
                .getCode(map)
                .compose(RxSchedulers.applySchedulers())
                .as(bindAutoDispose())
                .subscribe(new RequestCallback<BaseBean>() {
                    @Override
                    protected void onSuccess(Object object) {
                    }

                    @Override
                    protected void onFailer(String errStr) {
                    }
                });
        //设置获取验证码倒计时
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getCode.setEnabled(false);
                getCode.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                getCode.setEnabled(true);
                getCode.setText("重新获取");
            }
        }.start();
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
