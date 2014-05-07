/**
 * common.js
 */

var commonDebugPrefix = "[common.js] : ";
//버전관리용 변수, moss.kt.com/moss에 업로드 시 버전과 동일한 버전으로 변경 필요 !!
//버전관리용 변수, moss.kt.com/moss에 업로드 시 버전과 동일한 버전으로 변경 필요 !!
//버전관리용 변수, moss.kt.com/moss에 업로드 시 버전과 동일한 버전으로 변경 필요 !!

var fileName = "moss_app.apk";
var userVersion = "1.1.3";
			
//버전관리용 변수, moss.kt.com/moss에 업로드 시 버전과 동일한 버전으로 변경 필요 !!
//버전관리용 변수, moss.kt.com/moss에 업로드 시 버전과 동일한 버전으로 변경 필요 !!
//버전관리용 변수, moss.kt.com/moss에 업로드 시 버전과 동일한 버전으로 변경 필요 !!
var deviceInfo = "";
var userID = "";
var db = 0;
var currentLat = "";
var currentLng = "";
var watchID = null;
var appCheckTimer = null;
//	개발 서버 URL
//var url = "http://192.168.25.7:8080/moss/";
//var url = "http://192.168.25.18:8080/moss/";
//	TEST 서버 URL
//var url = "http://10.217.76.186:8081/moss/";
//	운용 서버 URL
var url = "http://moss.kt.com/moss/";
// 올레네비 미설치시 설치 유도 url 
var appstoreUrl = "https://itunes.apple.com/kr/app/id390369834";
var googlePlayUrl = "https://play.google.com/store/apps/details?hl=ko&id=kt.navi";
// 상황보고용 코드테이블 값 배열
var moveStateCode = new Array();
var moveStateCodeVal = new Array();
//	단코드 값 배열
var commonDanCodeArray = null;
var commonDanNameArray = null;
//	페이지별 현재위치 전송 팝업 Flag
var setLocPopupFlag = true;
//	폰갭 버그로 인한 확인창 중복 호출 방지 Flag
var confirmFlag = true;
//	올레맵 객체
var ollehMap = null;
//	타임아웃 시간 정의
//	타임아웃 시간 정의
//	타임아웃 시간 정의
var commonTimeout = 30 * 1000;
//	타임아웃 시간 정의
//	타임아웃 시간 정의
//	타임아웃 시간 정의

// IPv4 유효성 체크
function checkIP(strIP) {
    var expUrl = /^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/;
    
    if(expUrl.test(strIP) != true) {
    	return false;
    } else {
    	return true;
    }
}

//	menuPanel Content를 생성한다.
function commonCreateMenuPanel() {
	var panelText = "<div class='panel-content'>" +
	"<div class='ui-grid-a' id='menuPanel_header'>" +
	"<div class='ui-block-a'>" +
	//	홈 버튼
	"<a href='index.html' data-role='button' data-theme='a' id='btn_panelHome'>" +
	"<img src='../img/menu_home_o.png'>" +
	"<p>홈</p>" +
	"</a>" +
	"</div>" +
	"<div class='ui-block-b'>" +
	"<a href='#' data-role='button' data-theme='a' onclick='logoutConfirm();'>" +
	"<img src='../img/menu_logout_n.png'>" +
	"<p>로그아웃</p>" +
	"</a>" +
	"</div>" +
	"</div>" +
	"<div data-role='collapsible-set'>" +
	"<div data-role='collapsible' id='collapsible_1' data-collapsed='true' data-theme='a' data-content-theme='d' data-iconpos='right'>" +
	"<h4>상황공유</h4>" +
	"<ul data-role='listview' id='menuItemPadding'>" +
	"<li>" +
	//	고장상황 진행 버튼
	"<a href='alarmList_noti.html' id='btn_panelOccur'>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/menu_occur_n.png'></div>" +
	"<div><p>고장상황 진행</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"<li>" +
	//	고장상황 완료 버튼
	"<a href='alarmList_success.html' id='btn_panelSuccess'>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/menu_success_n.png'></div>" +
	"<div><p>고장상황 완료</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"<li>" +
	//	TT목록 버튼
	"<a href='alarmList_TFMS.html' id='btn_panelTfms'>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/menu_TFMS_TT_n.png'></div>" +
	"<div><p>TT 목록</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"<li>" +
	"<a href='#' onclick='setLocation();'>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/menu_location_n.png'></div>" +
	"<div><p>현재위치전송</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"</ul>" +
	"</div>" +
	"<div data-role='collapsible' id='collapsible_2' data-collapsed='true' data-theme='a' data-content-theme='d' data-iconpos='right'>" +
	"<h4>커뮤니티</h4>" +
	"<ul data-role='listview' id='menuItemPadding'>" +
	"<li>" +
	//	공지사항 버튼
	"<a href='board_notice.html' id='btn_panelNotice'>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/menu_notice_n.png'></div>" +
	"<div><p>공지사항</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"<li>" +
	//	업무매뉴얼 버튼
	"<a href ='board_manual.html' id='btn_panelManual'>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/menu_manual_n.png'></div>" +
	"<div><p>업무매뉴얼</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"<li>" +
	//	자유게시판 버튼
	"<a href ='board_open.html' id='btn_panelFreeBoard'>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/menu_freeBoard_n.png'></div>" +
	"<div><p>자유게시판</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"</ul>" +
	"</div>" +
	"<div data-role='collapsible' id='collapsible_3' data-collapsed='true' data-theme='a' data-content-theme='d' data-iconpos='right'>" +
	"<h4>부가기능</h4>" +
	"<ul data-role='listview' id='menuItemPadding'>" +
	"<li>" +
	//	사업장 정보 버튼
	"<a href='transOffice.html' id='btn_panelOffice'>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/menu_notice_n.png'></div>" +
	"<div><p>사업장 정보</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"<li>" +
	//	설정 버튼
	"<a href='config.html' id='btn_panelConfig'>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/menu_config_n.png'></div>" +
	"<div><p>설정</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"</ul>" +
	"</div>" +
	"</div>" +
	"<ul data-role='listview' data-inset='true' data-iconpos='right' id='menuItemPadding'>" +
	"<li>" +
	"<a href='#' onclick=callIntent('ktConsole');>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/mainimg04.png' id='panel_img_ktConsole'></div>" +
	"<div><p>원격콘솔</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"<li>" +
	"<a href='#' onclick=callIntent('qtest');>" +
	"<div id='menuPanel_collapsible'>" +
	"<div><img src='../img/mainimg05.png' id='panel_img_ktConsole'></div>" +
	"<div><p>품질측정</p></div>" +
	"</div>" +
	"</a>" +
	"</li>" +
	"</ul>" +
	"</div>";
	//	페이지 생성 시 고유한 Panel ID 부여를 위해 ms를 사용
	var d = new Date();
	var n = d.getMilliseconds();
	
	console.log(commonDebugPrefix + "============== MenuPanel Create ==============");
	console.log(commonDebugPrefix + "Current Panel id : " + $('#menuPanel').attr('id'));
	console.log(commonDebugPrefix + "Current href : " + $('.header a:first-child').attr('href'));
	
	//	고유한 Panel ID를 부여
	$('#menuPanel').attr('id', 'menuPanel' + n);
	//	고유한 Panel ID를 호출하는 링크를 부여
	$('.header a:first-child').attr('href', "#menuPanel" + n);
	
	console.log(commonDebugPrefix + "============== MenuPanel Generated ==============");
	console.log(commonDebugPrefix + "Current Panel id : " + $('#menuPanel' + n).attr('id'));
	console.log(commonDebugPrefix + "Current href : " + $('.header a:first-child').attr('href'));
	//	메뉴패널 생성
	$('#menuPanel' + n).html(panelText);
}

