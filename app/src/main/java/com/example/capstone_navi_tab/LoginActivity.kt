package com.example.capstone_navi_tab

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_navi_tab.databinding.ActivityLoginBinding
import com.example.capstone_navi_tab.ui.mypage.MyPageActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001
    private val GOOGLE_REQUEST_CODE = 99
    val TAG = "googleLogin"
    var callbackManager: CallbackManager? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val usersCollectionRef = db.collection("Users")
    private var adapter: UserAdapter? = null

    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //printHashKey()
        callbackManager = CallbackManager.Factory.create()

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

        binding.btnGoogleLogin.setOnClickListener {
            val signIntent = googleSignInClient?.signInIntent
            startActivityForResult(signIntent, RC_SIGN_IN)
        }

        binding.btnFacebookLogin.setOnClickListener {
            facebookLogin()
        }

        binding.btnLogin.setOnClickListener {
            val userEmail = binding.inputEmail.text.toString()
            val password = binding.inputPw.text.toString()
            auth.signInWithEmailAndPassword(userEmail, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            //displayname null????????? ??? => cloudstore??? ???????????????
                            //Toast.makeText(this, Firebase.auth.currentUser?.displayName.toString(),Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else if(it==null){
                            Toast.makeText(this, "????????? ?????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("LoginActivity", "signInWithEmail", it.exception);
                            Toast.makeText(this, "????????? ?????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }
                    }
        }

        binding.signinTextView.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode,resultCode,data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException){

            }
        }
    }

    private fun updateList(){
        usersCollectionRef.get().addOnSuccessListener {
            val users = mutableListOf<User>()
            for(doc in it){
                users.add(User(doc))
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken,null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful){
                    val user = auth?.currentUser
                    //Toast.makeText(this, "????????? ??????", Toast.LENGTH_SHORT).show()
                    if(user != null) {
                        //displayname ?????? ???????????? ???
                        //Toast.makeText(this, Firebase.auth.currentUser?.displayName.toString(),Toast.LENGTH_SHORT).show()
                        if (!usersCollectionRef.document().equals(user.uid)) {
                            val userMap = hashMapOf(
                                "displayname" to user.displayName,
                                "email" to user.email,
                                "uid" to user.uid
                            )
                            usersCollectionRef.document(user.uid).set(userMap)
                                .addOnSuccessListener { updateList() }.addOnFailureListener { }
                        }

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else{
                    Toast.makeText(this, "????????? ??????", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun facebookLogin(){
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("public_profile","email"))
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    handleFBToken(result?.accessToken)
                }

                override fun onCancel() {}
                override fun onError(error: FacebookException?) {}
            })
    }

    private fun handleFBToken(token : AccessToken?){
        var credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    val user = auth!!.currentUser
                    if(user != null) {
                        if (!usersCollectionRef.document().equals(user.uid)) {
                            Toast.makeText(this,"???!",Toast.LENGTH_SHORT).show()
                            val userMap = hashMapOf(
                                "displayname" to user.displayName,
                                "email" to user.email,
                                "uid" to user.uid
                            )
                            usersCollectionRef.document(user.uid).set(userMap)
                                .addOnSuccessListener { updateList() }.addOnFailureListener { }
                        }

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else{
                    Toast.makeText(this, "????????? ??????", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // ?????? ????????? ??????????????? ??? ??????
    // ?????? ??????x
    fun printHashKey() {
        try {
            val info: PackageInfo = packageManager
                .getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))//Android.util??? Base64??? import ???????????? ?????????.
                Log.i("facebookLogin", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("facebookLogin", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("facebookLogin", "printHashKey()", e)
        }
    }
}