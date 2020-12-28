import { StyleSheet } from 'react-native'

export default StyleSheet.create({
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
})
