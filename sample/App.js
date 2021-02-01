import React, { useState, useEffect } from 'react'
import { SafeAreaView, Text, Image, Button, AppState, View, FlatList } from 'react-native'
import RNAndroidNotificationListener from 'react-native-android-notification-listener'
import AsyncStorage from '@react-native-async-storage/async-storage'

import styles from './App.styles.js'

let interval = null

const Notification = ({
    app,
    title,
    titleBig,
    text,
    subText,
    summaryText,
    bigText,
    audioContentsURI,
    imageBackgroundURI,
    extraInfoText,
    icon,
    image,
}) => {
    return (
        <View style={styles.notificationWrapper}>
            <View style={styles.notification}>
                <View style={styles.imagesWrapper}>
                    {!!icon && (
                        <View style={styles.notificationIconWrapper}>
                            <Image source={{ uri: icon }} style={styles.notificationIcon} />
                        </View>
                    )}
                    {!!image && (
                        <View style={styles.notificationImageWrapper}>
                            <Image source={{ uri: image }} style={styles.notificationImage} />
                        </View>
                    )}
                </View>
                <View style={styles.notificationInfoWrapper}>
                    <Text>{`app: ${app}`}</Text>
                    <Text>{`title: ${title}`}</Text>
                    <Text>{`text: ${text}`}</Text>
                    {!!titleBig && <Text>{`titleBig: ${titleBig}`}</Text>}
                    {!!subText && <Text>{`subText: ${subText}`}</Text>}
                    {!!summaryText && <Text>{`summaryText: ${summaryText}`}</Text>}
                    {!!bigText && <Text>{`bigText: ${bigText}`}</Text>}
                    {!!audioContentsURI && <Text>{`audioContentsURI: ${audioContentsURI}`}</Text>}
                    {!!imageBackgroundURI && (
                        <Text>{`imageBackgroundURI: ${imageBackgroundURI}`}</Text>
                    )}
                    {!!extraInfoText && <Text>{`extraInfoText: ${extraInfoText}`}</Text>}
                </View>
            </View>
        </View>
    )
}

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
            /**
             * As the notification is a JSON string,
             * here I just parse it
             */
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

    const hasGroupedMessages =
        lastNotification &&
        lastNotification.groupedMessages &&
        lastNotification.groupedMessages.length > 0

    return (
        <SafeAreaView style={styles.container}>
            <View style={styles.buttonWrapper}>
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
            </View>
            <View style={styles.notificationsWrapper}>
                {lastNotification && !hasGroupedMessages && <Notification {...lastNotification} />}
                {lastNotification && hasGroupedMessages && (
                    <FlatList
                        data={lastNotification.groupedMessages}
                        keyExtractor={(_, index) => index.toString()}
                        renderItem={({ item }) => (
                            <Notification app={lastNotification.app} {...item} />
                        )}
                    />
                )}
            </View>
        </SafeAreaView>
    )
}

export default App
