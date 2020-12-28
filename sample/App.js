import React, { useState, useEffect } from 'react'
import { SafeAreaView, Text, Button, AppState, View, AppRegistry } from 'react-native'
import RNAndroidNotificationListener from 'react-native-android-notification-listener'
import styles from './App.styles.js'

const App = () => {
    const [hasPermission, setHasPermission] = useState(false)
    const [lastNotification, setLastNotification] = useState(null)

    const handleOnPressPermissionButton = async () => {
        RNAndroidNotificationListener.requestPermission()
    }

    const handleAppStateChange = async nextAppState => {
        if (nextAppState === 'active') {
            RNAndroidNotificationListener.getPermissionStatus().then(status => {
                setHasPermission(status !== 'denied')
            })
        }
    }

    // THIS HANDLE MUST BE ASYNCRONOUS
    const headlessNotificationListener = async notification => {
        if (notification) {
            setLastNotification(notification)
        }
    }

    useEffect(() => {
        AppState.addEventListener('change', handleAppStateChange)

        AppRegistry.registerHeadlessTask(
            'RNAndroidNotificationListenerHeadlessJs',
            () => headlessNotificationListener
        )

        return () => {
            AppState.removeEventListener('change', handleAppStateChange)
        }
    }, [])

    return (
        <SafeAreaView style={styles.container}>
            <Text style={[styles.permissionStatus, { color: hasPermission ? 'green' : 'red' }]}>
                {hasPermission
                    ? 'Allowed to handle notifications'
                    : 'NOT allowed to handle notifications'}
            </Text>
            <Button
                title='Open Configuration'
                onPress={handleOnPressPermissionButton}
                disabled={hasPermission}
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
    )
}

export default App
