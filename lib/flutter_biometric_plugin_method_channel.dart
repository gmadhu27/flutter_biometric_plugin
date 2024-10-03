import 'package:flutter/services.dart';

import 'flutter_biometric_plugin_platform_interface.dart';

/// An implementation of [FlutterBiometricPluginPlatform] that uses method channels.
class MethodChannelFlutterBiometricPlugin
    extends FlutterBiometricPluginPlatform {
  /// The method channel used to interact with the native platform.

  final methodChannel = const MethodChannel('flutter_biometric_plugin');

  @override
  Future<String?> checkIsBiometricChange() async {
    final result =
        await methodChannel.invokeMethod<String>('checkIsBiometricChange');
    return result;
  }

  @override
  Future<String?> getShowBiometricPromt(String? title, String? message) async {
    final result =
        await methodChannel.invokeMethod<String>('showBiometricPrompt', {
      "title": title,
      "subtitle": message,
    });
    //    await methodChannel.invokeMethod<String>('checkIsBiometricChange');
    // await methodChannel.invokeMethod<String>('showBiometricPrompt');
    return result;
  }
}
