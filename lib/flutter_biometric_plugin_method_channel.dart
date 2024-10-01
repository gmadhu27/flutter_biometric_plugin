import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_biometric_plugin_platform_interface.dart';

/// An implementation of [FlutterBiometricPluginPlatform] that uses method channels.
class MethodChannelFlutterBiometricPlugin extends FlutterBiometricPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_biometric_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
