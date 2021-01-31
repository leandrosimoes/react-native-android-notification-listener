import { Dimensions, StyleSheet } from 'react-native'

const { height } = Dimensions.get('screen')
const FORM_HEIGHT = 250

export default StyleSheet.create({
    container: {
        justifyContent: 'center',
        alignItems: 'center',
        height: FORM_HEIGHT,
        marginTop: height / 2 - FORM_HEIGHT,
    },
    permissionStatus: {
        marginBottom: 20,
        fontSize: 18,
    },
    notificationsWrapper: {
        flex: 2,
        justifyContent: 'center',
        alignItems: 'center',
        marginBottom: 20,
    },
    notification: {
        width: 300,
        backgroundColor: '#f2f2f2',
        padding: 20,
        marginTop: 20,
        borderRadius: 5,
        elevation: 2,
    },
    buttomWrapper: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
})
