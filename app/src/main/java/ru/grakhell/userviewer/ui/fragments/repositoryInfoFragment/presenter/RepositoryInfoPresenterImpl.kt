package ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.presenter

import android.accounts.NetworkErrorException
import android.graphics.Color
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.domain.QueryParams
import ru.grakhell.userviewer.domain.usecase.GetBranchesInfo
import ru.grakhell.userviewer.domain.usecase.GetLanguageInfo
import ru.grakhell.userviewer.domain.usecase.GetRepositoryInfo
import ru.grakhell.userviewer.domain.usecase.GetStargazersInfo
import ru.grakhell.userviewer.domain.usecase.GetWatchersInfo
import ru.grakhell.userviewer.ui.common.presenter.BasePresenter
import ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view.RepositoryInfoBranchRecyclerViewAdapter
import ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view.RepositoryInfoLanguageRecyclerViewAdapter
import ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view.RepositoryInfoStargazerRecyclerViewAdapter
import ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view.RepositoryInfoView
import ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view.RepositoryInfoWatcherRecyclerViewAdapter
import ru.grakhell.userviewer.util.NetworkUtil
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber
import javax.inject.Inject

class RepositoryInfoPresenterImpl @Inject constructor(
    private val mRepoSource: GetRepositoryInfo,
    private val mLangSource: GetLanguageInfo,
    private val mBranchesSource: GetBranchesInfo,
    private val mWatchersSource: GetWatchersInfo,
    private val mStargazersSource: GetStargazersInfo,
    view: RepositoryInfoView
) : BasePresenter<RepositoryInfoView>(view), RepositoryInfoPresenter {

    private var disposable: CompositeDisposable = CompositeDisposable()
    private var params: QueryParams = QueryParams()

    override fun setRepositoryInfo(ownerName: String?, repositoryName: String?) {
        params.userName = checkNotNull(ownerName)
        params.repositoryName = checkNotNull(repositoryName)
    }

    private fun getRepositoryInfo() {
        try {
            if (!NetworkUtil.isNetworkConnected(checkNotNull(mView?.getParentActivity()
                    ?.baseContext))) throw NetworkErrorException()
            disposable.add(
                mRepoSource.execute(params).subscribe(
                    { item ->
                        mView?.showRepositoryInfo(item)
                        runBlocking {
                            if (checkNotNull(item?.languages()?.totalCount())>0) {
                                setLanguagesList().await() }
                            if (checkNotNull(item?.refs()?.totalCount())>0) {
                                setBranchesList().await() }
                            if (checkNotNull(item?.watchers()?.totalCount())>0) {
                                setWatchersList().await() }
                            if (checkNotNull((item?.stargazers()?.totalCount()))>0) {
                                setStargazersList().await() }
                        } },
                    { ex -> Timber.e(ex)
                            mView?.getParentActivity()?.getNavController()?.navigateUp()
                            Snackbar.make(checkNotNull(mView?.getBaseView()), ex.localizedMessage,
                                Snackbar.LENGTH_LONG)
                                .setDuration(6000)
                                .setAction(R.string.ok_string) {}
                                .setActionTextColor(Color.WHITE)
                                .show() }))
        } catch (e: Exception) {
            Timber.e(e)
            Crashlytics.logException(e)
            Snackbar.make(checkNotNull(mView?.getBaseView()), e.localizedMessage,
                Snackbar.LENGTH_LONG)
                .setDuration(6000)
                .setAction(R.string.retry_string) { getRepositoryInfo() }
                .show()
        }
    }

    private fun setLanguagesList() = GlobalScope.async {
        val adapter = RepositoryInfoLanguageRecyclerViewAdapter()
        mView?.getLanguagesRecyclerView()?.swapAdapter(adapter, true)
        disposable.add(
            mLangSource.execute(params).subscribe(
                { list ->
                    adapter.submitList(list)
                    adapter.notifyDataSetChanged()
                },
                { ex -> Timber.e(ex)
                    Crashlytics.logException(ex) })
        )
    }

    private fun setBranchesList() = GlobalScope.async {
        val adapter = RepositoryInfoBranchRecyclerViewAdapter()
        mView?.getBranchesRecyclerView()?.swapAdapter(adapter, true)
        disposable.add(mBranchesSource.execute(params).subscribe(
            { list ->
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
            },
            { ex -> Timber.e(ex)
                Crashlytics.logException(ex) }))
    }

    private fun setWatchersList() = async {
        val adapter = RepositoryInfoWatcherRecyclerViewAdapter()
        mView?.getWatchersRecyclerView()?.swapAdapter(adapter, true)
        disposable.add(mWatchersSource.execute(params).subscribe(
            { list ->
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
            },
            { ex -> Timber.e(ex)
                Crashlytics.logException(ex) }))
    }

    private fun setStargazersList() = async {
        val adapter = RepositoryInfoStargazerRecyclerViewAdapter()
        mView?.getStargazersRecyclerView()?.swapAdapter(adapter, true)
        disposable.add(mStargazersSource.execute(params).subscribe(
            { list ->
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
            },
            { ex -> Timber.e(ex)
                Crashlytics.logException(ex) }))
    }

    override fun onStart(savedInstanceState: Bundle?) {
        if (params.userName.isNotEmpty() && params.repositoryName.isNotEmpty())
            getRepositoryInfo()
        super.onStart(savedInstanceState)
    }

    override fun onEnd() {
        RxUtil.composedDispose(disposable)
        super.onEnd()
    }
}