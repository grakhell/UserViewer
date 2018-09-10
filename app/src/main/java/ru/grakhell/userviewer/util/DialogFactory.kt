package ru.grakhell.userviewer.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes
import ru.grakhell.userviewer.R

class DialogFactory{
    companion object {

        private fun createGenericErrorDialog(context:Context, title:String, message:String):Dialog{
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null)
            return alertDialog.create()
        }

        fun createSimpleErrorDialog(context: Context, message: String): Dialog {
            return  createGenericErrorDialog(context,
                context.getString(R.string.dialog_title_error),
                message)
        }

        fun createGenericErrorDialog(context: Context,
                                     @StringRes titleRes:Int,
                                     @StringRes messageRes:Int):Dialog{
            return  createGenericErrorDialog(context,
                context.getString(titleRes),
                context.getString(messageRes))
        }

    }
}