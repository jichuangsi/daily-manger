var url;
var token;
//修改路径
function httpUrl() {
<<<<<<< Updated upstream
	url = "https://sample.zaixian.jichuangsi.com";
//	url='http://192.168.5.10:8083'
//		url = 'http://192.168.31.83:8083'	
//		url = 'http://192.168.31.145:8083'
=======
	url = "https://api.dailymanager.jichuangsi.com"
	//	url = 'http://192.168.5.10:8083'
//	url = 'http://192.168.31.83:8083'
		url = 'http://192.168.31.145:8083'
>>>>>>> Stashed changes
	return url;
}
//获取token
function getToken() {
	return token = sessionStorage.getItem('token');
}
