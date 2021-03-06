package ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.domain.entity.GetLanguageInfoQuery

class RepositoryInfoLanguageRecyclerViewAdapter : PagedListAdapter<GetLanguageInfoQuery.Node,
    RepositoryInfoLanguageRecyclerViewAdapter.ViewHolder>(CALLBACK()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_repository_info_language_and_branch, parent, false)
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

        fun bind(item: GetLanguageInfoQuery.Node?) {
            mName.text = item?.name() ?: itemView.resources.getString(R.string.emptyname)
        }
    }

    class CALLBACK : DiffUtil.ItemCallback<GetLanguageInfoQuery.Node>() {
        override fun areContentsTheSame(
            oldItem: GetLanguageInfoQuery.Node,
            newItem: GetLanguageInfoQuery.Node
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: GetLanguageInfoQuery.Node,
            newItem: GetLanguageInfoQuery.Node
        ): Boolean {
            return oldItem.id() == newItem.id()
        }
    }
}