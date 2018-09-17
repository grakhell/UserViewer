package ru.grakhell.userviewer.ui.common.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseFragment : DialogFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    private var viewUnbinder: Unbinder? = null

    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity?) {
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.M) AndroidSupportInjection.inject(this)
        super.onAttach(activity)
    }

    override fun onAttach(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @SuppressWarnings("ConstantConditions")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewUnbinder = ButterKnife.bind(this, checkNotNull(view))
    }

    override fun onDestroyView() {
        if (viewUnbinder != null) viewUnbinder?.unbind()
        super.onDestroyView()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector

    protected fun addChildFragment(@IdRes containerViewId: Int, fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .add(containerViewId, fragment)
            .commit()
    }

    protected fun replaceChildFragment(@IdRes containerViewId: Int, fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(containerViewId, fragment)
            .commit()
    }
}