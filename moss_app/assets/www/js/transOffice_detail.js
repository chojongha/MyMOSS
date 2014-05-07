/**
 * 		사업장 상세페이지
 */
var transOfficeDetail_debugPrefix = "[transOffice_detail.js] : "; 
var workplaceSN="";
var officeAddr="";
var mapOfficeName="";
var transScroll;
var imageSeqNum="";

/**
 * 데이터가 Null일 경우 ""으로 대체
 */
function ifNull(data) {	
	//	문자열로 변환하여 비교
	data = String(data);
	
	if(data == null || data == "" || data == "null") {
		return "";
	} else {
		return data;
	}
}

/**
 * 테이블 생성
 */
function workPlaceTable(){
	var transOfficeTB ="";

		transOfficeTB += '<table>'+
						 '<tr>'+									
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" colspan="2" bgcolor="#5d5d5d" style="width:5.5em; -moz-border-radius-topleft:5px; -webkit-border-top-left-radius:5px; border-top-left-radius:5px; -moz-border-radius-topright:5px; -webkit-border-top-right-radius:5px; border-top-right-radius:5px;"><font color="#ffffff"><b>사업장 정보</b></font></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">모국명</td>'+
						 '<td align="left" id="TO_pOfficeName"></td>'+					
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">사업장유형</td>'+
						 '<td align="left" id="TO_workPlaceType"></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">사업장명</td>'+
						 '<td align="left" id="TO_workPlaceName"></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">사진정보</td>'+
						 '<td id="TO_pic" style="display:table-cell; vertical-align:middle;"><div id="pictureYN" style="float:left; padding:5px 0px 0px 7px;"></div><div style="float:right;"><a id="officeCamera"><img src="../img/detail_cam_n.png"></a></div></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" colspan="2" bgcolor="#5d5d5d"><font color="#ffffff"><b>위치 정보</b></font><div id="officeMapBtnDiv" style="float:right;"><a href="#" data-role="button" id="officeMapButton" onClick="officeAddressCheck();" data-inline="true" data-mini="true" data-theme="c">지도보기</a></div></td>'+				
						 '</tr>'+																												
						 '<tr>'+
						 '<td align="left" width="30%">주소</td>'+
						 '<td align="left" id="TO_addr"></td>'+
						 '</tr>'+ 
						 '<tr>'+
						 '<td align="left" width="30%">상세정보</td>'+
						 '<td align="left" id="TO_addInfo"></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">위도</td>'+
						 '<td align="left" id="TO_lati"></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">경도</td>'+
						 '<td align="left" id="TO_longi"></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" colspan="2" bgcolor="#5d5d5d"><font color="#ffffff"><b>출입 정보</b></font><div style="float:right"><a href="#" data-role="button" id="btn_officeModify1" data-inline="true" data-mini="true" data-theme="c">수정</a></div></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">출입제한시간</td>'+
						 '<td align="left" id="TO_timeLimit"><input type="text" id="inputTimeLimit" placeholder="최대 15자 입력 가능" style="font-size:14px" value="" /></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">출입방법</td>'+
						 '<td align="left" id="TO_visitRule"><textarea id="inputVisitRule" placeholder="최대 100자 입력 가능" style="font-size:14px" value=""></textarea></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">건물관리자</td>'+
						 '<td align="left" id="TO_manager"><input type="text" id="inputManager" placeholder="최대 15자 입력 가능" style="font-size:14px" value=""/></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">건물전화</td>'+
						 '<td align="left" id="TO_tel"><input type="tel" id="inputTel" placeholder="최대 15자 입력 가능" style="font-size:14px" value=""/></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">건물휴대폰</td>'+
						 '<td align="left" id="TO_manaPhone"><input type="tel" id="inputManaPhone" placeholder="최대 15자 입력 가능" style="font-size:14px" value=""/></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" colspan="2" bgcolor="#5d5d5d"><font color="#ffffff"><b>시설 정보</b></font><div style="float:right"><a href="#" data-role="button" id="btn_officeModify2" data-inline="true" data-mini="true" data-theme="c">수정</a></div></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">장비내역1</td>'+
						 '<td align="left" id="TO_equiHistory1"><textarea id="inputEquiHistory1" placeholder="최대 100자 입력 가능" style="font-size:14px" value=""></textarea></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">장비내역2</td>'+
						 '<td align="left" id="TO_equiHistory2"><textarea id="inputEquiHistory2" placeholder="최대 100자 입력 가능" style="font-size:14px" value=""></textarea></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">장비내역3</td>'+
						 '<td align="left" id="TO_equiHistory3"><textarea id="inputEquiHistory3" placeholder="최대 100자 입력 가능" style="font-size:14px" value=""></textarea></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" colspan="2" bgcolor="#5d5d5d"><font color="#ffffff"><b>관리 정보</b></font></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">시설운용단</td>'+
						 '<td align="left" id="TO_manageCenter"></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">시설운용팀</td>'+
						 '<td align="left" id="TO_manageTeam"></td>'+
						 '</tr>'+
						 '<td align="left" width="30%">유지보수사</td>'+
						 '<td align="left" id="TO_manageCompany"></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">유지 보수<br>담당 (정)</td>'+
						 '<td align="left" id="TO_managerName1"></td>'+
						 '</tr>'+
						 '<td align="left" width="30%">담당연락처</td>'+
						 '<td align="left" id="TO_managerTel1"></td>'+
						 '</tr>'+
						 '<tr>'+
						 '<td align="left" width="30%">유지 보수<br>담당 (부)</td>'+
						 '<td align="left" id="TO_managerName2"></td>'+
						 '</tr>'+
						 '<td align="left" width="30%">담당연락처</td>'+
						 '<td align="left" id="TO_managerTel2"></td>'+
						 '</tr>'+
						 '</table>'+
						 '<form id="myForm_officeUpdate" enctype="multipart/form-data" method="post">'+
						 '<div style="width: 0; height: 0; overflow: hidden;">'+
						 '<input type="text" id="workplaceSeqNum" name="workplaceSeqNum"/><br/>'+
						 '<input type="text" id="enterLimitedTime" name="enterLimitedTime"/><br/>'+
						 '<input type="text" id="enterMethod" name="enterMethod"/><br/>'+
						 '<input type="text" id="enterManager" name="enterManager"/> <br/>'+
						 '<input type="text" id="enterTel1" name="enterTel1"/><br/>'+ 
						 '<input type="text" id="enterTel2" name="enterTel2"/><br/>'+ 
						 '<input type="text" id="equipMentmem01" name="equipMentmem01"/><br/>'+
						 '<input type="text" id="equipMentmem02" name="equipMentmem02"/><br/>'+ 
						 '<input type="text" id="equipMentmem03" name="equipMentmem03"/> <br/>'+
						 '</div>'+
						 '</form>';

	//테이블 삽입	
	$("#table_transOfficeDetail").html(transOfficeTB);				
};

