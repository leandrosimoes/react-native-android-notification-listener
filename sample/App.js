import React, { useState, useEffect } from 'react'
import { SafeAreaView, Text, Button, AppState, View } from 'react-native'
import RNAndroidNotificationListener from 'react-native-android-notification-listener'
import AsyncStorage from '@react-native-async-storage/async-storage'

import styles from './App.styles.js'

let interval = null

const App = () => {
    const [hasPermission, setHasPermission] = useState(false)
    const [lastNotification, setLastNotification] = useState(null)

    const handleOnPressPermissionButton = async () => {
        /**
         * Open the notification settings so the user
         * so the user can enable it
         */
        RNAndroidNotificationListener.requestPermission()
    }

    const handleAppStateChange = async nextAppState => {
        if (nextAppState === 'active') {
            /**
             * Check the user current notification permission status
             */
            RNAndroidNotificationListener.getPermissionStatus().then(status => {
                setHasPermission(status !== 'denied')
            })
        }
    }

    const handleCheckNotificationInterval = async () => {
        const lastStoredNotification = await AsyncStorage.getItem('@lastNotification')

        if (lastStoredNotification) {
            setLastNotification(JSON.parse(lastStoredNotification))
        }
    }

    useEffect(() => {
        AppState.addEventListener('change', handleAppStateChange)

        clearInterval(interval)

        /**
         * Just setting a interval to check if
         * there is a notification in AsyncStorage
         * so I can show it in the application
         */
        interval = setInterval(handleCheckNotificationInterval, 3000)

        return () => {
            clearInterval(interval)
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
                    {Object.keys(lastNotification).map((key, index) => {
                        if (!lastNotification[key]) return null

                        return (
                            <Text key={index.toString()}>{`${key}: ${lastNotification[key]}`}</Text>
                        )
                    })}
                </View>
            )}
        </SafeAreaView>
    )
}

export default App
