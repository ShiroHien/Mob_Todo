package com.example.mobiletodoapp.phuc_activity.view.Login;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.saveSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.validateEmpty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.User;
import com.example.mobiletodoapp.phuc_activity.view.Logup.LogupActivity;
import com.example.mobiletodoapp.phuc_activity.dto.Login;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.UserApi;
import com.example.mobiletodoapp.thuyen_services.main_screen.MainScreenActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView logupDirectText;
    Button loginButton;
    EditText loginEmail, loginPassword;
    private boolean isPasswordVisible = false;
    private ProgressDialog progressDialog;
    private GoogleSignInClient googleSignInClient;
    private ShapeableImageView googleSignInButton;
    private RetrofitService retrofitService;
    private UserApi userApi;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
        checkLoginWithCondition();

        //Tạo retrofit
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        logupDirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LogupActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmpty(loginEmail.getText().toString(), "Email không được để trống", LoginActivity.this) || !validateEmpty(loginPassword.getText().toString(), "Mật khẩu không được để trống", LoginActivity.this)) {
                } else {
                    showLoading();
                    loginUser(userApi);
                }
            }
        });
        loginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2; // Index of the drawable right of the EditText

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (loginPassword.getRight() - loginPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Toggle password visibility
                        if (isPasswordVisible) {
                            // Password is currently visible, hide it
                            loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            // Password is currently hidden, show it
                            loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                        isPasswordVisible = !isPasswordVisible; // Toggle the visibility flag
                        return true;
                    }
                }
                return false;
            }
        });
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }

    private void initialize() {
        logupDirectText = findViewById(R.id.logupDirectText);
        loginButton = findViewById(R.id.loginButton);
        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, options);
    }

    private CompletableFuture<Void> loginUser(UserApi userApi) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Login login = new Login(loginEmail.getText().toString(), loginPassword.getText().toString());
        userApi.loginUser(login).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    processLoginResponse(response);
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoading();
                showToast(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại đường truyền của bạn.");
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Error: ", t);
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    private void processLoginResponse(Response<User> response) {
        if (response.isSuccessful()) {
            User loginResult = response.body();
            if (loginResult != null) {
                saveSharedPref(LoginActivity.this, "userId", loginResult.getId());
                saveSharedPref(LoginActivity.this, "email", loginResult.getEmail());
                saveSharedPref(LoginActivity.this, "name", loginResult.getName());
                saveSharedPref(LoginActivity.this, "username", loginResult.getUsername());
                showToast(LoginActivity.this, "Đăng nhập thành công");
                hideLoading();
                Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            } else {
                showToast(LoginActivity.this, "Sai email hoặc mật khẩu");
            }
        } else {
            showToast(LoginActivity.this, "Lỗi server");
        }
    }

    private void checkLoginWithCondition() {
        String savedEmail = getSharedPref(LoginActivity.this, "email", "");
        String userId = getSharedPref(LoginActivity.this, "userId", "");
        if (!savedEmail.isEmpty() && !userId.isEmpty()) {
            showToast(LoginActivity.this, "Tự động đăng nhập thành công");
            Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                signInDatabase(userApi, account);
            } else {
                showToast(LoginActivity.this, "Đăng nhập không thành công với Google");
            }
        } catch (ApiException e) {
            Log.e("error", "messgae: ", e);
            showToast(LoginActivity.this, "Đăng nhập không thành công với Google: " + e.getStatusCode());
        }
    }

    private CompletableFuture<Void> signInDatabase(UserApi userApi, GoogleSignInAccount account) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        User user = new User(account.getDisplayName(), account.getGivenName(), account.getEmail(), "svVNU123");
        userApi.saveUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    processSignInWithGoogleResponse(response);
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("error", "loi gi do", t);
                showToast(LoginActivity.this, "Có lỗi xảy ra");
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    private void processSignInWithGoogleResponse(Response<User> response) {
        if (response.isSuccessful()) {
            User logupResult = response.body();
            if (logupResult != null) {
                saveSharedPref(LoginActivity.this, "userId", logupResult.getId());
                saveSharedPref(LoginActivity.this, "email", logupResult.getEmail());
                saveSharedPref(LoginActivity.this, "name", logupResult.getName());
                saveSharedPref(LoginActivity.this, "username", logupResult.getUsername());
                showToast(LoginActivity.this, "Đăng nhập thành công với Google");
                Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            } else {
                showToast(LoginActivity.this, "Đăng nhập với tài khoản Google không thành công");
            }
        } else {
            showToast(LoginActivity.this, "Lỗi server");
        }
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}