//	페이지에 따른 메뉴모음 리스트 펴기/접기 설정
function commonConfigMenuPanel() {
	//	** 메인화면 **
	if($.mobile.activePage.attr('id') == "page_index") {
		$('#btn_panelHome').addClass('ui-btn-active');
		$('#collapsible_1').attr('data-collapsed', false);
	}
	//	** 상황공유 **
	//	고장상황 진행
	else if($.mobile.activePage.attr('id') == "page_alarmList_noti") {
		$('#btn_panelOccur').addClass('ui-btn-active');
		$('#collapsible_1').attr('data-collapsed', false);
	}
	//	고장상황 완료
	else if($.mobile.activePage.attr('id') == "page_alarmList_success") {
		$('#btn_panelSuccess').addClass('ui-btn-active');
		$('#collapsible_1').attr('data-collapsed', false);
	}
	//	TT목록
	else if($.mobile.activePage.attr('id') == "page_alarmList_TFMS") {
		$('#btn_panelTfms').addClass('ui-btn-active');
		$('#collapsible_1').attr('data-collapsed', false);
	}
	//	** 커뮤니티 **
	//	공지사항
	else if($.mobile.activePage.attr('id') == "page_notice_list") {
		$('#btn_panelNotice').addClass('ui-btn-active');
		$('#collapsible_2').attr('data-collapsed', false);
	}
	//	업무매뉴얼
	else if($.mobile.activePage.attr('id') == "page_manualList") {
		$('#btn_panelManual').addClass('ui-btn-active');
		$('#collapsible_2').attr('data-collapsed', false);
	}
	//	자유게시판
	else if($.mobile.activePage.attr('id') == "page_board_open") {
		$('#btn_panelFreeBoard').addClass('ui-btn-active');
		$('#collapsible_2').attr('data-collapsed', false);
	}
	//	** 부가기능 **
	//	사업장 정보
	else if($.mobile.activePage.attr('id') == "page_transOffice") {
		$('#btn_panelOffice').addClass('ui-btn-active');
		$('#collapsible_3').attr('data-collapsed', false);
	}
	//	설정
	else if($.mobile.activePage.attr('id') == "page_config") {
		$('#btn_panelConfig').addClass('ui-btn-active');
		$('#collapsible_3').attr('data-collapsed', false);
	}
}

//	올레맵용 좌표 변환 UTM_K >> WGS84
function transformLatLng(longitude, latitude){
	var srcproj = new olleh.maps.Projection("UTM_K");
	var destproj = new olleh.maps.Projection("WGS84");

	var ollehMapPoint = new olleh.maps.Point(longitude, latitude);
	//	좌표계 변환 : UTM_K >> WGS84
	olleh.maps.Projection.transform(ollehMapPoint, destproj, srcproj);
	var ollehMapLat = ollehMapPoint.y;
	var ollehMapLng = ollehMapPoint.x;
	
	return new olleh.maps.Coord(ollehMapLng, ollehMapLat);
}

//	맵이 들어갈 div의 사이즈를 계산한다.
function setDynamicDivSize(id) {
//	지도가 들어갈 div 사이즈를 계산하여 설정한다.
	var header = $("div[data-role='header']:visible");
	var footer = $("div[data-role='footer']:visible");
	var content = $("div[data-role='content']:visible");
	var viewport_height = $(window).height();
	var viewport_width = $(window).width();
	
	var content_height = viewport_height - header.outerHeight() - footer.outerHeight();
	/* Trim margin/border/padding height */
	content_height -= (content.outerHeight() - content.height());
	
	var divId = "#" + id;
	//	맵이 그려질 div 포지션을 셋팅한다.
	$(divId).css('position', 'absolute');
	$(divId).css('left', 0);
	
	// 디바이스 사이즈 따라 동적으로 map height를 설정
	$(divId).css('min-height', content_height);
	// 디바이스 사이즈에 따라 동적으로 map width를 설정
	$(divId).css('width', viewport_width);
	// 지도를 header 사이즈 만큼 아래에 위치한다.
	$(divId).css('top', header.outerHeight());
	// 지도를 footer 사이즈 만큼 위에 위치한다.
	$(divId).css('bottom', footer.outerHeight());
}

