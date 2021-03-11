package com.example.capstone_navi_tab

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.capstone_navi_tab.databinding.ActivityMainBinding
import com.example.capstone_navi_tab.databinding.ActivityMypageBinding
import com.example.capstone_navi_tab.ui.calendar.CalendarFragment
import com.example.capstone_navi_tab.ui.home.ExerciseTab
import com.example.capstone_navi_tab.ui.mypage.MyPageActivity
import com.example.capstone_navi_tab.ui.settings.SettingsFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    lateinit var binding: ActivityMainBinding
    private var drawer: DrawerLayout? = null
    private var appBarTextView: TextView? = null
    private val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

    private lateinit var navigationView: NavigationView
    private lateinit var headerView: View
    private var mypageimageview : ImageView? = null

    private lateinit var auth: FirebaseAuth
    private val db: FirebaseFirestore = Firebase.firestore
    private val usersCollectionRef = db.collection("Users")

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        if (auth.currentUser == null) { // 사용자 로그인 안된 상태
            startActivity(Intent(this, LoginActivity::class.java)) // 로그인 시작
            finish()
        }

        drawer = findViewById(R.id.drawer_layout)
        appBarTextView = findViewById(R.id.appbar_text_view)


        val menuRight: ImageButton = findViewById(R.id.leftRight)
        menuRight.setOnClickListener {
            if (drawer!!.isDrawerOpen(GravityCompat.START)!!) {
                drawer!!.closeDrawer(GravityCompat.START)
            } else {
                drawer!!.openDrawer(GravityCompat.START)
            }
        }
        navigationView= findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        headerView= navigationView.getHeaderView(0)
       // mypageimageview = headerView.findViewById<ImageView>(R.id.my_imageView)

        //navigation drawer에 있는 프로필 클릭시 마이페이지로 이동
       /* mypageimageview?.setOnClickListener {
            //Toast.makeText(applicationContext,"click",Toast.LENGTH_SHORT).show()
            val mypageIntent = Intent(this, MyPageActivity::class.java)
            startActivity(mypageIntent)
        }*/

        //logout button 클릭 시, 로그아웃이 되면서 로그인 화면으로 이동
        binding.logoutImageView.setOnClickListener {
            Firebase.auth.signOut()
            val logoutIntent = Intent(this, LoginActivity::class.java)
            startActivity(logoutIntent)
        }

        appBarTextView!!.text = "Exercise"
        ft.replace(R.id.f_container,ExerciseTab())
        ft.commit()
    }

    private fun getUserProfile(){
        var user = Firebase.auth.currentUser
        db.collection("Users")
            .get()
            .addOnSuccessListener { result->
                for(document in result){

                }
            }

        user?.let {
            var name = user.displayName
            var email = user.email
            var photoUrl = user.photoUrl

            // 고유 id값 => 사용금지
            val uid = user.uid
        }
    }

    // 구글, 페북 계정 data 가져오기
    private fun getProviderData() {
        // [START get_provider_data]
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }
    }

    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer?.closeDrawers()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        // Handle navigation view item clicks here.

        val id = item.itemId
        if (id == R.id.nav_home) { // activity_main_drawer
            appBarTextView!!.text = "Exercise"
            // Home에서 운동 탭이 보이게 할거임
            val homeFragment = ExerciseTab()
            ft.replace(R.id.f_container, homeFragment)
            ft.commit()
        } else if(id == R.id.nav_mypage){
            val mypageIntent = Intent(this, MyPageActivity::class.java)
            startActivity(mypageIntent)
        }else if (id == R.id.nav_calendar) {
            appBarTextView!!.text = "Calendar"
            val calendarFragment = CalendarFragment()
            ft.replace(R.id.f_container, calendarFragment)
            ft.commit()
        } else if (id == R.id.nav_settings) {
            appBarTextView!!.text = "Settings"
            val settingsFragment = SettingsFragment()
            ft.replace(R.id.f_container, settingsFragment)
            ft.commit()
        }
        val drawer: DrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
