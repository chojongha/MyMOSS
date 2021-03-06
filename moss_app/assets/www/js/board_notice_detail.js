var notice_Detail_debugPrefix = "[board_notice_detail.js] : ";
var filedownloadFlag=false;

function Downloadloder(){
    $.mobile.loading( "show", {
        text: "다운로드 중.",
        textVisible: true,
        theme: "a",
        html: ""
  }); 
    filedownloadFlag=true;
}


function showNoticeDetail(){
    
  //디바이스에 따라 화면 높이 맞추기
    var header = $("div[data-role='header']:visible");
    var footer = $("div[data-role='footer']:visible");
    var content = $("div[data-role='content']:visible"); 
    var viewport_height = $(window).height();
                    
    var content_height = viewport_height - header.outerHeight() - footer.outerHeight(); 
    
    content_height -= (content.outerHeight() - content.height());
    
    $("#notice_table").css('min-height', content_height);
    
    noticeSN=commonRequest("noticeSeqNum");
    
    console.log(notice_Detail_debugPrefix + "넘어온 시퀀스넘버 = " + noticeSN);
    
    $.ajax({
        url: url+"NoticeDetail",        
        type: "post",
        timeout : commonTimeout,
        dataType : "json",
        data: {noticeSeqNum : noticeSN}, //인풋(접속)
        
        
        success: function (result) {
            var bodyData = result.body; //아웃풋 정보 가져오기
            
            $.each(bodyData, function(key, val){ //아웃풋 뿌리기
                
                $.each(val, function(key, val) {
                    console.log(notice_Detail_debugPrefix + key + " : " + val);
                });
                
                var noticeDetailTB =   "<table id='table_detail'>"+
                "<tr>"+ 
                "<td width='25%'>제 목</td>"+
                "<td colspan='3' id='notice_title'></td>"+
                "</tr>"+
                "<tr>"+
                "<td>작성일시</td>"+
                "<td colspan='3' id='notice_writeDate'></td>"+
                "</tr>"+                           
                "<tr>"+
                "<td width='25%'>작 성 자</td>"+
                "<td width='25%' id='notice_writer'></td>"+
                "<td width='25%'>조 회 수</td>"+
                "<td width='25%' id='notice_viewCount'></td>"+
                "</tr>"+   
                "<tr>"+
                "<td colspan='4' id='notice_content' valign='top'></td>"+
                "</tr>"+
                "<tr>"+
                "<td width='20%'>첨부파일</td>"+
                "<td colspan='3' id='notice_file'></td>"+
                "</tr>"+
                "</table>";         
                var noticeCon = "<div id='NoticeContents' style='overflow-y:auto; width:100%; height:100%; padding:4px'><div>";
              
                console.log(notice_Detail_debugPrefix + "첨부파일 일련번호 : " + val.ATTACHSEQNUM);
                console.log(notice_Detail_debugPrefix + "content : " + val.CONTENT);
                
                //본문 테이블 넣기                                                                             
                $('#notice_table').html(noticeDetailTB);                
                $('#notice_content').html(noticeCon);
                //본문 데이터 세팅             
                $('#notice_title').text(val.SUBJECT);
                $('#notice_writeDate').text(val.WRITEDATE);
                $('#notice_writer').text(val.WRITERNAME);
                $('#notice_viewCount').text(val.READCNT);
                $('#NoticeContents').text(val.CONTENT);
                
                console.log(notice_Detail_debugPrefix + "content : " +  $('#NoticeContents').val(val.CONTENT));
                if(val.FILENAME==null){
                    $('#notice_file').html("<div id='noFileName'>없음</div>");
                }else{
                    if(deviceInfo == "Android"){ //안드로이드일땐 로더가 돌며 
                        $('#notice_file').append( 
                                "<div id='wrapper_filename'>" + val.FILENAME + "</div>" + 
                                "<img id='img_filename' style='float:right; margin-right:0.25em;' class='fileDown' src='../img/floppy.png' onmouseover='Downloadloder();' onclick=commonDownloadURL('filedownload?attachSeqNum="+ val.ATTACHSEQNUM +"');>");
                    }else{
                        $('#notice_file').append(
                                "<div id='wrapper_filename'>" + val.FILENAME + "</div>" + 
                                "<img id='img_filename' style='float:right; margin-right:0.25em;' class='fileDown' src='../img/floppy.png' onclick=commonDownloadURL('filedownload?attachSeqNum="+ val.ATTACHSEQNUM +"');>");
                    }
                    //첨부파일 사이즈         
                    var fileTextLength = $('#notice_file').outerWidth() - 50;
                    $('#wrapper_filename').css("width", fileTextLength);
                }
                var div_TB = $("#notice_table").height();
                var TB_all = $("#table_detail").height();

                
                console.log("div 길이 ::::: " + div_TB);
                console.log("테이블 길이 ::::: " + TB_all);

                var TB_content = div_TB - TB_all + 8;
                
                                
                console.log("테이블 내용길이 ::::: " + TB_content);
                                                        
                $("#notice_content").css('height', TB_content);
                $.mobile.hidePageLoadingMsg();
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $.mobile.hidePageLoadingMsg();
            showCommonAlert("공지사항 상세", textStatus);
            console.log(notice_Detail_debugPrefix + "매뉴얼 상세화면 출력 에러 : " + jqXHR.status);
            console.log(notice_Detail_debugPrefix + "매뉴얼 상세화면 출력 에러 : " + jqXHR.responseText);
            console.log(notice_Detail_debugPrefix + "매뉴얼 상세화면 출력 에러 : " + errorThrown);
        }
    }); 
}   


    
$(document).on("pagebeforeshow","#page_notice_detail",function(event){
    //showManualDetail();
}); 
$(document).on("pageshow","#page_notice_detail",function(event){
    $.mobile.loading( "show", {
        text: "리스트를 읽어오는 중 입니다.",
        textVisible: true,
        theme: "a",
        html: ""
  }); 
    
    showNoticeDetail();
}); 