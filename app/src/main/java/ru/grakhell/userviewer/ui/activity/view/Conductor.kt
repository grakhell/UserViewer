package ru.grakhell.userviewer.ui.activity.view

import android.view.View
import ru.grakhell.userviewer.ui.common.Activity

interface Conductor : Activity {
    fun showMenuItems(flag: Boolean)
    fun getView(): View
}