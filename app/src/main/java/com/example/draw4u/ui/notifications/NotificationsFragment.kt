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

    private lateinit var notificationsViewModel: NotificationsViewModel

       override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
        textView.text = it
        })*/
         val btn: Button = root.findViewById(R.id.btn_logout)

         btn.setOnClickListener{
             MySharedPreferences.clearUser(activity)
             (activity as LoginActivity).signOut()
             startActivity(Intent(activity, LoginActivity::class.java))
         }
    return root
  }
}