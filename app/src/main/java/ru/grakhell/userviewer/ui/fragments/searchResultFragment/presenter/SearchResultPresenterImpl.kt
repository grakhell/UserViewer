package ru.grakhell.userviewer.ui.fragments.searchResultFragment.presenter

import android.os.Bundle
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.launch
import ru.grakhell.userviewer.domain.QueryParams
import ru.grakhell.userviewer.domain.entity.GetUserSearchResultQuery
import ru.grakhell.userviewer.domain.usecase.GetUserSearchResult
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.common.presenter.BasePresenter
import ru.grakhell.userviewer.ui.fragments.searchResultFragment.view.SearchResultRecyclerViewAdapter
import ru.grakhell.userviewer.ui.fragments.searchResultFragment.view.SearchResultView
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber
import javax.inject.Inject

@FragmentScope
class SearchResultPresenterImpl @Inject constructor(
    private val mSource: GetUserSearchResult,
    view: SearchResultView?
) : BasePresenter<SearchResultView>(view),
    SearchResultPresenter {

    private lateinit var disposable: Disposable

    private var params = QueryParams()

    override fun setNameToSearch(value: String) { params.userName = value }

    override fun updateData() {
        launch {
            disposable = mSource.execute(params).subscribe({ items ->
                kotlin.run {
                    val adapter = SearchResultRecyclerViewAdapter()
                    adapter.getClickedItems().subscribe { it -> onListFragmentInteraction(it) }
                    mView?.getRecyclerView()?.swapAdapter(adapter, true)
                    adapter.submitList(items)
                    adapter.notifyDataSetChanged()
                    mView?.getRefreshLayout()?.isRefreshing = false
                }
            },
                { ex -> Timber.e(ex) })
        }
    }

    override fun onRefresh() {
        updateData()
    }

    override fun onEnd() {
        RxUtil.dispose(disposable)
        super.onEnd()
    }

    override fun onStart(savedInstanceState: Bundle?) {
        mView?.getParentActivity()?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onStart(savedInstanceState)
    }

    private fun onListFragmentInteraction(item: GetUserSearchResultQuery.AsUser?) {
        mView?.getParentActivity()?.showUser(checkNotNull(item?.login()))
    }
}