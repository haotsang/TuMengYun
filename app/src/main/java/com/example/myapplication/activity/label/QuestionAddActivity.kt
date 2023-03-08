package com.example.myapplication.activity.label

import android.app.Activity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityQuestionAddBinding
import com.example.myapplication.entity.LabelQuestionBean
import com.example.myapplication.http.LabelQuestionUtils
import com.example.myapplication.utils.Utils
import com.example.myapplication.viewmodel.LabelViewModel
import com.example.myapplication.viewmodel.UserViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class QuestionAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionAddBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val isEdit = intent.getBooleanExtra("is_edit", false)
        var questionId: Int? = -1

        binding.toolbarBack.setOnClickListener { finish() }
        binding.toolbarTitle.text = if (isEdit) "修改答题" else "添加答题（${UserViewModel.region?.name}）"

        binding.buttonAddQuestion.text = if (isEdit) "确认修改" else "确认添加"

        var startTime: Date? = null
        var endTime: Date? = null

        if (isEdit) {
            val editData = intent.getStringExtra("edit_data")
            val bean = Gson().fromJson(editData, LabelQuestionBean::class.java)

            binding.questionEdit.setText(bean.question)
            binding.answerEdit1.setText(bean.answerA)
            binding.answerEdit2.setText(bean.answerB)
            binding.answerEdit3.setText(bean.answerC)

            binding.rightEdit.setText(bean.rightAnswer)
            binding.rewardEdit.setText(bean.reward?.toString())

            startTime = bean.startTime
            endTime = bean.endTime
            binding.timeStatus.text = "开始时间：${Utils.formatTime(startTime) ?: ""}\n结束时间：${Utils.formatTime(endTime) ?: ""}"
            questionId = bean.id
        }

        binding.buttonStartTime.setOnClickListener {
            CardDatePickerDialog.Builder(this).setTitle("选择开始时间").setOnChoose {
                startTime = Date(it)
                binding.timeStatus.text = "开始时间：${Utils.formatTime(startTime) ?: ""}\n结束时间：${Utils.formatTime(endTime) ?: ""}"
            }.build().show()
        }
        binding.buttonEndTime.setOnClickListener {
            CardDatePickerDialog.Builder(this).setTitle("选择结束时间").setOnChoose {
                if (startTime != null && startTime!!.time < it) {
                    endTime = Date(it)
                    binding.timeStatus.text = "开始时间：${Utils.formatTime(startTime) ?: ""}\n结束时间：${Utils.formatTime(endTime) ?: ""}"
                } else {
                    Toast.makeText(this, "结束时间应大于开始时间", Toast.LENGTH_SHORT).show()
                }
            }.build().show()
        }

        val codeInputFilter = object : InputFilter {
            override fun filter(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Spanned?,
                p4: Int,
                p5: Int
            ): CharSequence {
                if (p0 != null) {
                    when (p0) {
                        "A", "a" -> return "A"
                        "B", "b" -> return "B"
                        "C", "c" -> return "C"
                        "D", "d" -> return "D"
                    }
                }

                return ""
            }

        }
        binding.rightEdit.filters = arrayOf(InputFilter.LengthFilter(1), InputFilter.AllCaps())

        binding.buttonAddQuestion.setOnClickListener {
            val question = binding.questionEdit.text.trim().toString()
            val answerA = binding.answerEdit1.text.trim().toString()
            val answerB = binding.answerEdit2.text.trim().toString()
            val answerC = binding.answerEdit3.text.trim().toString()

            val rightAnswer = binding.rightEdit.text.trim().toString()
            val reward = binding.rewardEdit.text.trim().toString()


            if (question.isEmpty() ||
                answerA.isEmpty() || answerB.isEmpty() || answerC.isEmpty()
            ) {
                Toast.makeText(this@QuestionAddActivity, "问题或答案不可为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rightAnswer.isEmpty()) {
                Toast.makeText(this@QuestionAddActivity, "请输入正确答案", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val questionEntity = LabelQuestionBean().apply {
                this.question = question
                this.answerA = answerA
                this.answerB = answerB
                this.answerC = answerC
                this.rightAnswer = rightAnswer
                this.explanation = ""
                this.startTime = startTime
                this.endTime = endTime
                this.reward = try {
                    reward.toInt()
                } catch (e: Exception) {
                    0
                }
                this.labelId = LabelViewModel.label?.id
            }

            if (isEdit && questionId != -1) {
                questionEntity.id = questionId
            }

            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
            val gsonString = gson.toJson(questionEntity)
            lifecycleScope.launch(Dispatchers.IO) {
                val flag = try {
                    if (isEdit) {
                        LabelQuestionUtils.updateQuestion(gsonString)
                    } else {
                        LabelQuestionUtils.insertQuestion(gsonString)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    false
                }

                withContext(Dispatchers.Main) {
                    if (flag) {
                        setResult(Activity.RESULT_OK)

                        Toast.makeText(this@QuestionAddActivity, if (isEdit) "修改成功" else "添加成功", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@QuestionAddActivity, "失败", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }
    }
}