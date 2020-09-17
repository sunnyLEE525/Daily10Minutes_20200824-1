package kr.co.tjoeun.daily10minutes_20200824.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kr.co.tjoeun.daily10minutes_20200824.R
import kr.co.tjoeun.daily10minutes_20200824.ViewDailyProofActivity
import kr.co.tjoeun.daily10minutes_20200824.ViewProjectMembersActivity
import kr.co.tjoeun.daily10minutes_20200824.datas.Project
import kr.co.tjoeun.daily10minutes_20200824.datas.Proof
import kr.co.tjoeun.daily10minutes_20200824.datas.User
import kr.co.tjoeun.daily10minutes_20200824.utils.ServerUtil
import org.json.JSONObject

class ProofAdapter(
    val mContext:Context,
    resId: Int,
    val mList: List<Proof>) : ArrayAdapter<Proof>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.proof_list_item, null)
        }

        val row = tempRow!!

        val proofContentTxt = row.findViewById<TextView>(R.id.proofContentTxt)
        val proofFirstImg = row.findViewById<ImageView>(R.id.proofFirstImg)
        val writeProfileImg = row.findViewById<ImageView>(R.id.writeProfileImg)
        val writerNickName = row.findViewById<TextView>(R.id.writerNickName)
        val replyBtn = row.findViewById<Button>(R.id.replyBtn)
        val likeBtn = row.findViewById<Button>(R.id.likeBtn)

        val data = mList[position]

        proofContentTxt.text = data.content

//        첨부된 이미지가 있다면 => 이미지뷰를 화면에 표시
//        인증글에 사진이 아예 없다면 => 이미지뷰 자체를 숨김처리

        if (data.imgList.size == 0) {
//            사진이 없는 글 => 숨김처리

            proofFirstImg.visibility = View.GONE
        }
        else {
//            하나 이상의 사진이 있는 경우
            proofFirstImg.visibility = View.VISIBLE
            Glide.with(mContext).load(data.imgList[0]).into(proofFirstImg)
        }

//        작성자 프사/닉네임 반영

        Glide.with(mContext).load(data.writer.profileImageArrayList[0]).into(writeProfileImg)

        writerNickName.text = data.writer.nickName

//        댓글 갯수 / 좋아요 갯수 반영

        likeBtn.text = "좋아요 : ${data.likeCount}개"
        replyBtn.text = "댓글 : ${data.replyCount}개"

//        내 좋아요 여부 반영
//        내가 좋아요 X => 회색 테두리 박스
//        내가 좋아요 O => 빨간 테두리 박스

        if (data.isMyLikeProof) {
            likeBtn.setBackgroundResource(R.drawable.red_border_box)
            likeBtn.setTextColor(mContext.resources.getColor(R.color.red))
        }
        else {
            likeBtn.setBackgroundResource(R.drawable.gray_border_box)
            likeBtn.setTextColor(mContext.resources.getColor(R.color.darkGray))
        }

//        좋아요 버튼이 눌리면 => /like_proof = POST 호출하도록

        likeBtn.setOnClickListener {

            ServerUtil.postRequestLikeProof(mContext, data.id, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

                    val dataObj = json.getJSONObject("data")
                    val likeObj = dataObj.getJSONObject("like")

                    data.likeCount = likeObj.getInt("like_count")

//                    내 좋아요 눌렸을 때 변경
                    data.isMyLikeProof = likeObj.getBoolean("my_like")

//                    data의 항목 변경 => 리스트뷰의 내용 변경 발생 => notifyDataSetChanged 실행

                    val myHandler = Handler(Looper.getMainLooper())

                    myHandler.post {
                        notifyDataSetChanged()
                    }
                }
            })

        }



        return row
    }

}