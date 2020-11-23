package com.example.draw4u.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.draw4u.LoginActivity
import com.example.draw4u.MySharedPreferences
import com.example.draw4u.R
import com.example.draw4u.SignUpActivity
import android.content.Intent as Intent

class NotificationsFragment : Fragment() {

       override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val btn: Button = root.findViewById(R.id.btn_logout)

         btn.setOnClickListener{
             //(activity as LoginActivity).signOut()
             MySharedPreferences.clearUser(activity)
             startActivity(Intent(activity, LoginActivity::class.java))
         }
    return root
  }
}