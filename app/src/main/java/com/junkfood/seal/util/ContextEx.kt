package com.junkfood.seal.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
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

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}
