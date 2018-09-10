package ru.grakhell.userviewer.ui.activity

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
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation.findNavController
import com.crashlytics.android.Crashlytics
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import io.fabric.sdk.android.Fabric
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.injection.scope.ActivityScope
import ru.grakhell.userviewer.storage.local.UserViewerSuggestionProvider
import ru.grakhell.userviewer.ui.common.BaseActivity
import ru.grakhell.userviewer.util.NetworkUtil
import ru.grakhell.userviewer.util.ViewUtil
import javax.inject.Inject


const val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE:Int = 5469

@ActivityScope
class ConductorActivity @Inject constructor() : BaseActivity(){
    private val suggestions: SearchRecentSuggestions by lazy{
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
        menuInflater.inflate(R.menu.main,menu)

        val searchManager:SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = (menu?.findItem(R.id.app_bar_search)?.actionView) as SearchView
        searchView.run {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(true)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String?): Boolean = false
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null) {
                        saveQueryToSuggestion(query)
                        search(query)
                        searchView.clearFocus()
                    }
                    return true
                }
            })
            setOnSuggestionListener(object : SearchView.OnSuggestionListener{
                override fun onSuggestionSelect(position: Int): Boolean =false
                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = searchView.suggestionsAdapter.cursor
                    cursor.moveToPosition(position)
                    val string = cursor.getString(2)
                    searchView.setQuery(string,true)
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onStart()
    }

    override fun onSupportNavigateUp()
        = findNavController(this,R.id.nav_host_fragment).navigateUp()

    private fun search(query:String) {
        if (NetworkUtil.isNetworkConnected(this)) {
            val arg = Bundle()
            arg.putString("USER_NAME", query)
            getNavController().navigate(R.id.action_startFragment_to_searchResultFragment, arg)
            ViewUtil.hideKeyboard(this)
        }
        else{
            Snackbar.make(findViewById(R.id.mainLayout),R.string.net_error,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry_string){_ -> kotlin.run {
                    search(query)
                }}
                .setActionTextColor(Color.WHITE)
                .show()
            ViewUtil.hideKeyboard(this)
        }
    }

    private fun saveQueryToSuggestion(query: String) {
        suggestions.saveRecentQuery(query,null)
    }

    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onPause() {
        if(isFinishing)suggestions.clearHistory()
        super.onPause()
    }

    fun showUser(user: String) {
        val arg = Bundle()
        arg.putString("USER_NAME", user)
        getNavController().navigate(R.id.action_searchResultFragment_to_userInfoFragment, arg)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showRepository(owner:String, name:String){
        val arg = Bundle()
        arg.putString("OWNER", owner)
        arg.putString("REPOSITORY", name)
        getNavController().navigate(R.id.action_userInfoFragment_to_repositoryInfoFragment, arg)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                checkPermission()
            }
        }
    }

    fun getNavController() = findNavController(this, R.id.nav_host_fragment)
}