//올레맵을 화면에 그린다.
function showOllehMap(divId, lng, lat) {
	// 디바이스 크기를 계산하여 맵 크기를 셋팅, 인자로 div id를 넘긴다.
	setDynamicDivSize(divId);
	// 올레맵용 좌표 변환 UTM_K >> WGS84, 인자로 경도, 위도를 넘긴다.
	var coordinate = transformLatLng(lng, lat);
	//	맵 옵션 설정
	var mapOptions = {  		
  	center : coordinate,
  	zoom : 10,
		panControl : false,
		zoomControl : true,
		scaleControl : true,
  	mapTypeId : olleh.maps.MapTypeId.BASEMAP
	};
	//	맵을 해당 div에 그린다.
	ollehMap = new olleh.maps.Map(document.getElementById(divId), mapOptions);
	//	infoWindow contentString 터치 시 이벤트 발생
	
	var contentString = "";
	
	//	호출되는 페이지에 따라 인포윈도우의 내용을 달리한다.
	if($.mobile.activePage.is('#page_alarmList_detail_map')) {
		//	사업장 정보가 없는 경우를 체크
		if(detail_workPlaceSeqNum == "-" || detail_workPlaceSeqNum == null || detail_workPlaceSeqNum == "null") {
			contentString = "<p style='font:12px/1.5 sans-serif; color:blue; text-decoration:underline;' onclick='showOfficeAlert();'>" + setOfficeName + "</p>";
		} else {
			contentString = "<p style='font:12px/1.5 sans-serif; color:blue; text-decoration:underline;' onclick='changePageOfficeDetail("+detail_workPlaceSeqNum+");'>" + setOfficeName + "</p>";
		}
		
	} 
	else if($.mobile.activePage.is('#page_transOffice_detail_map')) {
		contentString = "<p style='font:12px/1.5 sans-serif;'>" + mapOfficeName + "</p>";
	}
	
	var infowindow = new olleh.maps.InfoWindow({ 
	    content: contentString
	});
	
	//	marker icon 이미지, 사이즈, 위치 설정
	var icon = new olleh.maps.MarkerImage(
		'../img/map_office.png',
		new olleh.maps.Size(20, 32),
		new olleh.maps.Pixel(0,0),
		new olleh.maps.Pixel(0,32)
	);
	var shadow = new olleh.maps.MarkerImage(
		'../img/map_office_shadow.png',
		new olleh.maps.Size(37, 32),
		new olleh.maps.Pixel(0,0),
		new olleh.maps.Pixel(0,32)
	);
	//	map에 마커를 셋팅
	var marker = new olleh.maps.Marker({ 
	    position: coordinate,
	    icon: icon,
	    shadow: shadow,
	    map: ollehMap
	});
	//	마커에 클릭 이벤트 할당 Android의 경우 click 이벤트, ios의 경우 touchstart 이벤트를 사용한다.
	if (deviceInfo != "Android") {
		olleh.maps.event.addListener(marker, 'touchstart', function() { 
			  infowindow.open(ollehMap,marker); 
		});
	} 
	else {
		olleh.maps.event.addListener(marker, 'click', function() { 
			  infowindow.open(ollehMap,marker); 
		});
	}
}

//Request
function commonRequest(valuename) {
	var rtnval = "";
	var nowAddress = unescape(location.href);
	var parameters = (nowAddress.slice(nowAddress.indexOf("?")+1,nowAddress.length)).split("&");

	for(var i = 0 ; i < parameters.length ; i++) {
		var varName = parameters[i].split("=")[0];
		if(varName.toUpperCase() == valuename.toUpperCase())
		{
			rtnval = parameters[i].split("=")[1];
			break;
		}
	}
	
	//	파라미터를 리턴하기 전 비정상적인 파라미터의 경우 슬래쉬부터 쓰래기 값을 제거한다.
	if(rtnval.match('/')) {
		console.log(commonDebugPrefix + "============== Invalid getParameter !! ==============");
		rtnval = rtnval.substr(0, rtnval.indexOf('/'));
		console.log(commonDebugPrefix + "============== Return Parameter : " + rtnval + " ==============");
	}
	
	return rtnval;
}

// notification Confirm
function showiOSAlert() {
    navigator.notification.confirm(
    	'iOS에서는 지원되지 않는 기능입니다.',  		// message
        null, // callback
        'KT MOSS',          // title
        '확인'          // buttonName
    );
}

function showCommonAlert(title, message) {
	function showCommonAlertCallback(button) {
		console.log(commonDebugPrefix + "============== Alert paramter==============");
		console.log(commonDebugPrefix + "title : " + title);
		console.log(commonDebugPrefix + "message : " + message);
		
		confirmFlag = true;
    }
	if(confirmFlag) {
		confirmFlag = false;
		
	    navigator.notification.alert(
	        	message,  		// message
	        	showCommonAlertCallback, // callback
	            title,          // title
	            '확인'          // buttonName
	    );
	}
	console.log(commonDebugPrefix + "============== showCommonAlert ==============");
	console.log(commonDebugPrefix + "confirmFlag : " + confirmFlag);
}

