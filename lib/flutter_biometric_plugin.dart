import 'flutter_biometric_plugin_platform_interface.dart';

class FlutterBiometricPlugin {
  Future<String?> getPlatformVersion() {
    return FlutterBiometricPluginPlatform.instance.getPlatformVersion();
  }

  Future<String?> getShowBiometricPromt() {
    return FlutterBiometricPluginPlatform.instance.getShowBiometricPromt();
  }

  Future<String?> checkIsBiometricChange() {
    return FlutterBiometricPluginPlatform.instance.checkIsBiometricChange();
  }
}
