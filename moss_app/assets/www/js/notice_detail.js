var NoticeList_debugPrefix = "[notice_detail.js] : ";

//라디오버튼, 셀렉트박스 생성 및 연결
function list_radio_select(){
    
    $.ajax({        
        url: url+"WorkOdrList",
        type: "post",
        timeout: 10*1000,
        dataType: "json",
        data: {workerID:'91038948'},
        success: function(result){
            var bodyData=result.body;
            var noticeRa="";
            var noticeSel="";
            
            if(bodyData.length > 0){
                $.each(bodyData, function(key,val){
                    noticeRa += "<input type='radio' name='notice' data-theme='d' data-iconpos='right' id='" + val.custName + "' value='" + val.custName + "' checked=''>" +
                                 "<label for='" + val.custName + "'>"+val.custName+"</label>\n";
                    
                    noticeSel += "<option value='"+val.custName+"'>"+val.custName+"</option>";
                    
                });
            }else{
                noticeRa = "<input type='radio' name='notice' data-theme='d' data-iconpos='right' id='not' value='not' checked=''>" +
                           "<label for='not'>데이터없음</label>\n";
                noticeSel = '<option value="no">없음</option>';
            }
            //라디오버튼생성
            $('#noticeRa-controlgroup').html(noticeRa).trigger("create");
            //셀렉트박스 생성          
            $('#noticeField').append(noticeSel).trigger("create").selectmenu( "refresh" );

            //라디오버튼, 셀렉트박스 연결       
            var choice = getParam("checked");
            $("#noticeField").val(choice).selectmenu( "refresh" );
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log(NoticeList_debugPrefix + "라디오버튼, 셀렉트박스 출력 에러 : " + jqXHR.status);
            console.log(NoticeList_debugPrefix + "라디오버튼, 셀렉트박스 출력 에러 : " + jqXHR.responseText);
            console.log(NoticeList_debugPrefix + "라디오버튼, 셀렉트박스 출력 에러 : " + errorThrown);
        }
    });
}

//라디오 버튼 선택후 확인 클릭시 파라미터 전송
function SendParameter(){
    
    var isChecked=$(":input:radio[name=notice]:checked").val();
    $.mobile.changePage('board_notice.html?checked='+isChecked);    
}

//get parameter 파라미터 받기
    var getParam = function(key){
        var _parammap = {};
        document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
            function decode(s) {
                return decodeURIComponent(s.split("+").join(" "));
            }

            _parammap[decode(arguments[1])] = decode(arguments[2]);
        });

        return _parammap[key];
    };




function showNoticeList(){
    // 페이지가 보여지면서 로더를 보여준다.
    $.mobile.loading( "show", {
          text: "리스트를 읽어오는 중 입니다.",
          textVisible: true,
          theme: "a",
          html: ""
    });
    
    $.ajax({
        url: url + "WorkOdrList",
        type: "post",
        timeout: 10*1000,
        dataType: "json",
        data:{workerID:'91038948'},
        success: function(result){
            var bodyData = result.body;
            var noticeLi = "";
            
            if(bodyData.length > 0){
                $.each(bodyData, function(key, val){                    
                        noticeLi += "<li data-icon='false'>" +
                                         "<a href='board_notice_detail.html'>" +
                                            "<table id='dynamicList'>" +
                                                "<tr>" +
                                                    "<td class='dynamicTable_td'><h2 style='display:inline-block;'>"+ val.odrType +"</h2></td>" +
                                                "</tr>" +
                                                "<tr>" +
                                                    "<td class='dynamicTable_td'>"+ val.rsvDT +"</td>" +
                                                "</tr>" +
                                                "<tr>" +
                                                    "<td class='dynamicTable_td' align='right'>"+ val.custName +"</td>" +
                                                "</tr>" +
                                            "</table>" +
                                        "</a>" +
                                    "</li>";                
                });
            }else{
                noticeLi = "<li data-icon='false'> "+
                           "    <a href='#'>" +
                           "        <center><h2>데이터가 존재하지 않습니다.</h2></center>" +
                           "    </a>" +
                           "</li>";
            }
            $('#listview').append(noticeLi).listview("refresh");
            $.mobile.hidePageLoadingMsg();
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log(NoticeList_debugPrefix + "매뉴얼리스트 출력 에러 : " + jqXHR.status);
            console.log(NoticeList_debugPrefix + "매뉴얼리스트 출력 에러 : " + jqXHR.responseText);
            console.log(NoticeList_debugPrefix + "매뉴얼리스트 출력 에러 : " + errorThrown);
        } 
    });
}   

$(document).on("pagebeforeshow","#page_notice_list", function(event){
    list_radio_select();
    showNoticeList();
});

$(document).on("pageshow","#page_notice_list", function(event){
//  showManualList();
});