package fi.jamk.hunteam.explorejyvaskylaandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import fi.jamk.hunteam.explorejyvaskylaandroid.serverconnection.Login;

import static android.R.attr.id;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        Login.LoginCallBack{

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isUserLoggedIn();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("155033622944-qvat1jcvr6o5u709g8n50ecr889osrl7.apps.googleusercontent.com")
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                TextView status = (TextView) findViewById(R.id.sign_in_status);
                if (!status.getText().equals(getResources().getString(R.string.login_wait_for_server)))
                    signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        System.out.println("____ handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String idToken = acct.getIdToken();
            // Send id token to server
            new Login(this).execute(idToken);
            TextView status = (TextView) findViewById(R.id.sign_in_status);
            status.setText("Waiting for the server...");
        } else {
            // Signed out, show unauthenticated UI.
            TextView status = (TextView) findViewById(R.id.sign_in_status);
            status.setText("Something happened during the process.\nPlease try again.");
        }
    }

    public void isUserLoggedIn(){
        // Restore preferences
        String id = new ManageSharedPreferences.Manager(this).getId();
        if (!id.equals("")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRemoteCallComplete(String id, String name, String picture) {
        System.out.println("____ Received id: " + id);
        if (id != null){
            TextView status = (TextView) findViewById(R.id.sign_in_status);
            status.setText("Successful login.\nStart the application...");
            // Save login data and start MainActivity
            new ManageSharedPreferences.Manager(this).setIdNamePicture(id, name, picture);
            isUserLoggedIn();
        } else {

            TextView status = (TextView) findViewById(R.id.sign_in_status);
            status.setText("Login failed.\nPlease try again.");
        }
    }
}
