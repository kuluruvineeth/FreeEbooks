package com.kuluruvineeth.freeebooks.utils

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kuluruvineeth.freeebooks.MainActivity


fun Context.getActivity(): AppCompatActivity? = when (this){
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun String.toToast(context: Context, length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(context,this,length).show()
}