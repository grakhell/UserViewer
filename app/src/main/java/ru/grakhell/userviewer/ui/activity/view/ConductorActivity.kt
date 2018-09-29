package ru.grakhell.userviewer.ui.activity.view

import android.annotation.TargetApi
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation.findNavController
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.SearchEvent
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import io.fabric.sdk.android.Fabric
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.injection.scope.ActivityScope
import ru.grakhell.userviewer.storage.local.UserViewerSuggestionProvider
import ru.grakhell.userviewer.ui.activity.presenter.ConductorPresenter
import ru.grakhell.userviewer.ui.common.BaseActivity
import ru.grakhell.userviewer.util.NetworkUtil
import ru.grakhell.userviewer.util.ViewUtil
import timber.log.Timber
import javax.inject.Inject

const val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE: Int = 5469
const val ACCOUNT_CODE = 1601

@ActivityScope
class ConductorActivity @Inject constructor() : BaseActivity<ConductorPresenter>(), Conductor {

    private lateinit var menuUserChange: MenuItem
    private lateinit var menuUserExit: MenuItem
    private lateinit var menuUserName: MenuItem
    private lateinit var searchView: SearchView

    private val suggestions: SearchRecentSuggestions by lazy {
        SearchRecentSuggestions(this,
            UserViewerSuggestionProvider.AUTHORITY,
            UserViewerSuggestionProvider.MODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbarMain))
        checkPermission()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = (menu?.findItem(R.id.app_bar_search)?.actionView) as SearchView
        searchView.run {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(true)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean = false
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        saveQueryToSuggestion(query)
                        search(query)
                        searchView.clearFocus()
                    }
                    return true
                }
            })
            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean = false
                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = searchView.suggestionsAdapter.cursor
                    cursor.moveToPosition(position)
                    val string = cursor.getString(2)
                    searchView.setQuery(string, true)
                    return true
                }
            })
        }
        menuUserName = menu.findItem(R.id.menu_user_name)
        menuUserChange = menu.findItem(R.id.menu_user_change)
        menuUserExit = menu.findItem(R.id.menu_user_exit)
        showMenuItems(activityPresenter.isLogged())
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_user_change -> {
                activityPresenter.accountPicker()
                true
            }
            R.id.menu_user_exit -> {
                activityPresenter.clear()
                activityPresenter.setLogged(false)
                showMenuItems(activityPresenter.isLogged())
                getNavController().popBackStack(R.id.startFragment, false)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onStart()
    }

    override fun onSupportNavigateUp() =
        findNavController(this, R.id.nav_host_fragment).navigateUp()

    private fun search(query: String) {
        try {
            if (NetworkUtil.isNetworkConnected(this)) {
                Answers.getInstance().logSearch(
                    SearchEvent()
                        .putQuery(query)
                )
                val arg = Bundle()
                arg.putString("USER_NAME", query)
                getNavController().navigate(R.id.searchResultFragment, arg)
                ViewUtil.hideKeyboard(this)
            } else {
                Snackbar.make(
                    findViewById(R.id.mainLayout),
                    R.string.net_error,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.retry_string) { _ ->
                        kotlin.run {
                            search(query)
                        }
                    }
                    .setActionTextColor(Color.WHITE)
                    .show()
                ViewUtil.hideKeyboard(this)
            }
        } catch (e: Exception) {
            Timber.e(e)
            Crashlytics.logException(e)
        }
    }

    fun showUser(user: String) {
        try {
            Answers.getInstance().logContentView(
                ContentViewEvent()
                    .putContentName(user)
                    .putContentType("User Info")
            )
            val arg = Bundle()
            arg.putString("USER_NAME", user)
            getNavController().navigate(R.id.action_searchResultFragment_to_userInfoFragment, arg)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } catch (e: Exception) {
            Timber.e(e)
            Crashlytics.logException(e)
        }
    }

    fun showRepository(owner: String, name: String) {
        try {
        Answers.getInstance().logContentView(ContentViewEvent()
            .putContentName("Repository: $name with owner $owner")
            .putContentType("Repository Info"))
        val arg = Bundle()
        arg.putString("OWNER", owner)
        arg.putString("REPOSITORY", name)
        getNavController().navigate(R.id.action_userInfoFragment_to_repositoryInfoFragment, arg)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } catch (e: Exception) {
            Timber.e(e)
            Crashlytics.logException(e)
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent,
                    ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun saveQueryToSuggestion(query: String) {
        suggestions.saveRecentQuery(query, null)
    }

    override fun onPause() {
        if (isFinishing)suggestions.clearHistory()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE -> {
                @TargetApi(Build.VERSION_CODES.M)
                if (!Settings.canDrawOverlays(this)) {
                    checkPermission()
                }
            }
            ACCOUNT_CODE -> {
                activityPresenter.takeAccount(data)
            }
        }
    }

    override fun getView(): View {
        return checkNotNull(findViewById(R.id.mainLayout))
    }

    override fun showMenuItems(flag: Boolean) {
        if (flag) {
            menuUserName.title = activityPresenter.getAccountName()
            menuUserChange.title = resources.getString(R.string.menuUserChange)
        } else {
            menuUserChange.title = resources.getString(R.string.menuUserEnter)
            menuUserName.title = resources.getString(R.string.empty)
        }
        val clearButton =
            searchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        val searchButton =
            searchView.findViewById(androidx.appcompat.R.id.search_button) as ImageView
        val voiceButton =
            searchView.findViewById(androidx.appcompat.R.id.search_voice_btn) as ImageView
        val searchEditText =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete

        clearButton.isEnabled = flag
        searchEditText.isEnabled = flag
        searchButton.isEnabled = flag
        voiceButton.isEnabled = flag
        searchView.isSubmitButtonEnabled = flag
        searchView.isEnabled = flag
        searchView.isFocusable = flag
        menuUserExit.isVisible = flag
        menuUserName.isVisible = flag
    }

    fun getNavController() = findNavController(this, R.id.nav_host_fragment)
}
