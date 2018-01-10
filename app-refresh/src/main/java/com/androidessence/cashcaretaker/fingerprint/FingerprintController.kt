package com.androidessence.cashcaretaker.fingerprint


import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal
import android.widget.ImageView
import android.widget.TextView
import com.androidessence.cashcaretaker.R
import timber.log.Timber

/**
 * Class that handles fingerprint authentication.
 *
 * @property[cancellationSignal] The signal to callback when fingerprint auth is stopped.
 * @property[selfCancelled] Whether or not this fingerprintController was cancelled by itself.
 * @property[fingerprintManagerCompat] The FingerprintManager that provides any relevant hardware info.
 * @property[isFingerprintAuthAvailable] Boolean flag for whether or not this device supports fingerprint authentication.
 */
class FingerprintController(private val fingerprintManagerCompat: FingerprintManagerCompat, private val callback: Callback, private val errorText: TextView, private val icon: ImageView) : FingerprintManagerCompat.AuthenticationCallback() {
    private var cancellationSignal: CancellationSignal? = null
    private var selfCancelled: Boolean = false

    private val isFingerprintAuthAvailable: Boolean
        get() = fingerprintManagerCompat.isHardwareDetected && fingerprintManagerCompat.hasEnrolledFingerprints()

    /**
     * Helper variable to get the context from one of the views. The view used is arbitrary.
     */
    private val context: Context
        get() = errorText.context

    private val resetErrorTextRunnable: Runnable = Runnable {
        errorText.setTextColor(ContextCompat.getColor(context, R.color.mds_white))
        errorText.text = context.getString(R.string.confirm_fingerprint)
        icon.setImageResource(R.drawable.ic_fingerprint_white_24dp)
    }

    init {
        errorText.post(resetErrorTextRunnable)
    }

    fun startListening(cryptoObject: FingerprintManagerCompat.CryptoObject) {
        if (!isFingerprintAuthAvailable) return

        cancellationSignal = CancellationSignal()
        selfCancelled = false
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, this, null)
    }

    fun stopListening() {
        cancellationSignal?.let {
            selfCancelled = true
            it.cancel()
            cancellationSignal = null
        }
    }

    private fun showError(charSequence: CharSequence?) {
        errorText.text = charSequence
        icon.setImageResource(R.drawable.ic_error_white_24dp)
        errorText.removeCallbacks(resetErrorTextRunnable)
        errorText.postDelayed(resetErrorTextRunnable, ERROR_TIMEOUT_MILLIS)
        Timber.e(charSequence.toString())
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
        if (!selfCancelled) {
            showError(errString)
            icon.postDelayed({
                callback.onError()
            }, ERROR_TIMEOUT_MILLIS)
        }
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
        showError(helpString)
    }

    override fun onAuthenticationFailed() {
        showError("Fingerprint not recognized. Try again.")
    }

    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
        icon.setImageResource(R.drawable.ic_check_circle_white_24dp)
        Timber.d("Authenticated.")
        icon.postDelayed({
            callback.onAuthenticated()
        }, SUCCESS_DELAY_MILLIS)
    }

    companion object {
        private val ERROR_TIMEOUT_MILLIS = 1600L
        private val SUCCESS_DELAY_MILLIS = 1300L
    }

    interface Callback {
        fun onError()
        fun onAuthenticated()
    }
}