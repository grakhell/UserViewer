package ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.domain.entity.GetStargazersInfoQuery
import ru.grakhell.userviewer.injection.module.GlideApp

class RepositoryInfoStargazerRecyclerViewAdapter : PagedListAdapter<GetStargazersInfoQuery.Node,
    RepositoryInfoStargazerRecyclerViewAdapter.ViewHolder>(CALLBACK()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_repository_info_watcher_and_stargazer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        init {
            ButterKnife.bind(this, mView)
        }

        @BindView(R.id.tvName) lateinit var mName: TextView
        @BindView(R.id.tvLogin) lateinit var mLogin: TextView
        @BindView(R.id.ivAvatar) lateinit var mAvatar: ImageView

        fun bind(item: GetStargazersInfoQuery.Node?) {
            mLogin.text = item?.login() ?: itemView.resources.getString(R.string.emptylogin)
            mName.text = item?.name() ?: itemView.resources.getString(R.string.emptyname)
            GlideApp.with(mView)
                .load(item?.avatarUrl())
                .into(mAvatar)
        }
    }

    class CALLBACK : DiffUtil.ItemCallback<GetStargazersInfoQuery.Node>() {
        override fun areContentsTheSame(
            oldItem: GetStargazersInfoQuery.Node,
            newItem: GetStargazersInfoQuery.Node
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: GetStargazersInfoQuery.Node,
            newItem: GetStargazersInfoQuery.Node
        ): Boolean {
            return oldItem.id() == newItem.id()
        }
    }
}