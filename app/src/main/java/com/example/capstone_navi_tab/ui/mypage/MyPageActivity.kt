package com.example.capstone_navi_tab.ui.mypage

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_navi_tab.R
import kotlinx.android.synthetic.main.activity_mypage.*
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.capstone_navi_tab.LoginActivity
import com.example.capstone_navi_tab.MainActivity
import com.example.capstone_navi_tab.SigninActivity
import com.example.capstone_navi_tab.databinding.ActivityMypageBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class MyPageActivity : AppCompatActivity() {

    lateinit var binding: ActivityMypageBinding

    private lateinit var database: DatabaseReference

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var firestore: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    private var filePath: Uri? = null

    companion object{
        //이미지 고르는 코드
        private val IMAGE_PICK_CODE = 1000;
        //Permission 코드
        private val PERMISSION_CODE = 1001;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        database = Firebase.database.reference

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        firestore = FirebaseFirestore.getInstance()



        binding.nickname.setVisibility(View.VISIBLE)
        binding.nicknameChangeBtn.setVisibility(View.VISIBLE)
        binding.changeNickname.setVisibility(View.INVISIBLE)

        //닉네임 변경
        binding.nicknameChangeBtn.setOnClickListener {
            binding.nickname.setVisibility(View.INVISIBLE)
            binding.nicknameChangeBtn.setVisibility(View.INVISIBLE)
            binding.changeNickname.setVisibility(View.VISIBLE)
        }

        //프로필 변경 리스너
        binding.profileBtn.setOnClickListener {
            //check runtime permission
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }
        //변경 내용 저장
        binding.saveBtn.setOnClickListener {
            uploadImage()
            binding.progressBar.visibility = View.VISIBLE
        }

        //뒤로 가기 버튼
        binding.backBtn.setOnClickListener{
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        //startActivityForResult(intent, IMAGE_PICK_CODE)
        startActivityForResult(Intent.createChooser(intent,"Select Image"), MyPageActivity.IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,  grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup
                    Toast.makeText(this, "Permission denied!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //profile_image.setImageURI(data?.data)
            filePath = data!!.data
            try{
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.profileImage.setImageBitmap(bitmap)
                binding.saveBtn.visibility = View.VISIBLE
            }catch(e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {

            var ref: StorageReference = storageRef.child("image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE
                    binding.saveBtn.visibility = View.GONE
                    Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
                    ref.downloadUrl
                }
                .addOnFailureListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Failed" + it.message, Toast.LENGTH_SHORT).show()

                }
        }
    }

}