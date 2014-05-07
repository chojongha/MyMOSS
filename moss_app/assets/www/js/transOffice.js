/**
 * 		사업장 리스트
 */
var transOffice_debugPrefix = "[transOffice.js] : ";

var danCode="";
var teamCode="";
var moCode="";
var officeCode="";

var danChecked="";
var teamChecked="";
var moChecked="";
var officeChecked="";

var workPlaceCode = new Array();
var workPlaceName = new Array();
var workplaceSN="";
var icount;

/**
 * 검색창 고정을 위한 화면 맞추기
 */
function officeCSS(){
	//디바이스에 따라 화면 높이 맞추기
	var header = $("div[data-role='header']:visible");
	var footer = $("div[data-role='footer']:visible");
	var content = $("div[data-role='content']:visible"); 
	var viewport_height = $(window).height();
			
	var content_height = viewport_height - header.outerHeight() - footer.outerHeight();	
	
	content_height -= (content.outerHeight() - content.height());
	
	$("#transOffice_content").css('height', content_height);
	
	var officeConH = $("#transOffice_content").height();
	var officeSearH = $("#selectNsearch").height() + 13;	//13은 magine 준 부분
		
	var officeLiH = officeConH - officeSearH;
											
	$("#officeList").css('height', officeLiH);
}

/**
 * 운용단, 팀 생성(팀은 개인운용단 설정되어 있는 사람에 한해 적용 / 아이디에 의해 생성)
 */
function makedanAndTeamSel(){
	var danSel="";
	var teamSel="";
			
	$.ajax({
		url: url+"WorkPlaceCodeInfoByUserId",
		type: "post",
		timeout: commonTimeout,
		dataType: "json", 
		data: {userid : loginId},
		success:function(result){
			var headerData=result.header;
			icount = 0;		//운용단 갯수 세는 변수
			
			console.log(transOffice_debugPrefix + url+"WorkPlaceCodeInfoByUserId");
			
			$.each(headerData, function(key, val) {
                console.log(transOffice_debugPrefix + key + " : " + val);
            });
			
			if(headerData.result != 0){
				showCommonAlert("운용단, 팀", headerData.errMsg);
				console.log(transOffice_debugPrefix + "운용단, 팀 데이터 없음");
			}else{
				var danorgcodeData=result.danorgcode;
				var deptTeamData=result.deptTeam;
				//운용단 생성
				$.each(danorgcodeData, function(key,val){
					$.each(val, function(key, val) {
	                    console.log(transOffice_debugPrefix + key + " : " + val);
	                });						
					danSel += "<option value='" + val.ORG_ID + "'>" + val.ORG_NM + "</option>";
					++icount;
				});
				//운용단 삽입
				console.log(transOffice_debugPrefix + "운용단 개수 : " + icount);
				if(icount == 1){
					$('#danField').html(danSel).trigger("create").selectmenu( "refresh" );
					console.log(transOffice_debugPrefix + "자신운용단만 볼수 있는 유저");
				}else{
					$('#danField').append(danSel).trigger("create").selectmenu( "refresh" );
					console.log(transOffice_debugPrefix + "전국운용단 볼수 있는 유저");
				}
				//운용단 비활성화 풀기
				$('#danField').selectmenu("enable");
				
				//운용단 선택
				$("#danField").val(danChecked).selectmenu( "refresh" );
				
				console.log(transOffice_debugPrefix + "=============운용단 셀렉트박스생성 완료=============");
				//검색어 입력
				$("#word").val(inputWordText);
								
				if(deptTeamData != null){		//개인운용단 설정 사용자만 해당
					//운용팀 생성
					$.each(deptTeamData, function(key,val){
						$.each(val, function(key, val) {
		                    console.log(transOffice_debugPrefix + key + " : " + val);
		                });	
						teamSel += "<option value='" + val.ORG_ID + "'>" + val.ORG_NM + "</option>";
					});
					//운용팀 삽입
					$('#teamField').append(teamSel).trigger("create").selectmenu( "refresh" );
					
					//운용팀 비활성화 풀기
					$('#teamField').selectmenu("enable");
					
					//운용팀 선택
					$("#teamField").val(teamChecked).selectmenu( "refresh" );
					
					console.log(transOffice_debugPrefix + "=============운용팀 셀렉트박스생성 완료=============");
				}						
			}	
		},
		error : function(jqXHR, textStatus, errorThrown){
			showCommonAlert("운용단, 팀", textStatus);
			console.log(transOffice_debugPrefix + "운용단, 팀 생성 에러");
		}
	}).done(function(){
		if(icount == 1){	//개인운용단
			makeMoSel();	//모국만 불러옴 
		}else{	//전국운용단
			makeAllTeamSel();	//팀과 모국을 불러옴
			makeMoSel();	//onChange뿐만아니라 여기서도 불러오는 이유 : 상세에서 리스트로 돌아왔을때 선택값을 다시 설정하기 위해.
		}		
	});
}

