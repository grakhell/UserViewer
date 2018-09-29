package ru.grakhell.userviewer.ui.common

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity<T : ActivityPresenter> : AppCompatActivity(), HasSupportFragmentInjector, Activity {

    @Inject
    lateinit var activityPresenter: T

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    protected fun addFragment(@IdRes containerViewId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(containerViewId, fragment)
            .commit()
    }

    protected fun replaceFragment(@IdRes containerViewId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(containerViewId, fragment)
            .commit()
    }



    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        activityPresenter.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        activityPresenter.onStart(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        activityPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        activityPresenter.onPause()
    }
}