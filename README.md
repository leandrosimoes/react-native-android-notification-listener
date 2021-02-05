# react-native-android-notification-listener

React Native Android Notification Listener is a library that allows you to listen for status bar notifications from all applications. (Android Only)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bfbf75b8e92f446481f5ce4b0d077b0b)](https://app.codacy.com/manual/leandrosimoes/react-native-android-notification-listener?utm_source=github.com\&utm_medium=referral\&utm_content=leandrosimoes/react-native-android-notification-listener\&utm_campaign=Badge_Grade_Dashboard)
[![npm version](https://badge.fury.io/js/react-native-android-notification-listener.svg)](https://badge.fury.io/js/react-native-android-notification-listener)
![Node.js Package](https://github.com/leandrosimoes/react-native-android-notification-listener/workflows/Node%2Ejs%20Package/badge.svg)

## Installation

`$ yarn add react-native-android-notification-listener`

or

`$ npm intall react-native-android-notification-listener`

## Auto linking (React Native >= 0.60)

For RN version >= 0.60 there is no need to link or add any configurations manually. React Native will take care of linking the library using auto link.

## Manual linking (React Native < 0.60)

RN version < 0.60 require a manual link and some manual configurations as you can see bellow

### Manual link

`$ react-native link react-native-android-notification-listener`

### Manual installation

Some of this configurations will be automatically handled by the manual link process, but it is rightly recomended to check manually each file to ensure that everything is properly configured.

1.  Open the `android/app/src/main/java/[...]/MainApplication.java` file
    *   Add `import com.lesimoes.androidnotificationlistener.RNAndroidNotificationListenerPackage;` to the imports session at the top of the file;
    *   Add `new RNAndroidNotificationListenerPackage()` to the list returned by the `getPackages()` method;
2.  Append the following lines to `android/settings.gradle` file:
    ```java
    include ':react-native-android-notification-listener'
    project(':react-native-android-notification-listener').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-android-notification-listener/android')
    ```
3.  Insert the following lines inside the dependencies block in `android/app/build.gradle`:
    ```java
    compile project(':react-native-android-notification-listener')
    ```

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
 * Is that why I'm using a async function here.
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
     * Note that this properties depends on the sender configuration
     * so many times a lot of them will be empty
     */
    
    if (notification) {
        /**
         * Here you could store the notifications in a external API.
         * I'm using AsyncStorage here as an example.
         */
        
        ...
    }
}

/**
 * This should be required early in the require sequence
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

For more details, se the `sample/` project in this repository

## FAQ

"There are some limitations regarding the use of the Headless JS by this module that I should care about?"

Yes, there are some nuances that you should consern. For example, since Headless JS runs in a standalone "Task" you can't interact directly with it by the touch UI.
For more information about using Headless JS in React Native, I sugest to you to take a look at the official documentation [here](https://reactnative.dev/docs/headless-js-android).

***

"I keep receiving the warning `registerHeadlessTask or registerCancellableHeadlessTask called multiple times for same key '${taskKey}'`, is that a problem?

No, this warning is here, where you can see that the task providers are stored in a set, and there's no way to delete them, so react is just complaining about the fact that we are overwriting it.

***
