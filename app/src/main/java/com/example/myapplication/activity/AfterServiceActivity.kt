package com.example.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.http.HttpUtils
import com.example.myapplication.utils.GlideEngine
import com.example.myapplication.utils.Utils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


class AfterServiceActivity : BaseActivity() {

    private val list = mutableListOf<String>()

    private var viewPager:ViewPager? =null
    private var adapter: PagerAdapter? = null

    private var _id: String = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_shouhou)

        val zhanQuName = intent.getStringExtra("zhanQu")
        val zhanPinName = intent.getStringExtra("zhanPin")

        val id = "$zhanQuName-$zhanPinName"
        _id = id

        val inductor = findViewById<TextView>(R.id.main_pager_inductor)
        viewPager = findViewById<ViewPager>(R.id.main_photo_pager)
        adapter = object : PagerAdapter() {
            override fun getCount(): Int = list.size
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val v = View.inflate(this@AfterServiceActivity, R.layout.item_photo, null)

                val photoView = v.findViewById<ImageView>(R.id.photo_thumbnail)
                photoView.setOnClickListener {
//                    DetailsActivity.start(this@AfterServiceActivity, list[position])
                }
//                photoView.setImageBitmap(BitmapFactory.decodeFile(list[position]))
                Glide.with(photoView.context).load(list[position]).into(photoView)

                val deleteBtn = v.findViewById<ImageView>(R.id.photo_delete)
                deleteBtn.setOnClickListener {
//                    list.removeAt(position)
//
////                    container.removeViewAt(position)
//                    destroyItem(container, position, v)
//                    notifyDataSetChanged()
                }

                container.addView(v)
                return v
            }
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }
            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
        viewPager?.adapter = adapter
        viewPager?.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                inductor.text = "${position + 1}/${list.size}"
            }
        })
        inductor.text = "${if (list.size < 1) 0 else 1}/${list.size}"

        findViewById<Button>(R.id.main_button_capture).setOnClickListener {
            takePhoto()
        }
        findViewById<Button>(R.id.main_button_scan).setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
        findViewById<Button>(R.id.main_button_del).setOnClickListener {
            if (viewPager != null && list.isNotEmpty()) {
                list.removeAt(viewPager!!.currentItem)
                adapter?.notifyDataSetChanged()

                inductor.text = "${(viewPager?.currentItem ?: 0) + 1}/${list.size}"
            }
        }
        findViewById<Button>(R.id.confirm).setOnClickListener {
            confirmUpload(id)
        }
        findViewById<Button>(R.id.cancel).setOnClickListener {
            cancelUpload(id)
        }
        findViewById<Button>(R.id.fixed).setOnClickListener {
            fixDone(id)
        }
        findViewById<Button>(R.id.cangku).setOnClickListener {
            startActivity(Intent(this, CangkuActivity::class.java))
        }


        val zhanQu = findViewById<TextView>(R.id.zhan_qu_name)
        val zhanPin = findViewById<TextView>(R.id.zhan_pin_name)
        zhanQu.text = zhanQuName
        zhanPin.text = zhanPinName

        val editTextIssue = findViewById<EditText>(R.id.edittext_issue)
        val editTextPerson = findViewById<EditText>(R.id.edittext_person)


        Thread {
            val pic = getAllPictureFromId(id)
            runOnUiThread {
                list.clear()
                list.addAll(pic)
                adapter?.notifyDataSetChanged()
            }
        }.start()

        Thread {
            val list = getIssueAndPerson(id)
            runOnUiThread {
                editTextIssue.setText(list[0])
                editTextPerson.setText(list[1])
            }
        }.start()


    }

    private fun getIssueAndPerson(id: String): List<String> {
        return listOf("1", "2")
    }

    private fun getAllPictureFromId(id: String): List<String> {
        return emptyList()
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.deleteDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val thumbnail = data.extras?.get("data") as Bitmap
                val imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.jpg")
                val fos = FileOutputStream(imageFile)
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()

                list.add(imageFile.absolutePath)
                adapter?.notifyDataSetChanged()

                val inductor = findViewById<TextView>(R.id.main_pager_inductor)
                inductor.text = "${(viewPager?.currentItem ?: 0) + 1}/${list.size}"

                ///
//                if (!BuildConfig.DEBUG) {
                    HttpUtils.uploadImageFile(imageFile, _id)
//                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }
    }

    private fun takePhoto() {

//        val imageFile: File = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "output_image.jpg")
//        if (imageFile.exists()){//????????????????????????????????????????????????
//            imageFile.delete()
//        }
//        imageFile.createNewFile()
////


//        val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            FileProvider.getUriForFile(this, "$packageName.fileProvider", imageFile)
//        }else {
//            Uri.fromFile(imageFile)
//        }

//        try {
//            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            i.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
////        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//            startActivityForResult(i, 0)
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }

        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setMaxSelectNum(10)
            .setImageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    result?.forEach {
                        it?.path?.let { it1 -> list.add(it1) }
                    }

                    adapter?.notifyDataSetChanged()
                }
                override fun onCancel() {}
            })
    }


    private fun confirmUpload(id: String) {
        Toast.makeText(this, "1", 1).show()
    }
    private fun cancelUpload(id: String) {
        Toast.makeText(this, "2", 1).show()
    }
    private fun fixDone(id: String) {
        Toast.makeText(this, "3", 1).show()
    }

}
