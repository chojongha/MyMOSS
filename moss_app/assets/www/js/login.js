/**
 * login.js
 */
var loginDebugPrefix = "[login.js] : ";
var userID = null;
var userPW = null;
var userStartPage = "index.html";
var userAutoLogin = "N";
var userLocation = "Y";
var userPush = "Y";
var doLoginFlag = true;
var deviceId = "";
var deviceType = "";
var loginFlag = 1; // 인증 시 0값
var danConfigFlag = false;
var versionCheckFlag = false;

//로그인 유저 정보
var loginId = "";
var userName = "";
var departmentCd = "";
var departmentName = "";
var userTypeCD = "";
var companyCd = "";
var mobilePhoneNum = "";
var email = "";

// 테이블 생성 sql
function populateDB(tx) {
	// tx.executeSql('DROP TABLE IF EXISTS MOSS_USERINFO');
	tx.executeSql("CREATE TABLE IF NOT EXISTS MOSS_USERINFO (userID, userPW, startPage, autoLoginYN, locationYN, pushYN, fileName, version)");
	tx.executeSql("INSERT INTO MOSS_USERINFO (userID, userPW, startPage, autoLoginYN, locationYN, pushYN, fileName, version) VALUES ('"+ userID +"','"+ userPW +"','"+ userStartPage +"','"+ userAutoLogin +"','"+ userLocation +"','"+ userPush +"','"+ fileName +"','" + userVersion +"')");
}

function errorCB(err) {
	console.log(loginDebugPrefix + "db error");
	console.log(loginDebugPrefix + "============ 로그인 정보 : DB error ============");
}

function successCreateCB() {
	console.log(loginDebugPrefix + "============ 로그인 정보 : DB create ============");
	// table 생성이 완료 되면user 정보 읽기
	getSqlResultSet();
}

function createDB() {
	db.transaction(populateDB, errorCB, successCreateCB);
}

// 로그인 유저 테이블 조회
function selectDbSuccess(tx, results) {
	userID = results.rows.item(0).userID;
	userPW = results.rows.item(0).userPW;
	userStartPage = results.rows.item(0).startPage;
	userAutoLogin = results.rows.item(0).autoLoginYN;
	userLocation = results.rows.item(0).locationYN;
	userPush = results.rows.item(0).pushYN;
	fileName = results.rows.item(0).fileName;
	var userDbVersion = results.rows.item(0).version;
	
	if(userVersion != userDbVersion) {
		var sql = "UPDATE MOSS_USERINFO SET version='"+ userVersion +"' where version = '"+userDbVersion+"';";
		console.log(configDebugPrefix + "SQL = " + sql);
		tx.executeSql(sql, [], updateConfigInfoSuccess, errorConfigInfoCB);
	}
	
	var msg = "아이디 : " + userID + "\n" + "패스워드 : " + userPW + "\n" + "시작페이지 : " + userStartPage + "\n" + "AutoLogin : " + userAutoLogin + "\n" + "위치정보 : " + userLocation + "\n" + "푸시알림 : " + userPush + "\n" + "푸시알림 : " + fileName + "\n" + "버전 : " + userVersion;
	console.log(loginDebugPrefix + msg);
	
	// 로그인 정보를 모두 확인 한 후 최종적으로 버전 체크
	// Ajax async : false
	compareToVersion();
	
	// 로그인 시 사번 입력
	if("null" != userID) {
		$("#login_id").val(userID);
	} else {
//		통합NMS 계정 등록 안내 문구 팝업
		showCommonAlert("KT MOSS", '"KT MOSS"에 로그인하시기 전\n반드시 통합NMS에 접속하여 [Follower설정]을 완료해주시기 바랍니다.');
	}
	// DB 정보에 따른 자동로그인 슬라이더 값 할당
	$("#login_auto").val(userAutoLogin).slider( "refresh" );	
}

// 업데이트
function updateSuccess(tx, results) {
	console.log(loginDebugPrefix + "============ 로그인 정보 : update Success ============");
	// 로그인 인증 완료 후 업데이트까지 완료 한 뒤 메인 페이지로 이동
	if(danConfigFlag) {
		$.mobile.changePage("index.html");
	} else {
		$.mobile.changePage(userStartPage);
	}
}

