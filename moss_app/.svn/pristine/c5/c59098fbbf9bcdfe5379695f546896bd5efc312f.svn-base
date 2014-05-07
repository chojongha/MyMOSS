var configDebugPrefix = "[config.js] : ";
var db = 0;

var getStartPage = "";
var getAutoLogin = "";
var getVersion = "";

//조회
function getConfigInfoResultSet() {
	db.transaction(querySelectConfigInfoDB, errorConfigInfoCB);
}

function querySelectConfigInfoDB(tx) {
	tx.executeSql('SELECT startPage, autoLoginYN, version FROM MOSS_USERINFO', [], selectConfigInfoSuccess, errorConfigInfoCB);
}

function selectConfigInfoSuccess(tx, results) {
	getStartPage = results.rows.item(0).startPage;
	getAutoLogin = results.rows.item(0).autoLoginYN;
	getVersion = results.rows.item(0).version;
	
	$("#version").text(getVersion);
	$("#config_autoLogin").val(getAutoLogin).slider( "refresh" );
	$("#config_startPage").val(getStartPage).selectmenu( "refresh" );

	console.log(configDebugPrefix + "============ 설정 정보 ============");
	console.log(configDebugPrefix + "시작페이지 : " + getStartPage);
	console.log(configDebugPrefix + "AutoLogin : " + getAutoLogin);
	console.log(configDebugPrefix + "버전 : " + getVersion);
}

function errorConfigInfoCB(err) {
	console.log(configDebugPrefix + "============ 사용자 설정 : update error ============");
}

function checkField(value) {
	console.log(configDebugPrefix + "============ 설정 정보 파라미터 ============");
	console.log(configDebugPrefix + value);
	
	updateConfigInfo();
}

//업데이트
function updateConfigInfo() {
	db.transaction(queryUpdateConfigInfoDB, errorConfigInfoCB);
}

function queryUpdateConfigInfoDB(tx) {
	var userAutoLogin = $("#config_autoLogin").val();
	var userStartPage = $("#config_startPage").val();
	
//	DB field
//	(userID, userPW, startPage, autoLoginYN, locationYN, pushYN, version)
	var sql = "UPDATE MOSS_USERINFO SET startPage='"+ userStartPage +"', autoLoginYN='"+ userAutoLogin +"' where version = '"+userVersion+"';";
	console.log(configDebugPrefix + "SQL = " + sql);
	
	tx.executeSql(sql, [], updateConfigInfoSuccess, errorConfigInfoCB);
}

function updateConfigInfoSuccess(tx, results) {
	console.log(configDebugPrefix + "============ 사용자 설정 : update Success ============");
}

$(document).on("pagebeforecreate", "#page_config" ,function(event) {
	if (!db) {
		db = window.openDatabase("Database", "1.0", "myDb", 200000);
	}
	//	메뉴패널 생성, 반드시 pagebeforecreate 이벤트단에서 호출한다.
	commonCreateMenuPanel();
});

$(document).on("pagebeforeshow", "#page_config" ,function(event) {
	//	사용자 설정 정보를 읽어온다.
	getConfigInfoResultSet();
});

$(document).on("pageshow", "#page_config", function(event) {
	//	페이지에 따른 메뉴패널 설정
	commonConfigMenuPanel();
	//	페이지 위젯 초기화
	$('#page_config').trigger('pagecreate');
});