package com.example.myapplication.activity.label

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuestionBinding
import com.example.myapplication.entity.AdminBean
import com.example.myapplication.entity.LabelQuestionBean
import com.example.myapplication.entity.UserBean
import com.example.myapplication.entity.UserQuestionBean
import com.example.myapplication.http.LabelQuestionUtils
import com.example.myapplication.http.UserQuestionUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.extensions.toColor
import com.example.myapplication.view.CustomDialog
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionActivity : AppCompatActivity() {

    private val list = mutableListOf<LabelQuestionBean>()
    private var index = -1

    private lateinit var binding: ActivityQuestionBinding

    private var isReview = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbarBack.setOnClickListener { finish() }

        val user: UserBean? = try {
            Gson().fromJson(Prefs.userInfo, UserBean::class.java)
        } catch (e: Exception) {
            null
        }

        if (user == null) {
            Toast.makeText(this, "未登录，请先登录", Toast.LENGTH_SHORT).show()
            return
        }

        val admin: String? = try {
            Gson().fromJson(Prefs.adminInfo, AdminBean::class.java).name ?: ""
        } catch (e: Exception) { "" }
        binding.toolbarTitle.text = "答题（${admin}）"

        val labelId = intent.getIntExtra("label_id", -1)
        if (labelId == -1) return

        lifecycleScope.launch(Dispatchers.IO) {
            val subList = try {
                LabelQuestionUtils.getQuestionsById(labelId.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                list.clear()
                if (subList != null) {
                    list.addAll(subList)
                    index = 0
                    refreshData(index)
                }
            }
        }


        binding.buttonPrevious.setOnClickListener {
            if (index > 0) {
                index--
                refreshData(index)
            }
        }

        binding.buttonNext.setOnClickListener {
            if (index < list.size - 1) {
                index++
                refreshData(index)
            }
        }

        binding.buttonSubmit.isEnabled = !isReview
        binding.buttonSubmit.setOnClickListener {
//            val selectedCount = list.count { !it.selectedAnswer.isNullOrEmpty() }
//            if (selectedCount < list.size) {
//                CustomDialog.Builder2(this)
//                    .setIcon(R.drawable.ic_alert_ask)
//                    .setTitle("未全部答完，是否继续提交？")
//                    .setCancelText(android.R.string.cancel)
//                    .setConfirmText(android.R.string.ok)
//                    .setCancelListener { }
//                    .setConfirmListener {
//                        submitQuestion(user.id!!)
//                    }.show()
//            } else {
//                submitQuestion(user.id!!)
//            }

            submitAll(user.id!!)
        }

        binding.checkGroup.children.forEach {
            it.setOnClickListener { child ->
                checkItem(child.id, true)
                refreshData(index)
            }
        }
    }


    private fun submitAll(uid: Int) {
        val dialog = CustomDialog.Builder2(this)
            .setCancelable(false)
            .setCustomView(ProgressBar(this))
            .show()

        lifecycleScope.launch(Dispatchers.IO) {
            list.forEach { item ->
                try {
                    UserQuestionUtils.setAnswered(uid.toString(), item.id.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            withContext(Dispatchers.Main) {
                dialog.dismiss()
                Toast.makeText(this@QuestionActivity, "操作已完成", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun submitQuestion(uid: Int) {
        val item = list.getOrNull(index) ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val flag = try {
                UserQuestionUtils.setAnswered(uid.toString(), item.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }

            withContext(Dispatchers.Main) {
                if (flag) {
                    Toast.makeText(this@QuestionActivity, "回答完毕", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@QuestionActivity, "请勿重复回答", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verifyAlreadyAnswered(uid: Int): Boolean {
        val item = list.getOrNull(index) ?: return false

        val gsonString = Gson().toJson(UserQuestionBean().apply {
            this.uid = uid
            this.qid = item.id
        })

        return try {
            UserQuestionUtils.isAlreadyAnswered(gsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    private fun refreshData(index: Int) {
        if (index < 0 || index > list.size) {
            return
        }

        val item = list.getOrNull(index) ?: return

        binding.tvIndex.text = "${index + 1} / ${list.size}"

        binding.questionContent.text = item.question

        binding.singleSelect1.text = "A." + item.answerA
        binding.singleSelect2.text = "B." + item.answerB
        binding.singleSelect3.text = "C." + item.answerC
        binding.singleSelect4.text = "D." + item.answerD

        val checkId = when (item.selectedAnswer) {
            "A" -> binding.singleSelect1.id
            "B" -> binding.singleSelect2.id
            "C" -> binding.singleSelect3.id
            "D" -> binding.singleSelect4.id
            else -> -1
        }

        if (item.selectedAnswer.isNullOrEmpty()) {
            binding.tvSelectStatus.text = "已选答案："
            checkItem(-1, false)
        } else {
            //right answer
            binding.tvSelectStatus.text = "已选答案：${item.selectedAnswer}"
            val s = if (item.rightAnswer == item.selectedAnswer) "回答正确" else "回答错误"
            checkItem(checkId, false)
        }

        if (isReview) {
            binding.tvRightStatus.text = "正确答案：${item.rightAnswer}"
            if (item.rightAnswer == item.selectedAnswer) {

            }
        }


        val timeBuilder = StringBuilder()
        if (item.reward != null) {
            timeBuilder.append("积分奖励：" + item.reward + "\n")
        }

        var isEnd = false
        if (item.startTime != null && item.endTime != null) {
            //已结束
            isEnd = System.currentTimeMillis() < ((item.endTime?.time) ?: 0L)

            timeBuilder.append("时间：${Utils.formatTime(item.startTime)}" + "——" +
                    "${Utils.formatTime(item.endTime)}" + "\n")

            if (isEnd) {
                val s = "已结束"
                val span = SpannableString(s)
                span.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.tvSelectStatus.text = span
            }
        }

        binding.tvTime.text =  timeBuilder.toString()

        binding.checkGroup.children.forEach {
            it.isEnabled = !isEnd && !isReview
        }

    }


    private fun checkItem(id: Int, fromUser: Boolean = false) {
        val item = list.getOrNull(index) ?: return

        binding.checkGroup.children.forEach {
            val tv = it as TextView
            tv.setTextColor(toColor(R.color.black))
            tv.setBackgroundResource(R.drawable.shape_answer_normal)
        }

        if (id != -1) {
            findViewById<TextView>(id)?.apply {
                setTextColor(toColor(R.color.white))
                setBackgroundResource(R.drawable.shape_answer_selected)
            }
        }

        val checkId = when (id) {
            binding.singleSelect1.id -> "A"
            binding.singleSelect2.id -> "B"
            binding.singleSelect3.id -> "C"
            binding.singleSelect4.id -> "D"
            else -> null
        }
        if (fromUser) {
            item.selectedAnswer = checkId
        }

    }
}