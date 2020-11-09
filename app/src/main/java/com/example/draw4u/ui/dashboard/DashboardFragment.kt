package com.example.draw4u.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.draw4u.DiaryInfo
import com.example.draw4u.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item.view.*

class DashboardFragment : Fragment() {

  private lateinit var dashboardViewModel: DashboardViewModel
  var firestore : FirebaseFirestore? = null
  var fbAuth : FirebaseAuth? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ):  View? {
      dashboardViewModel =
      ViewModelProviders.of(this).get(DashboardViewModel::class.java)
      val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
    // 파이어스토어 인스턴스 초기화
    firestore = FirebaseFirestore.getInstance()

    val search_word: EditText = root.findViewById(R.id.searchWord)
    val search_btn: Button = root.findViewById(R.id.btn_search)
    val recyclerview: RecyclerView = root.findViewById(R.id.recyclerview)
    recyclerview.adapter = RecyclerViewAdapter()
    recyclerview.layoutManager = LinearLayoutManager(activity)

    search_btn.setOnClickListener(){
      (recyclerview.adapter as RecyclerViewAdapter).search(search_word.getText().toString())
    }

    return root
    }

  inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // Person 클래스 ArrayList 생성성
    var diaryinfo : ArrayList<DiaryInfo> = arrayListOf()

    init {  // telephoneBook의 문서를 불러온 뒤 Person으로 변환해 ArrayList에 담음
      firestore?.collection(fbAuth?.uid.toString())?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
        // ArrayList 비워줌
        diaryinfo.clear()

        for (snapshot in querySnapshot!!.documents) {
          var item = snapshot.toObject(DiaryInfo::class.java)
          diaryinfo.add(item!!)
        }
        notifyDataSetChanged()
      }
    }

    // xml파일을 inflate하여 ViewHolder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      var view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
      return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      var viewHolder = (holder as ViewHolder).itemView

      viewHolder.date.text = diaryinfo[position].date
      viewHolder.keywords.text =
        "#" + diaryinfo[position].keyword1 + " #" + diaryinfo[position].keyword2 + " #" + diaryinfo[position].keyword3
    }

    // 리사이클러뷰의 아이템 총 개수 반환
    override fun getItemCount(): Int {
      return diaryinfo.size
    }

    // 파이어스토어에서 데이터를 불러와서 검색어가 있는지 판단
    fun search(searchWord : String) {
      firestore?.collection(fbAuth?.uid.toString())
        ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
        // ArrayList 비워줌
        diaryinfo.clear()

        for (snapshot in querySnapshot!!.documents) {
          if (snapshot.getString("diary")!!.contains(searchWord)) {
            var item = snapshot.toObject(DiaryInfo::class.java)
            diaryinfo.add(item!!)
          }
        }
        notifyDataSetChanged()
      }
    }
  }

}