/**
 * 		업무매뉴얼 상세페이지
 */
var manualDetail_debugPrefix = "[board_manual_detail.js] : ";
var manualSeqNum="";
var beforeChecked="";
var filedownloadFlag=false;

/**
 * 파일 다운로드 프로그래스
 */
function downloading(){
	
	$.mobile.loading( "show", {
		  text: "파일 다운로드 중...",
		  textVisible: true,
		  theme: "a",
		  html: ""
	});
	
	filedownloadFlag= true;
}

/**
 * 상세 페이지 출력
 */
function showManualDetail(){
	
	//디바이스에 따라 화면 높이 맞추기
	var header = $("div[data-role='header']:visible");
	var footer = $("div[data-role='footer']:visible");
	var content = $("div[data-role='content']:visible"); 
	var viewport_height = $(window).height();
					
	var content_height = viewport_height - header.outerHeight() - footer.outerHeight();	
	
	content_height -= (content.outerHeight() - content.height());
	
	$("#manual_table").css('min-height', content_height);
					
	//매뉴얼 시퀀스넘버 받기
	manualSN=commonRequest("manualSeqNum");
	
	console.log(manualDetail_debugPrefix + "넘어온 시퀀스넘버 = " + manualSN);
	
	$.ajax({
		url: url+"ManualDetail",		
		type: "post",
		timeout : commonTimeout,
		dataType : "json",
		data: {manualSeqNum:manualSN}, //인풋(접속)		
		success: function (result) {		 
			var headerData = result.header;	//아웃풋 정보 가져오기
			var manualDetailTB = "";
			
			console.log(transOffice_debugPrefix + url+"ManualDetail");
			
			$.each(headerData, function(key, val) {
                console.log(transOffice_debugPrefix + key + " : " + val);
            });
			
			if(headerData.result != 0){
				$.mobile.hidePageLoadingMsg();
				showCommonAlert("업무매뉴얼 상세보기", headerData.errMsg);
			}else{
				var bodyData = result.body;
				
				$.each(bodyData, function(key, val){ //아웃풋 뿌리기
					$.each(val, function(key, val) {
	                    console.log(manualDetail_debugPrefix + key + " : " + val);
	                });
					
					//매뉴얼 상세테이블 생성					
					manualDetailTB = "<table id='table_detail'>"+
										 "<tr>"+ 
										 "<td width='25%'>제 목</td>"+
										 "<td colspan='3' id='manual_title'></td>"+
										 "</tr>"+
										 "<tr>"+
										 "<td>작성일시</td>"+
										 "<td colspan='3' id='manual_writeDate'></td>"+
										 "</tr>"+							
										 "<tr>"+
										 "<td>분   야</td>"+
										 "<td colspan='3' id='manual_services'></td>"+
										 "</tr>"+
										 "<tr>"+
										 "<td width='25%'>작 성 자</td>"+
										 "<td width='25%' id='manual_writer'></td>"+
										 "<td width='25%'>조 회 수</td>"+
										 "<td width='25%' id='manual_viewCount'></td>"+
										 "</tr>"+	
										 "<tr>"+
										 "<td colspan='4' id='manual_content' valign='top'></td>"+  //컨텐츠부분 크기고정
										 "</tr>"+
										 "<tr>"+
										 "<td width='20%'>첨부파일</td>"+
										 "<td colspan='3' id='manual_file'></td>"+	
										 "</tr>"+
										 "</table>"; 				
										
					var manualCon = "<div id='maContents' style='overflow-y:auto; width:100%; height:100%; padding:8px'><div>"; //화면범위 넘어가면 스크롤 생성				
					
					console.log(manualDetail_debugPrefix + "첨부파일 일련번호 : " + val.ATTACHSEQNUM);
							
					//본문 테이블 넣기													 			 				
					$('#manual_table').html(manualDetailTB);				
					$('#manual_content').html(manualCon);
					//본문 데이터 세팅				
					$('#manual_title').text(val.SUBJECT);
					$('#manual_writeDate').text(val.WRITEDATE);
					$('#manual_services').text(val.SERVICEIDNAME+" / "+val.SERVICESUBIDNAME);
					$('#manual_writer').text(val.WRITERNAME);
					$('#manual_viewCount').text(val.READCNT);
					$('#maContents').text(val.CONTENT);
																								
					if(val.FILENAME==null){
						$('#manual_file').html("<div id='noFileName'>없음</div>");
					}else{ 
					    if(deviceInfo == "Android"){ //안드로이드일땐 로더가 돈다 
                            $('#manual_file').append( 
                                    "<div id='wrapper_filename'>" + val.FILENAME + "</div>" + 
                                    "<img id='img_filename' style='float:right; margin-right:0.25em;' class='fileDown' src='../img/floppy.png' onmouseover='Downloadloder();' onclick=commonDownloadURL('filedownload?attachSeqNum="+ val.ATTACHSEQNUM +"');>");
                        }else{
                            $('#manual_file').append(
                                    "<div id='wrapper_filename'>" + val.FILENAME + "</div>" + 
                                    "<img id='img_filename' style='float:right; margin-right:0.25em;' class='fileDown' src='../img/floppy.png' onclick=commonDownloadURL('filedownload?attachSeqNum="+ val.ATTACHSEQNUM +"');>");
                        }
                        //첨부파일 사이즈         
                        var fileTextLength = $('#manual_file').outerWidth() - 50;
                        $('#wrapper_filename').css("width", fileTextLength);
					}
					
					//테이블 크기고정(컨텐츠에 맞춰서)
					var div_TB = $("#manual_table").height();
					var TB_all = $("#table_detail").height();
										
					var TB_content = div_TB - TB_all + 16;	//16은 여백준만큼 더해준것(manualCon의 패딩8px준것(위아래 해서 16)
															
					$("#manual_content").css('height', TB_content);
					
					//화면상 백버튼 링크 추가
					if($.mobile.activePage.attr('id') != 'page_manual_detail'){					
						$('#alarm_beforeManualList').attr('href','alarmList_noti_detail_manual.html?checked='+ val.SERVICEID);
					}else{
						$('#beforeManualList').attr('href','board_manual.html?checked='+ val.SERVICEID);
					}
					
					//안드로이드 백버튼 링크 걸때 쓰일 파라미터
					beforeChecked = val.SERVICEID;	
					
					$.mobile.hidePageLoadingMsg();
			    });	
			}					
		},
		error: function(jqXHR, textStatus, errorThrown) {
			$.mobile.hidePageLoadingMsg();
			showCommonAlert("업무매뉴얼 상세보기", textStatus);
			console.log(manualDetail_debugPrefix + "매뉴얼 상세화면 출력 에러 : " + jqXHR.status);
	        console.log(manualDetail_debugPrefix + "매뉴얼 상세화면 출력 에러 : " + jqXHR.responseText);
			console.log(manualDetail_debugPrefix + "매뉴얼 상세화면 출력 에러 : " + errorThrown);
	    }
	}); 
}

$(document).on("pageshow","#page_manual_detail",function(event){
	//상세화면 불러오기
	$.mobile.loading( "show", {
		  text: "로딩중...",
		  textVisible: true,
		  theme: "a",
		  html: ""
	}); 
	showManualDetail();
}); 
$(document).on("pageshow","#page_alarmManual_detail",function(event){
	//상세화면 불러오기
	$.mobile.loading( "show", {
		  text: "로딩중...",
		  textVisible: true,
		  theme: "a",
		  html: ""
	}); 
	showManualDetail();
}); 