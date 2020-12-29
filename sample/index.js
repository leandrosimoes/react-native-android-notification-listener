import { AppRegistry } from 'react-native'
import AsyncStorage from '@react-native-async-storage/async-storage'

import App from './App'
import { name as appName } from './app.json'

/**
 * Note that this method MUST return a Promise.
 * Is that why I'm using a async function here.
 */
const headlessNotificationListener = async notification => {
    if (notification) {
        /**
         * Here you could store the notifications in a external API.
         * I'm using AsyncStorage here as an example.
         */
        await AsyncStorage.setItem('@lastNotification', JSON.stringify(notification))
    }
}

/**
 * AppRegistry should be required early in the require sequence
 * to make sure the JS execution environment is setup before other
 * modules are required.
 */
AppRegistry.registerHeadlessTask(
    'RNAndroidNotificationListenerHeadlessJs',
    () => headlessNotificationListener
)

AppRegistry.registerComponent(appName, () => App)
