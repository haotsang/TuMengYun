package com.example.myapplication.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLabelTitleEditBinding
import com.example.myapplication.databinding.ActivityQuestionBinding
import com.example.myapplication.entity.QuestionBean
import com.example.myapplication.http.QuestionUtils
import com.example.myapplication.utils.Utils
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

        binding.singleSelect1.setOnClickListener { checkItem(it.id, true) }
        binding.singleSelect2.setOnClickListener { checkItem(it.id, true) }
        binding.singleSelect3.setOnClickListener { checkItem(it.id, true) }
        binding.singleSelect4.setOnClickListener { checkItem(it.id, true) }
        binding.singleSelect1.setOnClickListener { checkItem(it.id, true) }
    }

    private fun refreshData(index: Int) {
        binding.tvIndex.text = "${index + 1} / ${list.size}"

        val item = list[index]

        binding.questionContent.text = item.question

        binding.singleSelect1.text = item.answerA
        binding.singleSelect2.text = item.answerB
        binding.singleSelect3.text = item.answerC
        binding.singleSelect4.text = item.answerD

        val checkId = when (item.selectedAnswer) {
            "A" -> binding.singleSelect1.id
            "B" -> binding.singleSelect2.id
            "C" -> binding.singleSelect3.id
            "D" -> binding.singleSelect4.id
            else -> -1
        }

        if (item.selectedAnswer.isNullOrEmpty()) {
            binding.tvStatus.text ="暂未作答"
            checkItem(-1, false)
        } else {
            //right answer
            binding.tvStatus.text = if (item.rightAnswer == item.selectedAnswer) "回答正确" else "回答错误"
            checkItem(checkId, false)
        }

        binding.tvTime.text = "开始时间：${Utils.formatTime(item.startTime)}" + "\n" +
                "结束时间：${Utils.formatTime(item.endTime)}"

        if (item.startTime != null && item.endTime != null) {
            if (System.currentTimeMillis() > (item.endTime?.time ?: 0L)) {
                //已结束
                binding.tvStatus.text = "已结束"
            }
        }

    }


    private fun checkItem(id: Int, fromUser: Boolean = false) {
        binding.checkGroup.children.forEach {
            val tv = it as TextView
            tv.setTextColor(Color.BLACK)
        }

        if (id != -1) {
            findViewById<TextView>(id)?.setTextColor(Color.BLUE)
        }

        val checkId = when (id) {
            binding.singleSelect1.id -> "A"
            binding.singleSelect2.id -> "B"
            binding.singleSelect3.id -> "C"
            binding.singleSelect4.id -> "D"
            else -> null
        }
        if (fromUser) {
            list[index].selectedAnswer = checkId
        }

    }
}