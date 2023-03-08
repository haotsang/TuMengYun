package com.example.myapplication.activity.label

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.LabelQuestionBean
import com.example.myapplication.http.LabelQuestionUtils
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.LabelViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseListBinding

    private val questionList = mutableListOf<LabelQuestionBean>()
    private lateinit var adapter: KotlinDataAdapter<LabelQuestionBean>

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            getALl()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, true)
        binding = ActivityBaseListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.baseTitle.text = "题目列表"
        binding.baseBack.setOnClickListener { finish() }

        if (LabelViewModel.label?.id == null) return

        binding.baseOverflow.text = "添加题库"
        binding.baseOverflow.setOnClickListener {
            startActivityForResult(Intent(this, QuestionAddActivity::class.java).apply {
                putExtra("is_edit", false)
                putExtra("label_id", LabelViewModel.label?.id)
            }, 101)
        }

        adapter = KotlinDataAdapter.Builder<LabelQuestionBean>()
            .setLayoutId(R.layout.item_nav)
            .setData(questionList)
            .addBindView2 { itemView, pos ->
                val icon = itemView.findViewById<ImageView>(R.id.item_nav_icon)
                val title = itemView.findViewById<TextView>(R.id.item_nav_title)
                val arrow = itemView.findViewById<ImageView>(R.id.item_nav_arrow)
                icon.setImageResource(R.drawable.ic_nav_issue)
                arrow.setImageResource(R.drawable.baseline_delete_forever_24)
                arrow.setOnClickListener {
                    CustomDialog.Builder2(this)
                        .setIcon(R.drawable.ic_alert_ask)
                        .setTitle("确定继续删除吗？")
                        .setCancelListener {}
                        .setConfirmListener {
                            deleteQuestion(pos)
                        }
                        .show()
                }

                val item = questionList[pos]
                val isEnd = if (LabelViewModel.label?.startTime != null && LabelViewModel.label?.endTime != null) {
                    //已结束
                    System.currentTimeMillis() > ((LabelViewModel.label?.endTime?.time) ?: 0L)
                } else {
                    false
                }

                title.text = if (isEnd) "${item.question}@已结束" else item.question
            }.create()

        binding.baseRecyclerView.adapter = adapter
        binding.baseRecyclerView.setOnItemClickListener { holder, position ->
            startActivityForResult(Intent(this, QuestionAddActivity::class.java).apply {
                putExtra("is_edit", true)
                putExtra("label_id", LabelViewModel.label?.id)
                putExtra("edit_data", Gson().toJson(questionList[position]))
            }, 101)
        }


        getALl()
    }

    private fun getALl() {
        lifecycleScope.launch(Dispatchers.IO) {
            val subList = try {
                LabelQuestionUtils.getQuestionsById(LabelViewModel.label?.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                questionList.clear()
                if (subList != null) {
                    questionList.addAll(subList)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteQuestion(position: Int) {
        val question = questionList[position]
        lifecycleScope.launch(Dispatchers.IO) {
            val flag = try {
                LabelQuestionUtils.deleteQuestion(question.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }

            withContext(Dispatchers.Main) {
                if (flag) {
                    questionList.remove(question)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@QuestionListActivity, "已刪除", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@QuestionListActivity, "删除失败", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }



}