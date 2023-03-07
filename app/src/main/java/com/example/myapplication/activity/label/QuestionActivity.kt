package com.example.myapplication.activity.label

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuestionBinding
import com.example.myapplication.entity.RegionBean
import com.example.myapplication.entity.LabelQuestionBean
import com.example.myapplication.entity.UserBean
import com.example.myapplication.entity.UserQuestionBean
import com.example.myapplication.http.LabelQuestionUtils
import com.example.myapplication.http.UserQuestionUtils
import com.example.myapplication.http.UserRewardUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.extensions.toColor
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.UserViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class QuestionActivity : AppCompatActivity() {

    private val list = mutableListOf<LabelQuestionBean>()
    private var index = -1

    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbarBack.setOnClickListener { finish() }


        if (UserViewModel.user == null) {
            Toast.makeText(this, "未登录，请先登录", Toast.LENGTH_SHORT).show()
            return
        }

        binding.toolbarTitle.text = "答题（${UserViewModel.region?.name ?: ""}）"

        val labelId = intent.getIntExtra("label_id", -1)
        if (labelId == -1) return

        lifecycleScope.launch(Dispatchers.IO) {
            val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "${UserViewModel.user?.id}.questions")
            val localList = try {
                Gson().fromJson<List<LabelQuestionBean>>(
                    file.readText(),
                    object : TypeToken<List<LabelQuestionBean>>() {}.type
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            if (localList != null) {
                withContext(Dispatchers.Main) {
                    list.clear()
                    list.addAll(localList)
                    index = 0
                    refreshData(index)
                }
            } else {
                val subList = try {
                    LabelQuestionUtils.getQuestionsById(labelId.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                if (subList != null) {
                    verifyAlreadyAnswered(UserViewModel.user?.id!!, subList)
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

        binding.buttonSubmit.setOnClickListener {
            val selectedCount = list.count { it.selectedAnswer.isNotEmpty() }

            CustomDialog.Builder2(this)
                .setIcon(R.drawable.ic_alert_ask)
                .setTitle("共计${list.size}题，已选择${selectedCount}题，是否继续提交？")
                .setCancelText(android.R.string.cancel)
                .setConfirmText(android.R.string.ok)
                .setCancelListener { }
                .setConfirmListener {
                    submitAll(UserViewModel.user?.id!!)
                }.show()
        }

        binding.buttonSave.setOnClickListener {
            try {
                val jsonListString = Gson().toJson(list)
                val file = File(
                    this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "${UserViewModel.user?.id}.questions"
                )
                file.writeText(jsonListString)

                Toast.makeText(this, "暂存成功！", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

        var rightCount = 0
        var reward = 0
        lifecycleScope.launch(Dispatchers.IO) {
            list.filter { !it.alreadyAnswered }.forEach { item ->
                try {
                    UserQuestionUtils.setAnswered(uid.toString(), item.id.toString(),
                        item.selectedAnswer
                    )

                    if (item.selectedAnswer == item.rightAnswer) {
                        rightCount++
                        val qReward = item.reward
                        if (qReward != null) reward += qReward
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            try {
                UserRewardUtils.setReward(uid.toString(), UserViewModel.region?.pin.toString(), reward.toString())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(this@QuestionActivity, "操作已完成，答对${rightCount}题，共获得${reward}积分", Toast.LENGTH_LONG).show()

                dialog.dismiss()
                finish()
            }
        }

    }
    private fun submitQuestion(uid: Int) {
        val item = list.getOrNull(index) ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val flag = try {
                UserQuestionUtils.setAnswered(uid.toString(), item.id.toString(),
                    item.selectedAnswer
                )
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

    private fun verifyAlreadyAnswered(uid: Int, list: List<LabelQuestionBean>) {
        for (item in list) {
            val gsonString = Gson().toJson(UserQuestionBean().apply {
                this.uid = uid
                this.qid = item.id
            })

            try {
                val response = UserQuestionUtils.isAlreadyAnswered(gsonString) ?: return
                if (response.code == 200) {
                    item.alreadyAnswered = true
                    item.selectedAnswer = response.data.toString()
                } else {
                    item.alreadyAnswered = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

        val checkId = when (item.selectedAnswer) {
            "A" -> binding.singleSelect1.id
            "B" -> binding.singleSelect2.id
            "C" -> binding.singleSelect3.id
            else -> -1
        }

        if (item.selectedAnswer.isEmpty()) {
            binding.tvSelectStatus.text = "已选答案："
            checkItem(-1, false)
        } else {
            binding.tvSelectStatus.text = "已选答案：${item.selectedAnswer}"
            checkItem(checkId, false)
        }

        //已经回答过，不能重复回答
        binding.tvAlreadyAnswered.visibility = if (item.alreadyAnswered) View.VISIBLE else View.GONE
        if (item.alreadyAnswered) {
            binding.tvRightStatus.text = "正确答案：${item.rightAnswer}"
            val s = if (item.rightAnswer == item.selectedAnswer) "回答正确" else "回答错误"
            binding.tvAlreadyAnswered.text = "已回答：\n${s}"
        }
//        binding.buttonSubmit.isEnabled = !item.alreadyAnswered


        val timeBuilder = StringBuilder()
        if (item.reward != null) {
            timeBuilder.append("积分奖励：" + item.reward + "\n")
        }

        var isEnd = false
        if (item.startTime != null && item.endTime != null) {
            //已结束
            isEnd = System.currentTimeMillis() > ((item.endTime?.time) ?: 0L)

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
            it.isEnabled = !isEnd && !item.alreadyAnswered
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
            else -> ""
        }
        if (fromUser) {
            item.selectedAnswer = checkId
        }


        lifecycleScope
    }
}