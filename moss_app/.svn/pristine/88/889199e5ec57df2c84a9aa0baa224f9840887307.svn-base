var board_notice_debugPrefix = "[board_notice.js] : ";

function showNoticeList(){
    // 페이지가 보여지면서 로더를 보여준다.
    $.mobile.loading( "show", {
          text: "리스트를 읽어오는 중 입니다.",
          textVisible: true,
          theme: "a",
          html: ""
    });
    
    $.ajax({
        url: url + "NoticeList",
        type: "post",
        timeout: commonTimeout,
        dataType: "json",
        
        success: function(result){
            var bodyData = result.body;
            var noticeLi = "";
            var headerData = result.header;
            var yflag = 1;
            var nflag = 1;
            
            if(headerData.result != 0){
                noticeLi = "<li data-icon='false'> "+
                "    <a href='#'>" +
                "        <center><h2>데이터가 존재하지 않습니다.</h2></center>" +
                "    </a>" +
                "</li>";
                
                console.log(board_notice_debugPrefix + "분야데이터없음");
            }else{
            
            if(bodyData.length > 0){
                $.each(bodyData, function(key, val){
                    
                    $.each(val, function(key, val) {
                        console.log(board_notice_debugPrefix + key + " : " + val);
                    });
                    
                    //최상위 공지사항 여부
                    if(val.TOPYN == "Y" ){
                        if(yflag > 0 ) {
                            noticeLi += "<li data-role='list-divider'>고정공지사항</li>";
                            yflag = 0;
                        }
                        
                        noticeLi += "<li data-icon='false'>" +
                                         "<a href='board_notice_detail.html?noticeSeqNum="+val.NOTICESEQNUM+"'>" +
                                         "<div class='listContent'>" +
                                         "<div style='overflow:hidden; text-overflow:ellipsis; margin-top:0.5em; margin-bottom:0.5em;'>" +
                                         "<div style='display:inline; border-width:1px;'><img src='../img/noticetop.png' style='height:1.5em; width:1.5em; margin-bottom: -0.3em;'></div>" +
                                         "<div style='display:inline; font-size:1.3em;'>"+ val.SUBJECT +"</div>" +
                                         "</div>" +
                                         "<p style='display:inline-block; float:left'>"+ val.WRITEDATE +"</p>" +
                                         "<p style='display:inline-block; float:right'>"+  val.WRITERNAME + "</p>" +
                                         "</div>" +
                                         "</a>" +
                                         "</li>";
                        
                    }else{
                        if(nflag > 0 ) {
                            noticeLi += "<li data-role='list-divider'>일반공지사항</li>";
                            nflag = 0;
                        }
                        
                            noticeLi += "<li data-icon='false'>" +
                                         "<a href='board_notice_detail.html?noticeSeqNum="+val.NOTICESEQNUM+"'>" +
                                         "<div class='listContent'>" +
                                         "<h2>"+ val.SUBJECT +"</h2>" +
                                         "<p style='display:inline-block; float:left'>"+ val.WRITEDATE +"</p>" +
                                         "<p style='display:inline-block; float:right'>"+  val.WRITERNAME + "</p>" +
                                         "</div>" +
                                         "</a>" +
                                         "</li>";
                        
                    }});
            }else{
                noticeLi = "<li data-icon='false'> "+
                           "    <a href='#'>" +
                           "        <center><h2>데이터가 존재하지 않습니다.</h2></center>" +
                           "    </a>" +
                           "</li>";
            }
            }
            $('#noticeList').append(noticeLi).listview("refresh");
            $.mobile.hidePageLoadingMsg();
             
        },
        error: function(jqXHR, textStatus, errorThrown){
            $.mobile.hidePageLoadingMsg();
            showCommonAlert("공지사항", textStatus);
            console.log(board_notice_debugPrefix + "매뉴얼리스트 출력 에러 : " + jqXHR.status);
            console.log(board_notice_debugPrefix + "매뉴얼리스트 출력 에러 : " + jqXHR.responseText);
            console.log(board_notice_debugPrefix + "매뉴얼리스트 출력 에러 : " + errorThrown);
        } 
    });
}

$(document).on("pagebeforecreate","#page_notice_list", function(event){   
//  메뉴패널 생성, 반드시 pagebeforecreate 이벤트단에서 호출한다.
    commonCreateMenuPanel();

});

$(document).on("pageshow","#page_notice_list", function(event){
    //  페이지에 따른 메뉴패널 설정
    commonConfigMenuPanel();
    
    $('#page_notice_list').trigger('pagecreate');
    
    showNoticeList();
    
    $("#btn_refresh").click(function () {
        $("#noticeList").empty();
        
        showNoticeList(); 
    });
});

$(document).on("pageshow","#page_notice_list", function(event){
//  showNoticeList();
});

