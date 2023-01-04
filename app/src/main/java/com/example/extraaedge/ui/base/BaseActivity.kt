package com.example.extraaedge.ui.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.extraaedge.App
import com.example.extraaedge.R
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<VM : BaseViewModel?> : AppCompatActivity() {

    private var app: App? = null
    private val handler = Handler()
    private val pauseCallback = Runnable { app!!.onActivityPaused() }

    override fun onResume() {
        super.onResume()
        app!!.onActivityResume()
    }

    override fun onPause() {
        super.onPause()
        handler.postDelayed(pauseCallback, PAUSE_CALLBACK_DELAY)
    }

    private var progressDialog: ProgressDialog? = null
    private var progressDialogOtp: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        app = application as App

        //lock to portrait orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        bindViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgressDialog()
    }

    protected fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, null, "Please wait...", false, false)
        }
    }

    protected fun hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    protected fun showError(e: Throwable) {
        val onErrorClickedListener: DialogInterface.OnClickListener? = null
        AlertDialog.Builder(this)
            .setMessage(e.localizedMessage)
            .setTitle("Error")
            .setPositiveButton(
                resources.getString(android.R.string.ok),
                onErrorClickedListener
            )
            .setCancelable(false)
            .create()
            .show()
    }

    protected fun setStatusBarColor() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.purple_200)
    }

    protected fun hideKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    protected abstract val viewModel: VM
    private fun bindViewModel() {
        viewModel!!.loadingIndicator
            .observe(
                this,
                Observer { shouldShowLoadingIndicator: Boolean? ->
                    showLoadingIndicator(shouldShowLoadingIndicator)
                }
            )
    }

    private fun showLoadingIndicator(shouldShowLoadingIndicator: Boolean?) {
        if (shouldShowLoadingIndicator != null && shouldShowLoadingIndicator) showProgressDialog() else hideProgressDialog()
    }

    protected fun showErrorDialog(message: String?) {
        val builder =
            AlertDialog.Builder(this@BaseActivity)
        builder.setCancelable(false)
        builder.setMessage(message)
        builder.setPositiveButton(
            "Ok"
        ) { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
            finish()
        }
        // Create the alert dialog
        val dialog = builder.create()
        dialog.show()
        // Get the alert dialog buttons reference
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.purple_200))
    }

    protected val isOnline: Boolean
        get() {
            val cm =
                (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

    companion object {
        private const val PAUSE_CALLBACK_DELAY: Long = 500
    }
}