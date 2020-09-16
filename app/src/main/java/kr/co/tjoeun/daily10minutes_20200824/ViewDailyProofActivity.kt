package kr.co.tjoeun.daily10minutes_20200824

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_view_daily_proof.*
import java.text.SimpleDateFormat
import java.util.*

class ViewDailyProofActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_daily_proof)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        selectDateBtn.setOnClickListener {

            val datePickerDialog = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

//                일단 Calendar 변수를 하나 생성, calendar 객체를 담아두자.
                val selectedDate = Calendar.getInstance()

//                selectedDate에는 몇년 몇월 몇일이 들어있나? 기본값 : 현재 일시가 자동으로 기록됨.


//                날짜가 선택되면 실행해줄 코드
//                => 연도/월/일 각각의 항목을 변경(SelectedDate 변수의 값 변경)

                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                //                년/월/일을 한꺼번에 세팅.
                selectedDate.set(year, month, dayOfMonth)

                Log.d("선택된 년", selectedDate.get(Calendar.YEAR).toString())
                Log.d("선택된 월", selectedDate.get(Calendar.MONTH).toString())
                Log.d("선택된 일", selectedDate.get(Calendar.DAY_OF_MONTH).toString())

                val sdf = SimpleDateFormat("yyyy-MM-dd")

                val selectedDateStr = sdf.format(selectedDate.time)

                selectedDateTxt.text = selectedDateStr


//                실생활 : 1~12월
//                JAVA / Kotlin : 0~11월

            }, 2020, Calendar.JUNE, 9)
            datePickerDialog.show()

        }

    }

    override fun setValues() {

//        이 화면이 실행되면 오늘 날짜를 => 2020년 9월 5일 양식으로 selectedDateTxt에 출력

        val todayCal = Calendar.getInstance()  // 기본값이 현재 일시 => 오늘 날짜가 담겨있다.

        val sdf = SimpleDateFormat("yyyy년 M월 d일")

        val todayStr = sdf.format(todayCal.time)

        selectedDateTxt.text = todayStr


    }

}