/**
 * 운용단 저장
 */
function storeDan(){
	danChecked = $("#danField > option:selected").val();
	
	console.log(transOffice_debugPrefix + "저장된 운용단 ==== " + danChecked);
}

/**
 * 운용단 리셋
 */
function resetDan(){
	$("select[name='select-choice-dan'] option").not("[value='selectDan']").remove();		//옵션 중복 방지
}

/**
 * 전국 운용팀 생성(단코드에 의해 생성)
 */
function makeAllTeamSel(){
	if(danChecked == "" || danChecked == "selectDan"){	//"운용단" 이 선택되어 있으면 서버 부르지 않음
		
	}else{		
		$.ajax({
			url: url+"WorkPlaceCodeInfoByDanOrgCode",
			type: "post",
			timeout: commonTimeout,
			dataType: "json",
			data: {danorgcode:danChecked},
			success:function(result){		
				var headerData=result.header; 
				var teamSel="";

				console.log(transOffice_debugPrefix + url+"WorkPlaceCodeInfoByDanOrgCode");

				$.each(headerData, function(key, val) {
					console.log(transOffice_debugPrefix + key + " : " + val);
				});

				if(headerData.result != 0){
					showCommonAlert("운용팀", headerData.errMsg);
					console.log(transOffice_debugPrefix + "전국 운용팀 데이터 없음");
				}else{
					var deptTeamData=result.deptTeam;
										
					$.each(deptTeamData, function(key,val){
						$.each(val, function(key, val) {
							console.log(transOffice_debugPrefix + key + " : " + val);
						});
						//전국 운용팀 생성
						teamSel += "<option value='" + val.ORG_ID + "'>" + val.ORG_NM + "</option>";
					});
					//전국 운용팀 삽입
					$('#teamField').append(teamSel).trigger("create").selectmenu( "refresh" );
					//전국 운용팀 비활성화 풀기
					$('#teamField').selectmenu("enable");
				}
				//전국 운용팀 선택
				$("#teamField").val(teamChecked).selectmenu( "refresh" );

				console.log(transOffice_debugPrefix + "=============전국운용팀 셀렉트박스생성 완료=============");
			},
			error : function(jqXHR, textStatus, errorThrown){
				showCommonAlert("운용팀", textStatus);
				console.log(transOffice_debugPrefix + "전국운용팀 셀렉트박스 생성 에러");
			}
		});
	}
}

/**
 * 운용팀 저장
 */
function storeTeam(){
	
	teamChecked = $("#teamField > option:selected").val();
	
	console.log(transOffice_debugPrefix + "저장된 운용팀 ==== " + teamChecked );
}

/**
 * 운용팀 리셋
 */
function resetTeam(){
	$("select[name='select-choice-team'] option").not("[value='selectTeam']").remove();
	
	teamChecked="";
	
	$("#teamField").selectmenu( "refresh" );
}

/**
 * 모국 생성
 */
