import React, {useState, useEffect} from 'react';
import {
  SafeAreaView,
  Text,
  StyleSheet,
  Button,
  AppState,
  View,
} from 'react-native';
import RNAndroidNotificationListener from 'react-native-android-notification-listener';

const App = () => {
  const [hasPermission, setHasPermission] = useState(false);
  const [lastNotification, setLastNotification] = useState(null);

  const handleOnPressPermissionButton = async () => {
    RNAndroidNotificationListener.requestPermission();
  };

  const handleAppStateChange = async nextAppState => {
    RNAndroidNotificationListener.getPermissionStatus().then(status => {
      setHasPermission(status !== 'denied');
    });
  };

  const handleNotificationReceived = notification => {
    setLastNotification(notification);
  };

  useEffect(() => {
    RNAndroidNotificationListener.getPermissionStatus().then(status => {
      setHasPermission(status !== 'denied');
    });

    RNAndroidNotificationListener.onNotificationReceived(
      handleNotificationReceived,
    );

    AppState.addEventListener('change', handleAppStateChange);

    return () => {
      AppState.removeEventListener('change', handleAppStateChange);
    };
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <Text
        style={[
          styles.permissionStatus,
          {color: hasPermission ? 'green' : 'red'},
        ]}>
        {hasPermission
          ? 'Allowed to handle notifications'
          : 'NOT allowed to handle notifications'}
      </Text>
      <Button
        title={'Open Configuration'}
        onPress={handleOnPressPermissionButton}
      />
      {lastNotification && (
        <View style={styles.notification}>
          <Text style={styles.notificationTitle}>Last Notification</Text>
          <Text>{lastNotification.app}</Text>
          <Text>{lastNotification.title}</Text>
          <Text>{lastNotification.text}</Text>
        </View>
      )}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  permissionStatus: {
    marginBottom: 20,
    fontSize: 18,
  },
  notification: {
    width: 200,
    backgroundColor: '#f2f2f2',
    padding: 20,
    marginTop: 20,
    borderRadius: 5,
    elevation: 2,
  },
  notificationTitle: {
    fontWeight: 'bold',
  },
});

export default App;
