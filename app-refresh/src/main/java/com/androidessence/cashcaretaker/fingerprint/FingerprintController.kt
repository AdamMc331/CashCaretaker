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
 * @property[context] Helper variable to get the context from one of the views. The view used is arbitrary.
 * @property[resetErrorTextRunnable] A runnable that resets the fingerprint view back to its default state.
 */
class FingerprintController(private val fingerprintManagerCompat: FingerprintManagerCompat, private val callback: Callback, private val errorText: TextView, private val icon: ImageView) : FingerprintManagerCompat.AuthenticationCallback() {
    private var cancellationSignal: CancellationSignal? = null
    private var selfCancelled: Boolean = false

    private val isFingerprintAuthAvailable: Boolean
        get() = fingerprintManagerCompat.isHardwareDetected && fingerprintManagerCompat.hasEnrolledFingerprints()

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

    /**
     * Begins listening for a fingerprint authentication action.
     *
     * @param[cryptoObject] The crypto object used to sign the fingerprint auth with.
     */
    fun startListening(cryptoObject: FingerprintManagerCompat.CryptoObject) {
        if (!isFingerprintAuthAvailable) return

        cancellationSignal = CancellationSignal()
        selfCancelled = false
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, this, null)
    }

    /**
     * Removes listeners from the fingerprint sensor.
     */
    fun stopListening() {
        cancellationSignal?.let {
            selfCancelled = true
            it.cancel()
            cancellationSignal = null
        }
    }

    /**
     * Displays an error text and image on the fingerprint view.
     *
     * @param[charSequence] The error message to display to the user.
     */
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
        /**
         * The amount of time (in milliseconds) to display an error before resetting the view.
         */
        private val ERROR_TIMEOUT_MILLIS = 1600L

        /**
         * The amount of time (in milliseconds) to display success before using the callback.
         */
        private val SUCCESS_DELAY_MILLIS = 1300L
    }

    /**
     * A callback that handles successful and unsuccessful authorization.
     */
    interface Callback {
        /**
         * Method that will be called if the user fails to authenticate a fingerprint.
         */
        fun onError()

        /**
         * Method that will be called if the user successfully authorizes their fingerprint.
         */
        fun onAuthenticated()
    }
}