function makeMoSel(){
	if(teamChecked == "" || teamChecked == "selectTeam"){
		
	}else{
		$.ajax({
			url: url+"WorkPlacePOfficeInfoByDeptTeam",
			type: "post",
			timeout: commonTimeout,
			dataType: "json",
			data: {deptTeam:teamChecked},
			success:function(result){
				var headerData=result.header;
				var moSel="";

				console.log(transOffice_debugPrefix + url+"WorkPlacePOfficeInfoByDeptTeam");

				$.each(headerData, function(key, val) {
					console.log(transOffice_debugPrefix + key + " : " + val);
				});

				if(headerData.result != 0){
					showCommonAlert("모국", headerData.errMsg);
					console.log(transOffice_debugPrefix + "모국 데이터 없음");
				}else{
					var pOfficeData=result.pOffice;
					$.each(pOfficeData, function(key,val){
						$.each(val, function(key, val) {
							console.log(transOffice_debugPrefix + key + " : " + val);
						});
						//모국 생성
						moSel += "<option value='" + val.POFFICESCODE + "'>" + val.POFFICENAMESCODE + "</option>";
					});
					//모국 삽입
					$('#moField').append(moSel).trigger("create").selectmenu( "refresh" );
					//모국 비활성화 풀기
					$('#moField').selectmenu("enable");
				}
				//모국 선택
				$("#moField").val(moChecked).selectmenu( "refresh" );

				console.log(transOffice_debugPrefix + "=============모국 셀렉트박스생성 완료=============");
			},
			error : function(jqXHR, textStatus, errorThrown){
				showCommonAlert("모국", textStatus);
				console.log(transOffice_debugPrefix + "모국 셀렉트박스 생성 에러");
			}
		});
	}	
}

/**
 * 모국 저장
 */
function storeMo(){
	
	moChecked = $("#moField > option:selected").val();
	
	console.log(transOffice_debugPrefix + "저장된 모국 ==== " + moChecked );
	
	officeChecked = $("#officeField > option:selected").val();
}

/**
 * 모국 리셋
 */
function resetMo(){
	$("select[name='select-choice-mo'] option").not("[value='selectMo']").remove();
	
	moChecked="";
	
	$("#moField").selectmenu( "refresh" );
}

/**
 * 사업장 유형 배열 생성
 */
function officeArray(){
	
	$.ajax({
		url: url+"MossCode",
		type: "post",
		timeout: commonTimeout,
		dataType: "json",
		data: {category : 'WORKPLACETYPE'},
		success:function(result){			
			var headerData=result.header;
			
			console.log(transOffice_debugPrefix + url+"MossCode");
			
			$.each(headerData, function(key, val) {
                console.log(transOffice_debugPrefix + key + " : " + val);
            });
			
			if(headerData.result != 0){
				showCommonAlert("사업장 유형", headerData.errMsg);
				console.log(commonDebugPrefix + "사업장 유형 데이터 없음");
			}else{
				var bodyData=result.body;
				
				$.each(bodyData, function(key,val){	
					$.each(val, function(key, val){
	                    console.log(transOffice_debugPrefix + key + " : " + val);
	                });
					workPlaceCode[key]=val.CODE;
					workPlaceName[key]=val.CODEVALUE;									
				});	
			}
		},
		error : function(jqXHR, textStatus, errorThrown){
			showCommonAlert("사업장 유형", textStatus);
			console.log(transOffice_debugPrefix + "사업장 유형 배열 생성 에러");
		}
	});
}

/**
 * 사업장 유형 생성
 */
function makeOfficeSel(){
	
	var officeSel="";
	
	//생성
	for(var i=0; i<workPlaceCode.length; i++){
		officeSel += "<option value='" + workPlaceCode[i] + "'>" + workPlaceName[i] + "</option>";
	}
	//삽입
	$('#officeField').html(officeSel).trigger("create").selectmenu( "refresh" );

	//사업장 선택
	$("#officeField").val(officeChecked).selectmenu( "refresh" );
	
	console.log(transOffice_debugPrefix + "=============사업장유형 셀렉트박스생성 완료=============");
}

/**
 * 사업장 저장
 */
function storeOffice(){
	officeChecked = $("#officeField > option:selected").val();
	
	console.log(transOffice_debugPrefix + "저장된 사업장 ==== " + officeChecked );
}

/**
 * 사업장 리셋
 */
function resetOffice(){
	$("select[name='select-choice-office'] option").remove();
		
	officeChecked="";
	
	makeOfficeSel();		//얘는 항상 불러와줌.
}

