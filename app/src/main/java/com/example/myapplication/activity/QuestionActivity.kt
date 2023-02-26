package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLabelTitleEditBinding
import com.example.myapplication.databinding.ActivityQuestionBinding
import com.example.myapplication.entity.QuestionBean
import com.example.myapplication.http.QuestionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionActivity : AppCompatActivity() {

    private val list = mutableListOf<QuestionBean>()
    private var index = 0

    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val id = intent.getStringExtra("region") ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val subList = QuestionUtils.getQuestionsById(id)
            withContext(Dispatchers.Main) {
                list.clear()
                list.addAll(subList)
                index = 0

                refreshData(index)
            }
        }


        binding.buttonLast.setOnClickListener {
            if (index > 0) {
                index--
                refreshData(index)
            }
        }

        binding.buttonNext.setOnClickListener {
            if (index <= list.size) {
                index++
                refreshData(index)
            }
        }
    }

    private fun refreshData(index: Int) {
        val item = list[index]
        if (System.currentTimeMillis() > (item.endTime?.time ?: 0L)) {
            //已结束
        }
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

        if (item.rightAnswer == item.selectedAnswer) {
            //right answer
        }

        if (binding.checkGroup.checkedRadioButtonId != -1) {
            binding.checkGroup.check(checkId)
        }
    }
}