/**
 * 데이터 셋팅
 */
function showTransOffice_detail(){
		
	//사업장 시퀀스 넘버 받기
	workplaceSN=commonRequest("workplaceSeqNum");
	
	console.log(transOfficeDetail_debugPrefix + "넘어온 시퀀스넘버 = " + workplaceSN);
	
	$.ajax({ 
		url: url + "MossWorkplaceDetail",
		type: "post",
		timeout: commonTimeout,
		dataType: "json",
		data: {workplaceSeqNum:workplaceSN},		
		success: function(result){
			
			var headerData = result.header;
			
			console.log(transOffice_debugPrefix + url+"MossWorkplaceDetail");
			
			$.each(headerData, function(key, val) {
              console.log(transOffice_debugPrefix + key + " : " + val);
          });
			
			if(headerData.result != 0){
				$.mobile.hidePageLoadingMsg();
				showCommonAlert("사업장 상세보기", headerData.errMsg);				
			}else{
				var bodyData = result.body;
				
				$.each(bodyData, function(key, val){
					$.each(val, function(key, val) {
	                    console.log(transOfficeDetail_debugPrefix + key + " : " + val);
	                });	
								
					//주소 저장(주소와 상세주소가 따로있어 이어 붙임)																					
					if(val.ADDR != null && val.ADDRDTL != null){
						officeAddr = ifNull(val.ADDR) + " " + ifNull(val.ADDRDTL);
					}else{
						officeAddr = ifNull(val.ADDR) + ifNull(val.ADDRDTL);
					}
					
					//사업장명 저장(지도보기의 마커에 사용될 변수)
					mapOfficeName = val.WORKPLACENAME;
															
					//데이터 세팅					
					$('#TO_pOfficeName').text(nullCheck(val.POFFICENAMESCODE));
					$('#TO_workPlaceType').text(nullCheck(val.WORKPLACETYPE_NM));					
					$('#TO_workPlaceName').text(nullCheck(val.WORKPLACENAME));
	
					$('#TO_addr').text(nullCheck(officeAddr));
					$('#TO_addInfo').text(nullCheck(val.ADDINFO));
					$('#TO_lati').text(nullCheck(val.LATITUDE));
					$('#TO_longi').text(nullCheck(val.LONGITUDE));				
										
					$('#inputTimeLimit').val(ifNull(val.ENTERLIMITEDTIME));
					$('#inputVisitRule').val(ifNull(val.ENTERMETHOD));
					$('#inputManager').val(ifNull(val.ENTERMANAGER));
					$('#inputTel').val(ifNull(val.ENTERTEL1));
					$('#inputManaPhone').val(ifNull(val.ENTERTEL2));
					$('#inputEquiHistory1').val(ifNull(val.EQUIPMENTMEMO1));
					$('#inputEquiHistory2').val(ifNull(val.EQUIPMENTMEMO2));
					$('#inputEquiHistory3').val(ifNull(val.EQUIPMENTMEMO3));
					
					$('#TO_manageCenter').text(nullCheck(val.MANAGECENTER));	
					$('#TO_manageTeam').text(nullCheck(val.MANAGETEAM));	
					$('#TO_manageCompany').text(nullCheck(val.MANAGECOMPANY));	
					$('#TO_managerName1').text(nullCheck(val.MANAGERNAME1));	
					$('#TO_managerTel1').text(nullCheck(val.MANAGERTEL1));	
					$('#TO_managerName2').text(nullCheck(val.MANAGERNAME2));	
					$('#TO_managerTel2').text(nullCheck(val.MANAGERTEL2));	
				
					if(val.FILEBINARY == null){
						$("#pictureYN").text("사진없음");
					}else{
						var pictureY = '<a href="#TO_img" data-rel="popup" data-position-to="window" data-transition="fade" style="text-decoration:none;">사진보기</a>';
						
						$("#pictureYN").html(pictureY);
						
						//사진 팝업 삽입
						var imgPop = '<div data-role="popup" id="TO_img" data-overlay-theme="a" data-theme="d" data-corners="false" data-dismissible="false">'+
								     '<a href="#" data-rel="back" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a><img id="imgSrc" class="popphoto" style="width:250px;" alt="국사사진">'+		
								     '</div>';					
																
							$("#table_transOfficeDetail").append(imgPop).trigger("create");
							$("#imgSrc").attr('src', 'data:image/jpeg;base64,' + val.FILEBINARY);
					}
					//사업장 사진 업로드 함수에 사용될 변수
					imageSeqNum = ifNull(val.IMAGESEQNUM);					
				});
				$.mobile.hidePageLoadingMsg();
			}	
		},
		error: function(jqXHR, textStatus, errorThrown) {
			$.mobile.hidePageLoadingMsg();
			showCommonAlert("사업장 상세보기", textStatus);
			console.log(transOfficeDetail_debugPrefix + "사업장상세정보 출력 에러 : " + jqXHR.status);
	        console.log(transOfficeDetail_debugPrefix + "사업장상세정보 출력 에러 : " + jqXHR.responseText);
			console.log(transOfficeDetail_debugPrefix + "사업장상세정보 출력 에러 : " + errorThrown);
	    }
	});
}

