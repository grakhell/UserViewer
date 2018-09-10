package ru.grakhell.userviewer.ui.fragments.userInfoFragment.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.subjects.BehaviorSubject
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.domain.entity.GetUserStarredRepoInfoQuery

class UserInfoStarredReposRecyclerViewAdapter: PagedListAdapter<GetUserStarredRepoInfoQuery.Node,
    UserInfoStarredReposRecyclerViewAdapter.ViewHolder>(CALLBACK()) {

    private val publisher: BehaviorSubject<GetUserStarredRepoInfoQuery.Node> = BehaviorSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_user_info_starredrepository, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        with(holder.mView) {
            setOnClickListener {
                if (item!=null)publisher.onNext(item)
            }
        }
    }

    fun getClickedItems() = publisher

    inner class ViewHolder(val mView: View):RecyclerView.ViewHolder(mView) {
        init {
            ButterKnife.bind(this, mView)
        }
        @BindView(R.id.tvName) lateinit var mName:TextView
        @BindView(R.id.tvDesc) lateinit var mDesc:TextView
        @BindView(R.id.tvOwner) lateinit var mOwner:TextView

        fun bind(item: GetUserStarredRepoInfoQuery.Node?) {
            mName.text = item?.name()?:itemView.resources.getString(R.string.emptyname)
            mDesc.text = item?.description()?:itemView.resources.getString(R.string.emptydesc)
            mOwner.text = item?.owner()?.login()?:itemView.resources.getString(R.string.emptyowner)
        }
    }

    class CALLBACK: DiffUtil.ItemCallback<GetUserStarredRepoInfoQuery.Node>() {

        override fun areContentsTheSame(
            oldItem: GetUserStarredRepoInfoQuery.Node,
            newItem: GetUserStarredRepoInfoQuery.Node
        ): Boolean {
            return oldItem == newItem
        }
        override fun areItemsTheSame(
            oldItem: GetUserStarredRepoInfoQuery.Node,
            newItem: GetUserStarredRepoInfoQuery.Node
        ): Boolean  = oldItem.id() == newItem.id()
    }
}