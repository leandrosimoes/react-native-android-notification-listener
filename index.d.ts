export declare type RNAndroidNotificationListenerPermissionStatus = 'unknown' | 'authorized' | 'denied'
export declare const RNAndroidNotificationListenerHeadlessJsName = 'RNAndroidNotificationListenerHeadlessJs'

export declare function requestPermission(): void
export declare function getPermissionStatus(): Promise<RNAndroidNotificationListenerPermissionStatus>