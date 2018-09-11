package ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindDrawable
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.google.android.material.button.MaterialButton
import net.cachapa.expandablelayout.ExpandableLayout
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.domain.entity.GetRepositoryInfoQuery
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.activity.ConductorActivity
import ru.grakhell.userviewer.ui.common.view.BaseView
import ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.presenter.RepositoryInfoPresenter
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@FragmentScope
class RepositoryInfoFragment @Inject constructor()
    : BaseView<RepositoryInfoPresenter>(), RepositoryInfoView {

    @BindView(R.id.tvName) lateinit var mName: TextView
    @BindView(R.id.tvFork) lateinit var mFork: TextView
    @BindView(R.id.tvDesc) lateinit var mDesc: TextView
    @BindView(R.id.tvCreatedAt) lateinit var mCreatedAt:TextView
    @BindView(R.id.tvForkCount) lateinit var mForkCount:TextView
    @BindView(R.id.tvResourcePath) lateinit var mResourcePath: TextView

    @BindView(R.id.languagesExpView) lateinit var mLangExpList: ExpandableLayout
    @BindView(R.id.branchesExpView) lateinit var mBranchesExpList: ExpandableLayout
    @BindView(R.id.watchersExpView) lateinit var mWatchersExpList: ExpandableLayout
    @BindView(R.id.stargazersExpView) lateinit var mStargazersExpList: ExpandableLayout

    @BindView(R.id.tvLanguages) lateinit var mLang: TextView
    @BindView(R.id.tvBranches) lateinit var mBranches: TextView
    @BindView(R.id.tvWatchers) lateinit var mWatchers: TextView
    @BindView(R.id.tvStargazers) lateinit var mStargazers: TextView

    @BindView(R.id.btnLanguages) lateinit var btnLang: MaterialButton
    @BindView(R.id.btnBranches) lateinit var btnBranches: MaterialButton
    @BindView(R.id.btnWatchers) lateinit var btnWatchers: MaterialButton
    @BindView(R.id.btnStargazers) lateinit var btnStargazers: MaterialButton

    @BindView(R.id.languagesList) lateinit var rvLang: RecyclerView
    @BindView(R.id.branchesList) lateinit var rvBranches: RecyclerView
    @BindView(R.id.watchersList) lateinit var rvWatchers: RecyclerView
    @BindView(R.id.stargazersList) lateinit var rvStargazers: RecyclerView

    @BindDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24px) lateinit var downArrow: Drawable
    @BindDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24px) lateinit var upArrow: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            presenter.setRepositoryInfo(it.getString("OWNER"),it.getString("REPOSITORY"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_repository_info, container, false)
        ButterKnife.bind(this,view)
        return view
    }

    override fun showRepositoryInfo(repository: GetRepositoryInfoQuery.Repository?) {
        mName.text = repository?.name()?:resources.getString(R.string.emptyname)
        mDesc.text = repository?.description()?:resources.getString(R.string.emptydesc)

        if (repository?.createdAt() != null) {
            val format = SimpleDateFormat("dd MMMM yyyy HH:mm",getCurrentLocale())
            mCreatedAt.text = String.format(resources.getString(R.string.RepoCreatedAt),
                format.format(repository.createdAt()).toString())
        } else {
            mCreatedAt.text = resources.getString(R.string.emptydate)
        }

        if(repository?.resourcePath() != null) {
            mResourcePath.text = String.format(resources.getString(R.string.RepoResPath),
                repository.resourcePath())
        } else {
            mResourcePath.text = resources.getString(R.string.emptypath)
        }

        if (checkNotNull(repository?.isFork)) {
            mFork.text = String.format(resources.getString(R.string.RepoForkedAt),
                repository?.parent()?.name())
        } else {
            mFork.text = resources.getString(R.string.empty)
        }
        if (checkNotNull(repository?.forkCount())>0) {
            mForkCount.text = String.format(resources.getString(R.string.RepoForkCount),repository?.forkCount())
        } else {
            mForkCount.text = resources.getString(R.string.RepoNoForks)
        }

        if (checkNotNull(repository?.languages()?.totalCount())>0) {
            mLang.text = String.format(resources.getString(R.string.RepoLangs),
                repository?.languages()?.totalCount(),
                repository?.primaryLanguage()?.name())
        } else {
            mLang.text = resources.getString(R.string.RepoNoLangs)
            btnLang.isEnabled = false
            btnLang.icon.alpha = 50
        }
        if (checkNotNull(repository?.refs()?.totalCount())>0) {
            mBranches.text = String.format(resources.getString(R.string.RepoBranches),
                repository?.refs()?.totalCount(),
                repository?.defaultBranchRef()?.name())
        } else {
            mBranches.text = resources.getString(R.string.RepoNoBranches)
            btnBranches.isEnabled = false
            btnBranches.icon.alpha = 50
        }
        if (checkNotNull(repository?.watchers()?.totalCount())>0) {
            mWatchers.text = String.format(resources.getString(R.string.RepoWatchers),
                repository?.watchers()?.totalCount())
        } else {
            mWatchers.text = resources.getString(R.string.RepoNoWatchers)
            btnWatchers.isEnabled = false
            btnWatchers.icon.alpha = 50
        }
        if (checkNotNull(repository?.stargazers()?.totalCount())>0) {
            mStargazers.text = String.format(resources.getString(R.string.RepoStargazers),
                repository?.stargazers()?.totalCount())
        } else {
            mStargazers.text = resources.getString(R.string.RepoNoStargazers)
            btnStargazers.isEnabled = false
            btnStargazers.icon.alpha = 50
        }
        rvLang.layoutManager = LinearLayoutManager(this.context)
        rvBranches.layoutManager = LinearLayoutManager(this.context)
        rvWatchers.layoutManager = LinearLayoutManager(this.context)
        rvStargazers.layoutManager = LinearLayoutManager(this.context)
    }

    @OnClick(R.id.btnLanguages)
    fun languagesOnClick(button: MaterialButton) {
        Answers.getInstance().logCustom(
            CustomEvent("On Expandable List Click")
                .putCustomAttribute("Click","Languages List"))
        if(mLangExpList.isExpanded) {
            button.icon = downArrow
            mLangExpList.collapse(true)
        } else {
            button.icon = upArrow
            mLangExpList.expand(true)
            if (mBranchesExpList.isExpanded){
            btnBranches.icon = downArrow
            mBranchesExpList.collapse(true)}
            if (mWatchersExpList.isExpanded){
            btnWatchers.icon = downArrow
            mWatchersExpList.collapse(true)}
            if (mStargazersExpList.isExpanded){
            btnStargazers.icon = downArrow
            mStargazersExpList.collapse(true)}
        }
    }

    @OnClick(R.id.btnBranches)
    fun branchesOnClick(button: MaterialButton) {
        Answers.getInstance().logCustom(
            CustomEvent("On Expandable List Click")
                .putCustomAttribute("Click","Branches List"))
        if(mBranchesExpList.isExpanded) {
            button.icon = downArrow
            mBranchesExpList.collapse(true)
        } else {
            button.icon = upArrow
            mBranchesExpList.expand(true)
            if (mLangExpList.isExpanded){
            btnLang.icon = downArrow
            mLangExpList.collapse(true)}
            if (mWatchersExpList.isExpanded){
            btnWatchers.icon = downArrow
            mWatchersExpList.collapse(true)}
            if (mStargazersExpList.isExpanded){
            btnStargazers.icon = downArrow
            mStargazersExpList.collapse(true)}
        }
    }

    @OnClick(R.id.btnWatchers)
    fun watchersOnClick(button: MaterialButton) {
        Answers.getInstance().logCustom(
            CustomEvent("On Expandable List Click")
                .putCustomAttribute("Click","Watchers List"))
        if(mWatchersExpList.isExpanded) {
            button.icon = downArrow
            mWatchersExpList.collapse(true)
        } else {
            button.icon = upArrow
            mWatchersExpList.expand(true)
            if (mLangExpList.isExpanded){
            btnLang.icon = downArrow
            mLangExpList.collapse(true)}
            if (mBranchesExpList.isExpanded){
            btnBranches.icon = downArrow
            mBranchesExpList.collapse(true)}
            if (mStargazersExpList.isExpanded){
            btnStargazers.icon = downArrow
            mStargazersExpList.collapse(true)}
        }
    }

    @OnClick(R.id.btnStargazers)
    fun stargazersOnClick(button: MaterialButton) {
        Answers.getInstance().logCustom(
            CustomEvent("On Expandable List Click")
                .putCustomAttribute("Click","Stargazers List"))
        if(mStargazersExpList.isExpanded) {
            button.icon = downArrow
            mStargazersExpList.collapse(true)
        } else {
            button.icon = upArrow
            mStargazersExpList.expand(true)
            if (mLangExpList.isExpanded){
            btnLang.icon = downArrow
            mLangExpList.collapse(true)}
            if (mBranchesExpList.isExpanded){
            btnBranches.icon = downArrow
            mBranchesExpList.collapse(true)}
            if (mWatchersExpList.isExpanded){
            mWatchersExpList.collapse(true)
            btnWatchers.icon = downArrow}

        }
    }

    override fun getLanguagesRecyclerView() = rvLang
    override fun getBranchesRecyclerView() = rvBranches
    override fun getWatchersRecyclerView() = rvWatchers
    override fun getStargazersRecyclerView() = rvStargazers

    override fun getParentActivity(): ConductorActivity {
        return activity as ConductorActivity
    }

    override fun getBaseView(): View {
        return checkNotNull(activity?.findViewById(R.id.mainLayout))
    }

    private fun getCurrentLocale():Locale? {
        val locale:Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            locale = checkNotNull(context?.resources?.configuration?.locales?.get(0))
        } else{
            locale = checkNotNull(context?.resources?.configuration?.locale)
        }
        return locale
    }

}
