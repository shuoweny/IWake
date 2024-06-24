package com.COMP900018.finalproject.Auth;

import com.google.firebase.auth.FirebaseAuth;

public class AuthAPI {

    private FirebaseAuth mAuth;

    public AuthAPI(){
        this.mAuth = FirebaseAuth.getInstance();
    }
//    public void createUser(String email, String password, String firstName, String lastName, DatabaseApi db){
//        this.mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            db.addNewUser(email, user.getUid(), firstName, lastName);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                        }
//                    }
//                });
//    }

}
