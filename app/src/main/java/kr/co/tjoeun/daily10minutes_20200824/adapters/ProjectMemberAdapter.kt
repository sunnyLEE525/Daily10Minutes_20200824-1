package kr.co.tjoeun.daily10minutes_20200824.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kr.co.tjoeun.daily10minutes_20200824.R
import kr.co.tjoeun.daily10minutes_20200824.datas.Project
import kr.co.tjoeun.daily10minutes_20200824.datas.User

class ProjectMemberAdapter(
    val mContext:Context,
    resId: Int,
    val mList: List<User>) : ArrayAdapter<User>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.user_list_item, null)
        }

        val row = tempRow!!

        val userFirstProfileImg = row.findViewById<ImageView>(R.id.userFirstProfileImg)
        val userNickNameTxt = row.findViewById<TextView>(R.id.userNickNameTxt)
        val userEmailTxt = row.findViewById<TextView>(R.id.userEmailTxt)

        val user = mList[position]

        userNickNameTxt.text = user.nickName
        userEmailTxt.text = user.email

        Glide.with(mContext).load(user.profileImageArrayList[0]).into(userFirstProfileImg)


        return row
    }

}