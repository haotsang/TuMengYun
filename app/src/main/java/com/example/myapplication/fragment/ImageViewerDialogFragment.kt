package com.example.myapplication.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.R

class ImageViewerDialogFragment : DialogFragment() {

    companion object {
        fun show(target: AppCompatActivity, uri: String) {
            val fragment = ImageViewerDialogFragment()
            val bundle = Bundle()
            bundle.putString("uri", uri)
            fragment.arguments = bundle

            fragment.show(target.supportFragmentManager, ImageViewerDialogFragment::class.java.toString())
        }

        fun show(target: Fragment, uri: String) {
            val fragment = ImageViewerDialogFragment()
            val bundle = Bundle()
            bundle.putString("uri", uri)
            fragment.arguments = bundle

            fragment.show(target.childFragmentManager, ImageViewerDialogFragment::class.java.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_image_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageViewer2 = view.findViewById<com.luck.picture.lib.photoview.PhotoView>(R.id.image_viewer2)
        Glide.with(imageViewer2.context).load(requireArguments().getString("uri")).into(imageViewer2)
        imageViewer2.setOnViewTapListener { v, x, y ->
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
//        val lp: WindowManager.LayoutParams? = dialog?.window?.attributes
//        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
//        lp?.height = WindowManager.LayoutParams.MATCH_PARENT
//        dialog?.window?.attributes = lp
//
//        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

}
