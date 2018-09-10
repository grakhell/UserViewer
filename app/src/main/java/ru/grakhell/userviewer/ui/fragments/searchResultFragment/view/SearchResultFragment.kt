package ru.grakhell.userviewer.ui.fragments.searchResultFragment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.activity.ConductorActivity
import ru.grakhell.userviewer.ui.common.view.BaseView
import ru.grakhell.userviewer.ui.fragments.searchResultFragment.presenter.SearchResultPresenter
import javax.inject.Inject

@FragmentScope
class SearchResultFragment @Inject constructor(): BaseView<SearchResultPresenter>(),
    SearchResultView {
    @BindView(R.id.list) lateinit var mUserListView: RecyclerView
    @BindView(R.id.refreshLayout) lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val str = arguments?.getString("USER_NAME")
        if(str!=null) presenter.setNameToSearch(str)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_searchresult_list, container, false)
        ButterKnife.bind(this,view)
        mSwipeRefreshLayout.setOnRefreshListener(presenter)
        mUserListView.layoutManager =
            GridLayoutManager(this.context, 3)
        mSwipeRefreshLayout.isRefreshing = true
        presenter.updateData()
        return view
    }

    override fun getRecyclerView() = mUserListView
    override fun getRefreshLayout(): SwipeRefreshLayout = mSwipeRefreshLayout

    override fun getParentActivity(): ConductorActivity {
        return activity as ConductorActivity
    }


    fun isActive(): Boolean = isAdded
}