/**
 * 수정 사항 입력
 */
function transInfoUpdate() {
		
	$("#workplaceSeqNum").val(workplaceSN);
	$("#enterLimitedTime").val($("#inputTimeLimit").val());
	$("#enterMethod").val($("#inputVisitRule").val());
	$("#enterManager").val($("#inputManager").val());
	$("#enterTel1").val($("#inputTel").val());
	$("#enterTel2").val($("#inputManaPhone").val());
	$("#equipMentmem01").val($("#inputEquiHistory1").val());
	$("#equipMentmem02").val($("#inputEquiHistory2").val());
	$("#equipMentmem03").val($("#inputEquiHistory3").val());

	console.log(transOfficeDetail_debugPrefix + "============= 수정 정보 입력 파라미터 ==========");
	console.log(transOfficeDetail_debugPrefix + "workplaceSeqNum = " + $("#workplaceSeqNum").val());
	console.log(transOfficeDetail_debugPrefix + "enterLimitedTime = " + $("#enterLimitedTime").val());
	console.log(transOfficeDetail_debugPrefix + "enterMethod = " + $("#enterMethod").val());
	console.log(transOfficeDetail_debugPrefix + "enterManager = " + $("#enterManager").val());
	console.log(transOfficeDetail_debugPrefix + "enterTel1 = " + $("#enterTel1").val());
	console.log(transOfficeDetail_debugPrefix + "enterTel2 = " + $("#enterTel2").val());
	console.log(transOfficeDetail_debugPrefix + "equipMentmem01 = " + $("#equipMentmem01").val());
	console.log(transOfficeDetail_debugPrefix + "equipMentmem02 = " + $("#equipMentmem02").val());
	console.log(transOfficeDetail_debugPrefix + "equipMentmem03 = " + $("#equipMentmem03").val());

	$( "#myForm_officeUpdate" ).submit();	
}

