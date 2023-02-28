package com.example.myapplication.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuestionBinding
import com.example.myapplication.entity.QuestionBean
import com.example.myapplication.http.QuestionUtils
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.extensions.toColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionActivity : AppCompatActivity() {

    private val list = mutableListOf<QuestionBean>()
    private var index = -1

    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbarBack.setOnClickListener { finish() }

        val id = intent.getStringExtra("region") ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val subList = try {
                QuestionUtils.getQuestionsById("-1")
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


        binding.buttonLast.setOnClickListener {
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

        binding.checkGroup.children.forEach {
            it.setOnClickListener { child ->
                checkItem(child.id, true)
                refreshData(index)
            }
        }
    }

    private fun refreshData(index: Int) {
        if (index < 0 || index > list.size) {
            return
        }

        binding.tvIndex.text = "${index + 1} / ${list.size}"

        val item = list[index]

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

        binding.tvRightStatus.text = "正确答案：${item.rightAnswer}"

        if (item.selectedAnswer.isNullOrEmpty()) {
            binding.tvSelectStatus.text = "已选答案："
            checkItem(-1, false)
        } else {
            //right answer
            binding.tvSelectStatus.text = "已选答案：${item.selectedAnswer}"
            val s = if (item.rightAnswer == item.selectedAnswer) "回答正确" else "回答错误"
            checkItem(checkId, false)
        }

        val timeBuilder = StringBuilder()
        if (item.reward != null) {
            timeBuilder.append("积分：" + item.reward + "\n")
        }

        var isEnd = false
        if (item.startTime != null && item.endTime != null) {
            //已结束
            isEnd = System.currentTimeMillis() < ((item.endTime?.time) ?: 0L)

            timeBuilder.append("时间：${Utils.formatTime(item.startTime)}" + "————" +
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
            it.isEnabled = !isEnd
        }

    }


    private fun checkItem(id: Int, fromUser: Boolean = false) {
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
            list.getOrNull(index)?.selectedAnswer = checkId
        }

    }
}