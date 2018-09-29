package ru.grakhell.oauthlib.ui

import android.accounts.NetworkErrorException
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.second_factor_fragment.*
import kotlinx.coroutines.experimental.runBlocking

import ru.grakhell.oauthlib.R
import ru.grakhell.oauthlib.util.NetworkUtil
import timber.log.Timber
import java.lang.Exception

class SecondFactorFragment : Fragment() {

    companion object {
        fun newInstance() = SecondFactorFragment()
    }

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.second_factor_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(checkNotNull(activity)).get(AuthViewModel::class.java)
    }

    override fun onStart() {
        sendButton.setOnClickListener {
            try {
                if (!NetworkUtil.isNetworkConnected(checkNotNull(activity))) throw NetworkErrorException()
                viewModel.userToken.value = userCode.text.toString()
                runBlocking { viewModel.getResponse2FA().await() }
            } catch (ex: NetworkErrorException) {
                viewModel.errCodes.value = AuthViewModel.NET_ERROR
                Timber.e(ex)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
        super.onStart()
    }
}
