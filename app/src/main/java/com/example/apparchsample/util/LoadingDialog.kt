package com.example.apparchsample.util

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.example.apparchsample.R

class LoadingDialog(context:Context) {
    private val dialog = Dialog(context)

    fun show() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.show()
    }

    fun dismiss(){
        dialog.dismiss()
    }

}