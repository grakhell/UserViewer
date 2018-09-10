package ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.grakhell.userviewer.domain.entity.GetRepositoryInfoQuery
import ru.grakhell.userviewer.ui.activity.ConductorActivity
import ru.grakhell.userviewer.ui.common.view.MVP

interface RepositoryInfoView:MVP {
    fun showRepositoryInfo(repository: GetRepositoryInfoQuery.Repository?)
    fun getLanguagesRecyclerView(): RecyclerView
    fun getBranchesRecyclerView(): RecyclerView
    fun getWatchersRecyclerView(): RecyclerView
    fun getStargazersRecyclerView(): RecyclerView
    fun getParentActivity(): ConductorActivity
    fun getBaseView():View
}