/**
 * 수정요청
 * @param formData
 * @param jqForm
 * @param options
 * @returns {Boolean}
 */
function showOfficeRequest(formData, jqForm, options) {
	$.mobile.loading( "show", {
		  text: "수정중...",
		  textVisible: true,
		  theme: "a",
		  html: ""
	});
	
    var queryString = $.param(formData); 
    console.log(transOfficeDetail_debugPrefix + 'About to submit: \n\n' + queryString);

    return true; 
} 

/**
 * 수정완료
 * @param responseText
 * @param statusText
 * @param xhr
 * @param $form
 */
function showOfficeResponse(responseText, statusText, xhr, $form)  { 
	
    var headerData = responseText.header;      
	// header log
	console.log(transOfficeDetail_debugPrefix + "============= showOfficeUpdateResponse =============");
	$.each(headerData, function(key, val){
		console.log(transOfficeDetail_debugPrefix + key + " : " + val);
	});
	
	if(headerData.result != 0) {
		showCommonAlert("사업장정보", headerData.errMsg);
		$.mobile.hidePageLoadingMsg();		
	} else { 				
		// 화면에 리스트 출력이 완료되면 loader를 종료
		showCommonAlert("사업장정보", "수정 완료");
		$.mobile.hidePageLoadingMsg();									    
	}
          
	showTransOffice_detail();
} 

//수정하기
var officeOptions = { 
        beforeSubmit:  showOfficeRequest,  	// pre-submit callback 
        success:       showOfficeResponse,  	// post-submit callback
        url : url + "UpdateMossWorkplace",
        dataType:  "json",        		// 'xml', 'script', or 'json' (expected server response type)
        clearForm: true,        		// clear all form fields after successful submit 
        resetForm: true,        		// reset the form after successful submit 
        timeout:   commonTimeout
};

