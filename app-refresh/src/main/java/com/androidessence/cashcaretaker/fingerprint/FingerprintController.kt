package com.androidessence.cashcaretaker.fingerprint

import android.annotation.TargetApi
import android.app.FragmentManager
import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import timber.log.Timber

/**
 * Class that handles fingerprint authentication.
 *
 * @property[cancellationSignal] The signal to callback when fingerprint auth is stopped.
 * @property[selfCancelled] Whether or not this fingerprintController was cancelled by itself.
 * @property[fingerprintManagerCompat] The FingerprintManager that provides any relevant hardware info.
 * @property[isFingerprintAuthAvailable] Boolean flag for whether or not this device supports fingerprint authentication.
 */
@RequiresApi(23)
class FingerprintController(private val fingerprintManagerCompat: FingerprintManager, private val callback: Callback) : FingerprintManager.AuthenticationCallback() {
    private var cancellationSignal: CancellationSignal? = null
    private var selfCancelled: Boolean = false

    val isFingerprintAuthAvailable: Boolean
        get() = fingerprintManagerCompat.isHardwareDetected && fingerprintManagerCompat.hasEnrolledFingerprints()

    fun startListening(cryptoObject: FingerprintManager.CryptoObject) {
        if (!isFingerprintAuthAvailable) return

        cancellationSignal = CancellationSignal()
        selfCancelled = false
        fingerprintManagerCompat.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    fun stopListening() {
        cancellationSignal?.let {
            selfCancelled = true
            it.cancel()
            cancellationSignal = null
        }
    }

    private fun showError(charSequence: CharSequence?) {
        //TODO: Update icon
        //TODO: Display error
        Timber.e(charSequence.toString())
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
        if (!selfCancelled) {
            showError(errString)
            //TODO: Update icon
            callback.onError()
        }
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
        showError(helpString)
    }

    override fun onAuthenticationFailed() {
        showError("Fingerprint not recognized. Try again.")
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        //TODO: Update error text and icon
        Timber.d("Authenticated.")
        callback.onAuthenticated()
    }

    interface Callback {
        fun onError()
        fun onAuthenticated()
    }
}