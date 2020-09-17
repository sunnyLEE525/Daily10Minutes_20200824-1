package kr.co.tjoeun.daily10minutes_20200824

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.tjoeun.daily10minutes_20200824.adapters.ProjectAdapter
import kr.co.tjoeun.daily10minutes_20200824.datas.Project
import kr.co.tjoeun.daily10minutes_20200824.utils.ContextUtil
import kr.co.tjoeun.daily10minutes_20200824.utils.ServerUtil
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val mProjectList = ArrayList<Project>()

    lateinit var mProjectAdapter : ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        projectListView.setOnItemClickListener { parent, view, position, id ->

            val clickedProject = mProjectList[position]

            val myIntent = Intent(mContext, ViewProjectDetailActivity::class.java)
            myIntent.putExtra("project", clickedProject)
            startActivity(myIntent)

        }

        logoutBtn.setOnClickListener {

            val alert = AlertDialog.Builder(mContext)
            alert.setTitle("로그아웃 확인")
            alert.setMessage("정말 로그아웃 하시겠습니까?")
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

//                실제 로그아웃 처리 => 저장되어있는 토큰을 날려버리자.

                ContextUtil.setLoginUserToken(mContext, "")

                val myIntent = Intent(mContext, LoginActivity::class.java)
                startActivity(myIntent)

                finish()

            })
            alert.setNegativeButton("취소", null)
            alert.show()

        }

    }

    override fun setValues() {

//        등록된 기기 토큰이 어떤건지 확인 -> ex. dFcyADKLZJCKLVJZCJVIEWQJLKADJFCZ2312_DAFdaskjl
        Log.d("기기토큰", FirebaseInstanceId.getInstance().token!!)

        getProjectListFromServer()

        mProjectAdapter = ProjectAdapter(mContext, R.layout.project_list_item, mProjectList)
        projectListView.adapter = mProjectAdapter

    }

    fun getProjectListFromServer() {
        ServerUtil.getRequestProjectList(mContext, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(json: JSONObject) {

                val dataObj = json.getJSONObject("data")

                val projectArr = dataObj.getJSONArray("projects")

//                프로젝트가 10개 : 반복 -> 0,1,2,..,9

                for (i in 0 until projectArr.length()) {
                    val projectObj = projectArr.getJSONObject(i)

                    val project = Project.getProjectFromJson(projectObj)

                    mProjectList.add(project)
                }

//                비동기처리 => 어댑터 연결이 먼저 끝나고, 프로젝트목록이 나중에 추가될 수 있다.
//                리스트뷰의 입장에서는 => 내용물 목록이 변경된 상황. => notifyDataSetChanged()로 새로고침.
//                새로고침 : UI에 영향을 주겠다. => runOnUiThread {  } 안에 작성해야 앱이 동작함.

                runOnUiThread {
                    mProjectAdapter.notifyDataSetChanged()
                }

            }

        })
    }

}