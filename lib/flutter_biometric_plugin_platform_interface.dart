import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_biometric_plugin_method_channel.dart';

abstract class FlutterBiometricPluginPlatform extends PlatformInterface {
  /// Constructs a FlutterBiometricPluginPlatform.
  FlutterBiometricPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterBiometricPluginPlatform _instance =
      MethodChannelFlutterBiometricPlugin();

  /// The default instance of [FlutterBiometricPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterBiometricPlugin].
  static FlutterBiometricPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterBiometricPluginPlatform] when
  /// they register themselves.
  static set instance(FlutterBiometricPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> getShowBiometricPromt() {
    throw UnimplementedError(
        'getShowBiometricPromt() has not been implemented.');
  }

  Future<String?> checkIsBiometricChange() {
    throw UnimplementedError(
        'checkIsBiometricChange() has not been implemented.');
  }
}