var mapSetupConfirm = function() {
	// 컨펌 팝업 시 로더 제거
	$.mobile.hidePageLoadingMsg();
	
	function mapSetupConfirmCallback(button) {
		if(button == 2) {
			// OS별 예외처리 분기
    	 	if(deviceInfo == "Android") {
        		CDV.WEBINTENT.startActivity({
        		    action: CDV.WEBINTENT.ACTION_VIEW,
        		    url: googlePlayUrl}, 
        		    function() {}, 
        		    function() {
        		    	alert('Failed to open URL via Android Intent');
        		    }
        		);
    	 	} else {
    	 		window.location.href = appstoreUrl;
    	 	}
		}
	}
	navigator.notification.confirm(
		'올레네비가 설치되어 있지 않습니다.\n설치페이지로 이동하시겠습니까?', // message
		mapSetupConfirmCallback, // callback to invoke with index of button pressed
		'올레네비', // title
		'취소,확인'
	);
};

var installAppConfirm = function() {
	// 컨펌 팝업 시 로더 제거
	$.mobile.hidePageLoadingMsg();
	
	function mapSetupConfirmCallback(button) {
		if(button == 2) {
			// OS별 예외처리 분기
    	 	if(deviceInfo == "Android") {
        		CDV.WEBINTENT.startActivity({
        		    action: CDV.WEBINTENT.ACTION_VIEW,
        		    url: url}, //	http://moss.kt.com/moss/
        		    function() {}, 
        		    function() {
        		    	alert('Failed to open URL via Android Intent');
        		    }
        		);
    	 	} else {
    	 		window.location.href = url;	//	http://moss.kt.com/moss/
    	 	}
		}
	}
	navigator.notification.confirm(
		'어플리케이션이 설치되어 있지 않습니다.\n설치페이지로 이동하시겠습니까?', // message
		mapSetupConfirmCallback, // callback to invoke with index of button pressed
		'KT MOSS', // title
		'취소,확인'
	);
};

var logoutConfirm = function() {
	function logoutConfirmCallback(button) {
		if (button == 2) {
			logout();
		}
		confirmFlag = true;
	}
	if(confirmFlag) {
		confirmFlag = false;
		navigator.notification.confirm(
				'로그아웃 하시겠습니까?', // message
				logoutConfirmCallback, // callback to invoke with index of button pressed
				'로그아웃', // title
				'취소,확인' // buttonLabels
			);
	}
};

var showExitConfirm = function() {
	function showExitConfirmCallback(button) {
//		alert('You selected button ' + button);
		if (button == 2) {
			navigator.app.exitApp();
		}
		confirmFlag = true;
	}
	if(confirmFlag) {
		confirmFlag = false;
		navigator.notification.confirm(
				'KT MOSS 앱을 종료하시겠습니까?', // message
				showExitConfirmCallback, // callback to invoke with index of button pressed
				'종료', // title
				'취소,확인' // buttonLabels
			);
	}
};

//	로그아웃 액션
function logout() {
	if (!db) {
		db = window.openDatabase("Database", "1.0", "myDb", 200000);
	}
	// 로그아웃 시 기존 사용자의 설정을 초기화한다.
	db.transaction(userInfoInitDB, errorIntiInfoCB);
}

function userInfoInitSuccess(tx, results) {
	console.log(commonDebugPrefix + "============= 로그아웃 : 사용자 정보 초기화 완료 =============");
	//	로그인을 위해 로그아웃 시 GET파라미터로 regId를 전달.
	if(deviceInfo == "Android") {
		window.location.href = "login.html?getRegId=" + deviceId;
	} else {
		$.mobile.changePage("login.html");
	}
}

function errorIntiInfoCB(err) {
	console.log(commonDebugPrefix + "============= 로그아웃 : 사용자 정보 초기화 실패 =============");
}

function userInfoInitDB(tx) {
	var query = 	"UPDATE MOSS_USERINFO " +
					"SET " +
//					"USERID = null, " +
					"USERPW = null, " +
					"STARTPAGE = 'index.html', " +
					"AUTOLOGINYN = 'N' " +
					"WHERE " + 
					"VERSION = '" + userVersion + "';";
	var queryLog = 	"\nUPDATE MOSS_USERINFO\n" +
					"SET\n" +
//					"USERID = null,\n" +
					"USERPW = null,\n" +
					"STARTPAGE = 'index.html',\n" +
					"AUTOLOGINYN = 'N'\n" +
					"WHERE\n" + 
					"VERSION = '" + userVersion + "';";
	
	console.log(commonDebugPrefix + "============= 로그아웃 : 사용자 정보 초기화 SQL Start ===========");
	console.log(queryLog);
	console.log(commonDebugPrefix + "============= 로그아웃 : 사용자 정보 초기화 SQL END =============");
	tx.executeSql(query, [], userInfoInitSuccess, errorIntiInfoCB);
}

function commonDownloadURL(str) {
   if(device.platform == "Android"){
       location.href = url + str;
   } else {
       window.open(url + str, '_blank', 'EnableViewPortScale=yes');
   }
}

//파일 읽기
function gotFS(fileSystem) {
    fileSystem.root.getFile("moss_regId.txt", null, gotFileEntry, fail);
}

function gotFileEntry(fileEntry) {
    fileEntry.file(gotFile, fail);
}

function gotFile(file){
    readDataUrl(file);
    readAsText(file);
}

function readDataUrl(file) {
    var reader = new FileReader();
    reader.onloadend = function(evt) {
        console.log(loginDebugPrefix + "=============== Read as ** moss_regId.txt URL ** ===============");
        console.log(loginDebugPrefix + evt.target.result);
    };
    reader.readAsDataURL(file);
}

function readAsText(file) {
    var reader = new FileReader();
    reader.onloadend = function(evt) {
        console.log(loginDebugPrefix + "=============== Read as *** moss_regId.txt ** ===============");
        console.log(loginDebugPrefix + evt.target.result);
        
        deviceId = evt.target.result;
    };
    reader.readAsText(file);
}

