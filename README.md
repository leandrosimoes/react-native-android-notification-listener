# react-native-android-notification-listener

React Native Android Notification Listener is a library that allows you to listen for status bar notifications from all applications. (Android Only)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bfbf75b8e92f446481f5ce4b0d077b0b)](https://app.codacy.com/manual/leandrosimoes/react-native-android-notification-listener?utm_source=github.com\&utm_medium=referral\&utm_content=leandrosimoes/react-native-android-notification-listener\&utm_campaign=Badge_Grade_Dashboard)
[![npm version](https://badge.fury.io/js/react-native-android-notification-listener.svg)](https://badge.fury.io/js/react-native-android-notification-listener)

## Installation

* For React Native greater or equal then 0.68.0 
   - `$ yarn add react-native-android-notification-listener`
* For React Native between 0.65.1 and 0.67.4 
   - `$ yarn add react-native-android-notification-listener@4.0.2`
* For React Native less then 0.65 
   - `$ yarn add react-native-android-notification-listener@3.1.2`

## Usage

```javascript
import { AppRegistry } from 'react-native'
import RNAndroidNotificationListener, { RNAndroidNotificationListenerHeadlessJsName } from 'react-native-android-notification-listener';

// To check if the user has permission
const status = await RNAndroidNotificationListener.getPermissionStatus()
console.log(status) // Result can be 'authorized', 'denied' or 'unknown'

// To open the Android settings so the user can enable it
RNAndroidNotificationListener.requestPermission()

/**
 * Note that this method MUST return a Promise.
 * Is that why I'm using an async function here.
 */
const headlessNotificationListener = async ({ notification }) => {/**
     * This notification is a JSON string in the follow format:
     *  {
     *      "time": string,
     *      "app": string,
     *      "title": string,
     *      "titleBig": string,
     *      "text": string,
     *      "subText": string,
     *      "summaryText": string,
     *      "bigText": string,
     *      "audioContentsURI": string,
     *      "imageBackgroundURI": string,
     *      "extraInfoText": string,
     *      "groupedMessages": Array<Object> [
     *          {
     *              "title": string,
     *              "text": string
     *          }
     *      ],
     *      "icon": string (base64),
     *      "image": string (base64), // WARNING! THIS MAY NOT WORK FOR SOME APPLICATIONS SUCH TELEGRAM AND WHATSAPP
     *  }
     * 
     * Note that these properties depend on the sender configuration so many times a lot of them will be empty
     */
    
    if (notification) {
        /**
         * You could store the notifications in an external API.
         * I'm using AsyncStorage in the example project.
         */
        
        ...
    }
}

/**
 * This should be required early in the sequence
 * to make sure the JS execution environment is setup before other
 * modules are required.
 * 
 * Your entry file (index.js) would be the better place for it.
 * 
 * PS: I'm using here the constant RNAndroidNotificationListenerHeadlessJsName to ensure
 *     that I register the headless with the right name
 */
AppRegistry.registerHeadlessTask(RNAndroidNotificationListenerHeadlessJsName,	() => headlessNotificationListener)
```

For more details, see the `example/` project in this repository

## FAQ & Known issues

**There are some limitations regarding the use of the Headless JS by this module that I should care about?**

Yes, there are some nuances that you should concern about. For example, since Headless JS runs in a standalone "Task" you can't interact directly with it by the touch UI.
For more information about using Headless JS in React Native, I suggest you take a look at the official documentation [here](https://reactnative.dev/docs/headless-js-android).

***

**I keep receiving the warning `registerHeadlessTask or registerCancellableHeadlessTask called multiple times for the same key '${taskKey}'`, is that a problem?**

No, this warning is here, where you can see that the task providers are stored in a set, and there's no way to delete them, so react is just complaining about the fact that we are overwriting it.

***

**I allowed the notifications, and the module shows that the notifications are allowed but even then the notifications are not retrieved. There is anything else that I can do in this case?**

It is known that old Android versions and device particularities sometimes require a reboot or even to be turned off completely and then turned on again to have the notification listener working properly.
