import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hawkergo.MainActivity;
import com.example.hawkergo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordActivity extends MainActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

    }

    public void onStart(){



        super.onStart();
        //check if user is signed in non-null and update UI accordinaly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null){
            reload();
        }
    }

    private void createAccount(String email, String password){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Sign in success, update UI with the signed0n user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    //if sign in fails display a message to the user
                    Log.w(TAG,"createUserWithEmail:failure", task.getException());
                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed ", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });


    }

    private void updateUI(FirebaseUser user) {
    }


    private void reload() { }
}