function fail(error) {
    console.log(loginDebugPrefix + "=============== file error =============== code : " + error.code);
}

function nullCheck(data) {
	
	//	문자열로 변환하여 비교
	data = String(data);
	
	if(data == null || data == "" || data == "null") {
		return "-";
	} else {
		return data;
	}
}

function onCurrentPosSuccess(position) {
    currentLat = position.coords.latitude;
    currentLng = position.coords.longitude;
    
    console.log(commonDebugPrefix + "============== get Current Location ==============");
    console.log(commonDebugPrefix + "currentLat : " + currentLat);
    console.log(commonDebugPrefix + "currentLng : " + currentLng);
    
    if($.mobile.activePage.is('#page_alarmList_detail_map')){
    	//	안드로이드의 경우 출발~도착 지점의 위도, 경도를 사용하고, iOS의 경우 경도, 위도를 사용한다.
    	if(deviceInfo == "Android") {
    		callDaumIntent("ollehnavi://ollehnavi.kt.com/?method=routeguide&start=(" + currentLat + "," + currentLng + ")&end=(" + geocodeLat + "," + geocodeLng + ")");
    	} else {
    		callDaumIntent("ollehnavi://ollehnavi.kt.com/navigation.req?method=routeguide&start=(" + currentLng + "," + currentLat + ")&end=(" + geocodeLng + "," + geocodeLat + ")&response=ollehmap");
    	}
    } else if($.mobile.activePage.is('#page_transOffice_detail_map')) {
    	if(deviceInfo == "Android") {
    		callDaumIntent("ollehnavi://ollehnavi.kt.com/?method=routeguide&start=(" + currentLat + "," + currentLng + ")&end=(" + geocodeLat + "," + geocodeLng + ")");
    	} else {
    		callDaumIntent("ollehnavi://ollehnavi.kt.com/navigation.req?method=routeguide&start=(" + currentLng + "," + currentLat + ")&end=(" + geocodeLng + "," + geocodeLat + ")&response=ollehmap");
    	}
    }
}

function onCurrentPosError(error) {
    console.log(commonDebugPrefix + 'code: '    + error.code    + '\n' + 'message: ' + error.message + '\n');
    
    if(error.code == 2) {
    	var message = "";
    	if(deviceInfo == "Android") {
    		message = "위치서비스가 비활성화 되어있습니다.\n정상적인 사용을 위해\n환경설정 -> 더보기 -> 위치서비스로 이동하여 [무선 네트워크 사용]을\n활성화하시기 바랍니다.";
    	} else {
    		message = "위치서비스가 비활성화 되어있습니다.\n정상적인 사용을 위해\n설정 -> 개인 정보 보호 -> 위치 서비스로 이동하여\nMOSS의 위치서비스를 활성화하시기 바랍니다.";
    	}
    	showCommonAlert("위치정보 수집 실패", message);
    }
}

function getUserID() {
	return userID;
}

function callDaumIntent(url) {
	//앱 미설치시 예외처리 로직
	var clickedAt = +new Date;
	appCheckTimer = setTimeout(function() { 
	      if (+new Date - clickedAt < 3000){ 
	    	  mapSetupConfirm();
	      }
	}, 2 * 1000);
	
	// OS별 앱 호출 분기
	if(deviceInfo == "Android") {
		CDV.WEBINTENT.startActivity({
		    action: CDV.WEBINTENT.ACTION_VIEW,
		    url: url}, 
		    function() {}, 
		    function() {
		    	alert('Failed to open URL via Android Intent');
		    }
		);
	} else {
		window.location.href = url;
	}
}

function callIntent(appName) {
	if(deviceInfo == "Android") {
		//앱 미설치시 예외처리 로직, 안드로이드만 해당
		var clickedAt = +new Date;
		appCheckTimer = setTimeout(function() { 
		      if (+new Date - clickedAt < 3000){ 
		    	  installAppConfirm();
		      }
		}, 2 * 1000);
		
		if(appName == 'qtest') {
			CDV.WEBINTENT.startActivity({
			    action: CDV.WEBINTENT.ACTION_VIEW,
			    url: 'qtest://moss'}, 
			    function() {}, 
			    function() {
			    	alert('Failed to open URL via Android Intent');
			    }
			);
		} else if(appName == 'ktConsole') {
			CDV.WEBINTENT.startActivity({
			    action: CDV.WEBINTENT.ACTION_VIEW,
			    url: 'ktConsole://moss'}, 
			    function() {}, 
			    function() {
			    	alert('Failed to open URL via Android Intent');
			    }
			);
		}
	}else {
		showiOSAlert();
	}
}
//	업데이트 시 다운로드 페이지 호출
function callDownloadPage() {
	CDV.WEBINTENT.startActivity({
	    action: CDV.WEBINTENT.ACTION_VIEW,
	    url: 'http://moss.kt.com/moss'}, 
	    function() {}, 
	    function() {
	    	alert('Failed to open URL via Android Intent');
	    }
	);
}

