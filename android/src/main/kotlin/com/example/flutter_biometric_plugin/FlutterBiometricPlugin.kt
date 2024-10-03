package com.example.flutter_biometric_plugin

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.annotation.NonNull
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

/** FlutterBiometricPlugin */
class FlutterBiometricPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    private lateinit var channel: MethodChannel
    private var activity: Activity? = null
    private val KEY_NAME = "biometric_key"
    private val ANDROID_KEYSTORE = "AndroidKeyStore"

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_biometric_plugin")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "checkIsBiometricChange") {
            val cipher = BioMetricHelper.getCipher(KEY_NAME, ANDROID_KEYSTORE)
            if (cipher == null) {
                result.success("Success")
            } else {
                result.success("Failed")
            }
        } else if (call.method == "showBiometricPrompt") {
            showBiometricPrompt("title", "sub title", result)
        } else {
            result.notImplemented()
        }
    }

    private fun showBiometricPrompt(title: String, subTitle: String, _result: MethodChannel.Result) {
      (activity as? FragmentActivity)?.let { currentActivity -> // Casting Activity to FragmentActivity
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
      } ?: run {
          _result.error("NO_ACTIVITY", "No activity available or activity is not a FragmentActivity", null)
      }
  }
  
  
         
    // ActivityAware methods
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
