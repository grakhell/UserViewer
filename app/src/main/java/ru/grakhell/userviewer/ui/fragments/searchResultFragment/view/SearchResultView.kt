package ru.grakhell.userviewer.ui.fragments.searchResultFragment.view

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.grakhell.userviewer.ui.activity.view.ConductorActivity
import ru.grakhell.userviewer.ui.common.view.MVP

interface SearchResultView : MVP {
    fun getRecyclerView(): RecyclerView
    fun getRefreshLayout(): SwipeRefreshLayout
    fun getParentActivity(): ConductorActivity
}