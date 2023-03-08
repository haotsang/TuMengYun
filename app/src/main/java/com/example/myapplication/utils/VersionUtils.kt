package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.example.myapplication.http.api.VersionApi
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL


object VersionUtils {

    @Throws(Exception::class)
    fun getNewVersion(): String {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(VersionApi::class.java).getNewVersion()
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return "-1"
    }


    fun getVersionName(context: Context): String {
        var verName = ""
        try {
            val pi = context.packageManager.getPackageInfo(context.packageName, 0)
            if (pi != null) {
                verName = pi.versionName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }


    @Throws(Exception::class)
    fun download(context: Context): String {
        val url = "http://106.15.94.206:8081/img/app-release.apk"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "app-release.apk")
        if (file.exists()) {
            file.delete()
        } else {
            file.createNewFile()
        }

        val inUri = URL(url)
        val inputStream = inUri.openStream()

        var length: Int
        val bytes = ByteArray(4096)

        val outputStream = FileOutputStream(file)
        while (inputStream.read(bytes).also { length = it } != -1) {
            outputStream.write(bytes, 0, length)
        }
        outputStream.flush()

        return file.absolutePath
    }

    @Throws(Exception::class)
    fun okDownload(context: Context): String {
        val url = "http://106.15.94.206:8081/img/app-release.apk"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "app-release.apk")

        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()

        val inputStream: InputStream = response.body?.byteStream() ?: return ""
        //文件的总长度
        val max: Long = response.body?.contentLength() ?: 0

        var progess = 0

        //当文件不存在，创建出来
        if (!file.exists()) {
            file.createNewFile()
        }
        val fileOutputStream = FileOutputStream(file)
        val bytes = ByteArray(1024)
        var readLength = 0
        var cureeLength: Long = 0
        while (inputStream.read(bytes).also { readLength = it } != -1) {
            fileOutputStream.write(bytes, 0, readLength)
            cureeLength += readLength.toLong()
            progess = (cureeLength * 100 / max).toInt()

            println(progess)
        }
        inputStream.close()
        fileOutputStream.close()

        return file.absolutePath
    }

    fun install(context: Context, file: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !context.packageManager.canRequestPackageInstalls()) {
            val packageURI = Uri.parse("package:" + context.packageName)
            context.startActivity(
                Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
            )
        } else {
            installApkFile(context, file)
        }
    }

    fun installApkFile(context: Context?, file: File?) {
        if (context == null || file == null) {
            return
        }

        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        intent.setDataAndType(toUri(context, file), "application/vnd.android.package-archive")
        context.startActivity(intent)
    }


    private fun installApk(context: Context?, apkUri: Uri?) {
        if (context == null || apkUri == null) {
            return
        }

        val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val uri = if (apkUri.scheme == "file") {
            toUri(context, apkUri.toFile())
        } else {
            apkUri
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        context.startActivity(intent)
    }


    private fun toUri(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, context.packageName + ".fileProvider", file)
        } else {
            Uri.fromFile(file)
        }
    }
}