
import 'flutter_biometric_plugin_platform_interface.dart';

class FlutterBiometricPlugin {
  Future<String?> getPlatformVersion() {
    return FlutterBiometricPluginPlatform.instance.getPlatformVersion();
  }
}