/**
 * 셀렉트박스 비활성화(onChange에 걸 함수. 운용단, 운용팀이 변경되면 비활성화 시킴)
 */
function selectBoxDisabled1(){
	$("#teamField").attr("disabled", true);
	$("#moField").attr("disabled", true);
}

/**
 * 셀렉트박스 비활성화(onChange에 걸 함수. 운용팀이 변경되면 비활성화 시킴)
 */
function selectBoxDisabled2(){
	$("#moField").attr("disabled", true);
}

/**
 * 불러올 리스트 선택하기(모국 onChange에서 사용)
 */
function selectWorkplaceList(){
	if(moChecked == "selectMo" || moChecked == ""){		//"모국"이 선택되어 있으면 알림말 출력
		showBeforeWorkplaceList();
	}else{		//모국유형이 선택되어 있으면 사업장 리스트 출력
		showWorkplaceList();
	}
}

/**
 * 미선택 국사 리스트불러오기
 */
function showBeforeWorkplaceList(){
	
	var workplaceLi="";
	
	//선택된 값 얻어오기
	danCode=$("#danField > option:selected").val();
	teamCode=$("#teamField > option:selected").val();
	moCode=$("#moField > option:selected").val();
	officeCode=$("#officeField > option:selected").val();
	
	//국사리스트 생성
	if(danCode == 'selectDan' || danCode == ''){
		workplaceLi = "<li data-icon='false'>" +
				   	  "<a href='#'>" +
				      "<center><h2>운용단을 선택하세요.</h2></center>" +     
				   	  "</a>" +
				      "</li>";
	}else if(teamCode == 'selectTeam' || teamCode == ''){
		workplaceLi = "<li data-icon='false'>" +
				   	  "<a href='#'>" +
				      "<center><h2>운용팀을 선택하세요.</h2></center>" +     
				   	  "</a>" +
				      "</li>";
	}else if(moCode == 'selectMo' || moCode == ''){
		workplaceLi = "<li data-icon='false'>" +
				   	  "<a href='#'>" +
				      "<center><h2>모국을 선택하세요.</h2></center>" +     
				   	  "</a>" +
				      "</li>";
	}
	//사업장 리스트 삽입
	$('#listview').html(workplaceLi).listview("refresh");
}

/**
 * 선택 국사 리스트불러오기
 */
function showWorkplaceList(){
	
	// 페이지가 보여지면서 로더를 보여준다.
	$.mobile.loading( "show", {
		  text: "리스트를 읽어오는 중 입니다.",
		  textVisible: true,
		  theme: "a",
		  html: ""
	});

	//선택된 값 얻어오기
	danCode=$("#danField > option:selected").val();
	teamCode=$("#teamField > option:selected").val();
	moCode=$("#moField > option:selected").val();
	officeCode=$("#officeField > option:selected").val();
	
	//국사리스트 생성
	$.ajax({
		url: url + "MossWorkplaceList",
		type: "post",
		timeout: commonTimeout,
		dataType: "json",
		data:{danorgcode:danCode, pofficescode:moCode, workplacetype:officeCode},
		success: function(result){ 			
			var headerData = result.header;
			var workplaceLi="";

			console.log(transOffice_debugPrefix + "리스트 생성 인풋 조건 ==== " +danCode+","+teamCode+","+moCode+","+officeCode);

			console.log(transOffice_debugPrefix + url+"MossWorkplaceList");
			
			$.each(headerData, function(key, val) {
                console.log(transOffice_debugPrefix + key + " : " + val);
            });
			
			if(headerData.result != 0){
					workplaceLi = "<li data-icon='false'> "+
									"<a href='#'>" +
									"<center><h2>정보가 없습니다.</h2></center>" +     
									"</a>" +
									"</li>";
					$.mobile.hidePageLoadingMsg();
			}else{
				var bodyData = result.body;
				
				$.each(bodyData, function(key, val){
					$.each(val, function(key, val) {
						console.log(transOffice_debugPrefix + key + " : " + val);
					});

					workplaceSN = val.WORKPLACESEQNUM;

						workplaceLi += "<li data-icon='false'>" +
										"<a href='transOffice_detail.html?workplaceSeqNum="+workplaceSN+"'>" +	
										"<div class='listContent'>" +
										"<h2>"+ val.WORKPLACENAME +"</h2>" ;

					if(val.ADDR != null && val.ADDR.length > 20){
						workplaceLi += "<p style='display:inline-block; float:right; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; width:80%;'>"+ val.ADDR +"</p>";					
					}else{
						workplaceLi += "<p style='display:inline-block; float:right'>"+ nullCheck(val.ADDR) + "</p>";	
					}
						workplaceLi +=	"</div>" +
										"</a>" +
										"</li>";										
				});
			}
			//사업장 리스트 삽입
			$('#listview').html(workplaceLi).listview("refresh");
			$.mobile.hidePageLoadingMsg();
		},
		error: function(jqXHR, textStatus, errorThrown){
			$.mobile.hidePageLoadingMsg();
			showCommonAlert("사업장 목록", textStatus);
			console.log(transOffice_debugPrefix + "사업장리스트 출력 에러 : " + jqXHR.status);
			console.log(transOffice_debugPrefix + "사업장리스트 출력 에러 : " + jqXHR.responseText);
			console.log(transOffice_debugPrefix + "사업장리스트 출력 에러 : " + errorThrown);		
		} 
	}).done(function(){	//필터링된 목록은 필터링 된 상태로 남아 있게 하기.
		if(inputWordText==""){
			
		}else{		
			filter();
		}
	});
}

