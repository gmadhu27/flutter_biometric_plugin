import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_biometric_plugin/flutter_biometric_plugin.dart';
import 'package:flutter_biometric_plugin/flutter_biometric_plugin_platform_interface.dart';
import 'package:flutter_biometric_plugin/flutter_biometric_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterBiometricPluginPlatform
    with MockPlatformInterfaceMixin
    implements FlutterBiometricPluginPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterBiometricPluginPlatform initialPlatform =
      FlutterBiometricPluginPlatform.instance;

  test('$MethodChannelFlutterBiometricPlugin is the default instance', () {
    expect(
        initialPlatform, isInstanceOf<MethodChannelFlutterBiometricPlugin>());
  });

  test('getPlatformVersion', () async {
    FlutterBiometricPlugin flutterBiometricPlugin = FlutterBiometricPlugin();
    MockFlutterBiometricPluginPlatform fakePlatform =
        MockFlutterBiometricPluginPlatform();
    FlutterBiometricPluginPlatform.instance = fakePlatform;

    expect(await flutterBiometricPlugin.checkIsBiometricChange(), '42');
  });
}
