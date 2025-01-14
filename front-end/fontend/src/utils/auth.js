const TokenKey = 'token';
const userInfoKey = 'userInfoKey'
// 认证令牌
export function getToken() {
	return uni.getStorageSync(TokenKey)
}

export function setToken(token) {
	return uni.setStorageSync(TokenKey, token)
}

export function removeToken() {
	return uni.removeStorageSync(TokenKey)
}

export function setLocalUserInfo (userInfo) {
	return uni.setStorageSync(userInfoKey, userInfo)
}

export function getLocalUserInfo () {
	return uni.getStorageSync(userInfoKey)
}