function onPause() {
	if(interval_detail != null && $.mobile.activePage.is('#page_alarmList_detail')) {
		clearInterval(interval_detail); // stop the interval
		console.log(commonDebugPrefix + "========= on Pause ========= \n고장상황(진행) 상세 페이지 폴링 중지, Polling Number => " + interval_detail);
	}
	if($.mobile.activePage.is('#page_alarmList_detail_map')) {
		$.mobile.hidePageLoadingMsg();
	}
	if($.mobile.activePage.is('#page_notice_detail')) {
        $.mobile.hidePageLoadingMsg();
        filedownloadFlag=false;
    }
	// 올레네비 앱이 설치되었을 경우 올레네비가 호출되며 MOSS 앱은 onPause 상태로 전환 되며 예외처리 타이머를 종료
	if(appCheckTimer != null) {
		clearTimeout(appCheckTimer);
		console.log(commonDebugPrefix + "========= on Pause ========= \n 앱 설치됨");
	}
	if($.mobile.activePage.is('#page_manual_detail') || $.mobile.activePage.is('#page_alarmManual_detail')) {
		$.mobile.hidePageLoadingMsg();
		filedownloadFlag=false;
	}
	if($.mobile.activePage.is('#page_transOffice_detail_map')) {			
		$.mobile.hidePageLoadingMsg();		
	}
	if($.mobile.activePage.is('#page_board_open_detail')) {           
        $.mobile.hidePageLoadingMsg();      
    }
}

function onResume() {
	if($.mobile.activePage.is('#page_alarmList_detail')) {
		interval_detail = setInterval(function(){
			faultNotiPolling();
		}, 10*1000);
		console.log(commonDebugPrefix + "========= on Resume ========= \n고장상황(진행) 상세 페이지 폴링 시작, Polling Number => " + interval_detail);
	}
	// on Resume 시 리스트 재조회
	if($.mobile.activePage.is('#page_alarmList_noti')) {
		console.log(commonDebugPrefix + "========= on Resume : 진행 리스트 재조회 ========= ");
		$("#btn_refresh").click();
	}
	if($.mobile.activePage.is('#page_alarmList_success')) {
		console.log(commonDebugPrefix + "========= on Resume : 완료 리스트 재조회 ========= ");
		$("#btn_refresh").click();
	}
	if($.mobile.activePage.is('#page_alarmList_TFMS')) {
		console.log(commonDebugPrefix + "========= on Resume : TT 리스트 재조회 ========= ");
		$("#btn_refresh").click();
	}
}

var onDeviceReady = function() {
	pictureSource = navigator.camera.PictureSourceType; 	// api-camera Photo URI
	destinationType = navigator.camera.DestinationType;
	
	deviceInfo = device.platform;							// device Info load
	
	document.addEventListener("pause", onPause, false);		// device pause, resume events
	document.addEventListener("resume", onResume, false);
	
	navigator.geolocation.getCurrentPosition(onCurrentPosSuccess, onCurrentPosError);	// 어플 구동 시 현재 위치 정보 수집
	
	// 안드로이드 backButton 예외처리
	if(deviceInfo == "Android") {
		document.addEventListener("backbutton", function(e){
			//	메인화면 또는 로그인화면에서 백버튼 터치 시 프로그램 종료 팝업 발생
		    if($.mobile.activePage.is('#page_index') || $.mobile.activePage.is('#page_login')){
		        e.preventDefault();
		        showExitConfirm();
		    }
		    //	상황공유 리스트에서 백버튼 이동 시 메인화면으로 이동
		    else if($.mobile.activePage.is('#page_alarmList_noti') || $.mobile.activePage.is('#page_alarmList_success') ||
		    		$.mobile.activePage.is('#page_alarmList_TFMS')) {
		    		$.mobile.changePage('index.html');
		    }
		    //	고장상황 진행 상세 화면에서 백버튼 이동 시 고장상황 진행 리스트로 이동
		    else if($.mobile.activePage.is('#page_alarmList_detail')) {
		    		$.mobile.changePage('alarmList_noti.html');
		    }
		    //	고장상황 진행 상세화면 내 페이지에서 백버튼 이동 시 고장상황 진행 상세화면으로 이동
		    else if($.mobile.activePage.is('#page_alarmList_detail_capture') || $.mobile.activePage.is('#page_alarmList_detail_map') ||
		    		$.mobile.activePage.is('#page_alarmManualList') || $.mobile.activePage.is('#page_contact_list')) {
		    		$.mobile.changePage('alarmList_noti_detail.html?notiseqnum='+ notiseqnum);
		    }
		    //	고장상황 완료 화면에서 백버튼 이동 시 고장상황 완료 리스트로 이동
		    else if($.mobile.activePage.is('#page_alarmList_detail_success')) {
		    		$.mobile.changePage('alarmList_success.html');
		    }
		    //	TFMS 상세화면에서 백버튼 이동 시 TFMS 리스트로 이동
		    else if($.mobile.activePage.is('#page_alarmList_detail_TFMS')) {
		    		$.mobile.changePage('alarmList_TFMS.html');
		    }
		    //	업무 매뉴얼, 사업장 정보 리스트에서 백버튼 이동 시 메인화면으로 이동
		    else if($.mobile.activePage.is('#page_manualList') || $.mobile.activePage.is('#page_transOffice')) {
		    		$.mobile.changePage('index.html');
		    }		    
		    //	업무매뉴얼 상세 화면에서 백버튼 이동시 업무매뉴얼 리스트로 이동
		    else if($.mobile.activePage.is('#page_manual_detail')){
		    	if(filedownloadFlag==true){	//	파일 다운로드 중 일때는 백버튼 제한
		    		e.preventDefault();
		    	}else if(filedownloadFlag==false){
		    		$.mobile.changePage('board_manual.html');
		    	}		 
		    }
		    //	고장상황 업무매뉴얼 상세 화면에서 백버튼 이동시 고장상황 업무매뉴얼 리스트로 이동
		    else if($.mobile.activePage.is('#page_alarmManual_detail')){
		    	if(filedownloadFlag==true){	// 파일 다운로드 중 일때는 백버튼 제한
		    		e.preventDefault();
		    	}else if(filedownloadFlag==false){
		    		$.mobile.changePage('alarmList_noti_detail_manual.html?checked=' + beforeChecked);
		    	}		    		    	
		    }
		    //	사업장 상세 화면에서 백버튼 이동 시 사업장 리스트로 이동
		    else if($.mobile.activePage.is('#page_transOffice_detail')){
		    	$.mobile.changePage('transOffice.html');
		    }
		    //	지도에서 들어온 사업장 상세 화면에서 백버튼 이동 시 고장상황 지도 화면으로 이동
		    else if($.mobile.activePage.is('#page_map_transOffice_detail')){
		    	$.mobile.changePage('alarmList_noti_detail_map.html');
		    }
		    //	사업장 상세 화면 내 페이지에서 백버튼 이동 시 사업장 상세 화면으로 이동 
		    else if($.mobile.activePage.is('#page_transOffice_detail_map') || $.mobile.activePage.is('#page_transOffice_detail_capture')){
		    	$.mobile.changePage('transOffice_detail.html?workplaceSeqNum=' + workplaceSN);
		    }
		    // 지도에서 들어온 사업장 상세 화면 내 페이지에서 백버튼 이동 시 지도사업장 상세 화면으로 이동
		    else if($.mobile.activePage.is('#page_map_transOffice_detail_capture')){
		    	$.mobile.changePage('map_transOffice_detail.html?workplaceSeqNum=' + workplaceSN);
		    }
		    //   공지사항 리스트에서 백버튼 이동 시 메인화면으로 이동
            else if($.mobile.activePage.is('#page_notice_list')){
                    $.mobile.changePage('index.html');
            }
	       //   공지사항 상세 화면에서 백버튼 이동시 공지사항 리스트로 이동
		    else if($.mobile.activePage.is('#page_notice_detail')){
		        if(filedownloadFlag==true){ //  파일 다운로드 중 일때는 백버튼 제한
                    e.preventDefault();
                }else if(filedownloadFlag==false){
                    $.mobile.changePage('board_notice.html');
                }        
            }
		    //   자유게시판 목록에서 메인화면으로 이동
		    else if($.mobile.activePage.is('#page_board_open')){
                $.mobile.changePage('index.html');
            }
		    //   자유게시판 상세보기에서 자유게시판 목록으로 이동
		    else if($.mobile.activePage.is('#page_board_open_detail')){
                $.mobile.changePage('board_open.html');
		    }
		    else {
		        navigator.app.backHistory();
		    }
		}, false);
	}
	//	iOS의 경우 파일로딩 수행(Device Token 정보 로딩)
    if(deviceInfo != "Android" && $.mobile.activePage.is('#page_login')){
    	window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, gotFS, fail);	// file api
    }
};
// api load
function init() {
	document.addEventListener("deviceready", onDeviceReady, true);
}

