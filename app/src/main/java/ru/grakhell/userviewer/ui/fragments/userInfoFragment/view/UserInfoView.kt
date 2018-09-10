package ru.grakhell.userviewer.ui.fragments.userInfoFragment.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.grakhell.userviewer.domain.entity.GetUserInfoQuery
import ru.grakhell.userviewer.ui.activity.ConductorActivity
import ru.grakhell.userviewer.ui.common.view.MVP

interface UserInfoView:MVP {
    fun showData(user:GetUserInfoQuery.User)
    fun getOrganisationRView():RecyclerView
    fun getStarredReposRView():RecyclerView
    fun getRepositoriesRView():RecyclerView
    fun getParentActivity():ConductorActivity
    fun getBaseView(): View
}