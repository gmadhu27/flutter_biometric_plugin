package com.example.flutter_biometric_plugin

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.annotation.NonNull
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import com.example.flutter_biometric_plugin.helper.BioMetricHelper

class FlutterBiometricPlugin : FlutterPlugin, ActivityAware, MethodCallHandler {

    private lateinit var channel: MethodChannel
    private var activity: FragmentActivity? = null
    private lateinit var applicationContext: Context

    private val KEY_NAME = "biometric_key"
    private val ANDROID_KEYSTORE = "AndroidKeyStore"

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.applicationContext = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_biometric_plugin")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (activity == null) {
            result.success("Activity is not attached")
            return
        }

        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
            "checkIsBiometricChange" -> {
                val cipher = BioMetricHelper.getCipher(KEY_NAME, ANDROID_KEYSTORE)
                result.success(if (cipher == null) "Success" else "Failed")
            }
            "showBiometricPrompt" -> {
                val title = call.argument<String>("title") ?: "Authentication Required"
                val subTitle = call.argument<String>("subtitle") ?: "Please authenticate"
                checkIsBiometricSupport(title, subTitle, result)
            }
            else -> result.notImplemented()
        }
    }

    private fun checkIsBiometricSupport(title: String, subTitle: String, _result: MethodChannel.Result) {
        val biometricManager = BiometricManager.from(applicationContext)
        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                                                BiometricManager.Authenticators.BIOMETRIC_WEAK)
        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Handler(Looper.getMainLooper()).post {
                    showBiometricPromptDialog(title, subTitle, _result)
                }
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                _result.success("No biometric hardware available")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                _result.success("Biometric hardware unavailable")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                _result.success("No biometric enrolled")
            }
            else -> {
                _result.success("Biometric authentication is not supported")
            }
        }
    }

    private fun showBiometricPromptDialog(title: String, subTitle: String, _result: MethodChannel.Result) {
        activity?.let { currentActivity ->
            val biometricPrompt = BiometricPrompt(
                currentActivity,
                ContextCompat.getMainExecutor(currentActivity),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        _result.success("Authentication error: $errString")
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        BioMetricHelper.createBiometricKey(KEY_NAME, ANDROID_KEYSTORE)
                        _result.success("Success")
                    }

                    override fun onAuthenticationFailed() {
                        _result.success("Authentication Failed")
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subTitle)
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
                )
                .setNegativeButtonText("Cancel")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        updateAttachedActivity(binding.activity)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        updateAttachedActivity(binding.activity)
    }

    private fun updateAttachedActivity(childActivity: Activity) {
        if (childActivity is FragmentActivity) {
            activity = childActivity
        }
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }
}
