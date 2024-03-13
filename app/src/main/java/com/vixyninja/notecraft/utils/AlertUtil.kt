package com.vixyninja.notecraft.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

class AlertUtil(private val context: Context) {

    fun showError(title: String, message: String, callback: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(android.R.string.ok) { dialog, _ ->
                callback().run {
                    dialog.dismiss()
                }
            }
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()

    }

    fun showBar(
        view: View,
        message: String,
        type: AlertEnum = AlertEnum.INFO,
        callback: () -> Unit? = {}
    ) {
        val bar: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        when (type) {
            AlertEnum.ERROR -> bar.setBackgroundTint(context.getColor(android.R.color.holo_red_light))
            AlertEnum.WARNING -> bar.setBackgroundTint(context.getColor(android.R.color.holo_green_light))
            AlertEnum.INFO -> bar.setBackgroundTint(context.getColor(android.R.color.holo_blue_light))
        }
        bar.setAction("Dismiss") {
            callback()
            bar.dismiss()
        }
        bar.show()
    }
}


