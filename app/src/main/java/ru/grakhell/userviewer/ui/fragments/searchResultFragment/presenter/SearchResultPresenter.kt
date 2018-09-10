package ru.grakhell.userviewer.ui.fragments.searchResultFragment.presenter

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.grakhell.userviewer.ui.common.presenter.Presenter

interface SearchResultPresenter: Presenter, SwipeRefreshLayout.OnRefreshListener {

    fun setNameToSearch(value:String)
    fun updateData()
}