//수정 확인창
var officeUpdateConfirm = function() {
	function officeUpdateConfirmCallback(button) {
		if (button == 2) {
			transInfoUpdate();
		}
		confirmFlag = true;
	}
	if(confirmFlag) {
		confirmFlag = false;
		navigator.notification.confirm(
				'수정 하시겠습니까?', // message
				officeUpdateConfirmCallback, // callback to invoke with index of button pressed
				'사업장정보', // title
				'취소,확인' // buttonLabels
			);
	}
};

/**
 * 사업장 사진 업로드 cordova api fileUploader
 */
function officeFileUpload() {
		
		$.mobile.loading( "show", {
			  text: "업로드 중...",
			  textVisible: true,
			  theme: "a",
			  html: ""
		});
			
	    var options = new FileUploadOptions();
	    options.fileKey="BDFile";
	    options.fileName=imgUrl.substr(imgUrl.lastIndexOf('/')+1);
	    options.mimeType="image/jpeg";
	    
	    var params = new Object();
	    params.workplaceSeqNum = workplaceSN;
	    params.imageSeqNum = imageSeqNum;
	    params.filename = options.fileName;	    
	    options.params = params;
	    
	    var ft = new FileTransfer();
	    ft.upload(imgUrl, encodeURI(url + "WorkplaceImage"), winUpload, failUpload, options);
}

/**
 * 지도보기 주소 nullCheck
 */
function officeAddressCheck(){
	if(nullCheck(officeAddr) == "-"){
		showCommonAlert("사업장상세정보", "주소가 올바르지 않습니다.");
	}else{
		$.mobile.changePage('transOffice_detail_map.html');
	}
}

//상세 페이지 생성 전
$(document).on("pagebeforecreate", "#page_transOffice_detail" , function(event) {
	//	테이블 생성, 반드시 pagebeforecreate 이벤트단에서 호출한다.
	workPlaceTable();		
});

//상세 페이지 보이기 전 
$(document).on("pagebeforeshow","#page_transOffice_detail",function(event){
	//상세화면 불러오기
	$.mobile.loading( "show", {
		  text: "로딩중...",
		  textVisible: true,
		  theme: "a",
		  html: ""
	}); 
	
	//데이터 세팅
	showTransOffice_detail();
	
	//카메라 링크설정
	$("#officeCamera").attr('href','transOffice_detail_capture.html');
	
	//스크롤 될때마다 리프레시됨
	$(window).on('scrollstart', function () {
		if($.mobile.activePage.is('#page_transOffice_detail')){
			transScroll.refresh();
		}
	});
	
	// 상단 하단 margin 적용(줌과 스크롤 주기 위함)
	$( '#contentWrapper' ).css( "top", 55 );
	$( '#contentWrapper' ).css( "bottom", 5 );
	
	// zoom enable
	transScroll = new iScroll('contentWrapper', { zoom: true });	
});

//상세 페이지 보임
$(document).on("pageshow","#page_transOffice_detail",function(event){
	
	$('#page_transOffice_detail').trigger('pagecreate');
	
	//수정하기
	$('#btn_officeModify1').click(function(){
		officeUpdateConfirm();
	});
	
	$('#btn_officeModify2').click(function(){
		officeUpdateConfirm();
	});

	//	하위버전 OS에서 해당 폼 안에 아래 속성이 존재할 경우 
	//	페이지 로딩이 되지 않아 페이지 로딩 후 스크립트로 속성 추가
	$("#myForm_officeUpdate").attr("data-ajax", false);

	$('#myForm_officeUpdate').ajaxForm(officeOptions);		// ajax 폼 전송	 
});

//맵에서 온 상세 페이지 생성 전
$(document).on("pagebeforecreate", "#page_map_transOffice_detail" , function(event) {
	//	테이블 생성, 반드시 pagebeforecreate 이벤트단에서 호출한다.
	workPlaceTable();		
});