//api-connection
function check_network() {
    var networkState = navigator.network.connection.type;

    var states = {};
    states[Connection.UNKNOWN]  = false;	//'Unknown connection';
    states[Connection.ETHERNET] = true;		//'Ethernet connection';
    states[Connection.WIFI]     = true;		//'WiFi connection';
    states[Connection.CELL_2G]  = true;		//'Cell 2G connection';
    states[Connection.CELL_3G]  = true;		//'Cell 3G connection';
    states[Connection.CELL_4G]  = true;		//'Cell 4G connection';
    states[Connection.NONE]     = false;	//'No network connection';
    
    return states[networkState];
}

function getDeviceInfo() {
	return deviceInfo.toLowerCase();
}

//조회
function getUserIDResult() {
	db.transaction(querySelectUserID, errorGetUserID);
}
function querySelectUserID(tx) {
	tx.executeSql('SELECT userID FROM MOSS_USERINFO', [], selectUserIDSuccess, errorGetUserID);
}
function selectUserIDSuccess(tx, results) {
	userID = results.rows.item(0).userID;
	console.log(commonDebugPrefix + "=============== DB userID =============== : " + userID);
}
function errorGetUserID(err) {
	console.log(commonDebugPrefix + "db error");
}

//	코드 테이블 데이터 로드 
function getMossCode(category) {
	$.ajax({
		url: url + "MossCode",
		data: {category : category},
		dataType: "json",
		type: "post",
		timeout: commonTimeout,
		success: function (result) {
			var headerData = result.header;
			// header log
			console.log(commonDebugPrefix + "########### Header Data : MossCode["+category+"]  ###########");
			$.each(headerData, function(key, val){
				console.log(commonDebugPrefix + key + " : " + val);
			});

			if(headerData.result != 0) {
				showCommonAlert("MossCode", headerData.errMsg);
			} else {
				var bodyData = result.body;
				
				// CodeValue Ascending
				function sortCodeList(prop, asc) {
					bodyData = bodyData.sort(function(a, b) {
				        if (asc) return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
				        else return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
				    });
				}
				
				sortCodeList("DISPLAYORDER", true);
				
				console.log(commonDebugPrefix + "============ response MossCode ============");
				
				$.each(bodyData, function(key, val){
					$.each(val, function(key, val){
						console.log(loginDebugPrefix + key + " : " + val);
					});
					// 코드값 배열에 값 푸시
					moveStateCode.push(val.CODE);
					moveStateCodeVal.push(val.CODEVALUE);
				});
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			$.mobile.hidePageLoadingMsg();
	        showCommonAlert("MossCode", textStatus);
	        console.log(commonDebugPrefix + jqXHR.responseText);
	        console.log(commonDebugPrefix + jqXHR.status);
	        console.log(commonDebugPrefix + errorThrown);
	    }
	});
}

function commonDanInfo() {
	$.ajax({
		url: url + "DanOrgCodeListAll",
		type: "post",
		timeout: commonTimeout,
		dataType: "json", 
//		data: {userid : loginId},
		success:function(result){
			var headerData = result.header;
			// header log
			console.log(commonDebugPrefix + "########### Header Data : DanOrgInfo  ###########");
			$.each(headerData, function(key, val){
				console.log(commonDebugPrefix + key + " : " + val);
			});
			
			if(headerData.result != 0) {
				showCommonAlert("DanInfo", headerData.errMsg);
			} else {
				var danData = result.danorgcode;
				commonDanCodeArray = new Array();
				commonDanNameArray = new Array();
				
//				// CodeValue Ascending
//				function sortCodeList(prop, asc) {
//					danData = danData.sort(function(a, b) {
//				        if (asc) return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
//				        else return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
//				    });
//				}
//				sortCodeList("ORG_NM", true);
				
				console.log(commonDebugPrefix + "============ response DanInfo ============");
				$.each(danData, function(key, val) {
					$.each(val, function(key, val){
						console.log(commonDebugPrefix + key + " : " + val);
					});
					commonDanCodeArray[key] = val.ORG_ID;
					commonDanNameArray[key] = val.ORG_NM;
				});	
			}			
		},
		error : function() {
	        showCommonAlert("DanInfo", textStatus);
	        console.log(commonDebugPrefix + jqXHR.responseText);
	        console.log(commonDebugPrefix + jqXHR.status);
	        console.log(commonDebugPrefix + errorThrown);
		}
	}).done(function() {
		if(commonDanCodeArray != null) {
			var popupHtml = "<div data-role='popup' id='danConfigPopup' class='ui-content' data-theme='a' data-dismissible='false' style='padding-left:1em; padding-right:1em; padding-bottom:0.5em;'>" +
							"<p style='text-align:center; font-size:1.5em;'><b>소속 운용단을 설정하세요</b></p>" +
							"<fieldset data-role='controlgroup' data-mini='false'>";

			for(var i=0; i <commonDanCodeArray.length; i++) {
				if(i == 0) {
					popupHtml += "<input type='radio' name='danChoice' id='radio-choice-v-" + commonDanCodeArray[i] + "' data-iconpos='right' value='" + commonDanCodeArray[i] + "' checked='checked'>" +
				 	 			 "<label for='radio-choice-v-" + commonDanCodeArray[i] + "'>" + commonDanNameArray[i] + "</label>";
				} else {
					popupHtml += "<input type='radio' name='danChoice' id='radio-choice-v-" + commonDanCodeArray[i] + "' data-iconpos='right' value='" + commonDanCodeArray[i] + "'>" +
								 "<label for='radio-choice-v-" + commonDanCodeArray[i] + "'>" + commonDanNameArray[i] + "</label>";
				}
			}
			popupHtml += "</fieldset>" +
				 		 "<fieldset class='ui-grid-solo' data-theme='a' id='btn_group'>" +
				 		 "<div class='ui-block-a'><a href='#' id='btn_danConfig' data-role='button' data-theme='b' data-mini='false' style='margin-top:1.5em;'>확인</a></div>" +
				 		 "</fieldset>";
				 		 "</div>";
			$("#page_index").append(popupHtml);
		}
	});
}

//iScroll Content Resize
$(window).on('scrollstart', function () {
	if($.mobile.activePage.is('#page_alarmList_detail')){
		console.log(alarmList_noti_detail_debugPrefix + "========= myScroll.refresh!! =========");
		myScroll.refresh();
	} else if($.mobile.activePage.is('#page_alarmList_detail_success')) {
		console.log(alarmList_noti_detail_debugPrefix + "========= myScroll.refresh!! =========");
		myScroll.refresh();
	} else if($.mobile.activePage.is('#page_alarmList_detail_TFMS')) {
		console.log(alarmList_noti_detail_debugPrefix + "========= myScroll.refresh!! =========");
		myScroll.refresh();
	}
});

$(document).ready(function() {
	// 로그인이 완료되면 로컬DB 에서 사번을 읽어온다.
	if (!db) {
		db = window.openDatabase("Database", "1.0", "myDb", 200000);
	}
	getUserIDResult();
	// 네트워크 상태 체크 
	setInterval(function (){
		if(!true == check_network()) {
			$.mobile.hidePageLoadingMsg();
			console.log(commonDebugPrefix + "=============== 네트워크 연결 상태 : NOK ===============");
		} else {
			console.log(commonDebugPrefix + "=============== 네트워크 연결 상태 : OK ===============");
		}
	},60 * 1000);
	
	// 코드 테이블의 내용으로 상황보고 라디오 버튼 구성
	getMossCode("MOVESTATE");
});

$(document).on('blur', 'input, textarea', function() {
//	iOS의 경우 OS 브라우저의 특성상 header 고정이 되지않아 키보드가 나타난뒤 헤더 위치 설정 처리
	if(deviceInfo != "Android") {
	    $.mobile.silentScroll($('div[data-role="header"]').offset().top);
	}
});

////	모든 페이지가 생성되기 전 이벤트 발생
//$(document).on("pagebeforecreate", function(event) {
//
//});
//
////	모든 페이지의 매뉴패널이 열릴 때 이벤트 발생
//$(document).on("pageshow", function(event) {
//
//});
//	모든 페이지가 변경 될 때 이벤트 발생
$(document).on("pagechange", function(event) {
	//	페이지 이동 시 폴링 인터벌 해제
	if(!$.mobile.activePage.is('#page_alarmList_detail')) {
		clearInterval(interval_detail); // stop the interval
		console.log(commonDebugPrefix + "========= Polling Number => " + interval_detail + "=========");
	}
	// 페이지 이동 시 페이징처리 변수 초기화
	if(!$.mobile.activePage.is('#page_board_open')) {
	    listPageNo = 1;
        console.log(commonDebugPrefix + "========= listPagingStart => " + listPageNo + "=========");
    }
});