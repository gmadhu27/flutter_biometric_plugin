import 'flutter_biometric_plugin_platform_interface.dart';

class FlutterBiometricPlugin {
  Future<String?> getShowBiometricPromt(String? title, String? message) {
    return FlutterBiometricPluginPlatform.instance
        .getShowBiometricPromt(title, message);
  }

  Future<String?> checkIsBiometricChange() {
    return FlutterBiometricPluginPlatform.instance.checkIsBiometricChange();
  }
}
