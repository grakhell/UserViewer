package ru.grakhell.userviewer.ui.fragments.userInfoFragment.presenter

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
import ru.grakhell.userviewer.domain.entity.GetUserRepoInfoQuery
import ru.grakhell.userviewer.domain.entity.GetUserStarredRepoInfoQuery
import ru.grakhell.userviewer.domain.usecase.GetUserInfo
import ru.grakhell.userviewer.domain.usecase.GetUserOrganisationInfo
import ru.grakhell.userviewer.domain.usecase.GetUserRepoInfo
import ru.grakhell.userviewer.domain.usecase.GetUserStarredRepoInfo
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.common.presenter.BasePresenter
import ru.grakhell.userviewer.ui.fragments.userInfoFragment.view.UserInfoOrganisationRecyclerViewAdapter
import ru.grakhell.userviewer.ui.fragments.userInfoFragment.view.UserInfoRepositoriesRecyclerViewAdapter
import ru.grakhell.userviewer.ui.fragments.userInfoFragment.view.UserInfoStarredReposRecyclerViewAdapter
import ru.grakhell.userviewer.ui.fragments.userInfoFragment.view.UserInfoView
import ru.grakhell.userviewer.util.NetworkUtil
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber
import javax.inject.Inject

@FragmentScope
class UserInfoPresenterImpl @Inject constructor(
    private val mUserInfoSource: GetUserInfo,
    private val mRepoSource: GetUserRepoInfo,
    private val mStarredRepoSource: GetUserStarredRepoInfo,
    private val mOrgSource: GetUserOrganisationInfo,
    view: UserInfoView?
) : BasePresenter<UserInfoView>(view), UserInfoPresenter {

    private var disposable: CompositeDisposable = CompositeDisposable()
    private var params: QueryParams = QueryParams()

    override fun setUserName(user: String?) {
        params.userName = checkNotNull(user)
    }

    private fun getUserInfo() {
        try {
            if (!NetworkUtil.isNetworkConnected(
                    checkNotNull(mView?.getParentActivity()?.baseContext))
            ) throw NetworkErrorException()
            disposable.add(
                mUserInfoSource.execute(params).subscribe(
                    { item ->
                        mView?.showData(checkNotNull(item))
                        runBlocking {
                            if (checkNotNull(item?.organizations()?.totalCount()) > 0) {
                                setOrganisationsList().await() }
                            if (checkNotNull(item?.repositories()?.totalCount()) > 0) {
                                setRepositoriesList().await() }
                            if (checkNotNull(item?.starredRepositories()?.totalCount()) > 0) {
                                setStarredRepoList().await() }
                        }
                    },
                    { ex -> Timber.e(ex)
                        Snackbar.make(checkNotNull(mView?.getBaseView()), ex.localizedMessage,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.cancel_string) {
                                mView?.getParentActivity()?.getNavController()?.popBackStack()
                            }
                            .setActionTextColor(Color.WHITE)
                            .show() })
            )
        } catch (e: Exception) {
            Timber.e(e)
            Crashlytics.logException(e)
            Snackbar.make(checkNotNull(mView?.getBaseView()), e.localizedMessage,
                Snackbar.LENGTH_LONG)
                .setDuration(6000)
                .setAction(R.string.retry_string) { getUserInfo() }
                .show()
        }
    }

    private fun setOrganisationsList() = GlobalScope.async {
        val adapter = UserInfoOrganisationRecyclerViewAdapter()
        mView?.getOrganisationRView()?.swapAdapter(adapter, true)
        disposable.add(
            mOrgSource.execute(params).subscribe(
                { list ->
                    adapter.submitList(list)
                    adapter.notifyDataSetChanged()
                },
                { ex -> Timber.e(ex)
                    Crashlytics.logException(ex) })
        ) }

    private fun setRepositoriesList() = GlobalScope.async {
        val adapter = UserInfoRepositoriesRecyclerViewAdapter()
        mView?.getRepositoriesRView()?.swapAdapter(adapter, true)
        disposable.add(
            mRepoSource.execute(params).subscribe(
                { list ->
                    adapter.submitList(list)
                    adapter.getClickedItems().subscribe(
                        { item -> onRepoClickListener(item) },
                        { ex -> Timber.e(ex) })
                    adapter.notifyDataSetChanged()
                    },
                { ex -> Timber.e(ex)
                    Crashlytics.logException(ex) })
        ) }

    private fun setStarredRepoList() = GlobalScope.async {
        val adapter = UserInfoStarredReposRecyclerViewAdapter()
        mView?.getStarredReposRView()?.swapAdapter(adapter, true)
        disposable.add(
            mStarredRepoSource.execute(params).subscribe(
                { list ->
                    adapter.submitList(list)
                    adapter.getClickedItems().subscribe(
                        { item -> onRepoClickListener(item) },
                        { ex -> Timber.e(ex) })
                    adapter.notifyDataSetChanged()
                },
                { ex -> Timber.e(ex)
                    Crashlytics.logException(ex) })
        ) }

    private fun onRepoClickListener(repository: GetUserRepoInfoQuery.Node) {
        mView?.getParentActivity()?.showRepository(params.userName, repository.name())
    }

    private fun onRepoClickListener(repository: GetUserStarredRepoInfoQuery.Node) {
        mView?.getParentActivity()?.showRepository(repository.owner().login(), repository.name())
    }

    override fun onStart(savedInstanceState: Bundle?) {
        if (params.userName.isNotEmpty()) getUserInfo()
        super.onStart(savedInstanceState)
    }

    override fun onEnd() {
        RxUtil.composedDispose(disposable)
        super.onEnd()
    }
}