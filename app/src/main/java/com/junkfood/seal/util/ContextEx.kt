package com.junkfood.seal.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.junkfood.seal.R

fun dialogLoading(context: Context): AlertDialog {
    val mView = LayoutInflater.from(context).inflate(R.layout.layout_loading, null)
    mView.findViewById<LottieAnimationView>(R.id.viewAnimation)
    return AlertDialog.Builder(context)
        .setView(mView)
        .setCancelable(false)
        .setOnDismissListener {
            it.dismiss()
        }
        .create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
}

fun Context.getColorCompat(id: Int): Int = ContextCompat.getColor(this, id)
