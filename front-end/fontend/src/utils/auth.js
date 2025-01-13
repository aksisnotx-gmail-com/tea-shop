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

export function setUserInfo (userInfo) {
	return uni.setStorageSync(userInfoKey, userInfo)
}