// 조회
function querySelectDB(tx) {
	tx.executeSql('SELECT * FROM MOSS_USERINFO', [], selectDbSuccess, errorCB);
}

function getSqlResultSet() {
	db.transaction(querySelectDB, errorCB);
}

//업데이트 sql
function queryUpdateDB(tx) {
//	(userID, userPW, startPage, autoLoginYN, locationYN, pushYN, version)
	var sql = "UPDATE MOSS_USERINFO SET userID = '"+ userID +"', userPW = '"+ userPW +"', startPage='"+ userStartPage +"', autoLoginYN='"+ userAutoLogin +"', locationYN='Y', pushYN='Y', version='" + userVersion + "' where version = '" + userVersion + "';";
	console.log(loginDebugPrefix + sql);
	tx.executeSql(sql, [], updateSuccess, errorCB);
}

function updateUserInfo() {
	db.transaction(queryUpdateDB, errorCB);
}

//	로그인 처리
function doLogin () {
	
	if(!doLoginFlag) {
		console.log(loginDebugPrefix + "=========== 로그인 프로세스 중복 실행 방지 !! ===========");
		return;
	} else {
		doLoginFlag = false;
	}
	
	if(!versionCheckFlag) {
		showUpgradeConfirm();
		doLoginFlag = true;
		return;
	}
	
	// 로그인 시 입력 체크
	if($("#login_id").val().length == "") {
		showCommonAlert("로그인", '사번을 입력하십시오');
		doLoginFlag = true;
		return;
	} 
	else if($("#login_pw").val().length == "") {
		showCommonAlert("로그인", '비밀번호를 입력하십시오');
		doLoginFlag = true;
		return;
	}
	
	// 페이지가 보여지면서 로더를 보여준다.
	$.mobile.loading( "show", {
		  text: "로그인 요청 중...",
		  textVisible: true,
		  theme: "a",
		  html: ""
	});
	
	if(!true == check_network()) {
		$.mobile.hidePageLoadingMsg();
		showCommonAlert("로그인", "네트워크 연결이 불안정합니다\n잠시후에 다시 시도 해주세요.");
	} else {
		userID = $("#login_id").val();
		userPW = $("#login_pw").val();
		userAutoLogin = $("#login_auto").val();
	    
		// 암호화 key
		var key = "1234567890abcdef";
		// aes 인코딩
		var encryptionPW = AES_Encode(userPW);
		console.log(loginDebugPrefix + "encryption Password =" + encryptionPW);
		
		function AES_Encode(plain_text)
		{
			GibberishAES.size(128);
			return GibberishAES.aesEncrypt(plain_text, key);
		}
		
		console.log(loginDebugPrefix + "ID : " + userID + ", Pw : " + userPW + ", AutoLoginFlag : " + userAutoLogin);
		
	    // 로그인
		$.ajax({
			url: url + "Login",
			type: "post",
			timeout: commonTimeout,
			dataType: "json",
			data: {userID:userID, userPW:encryptionPW},
			success: function (result) {
				var headerData = result.header;
				// header log
				console.log(loginDebugPrefix + "########### Header Data : Login  ###########");
				$.each(headerData, function(key, val){
					console.log(loginDebugPrefix + key + " : " + val);
				});
				
				if(headerData.result != 0) {
					$.mobile.hidePageLoadingMsg();
					showCommonAlert("로그인 인증 실패", headerData.errMsg);
					//	로그인 프로세스 완료 후 플래그 변경
					doLoginFlag = true;
				} 
				else {
					var bodyData = result.body;
					// 디바이스 인증 시 로그인 성공 여부를 판단하여 수행하기 위한 flag
					loginFlag = headerData.result;
					
					$.each(bodyData, function(key, val){
						console.log(loginDebugPrefix + "========= Login Response Data =========");
						$.each(val, function(key, val){
							console.log(loginDebugPrefix + key + " : " + val);
					    });
						
						console.log(loginDebugPrefix + "========= 로그인 인증 성공 =========");
						// 로그인 후 파라미터로 사용될 유저 정보 셋팅
						loginId = val.loginId;
						userName = val.userName;
						departmentCd = val.departmentCD;
						userTypeCD = val.userTypeCD;
						departmentName = val.departmentName;
						companyCd = val.companyCD;
						mobilePhoneNum = val.mobilePhoneNum;
						email = val.email;
				    });
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				$.mobile.hidePageLoadingMsg();
				showCommonAlert("로그인", textStatus);
		        console.log(loginDebugPrefix + jqXHR.responseText);
		        console.log(loginDebugPrefix + jqXHR.status);
		        console.log(loginDebugPrefix + errorThrown);
				//	로그인 프로세스 완료 후 플래그 변경
				doLoginFlag = true;
		    }
		}).done(function() {
			//	로그인 프로세스 완료 후 플래그 변경
			doLoginFlag = true;
			
			if(loginFlag != 0) {
				console.log(loginDebugPrefix + "======= 로그인 인증 실패 : 디바이스 등록 프로세스 중지 =======");
			} else {
				//	디바이스 인증 프로세스 가동
				setDeviceInfo(null);
			}
		});
	}
}

var showUpgradeConfirm = function() {
	if(confirmFlag) {
		confirmFlag = false;
		function versionConfirm(button) {
			if (button == 1) {
				if(getDeviceInfo() == "ios") {
					window.open("http://moss.kt.com/moss");
				} else {
					callDownloadPage();
				}
			}
			confirmFlag = true;
		}
	}

	navigator.notification.confirm(
		'최신버전으로 업데이트 하세요.\n확인 버튼을 누르면 설치 페이지로 이동합니다.', // message
		versionConfirm, // callback to invoke with index of button pressed
		'업데이트 안내', // title
		'확인' // buttonLabels
	);
};

//	디바이스 정보 등록
function setDeviceInfo(danOrgCode) {
	// 	로그인 로그아웃 시 화면이 생성 되며 액티비티로 또는 웹페이지에서 get Parameter로 registration id를 받는다.
	// 	안드로이드의 경우 액티비티에서 파라미터로 디바이스 아이디를 전달 받고
	//	iOS의 경우 AppDelegate.m 에서 생성된 파일을 읽는다.
	var deviceType = getDeviceInfo();
	
    if(deviceInfo == "Android" && deviceId == "") {
    	deviceId = commonRequest('getRegId');
    }
    
    //	deviceId 가 정상적으로 수집되지 않았을 경우, 사용자에게 재 로그인을 유도하는 경고창을 출력한다.
    if(deviceId == "" || deviceId == null || deviceId == "undefined") {
    	showCommonAlert("디바이스 인증", "디바이스 아이디가 정상적으로 수집되지 않았습니다. 푸시 메세지 수신을 위해 앱을 완전히 종료한 뒤 다시 실행해주시기 바랍니다.");
    }
    
    console.log(loginDebugPrefix + "userID = " + userID);
    console.log(loginDebugPrefix + "deviceType = " + deviceType);
    console.log(loginDebugPrefix + "deviceId = " + deviceId);
    
	// 사용자 기기 등록
    if(danOrgCode == null) {
    	console.log(loginDebugPrefix + "======= if(danOrgCode == null) =======");
    	$.ajax({
    		url: url + "Register",
    		type: "post",
    		timeout: commonTimeout,
    		dataType: "json",
    		data: {workerId:userID, deviceId:deviceId, deviceType:deviceType},
    		success: function (result) {
    			
    			var headerData = result.header;
    			// header log
    			console.log(loginDebugPrefix + "########### Header Data : Device register  ###########");
    			
    			$.each(headerData, function(key, val){
    				console.log(loginDebugPrefix + key + " : " + val);
    			});

    			if(headerData.result != 0) {
    				$.mobile.hidePageLoadingMsg();
    				if(headerData.result == 1) {
    					console.log(loginDebugPrefix + "======= 로그인 인증 성공 : 디바이스 등록 성공 =======");
    					//	결과 코드 1의 경우, 단 설정이 되지 않은 사용자이므로 팝업 플래그를 설정한다.
    					danConfigFlag = true;
    					// 로그인 및 디바이스 인증 완료 후 로그인 정보 업데이트하고 페이지 이동
    					updateUserInfo();	
    				}
//    				showCommonAlert("디바이스 인증", headerData.errMsg);
    				console.log(loginDebugPrefix + headerData.errMsg);
    			} 
    			else { 
    				$.mobile.hidePageLoadingMsg();
    				console.log(loginDebugPrefix + "======= 로그인 인증 성공 : 디바이스 등록 성공 =======");
					updateUserInfo();						// 로그인 및 디바이스 인증 완료 후 로그인 정보 업데이트하고 페이지 이동
    			}
    		},
    		error: function(jqXHR, textStatus, errorThrown) {
    			$.mobile.hidePageLoadingMsg();
    			showCommonAlert("디바이스 인증", textStatus);
    	        console.log(loginDebugPrefix + jqXHR.responseText);
    	        console.log(loginDebugPrefix + jqXHR.status);
    	        console.log(loginDebugPrefix + errorThrown);
    	    }
    	});
    } else {
    	console.log(loginDebugPrefix + "======= if(danOrgCode != null) =======");
    	console.log(loginDebugPrefix + "======= if(danOrgCode != null) =======");
    	console.log(loginDebugPrefix + "======= if(danOrgCode != null) =======");
    	console.log(loginDebugPrefix + "======= "+ danOrgCode +" =======");
    	console.log(loginDebugPrefix + "======= if(danOrgCode != null) =======");
    	console.log(loginDebugPrefix + "======= if(danOrgCode != null) =======");
    	$.ajax({
    		url: url + "Register",
    		type: "post",
    		timeout: commonTimeout,
    		dataType: "json",
    		data: {workerId:userID, deviceId:deviceId, deviceType:deviceType, danorgcode:danOrgCode},
    		success: function (result) {
    			
    			var headerData = result.header;
    			// header log
    			console.log(loginDebugPrefix + "########### Header Data : DanOrgCode register  ###########");
    			
    			$.each(headerData, function(key, val){
    				console.log(loginDebugPrefix + key + " : " + val);
    			});

    			if(headerData.result != 0) {
    				$.mobile.hidePageLoadingMsg();
    				showCommonAlert("디바이스 인증", headerData.errMsg);
    				
    				if(headerData.result == 1) {
        				$.mobile.hidePageLoadingMsg();
        				console.log(loginDebugPrefix + "======= 로그인 인증 성공 : 디바이스 등록 성공 =======");
        				console.log(loginDebugPrefix + "======= 단코드 등록 성공 : "+ danOrgCode +" =======");
        				//	등록완료 시 단 설정 팝업 플래그를 변경하여 팝업되지 않도록 한다.
        				danConfigFlag = false;
    				}
    			} else {
    				$.mobile.hidePageLoadingMsg();
    				console.log(loginDebugPrefix + "======= 로그인 인증 성공 : 디바이스 등록 성공 =======");
    				console.log(loginDebugPrefix + "======= 단코드 등록 성공 : "+ danOrgCode +" =======");
    				//	등록완료 시 단 설정 팝업 플래그를 변경하여 팝업되지 않도록 한다.
    				danConfigFlag = false;
    			}
    		},
    		error: function(jqXHR, textStatus, errorThrown) {
    			$.mobile.hidePageLoadingMsg();
    			showCommonAlert("디바이스 인증", textStatus);
    	        console.log(loginDebugPrefix + jqXHR.responseText);
    	        console.log(loginDebugPrefix + jqXHR.status);
    	        console.log(loginDebugPrefix + errorThrown);
    	    }
    	});
    }
}

// 버전 관리 
function compareToVersion() {
	//	버전정보 체크 시 OS에 따른 파일명 교체
	if(deviceInfo != "Android") {
		fileName = "moss_app.ipa";
	}
	
	console.log(loginDebugPrefix + "=========== compareToVersion : fileName  ===========");
	console.log(loginDebugPrefix + fileName);
	
	$.ajax({
		url: url + "SwVersion",
		type: "post",
		timeout: commonTimeout,
		dataType: "json",
		//파라미터 swname 추가. 추가 데이터는  앱 파일 명.
		data: {swname:fileName},
		success: function (result) {
			var headerData = result.header;
			// header log
			console.log(loginDebugPrefix + "########### Header Data : Version check  ###########");
			$.each(headerData, function(key, val){
				console.log(loginDebugPrefix + key + " : " + val);
			});

			if(headerData.result != 0) {
				showCommonAlert("버전관리", headerData.errMsg);
			} else {
				var bodyData = result.body;
				$.each(bodyData, function(key, val){
					console.log(loginDebugPrefix + "=========== compareToVersion ===========");
					$.each(val, function(key, val){
						console.log(loginDebugPrefix + key + " : " + val);
				    });
					
					var currentVer = userVersion.split(".");
					var newVer = val.SWVERSION.split(".");
					//	버전 관리 시 자리수 맞춤 (예외처리)
					var strIdx, endIdx;
					if (currentVer.length > newVer.length) {
						strIdx = newVer.length;
						endIdx = currentVer.length;
						for (var i=strIdx; i<endIdx; i++ ) newVer[i] = 0;
					} else if (currentVer.length < newVer.length) {
						strIdx = currentVer.length;
						endIdx = newVer.length;
						for (var i=strIdx; i<endIdx; i++ ) currentVer[i] = 0;
					}
					
					console.log(loginDebugPrefix + "=========== currentVersion =========== : " + currentVer);
					console.log(loginDebugPrefix + "=========== NewVersion =============== : " + newVer);
					
					var updateYN = false;		// 업데이트 불 필요
					for(var i=0; i<currentVer.length; i++){
						if (parseInt(currentVer[i]) < parseInt(newVer[i])) {
							updateYN = true;
							break;
						}
					}
					
//					if(parseInt(currentVer) < parseInt(newVer)) {
					if( updateYN == true ) {
						//	로그인 유도 팝업
						showUpgradeConfirm();
					} else {
						// 현재 버전이 최신 버젼일 경우에만 오토로그인 기능 수행
						// 자동로그인 여부에 따른 패스워드 입력
						// 버전체크 플래그 수정
						versionCheckFlag = true;
						
						if("Y" == userAutoLogin) {
							console.log(loginDebugPrefix + "=========== compareToVersion ========= : 현재 최신 버전 입니다. 오토로그인 실행...");
							$("#login_pw").val(userPW);
							doLogin();
						}
					}
			    });
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			$.mobile.hidePageLoadingMsg();
	        showCommonAlert("버전관리", textStatus);
	        console.log(loginDebugPrefix + jqXHR.responseText);
	        console.log(loginDebugPrefix + jqXHR.status);
	        console.log(loginDebugPrefix + errorThrown);
	    }
	});
}

$(document).on("pageshow", "#page_login", function(event) {
	// 로그인 액션
	$("#btn_submit").click(function() {
		doLogin();
	});
	//	OS별 기능에 따른 이벤트 분리
	$("#btn_cancel").click(function() {
		if(deviceInfo == "Android") {
			showExitConfirm();
		}else {
			showiOSAlert();
		}
	});
	// 디바이스별 크기에 따라 margin 처리
	if($( window ).width() > 320) {
		$("#login_margin").css( { marginLeft : "3.5em"} );
	} else {
		$("#login_margin").css( { marginLeft : "2.5em"} );
	}
});
$(document).on("pagebeforecreate", "#page_login", function(event) {	
	if (!db) {
		db = window.openDatabase("Database", "1.0", "myDb", 200000);
	}
	// user 정보 생성
	createDB();
});
$(document).on("pagehide", "#page_login", function(event) {
    //	iOS의 경우 로그아웃 -> 로그인 페이지 전환 후 입력폼 초기화
    if(deviceType != "Android") {
    	$("#login_pw").val("");
    	$("#login_auto").val("N").slider( "refresh" );
    }
});