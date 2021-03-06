/**
 * 		index.js
 */

var indexDebugPrefix = "[index.js] : ";

$(document).on("pagebeforecreate", "#page_index" , function(event) {
	//	단 코드, 단 이름 배열 생성
	if(danConfigFlag) {
		commonDanInfo();
	}
	//	메뉴패널 생성, 반드시 pagebeforecreate 이벤트단에서 호출한다.
	commonCreateMenuPanel();
});

$(document).on("pagecreate", "#page_index" , function(event) {

});

$(document).on("pageshow", "#page_index" , function(event) {
	//	페이지에 따른 메뉴패널 설정
	commonConfigMenuPanel();
	//	페이지가 생성된 후 상황에 단설정 상태에 따른 팝업창을 호출
	$('#page_index').trigger('pagecreate');
	if(danConfigFlag) {
		$("#danConfigPopup").popup("open", "slidedown");
		$("#btn_danConfig").click(function () {
			console.log(indexDebugPrefix + "================== set DanOrgCode ==================");
			setDeviceInfo($(":input:radio[name=danChoice]:checked").val());
			$("#danConfigPopup").popup("close", "slideup");
		});
	}
});