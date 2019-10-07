import { NativeModules, DeviceEventEmitter } from 'react-native';

const { RNAndroidNotificationListener } = NativeModules;
const NotificationListener = {}

NotificationListener.getPermissionStatus = () => {
    return RNAndroidNotificationListener.getPermissionStatus();
}
  
NotificationListener.requestPermission = () => {
    return RNAndroidNotificationListener.requestPermission();
}

NotificationListener.onNotificationReceived = callback => {
    DeviceEventEmitter.removeAllListeners('notificationReceived');
    return DeviceEventEmitter.addListener('notificationReceived', callback);
}

export default NotificationListener;