//맵에서온 상세페이지 보이기 전 
$(document).on("pagebeforeshow","#page_map_transOffice_detail",function(event){		
	//상세화면 불러오기
	$.mobile.loading( "show", {
		  text: "로딩중...",
		  textVisible: true,
		  theme: "a",
		  html: ""
	}); 
	
	//데이터 세팅
	showTransOffice_detail();
	
	//지도보기 버튼 없애기
	$("#officeMapBtnDiv").text("");
	//카메라 링크 설정
	$("#officeCamera").attr('href','map_transOffice_detail_capture.html');
	
	//스크롤 될때마다 리프레시됨	
	$(window).on('scrollstart', function () {
		if($.mobile.activePage.is('#page_map_transOffice_detail')){
			transScroll.refresh();
		}
	});
	
	// 상단 하단 margin 적용
	$( '#contentWrapper' ).css( "top", 55 );
	$( '#contentWrapper' ).css( "bottom", 5 );
	
	// zoom enable
	transScroll = new iScroll('contentWrapper', { zoom: true });
});

//맵에서온 상세페이지 보임
$(document).on("pageshow","#page_map_transOffice_detail",function(event){
	
	$('#page_map_transOffice_detail').trigger('pagecreate');

	//수정하기
	$('#btn_officeModify1').click(function(){
		officeUpdateConfirm();
	});
	
	$('#btn_officeModify2').click(function(){
		officeUpdateConfirm();
	});

	//	하위버전 OS에서 해당 폼 안에 아래 속성이 존재할 경우 
	//	페이지 로딩이 되지 않아 페이지 로딩 후 스크립트로 속성 추가
	$("#myForm_officeUpdate").attr("data-ajax", false);

	$('#myForm_officeUpdate').ajaxForm(officeOptions);		// ajax 폼 전송	 
});

//지도 보기
$(document).on("pageshow","#page_transOffice_detail_map",function(event) {
	
	$("#btn_back").attr("href", $("#btn_back").attr("href") + "?workplaceSeqNum=" + workplaceSN);
	
	// 올레맵 호출 
	$("#btn_callNavi").click(function() {
		
		$.mobile.loading( "show", {
			  text: "올레네비를 실행합니다.",
			  textVisible: true,
			  theme: "a",
			  html: ""
		});
		// 현재 위치 정보를 수집 한 뒤 목적지까지 길찾기 시작
		navigator.geolocation.getCurrentPosition(onCurrentPosSuccess, onCurrentPosError);
	});
	getGeocode(officeAddr, "map_canvas");
});

//사업장 사진 입력
$(document).on("pagebeforeshow","#page_transOffice_detail_capture",function(event) {
	//	Navbar의 클릭 이벤트 시 버튼 토글 상태를 해제
	$("div:jqmData(role='navbar')").click(function () {
		$("a").removeClass("ui-btn-active");
	});
	// header의 back 버튼 클릭 시 이동할 상세 페이지 셋팅
	$("#btn_back").attr("href", $("#btn_back").attr("href") + "?workplaceSeqNum=" + workplaceSN);
	
	$('#btn_captureImg').click(function() {
		take_pic();
	});
	$('#btn_loadImg').click(function() {
		album_pic();
	});
	$('#btn_sandOfficePic').click(function() {
		officeFileUpload();
	});
});

//지도 사업장 사진 입력
$(document).on("pagebeforeshow","#page_map_transOffice_detail_capture",function(event) {
	//	Navbar의 클릭 이벤트 시 버튼 토글 상태를 해제
	$("div:jqmData(role='navbar')").click(function () {
		$("a").removeClass("ui-btn-active");
	});
	// header의 back 버튼 클릭 시 이동할 상세 페이지 셋팅
	$("#btn_back").attr("href", $("#btn_back").attr("href") + "?workplaceSeqNum=" + workplaceSN);
	
	$('#btn_captureImg').click(function() {
		take_pic();
	});
	$('#btn_loadImg').click(function() {
		album_pic();
	});
	$('#btn_sandOfficePic').click(function() {
		officeFileUpload();
	});
});