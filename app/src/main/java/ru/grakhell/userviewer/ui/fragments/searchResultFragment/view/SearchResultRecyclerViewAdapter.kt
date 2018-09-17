package ru.grakhell.userviewer.ui.fragments.searchResultFragment.view

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
import io.reactivex.subjects.BehaviorSubject
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.domain.entity.GetUserSearchResultQuery
import ru.grakhell.userviewer.injection.module.GlideApp

class SearchResultRecyclerViewAdapter : PagedListAdapter<GetUserSearchResultQuery.AsUser,
    SearchResultRecyclerViewAdapter.ViewHolder>
    (CALLBACK()) {

    private val publisher: BehaviorSubject<GetUserSearchResultQuery.AsUser> = BehaviorSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_searchresult, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        val item = getItem(position)
        with(holder.mView) {
            setOnClickListener {
                if (item != null)publisher.onNext(item)
            }
        }
    }

    fun getClickedItems() = publisher

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        init {
            ButterKnife.bind(this, mView)
        }
        @BindView(R.id.tvLogin) lateinit var mLogin: TextView
        @BindView(R.id.tvName) lateinit var mName: TextView
        @BindView(R.id.tvRepo) lateinit var mRepositories: TextView
        @BindView(R.id.starCount) lateinit var mStarCount: TextView
        @BindView(R.id.ivAvatar) lateinit var mAvatar: ImageView

        fun bind(item: GetUserSearchResultQuery.AsUser?) {
            if (item?.login() == null) {
                mLogin.text = itemView.resources.getString(R.string.UserNoLogin)
            } else {
                mLogin.text = String.format(itemView.resources.getString(R.string.UserLogin),
                    item.login())
            }
            if (item?.name() == null) {
                mName.text = itemView.resources.getString(R.string.UserNoName)
            } else {
                mName.text = String.format(itemView.resources.getString(R.string.UserName),
                item.name())
            }
            mRepositories.text = String.format(itemView.resources.getString(R.string.UserRepositories),
                item?.repositories()?.totalCount())

            mStarCount.text = item?.starredRepositories()?.totalCount().toString()
            GlideApp.with(mView)
                .load(item?.avatarUrl())
                .into(mAvatar)
        }
    }

    class CALLBACK : DiffUtil.ItemCallback<GetUserSearchResultQuery.AsUser>() {

        override fun areContentsTheSame(
            oldItem: GetUserSearchResultQuery.AsUser,
            newItem: GetUserSearchResultQuery.AsUser
        ): Boolean {
            return oldItem == newItem
        }
        override fun areItemsTheSame(
            oldItem: GetUserSearchResultQuery.AsUser,
            newItem: GetUserSearchResultQuery.AsUser
        ): Boolean = oldItem.id() == newItem.id()
    }
}
