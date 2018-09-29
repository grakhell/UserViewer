package ru.grakhell.userviewer.ui.fragments.userInfoFragment.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import ru.grakhell.userviewer.domain.entity.GetUserInfoQuery
import ru.grakhell.userviewer.injection.module.GlideApp
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.activity.view.ConductorActivity
import ru.grakhell.userviewer.ui.common.view.BaseView
import ru.grakhell.userviewer.ui.fragments.userInfoFragment.presenter.UserInfoPresenter
import javax.inject.Inject

@FragmentScope
class UserInfoFragment @Inject constructor()
    : BaseView<UserInfoPresenter>(), UserInfoView {

    @BindView(R.id.tvLogin) lateinit var mLogin: TextView
    @BindView(R.id.tvName) lateinit var mName: TextView
    @BindView(R.id.ivAvatar) lateinit var mAvatar: ImageView
    @BindView(R.id.tvURL) lateinit var mURL: TextView
    @BindView(R.id.tvEmail) lateinit var mEmail: TextView

    @BindView(R.id.tvOrganisations) lateinit var mOrganisations: TextView
    @BindView(R.id.tvRepo) lateinit var mRepos: TextView
    @BindView(R.id.tvStarredRepo) lateinit var mStarredRepos: TextView

    @BindView(R.id.orgExpView) lateinit var mOrgExpList: ExpandableLayout
    @BindView(R.id.strRepoExpView) lateinit var mStrRepoExpList: ExpandableLayout
    @BindView(R.id.repoExpView) lateinit var mRepoExpList: ExpandableLayout

    @BindView(R.id.btnOrgs) lateinit var btnOrganisations: MaterialButton
    @BindView(R.id.btnRepos) lateinit var btnRepositories: MaterialButton
    @BindView(R.id.btnStRepos) lateinit var btnStarredRepos: MaterialButton

    @BindView(R.id.organisationsList) lateinit var rvOrganisations: RecyclerView
    @BindView(R.id.reposList) lateinit var rvRepositories: RecyclerView
    @BindView(R.id.starredRepoList) lateinit var rvStarredRepos: RecyclerView

    @BindDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24px) lateinit var downArrow: Drawable
    @BindDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24px) lateinit var upArrow: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            presenter.setUserName(it.getString("USER_NAME"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun showData(user: GetUserInfoQuery.User) {
        mLogin.text = user.login()
        if (user.name() == null || user.name()?.length == 0) {
            mName.text = resources.getString(R.string.UserNoName)
        } else {
            mName.text = String.format(resources.getString(R.string.UserName),
                user.name())
        }
        if (user.email().isNotEmpty()) {
            mEmail.text = String.format(resources.getString(R.string.UserMail), user.email())
        } else {
            mEmail.text = resources.getString(R.string.UserNoEmail)
        }
        if (user.websiteUrl() == null || user.websiteUrl()?.length == 0) {
            mURL.text = resources.getString(R.string.UserNoURL)
        } else {
            mURL.text = String.format(resources.getString(R.string.UserURL),
                user.websiteUrl())
        }

        GlideApp.with(this)
            .load(user.avatarUrl())
            .into(mAvatar)

        if (user.organizations().totalCount()>0) {
            mOrganisations.text = String.format(resources.getString(R.string.UserOrganisations),
                user.organizations().totalCount())
        } else {
            mOrganisations.text = resources.getString(R.string.UserNoOrganisations)
            btnOrganisations.isEnabled = false
            btnOrganisations.icon.alpha = 50
        }

        if (user.repositories().totalCount()>0) {
            mRepos.text = String.format(resources.getString(R.string.UserRepositories),
                user.repositories().totalCount())
        } else {
            mRepos.text = resources.getString(R.string.UserNoRepositories)
            btnRepositories.isEnabled = false
            btnRepositories.icon.alpha = 50
        }

        if (user.starredRepositories().totalCount()>0) {
            mStarredRepos.text = String.format(resources.getString(R.string.UserStarredRepos),
                user.starredRepositories().totalCount())
        } else {
            mStarredRepos.text = resources.getString(R.string.UserNoStarredRepos)
            btnStarredRepos.isEnabled = false
            btnStarredRepos.icon.alpha = 50
        }

        rvOrganisations.layoutManager = LinearLayoutManager(this.context)
        rvRepositories.layoutManager = LinearLayoutManager(this.context)
        rvStarredRepos.layoutManager = LinearLayoutManager(this.context)
    }

    override fun getParentActivity(): ConductorActivity {
        return activity as ConductorActivity
    }

    override fun getBaseView(): View {
        return checkNotNull(activity?.findViewById(R.id.mainLayout))
    }

    override fun getOrganisationRView() = rvOrganisations

    override fun getRepositoriesRView() = rvRepositories

    override fun getStarredReposRView() = rvStarredRepos

    @OnClick(R.id.btnOrgs)
    fun organisationsOnClick(button: MaterialButton) {
        Answers.getInstance().logCustom(
            CustomEvent("On Expandable List Click")
                .putCustomAttribute("Click", "Organisations List"))
        if (mOrgExpList.isExpanded) {
            button.icon = downArrow
            mOrgExpList.collapse(true)
        } else {
            button.icon = upArrow
            mOrgExpList.expand(true)
            if (mRepoExpList.isExpanded) {
            btnRepositories.icon = downArrow
            mRepoExpList.collapse(true) }
            if (mStrRepoExpList.isExpanded) {
            mStrRepoExpList.collapse(true)
            btnStarredRepos.icon = downArrow }
        }
    }

    @OnClick(R.id.btnRepos)
    fun repositoriesOnClick(button: MaterialButton) {
        Answers.getInstance().logCustom(
            CustomEvent("On Expandable List Click")
                .putCustomAttribute("Click", "Repositories List"))
        if (mRepoExpList.isExpanded) {
            button.icon = downArrow
            mRepoExpList.collapse(true)
        } else {
            button.icon = upArrow
            mRepoExpList.expand(true)
            if (mOrgExpList.isExpanded) {
            mOrgExpList.collapse(true)
            btnOrganisations.icon = downArrow }
            if (mStrRepoExpList.isExpanded) {
            mStrRepoExpList.collapse(true)
            btnStarredRepos.icon = downArrow }
        }
    }

    @OnClick(R.id.btnStRepos)
    fun starredReposOnClick(button: MaterialButton) {
        Answers.getInstance().logCustom(
            CustomEvent("On Expandable List Click")
                .putCustomAttribute("Click", "Starred repositories List"))
        if (mStrRepoExpList.isExpanded) {
            button.icon = downArrow
            mStrRepoExpList.collapse(true)
        } else {
            button.icon = upArrow
            mStrRepoExpList.expand(true)
            if (mOrgExpList.isExpanded) {
            btnOrganisations.icon = downArrow
            mOrgExpList.collapse(true) }
            if (mRepoExpList.isExpanded) {
            btnRepositories.icon = downArrow
            mRepoExpList.collapse(true) }
        }
    }
}
