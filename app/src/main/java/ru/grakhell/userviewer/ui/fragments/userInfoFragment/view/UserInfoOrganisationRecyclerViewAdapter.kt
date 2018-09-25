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
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.domain.entity.GetUserOrganisationInfoQuery

class UserInfoOrganisationRecyclerViewAdapter : PagedListAdapter<GetUserOrganisationInfoQuery.Node,
    UserInfoOrganisationRecyclerViewAdapter.ViewHolder>(CALLBACK()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_user_info_organisation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        init {
            ButterKnife.bind(this, mView)
        }
        @BindView(R.id.tvName) lateinit var mName: TextView
        @BindView(R.id.tvDesc) lateinit var mDesc: TextView

        fun bind(item: GetUserOrganisationInfoQuery.Node?) {
            mName.text = item?.name() ?: itemView.resources.getString(R.string.emptyname)
            mDesc.text = item?.description() ?: itemView.resources.getString(R.string.emptydesc)
        }
    }

    class CALLBACK : DiffUtil.ItemCallback<GetUserOrganisationInfoQuery.Node>() {

        override fun areContentsTheSame(
            oldItem: GetUserOrganisationInfoQuery.Node,
            newItem: GetUserOrganisationInfoQuery.Node
        ): Boolean {
            return oldItem == newItem
        }
        override fun areItemsTheSame(
            oldItem: GetUserOrganisationInfoQuery.Node,
            newItem: GetUserOrganisationInfoQuery.Node
        ): Boolean = oldItem.id() == newItem.id()
    }
}