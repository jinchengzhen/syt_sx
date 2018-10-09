//http://127.0.0.1:8080/SRT_QZYTH/#
var currpage = 1;
var pagesize = 10;
var totalpage = 0;


$(function(){
		notice_Init();
});

//查询
function notice_search(){
	var name = $("#notice_person").val();
	var datetime = $("#notice_date").val();
	var url = "selectlimit.action";
	var args = {"FBRXM":name,"FBSJ":datetime,"currpage":currpage,"pagesize":pagesize};
	$.post(url,args,function(data){
		var state = data.state;
		var msg = data.mag;
		var htmlstr = "";
		if(state == 1){
			var totaldata = data.totaldata;
			$("#total_data").html(totaldata);
			totalpage = Math.ceil(totaldata*1.0/pagesize);
			for(var i=0;i<msg.length;i++){
				var zt = "置顶";
				if(msg[i].ZT == '1'){
					zt = "取消置顶";
				}
				var ggnr = msg[i].GGNR;
				if(ggnr.length>12){
					ggnr = ggnr.substring(0,10)+"...";
				}
				htmlstr += "<div class=\"notice_col_1\"><span>"+((currpage-1)*pagesize+i+1)+"</span></div><div class=\"notice_col_2\"><span title=\""+msg[i].GGNR+"\">"+ggnr+"</span></div><div class=\"notice_col_3\"><span>"+msg[i].FBRXM+"</span></div><div class=\"notice_col_4\"><span>"+msg[i].FBSJ+"</span></div><div class=\"notice_col_5\"><span onclick=\"deleteInfo('"+msg[i].BH+"')\">删除</span>&nbsp;&nbsp;<span id=\"zt"+msg[i].BH+"\" onclick=\"setType('"+msg[i].BH+"')\">"+zt+"</span></div>";
			}
			$("#notice_list_body").html(htmlstr);
			pagenum();
		}else{
			alert(msg);
		}
	});
}
//页面加载后查询n条数据：8条
function notice_Init(){
	notice_info();
	notice_alert();
}
//滚动公告显示初始化
function notice_alert(){
	var url = "selectAlert.action";
	var args = {};
	$.post(url,args,function(data){
		var state = data.state;
		var msg = data.mag;
		var htmlstr = "";
		if(state == 1){
			for(var i=0;i<msg.length;i++){
				htmlstr += "<span class=\"pad_right\">"+msg[i].GGNR+"</span>";
//				htmlstr += "<li>"+msg[i].GGNR+"</li>";
			}
//			$("#announce_info").html(htmlstr);
			$("#scroll_begin").html(htmlstr);
			$("#scroll_end").html(htmlstr);
		}else{
//			$("#announce_info").html("<li>"+msg+"</li>");
			$("#scroll_begin").html("<span class=\"pad_right\">"+msg+"</span>");
			$("#scroll_end").html("<span class=\"pad_right\">"+msg+"</span>");
		}
	});
}
//公告栏信息初始化
function notice_info(){
	var url = "selectInfo.action";
	var args = {};
	$.post(url,args,function(data){
		var state = data.state;
		var msg = data.mag;
		var htmlstr = "";
		if(state == 1){
			for(var i=0;i<msg.length;i++){
				var ggnr = msg[i].GGNR;
				if(ggnr.length>8){
					ggnr = ggnr.substring(0,6)+"...";
				}
				htmlstr += "<div class=\"an_info_row1\">";
	    		htmlstr += "<div class=\"an_info_col1\"><font>"+(i+1)+"</font></div><div class=\"an_info_col2\" title=\""+msg[i].GGNR+"\"><font>"+ggnr+"</font></div><div class=\"an_info_col3\"><font>"+msg[i].FBRXM+"</font></div><div class=\"an_info_col4\"><font>"+msg[i].FBSJ+"</font></div>";
    			htmlstr += "</div>";
			}
			$("#an_info_list_block").html(htmlstr);
		}else{
			alert(msg);
		}
	});
}
//新增公告
function add_notice_submit(){
	var ggnr = $("#add_ggnr_in").val();
	var url = "addInfo.action";
	var args = {"GGNR":ggnr};
	$.post(url,args,function(data){
		alert(data.mag);
		close_add_alert();
		flush_search();
	});
}
//删除公告
function deleteInfo(bh){
	var url = "deleteInfo.action";
	var args = {"BH":bh};
	$.post(url,args,function(data){
		alert(data.mag);
		flush_search();
	});
}
//设置置顶
function setType(bh){
	var zt = $("#zt"+bh).text();
	var url = "setType.action";
	var ztnum = 1;
	if(zt == "取消置顶"){
		ztnum = 0;
		zt = "置顶";
	}else{
		zt = "取消置顶";
	}
	var args = {"BH":bh,"ZT":ztnum};
	$.post(url,args,function(data){
		var state = data.state;
		if(state == 1){
			$("#zt"+bh).html(zt);
		}
	});
}
//刷新查询
function flush_search(){
	notice_search();
	notice_Init();
}
//更多
function moreinfo(){
	$("#notice_date").val("");
	notice_search();
	$('#detail_notice').show(maskShow());
}
//关闭弹窗
function close_notice(){
	//清空数据
	$("#notice_person").val("");
	$("#detail_notice").hide();
	maskHide();
}
function close_add_alert(){
	$("#add_ggnr_in").val("");
	$("#add_notice_alert").hide();
	tmaskHide();
}
// 显示遮罩
function maskShow() {
	$("#mask").css("display", "inline-block");
}
function tmaskShow(){
	$("#t_mask").css("display", "inline-block");
}
// 隐藏遮罩
function maskHide() {
	$("#mask").css("display", "none");
}
function tmaskHide() {
	$("#t_mask").css("display", "none");
}

