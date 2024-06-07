package com.cookiehunterrr.profilelookup.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel


class HomeViewModel : ViewModel() {
    fun onCopyButtonClicked(context: Context, url: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied text", url)
        clipboard!!.setPrimaryClip(clip)
    }
}