//로그인할때 사업장유형배열 생성
$(document).on("pageshow","#page_login", function(event){  
	officeArray();	
});

//메뉴패널 생성, 반드시 pagebeforecreate 이벤트단에서 호출한다.
$(document).on("pagebeforecreate", "#page_transOffice" , function(event){
	
	commonCreateMenuPanel();
});

//페이지 보여지기 전에 셀렉트 박스 설정
$(document).on("pagebeforeshow","#page_transOffice", function(event){	
	makedanAndTeamSel();
	makeOfficeSel();
});

//페이지가 보여짐과 동시에 리스트 뿌려줌
$(document).on("pageshow","#page_transOffice", function(event){
	//	페이지에 따른 메뉴패널 설정
	$.mobile.selectmenu.prototype.options.hidePlaceholderMenuItems = false;
	
	commonConfigMenuPanel();
				
	$('#page_transOffice').trigger('pagecreate');
		
	//검색창 고정
	officeCSS();	
	
	console.log("단, 팀, 모국, 사업장 값 == " + danChecked +", "+teamChecked+", "+moChecked+", "+officeChecked);
	
	if(icount == 1){//운용단 하나일때
		if(teamChecked == "" || moChecked == ""){//팀, 모국만 값확인
			showBeforeWorkplaceList();
		}else{
			showWorkplaceList();
		}
	}else{//운용단 여러개일때
		if(danChecked == "" || teamChecked == "" || moChecked == ""){//운용단, 팀, 모국 값 확인
			showBeforeWorkplaceList();
		}else{
			showWorkplaceList();
		}
	}

	$("#btn_refresh").click(function() {
		
		$("#listview").empty();

		if(icount == 1){
			if(teamChecked == "" || moChecked == "" || teamChecked == "selectTeam" || moChecked == "selectMo"){	
				showBeforeWorkplaceList();
			}else{
				showWorkplaceList();
			}
		}else{
			if(danChecked == "" || teamChecked == "" || moChecked == "" || danChecked == "selectDan" || teamChecked == "selectTeam" || moChecked == "selectMo"){	
				showBeforeWorkplaceList();
			}else{
				showWorkplaceList();
			}
		}
	});
});

//사업장 페이지를 벗어나면 저장했던 운용단, 운용팀, 모국, 사업장 유형, 검색어 값 초기화
$(document).on("pagehide","#page_transOffice", function(event){
	if($.mobile.activePage.is('#page_transOffice_detail')){	
		
	}else{
		danChecked="";
		teamChecked="";
		moChecked="";
		officeChecked="";
		inputWordText="";
	}
});