//分页
function pageturn(ptname){
	if(ptname=="firstpage"){
		if(currpage>1){
			currpage=1;
		}
	}
	else if(ptname=="pastpage"){
		if(currpage>1){
			currpage--;
		}
	}
	else if(ptname=="nextpage"){
		if(currpage<totalpage){
			currpage++;
		}
	}
	else if(ptname=="endpage"){
		if(currpage!=totalpage){
			currpage=totalpage;
		}
	}else{
		currpage =  parseInt(ptname);
	}
	//刷新查询页面
	flush_search();
}
function pagenum(){
	var htmlstr = "";
	if(totalpage>3){
		if(currpage <= 2){
			htmlstr += "<span onclick=\"pageturn('1')\" class=\"notice_page_num\">1</span><span onclick=\"pageturn('2')\" class=\"notice_page_num\">2</span><span onclick=\"pageturn('3')\" class=\"notice_page_num\">3</span><span class=\"notice_page_num\">...</span>";
			
		}else if(currpage >= (totalpage-1)){
			htmlstr += "<span class=\"notice_page_num\">...</span><span onclick=\"pageturn('"+(totalpage-2)+"')\" class=\"notice_page_num\">"+(totalpage-2)+"</span><span onclick=\"pageturn('"+(totalpage-1)+"')\" class=\"notice_page_num\">"+(totalpage-1)+"</span><span onclick=\"pageturn('"+totalpage+"')\" class=\"notice_page_num\">"+totalpage+"</span>";
		}else{
			htmlstr += "<span class=\"notice_page_num\">...</span><span onclick=\"pageturn('"+(currpage-1)+"')\" class=\"notice_page_num\">"+(currpage-1)+"</span><span onclick=\"pageturn('"+currpage+"')\" class=\"notice_page_num\">"+currpage+"</span><span onclick=\"pageturn('"+(currpage+1)+"')\" class=\"notice_page_num\">"+(currpage+1)+"</span><span class=\"notice_page_num\">...</span>";
		}
	}else{
		for(var i=1;i<=totalpage;i++){
			htmlstr += "<span onclick=\"pageturn('"+i+"')\" class=\"notice_page_num\">"+i+"</span>";
		}
	}
	$("#notice_page_num_block").html(htmlstr);
}
//var num_data = 0;
//var notice_info = ["aaaaaaaaaaaaa","bbbbbbbbbbbbbbb","ccccccccccccccccc","ggggggggggg"];
	//定时器滚动显示公告
//	$('body').everyTime('2s',function(){
//	if(num_data==notice_info.length){
//		num_data = 0;
//	}
//	$("#announce_info").html(notice_info[num_data++]);
//	 $("#up_block").slideDown();
//
//	});