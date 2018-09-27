//通过cookie获取登录人信息
var cookie_datastr=$.cookie("data");
var loginer=JSON.parse(cookie_datastr);
var login_time = loginer.time;
var loginer_type = loginer.type;
var loginer_ID = loginer.userID;
//分页设置
var currpage = 1;
var pagesize = 10;
var totalpage = 0;
//树初始化设置
var stusetting = {view: {selectedMulti: false,txtSelectedEnable: true},
            check: {enable: true,  chkStyle: "radio" ,/*单选框*/   /*radioType: "all" ,  */  /*对所有节点设置单选*/chkboxType :{ "Y" : "", "N" : "" }},
            data: {simpleData: {enable: true } },
            edit: { enable: false },
            callback: { onClick: blocktreehide } 
         	};
var teasetting = {view: {selectedMulti: false,txtSelectedEnable: true},
        check: {enable: true,  chkboxType :{ "Y" : "", "N" : "" }},
        data: {simpleData: {enable: true } },
        edit: { enable: false },
     	};

var treedata_Tea = null;//课程
var treedata_Stu = null;//课程
var treedata_grade = null;//班级
var chinesechar = /[\u4e00-\u9fa5]+/;//匹配中文字符
var patrn=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/;//判断是否存在非法字符
var retpassword = /[a-zA-Z0-9_]{6,20}$/;//匹配密码
var retphone =  /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
var retnum = /\d{10}/;
//页面加载后执行
$(function(){
//	user_type = loginer.type;
	loginerInfo();
	if(loginer_type=="0"){
		load_addtree();
		$("#init").click();
	}
});
//退出操作
function exist(){ location.href="../../";}
//登录后个人信息显示detailInfo
function loginerInfo(){
	detailInfo(loginer_ID,loginer_type);
	$("#logintime").text(login_time);
	$("body").keydown(function() {
		if (event.keyCode == "13") {//keyCode=13是回车键
			var id=$("input:focus").attr("id");
		}
	});
}
/**
 * 学生成绩表单
 */
function stu_score_list(userID,name){
	$("#personal_nameval").text(name);
	$("#personal_idval").text(userID);
	var url = "stuScore.action";
	var args = {"userID":userID};
	$.post(url,args,function(data){
		var state = data.state;
		var mag = data.message;
		var htmlstr="";
		if(state=="1"){
			for(var i=1;i<mag.length;i++){
				var jsonobj = mag[i];
				htmlstr += "<tr><td >"+jsonobj.className+"</td><td >"+jsonobj.score+"</td></tr>";
			}
			//总成绩
//			htmlstr += "<tr><td >"+mag[0].className+"</td><td >"+mag[0].score+"</td></tr>";
			$("#person_score_list").html(htmlstr);
		}else{
			html += mag;
		}
	});
}

/**
 * 教师操作
 */
//查询成绩列表
function deal_search_score(num){
	var className = $("#c"+num).val();
	var gradeID = $("#g"+num).val();
	//查询条件判断是否为空
	if(className==""||gradeID==""){
		msg("请选择查询条件！");
		return;
	}
	if(num=="2"){
		select_done(className,gradeID);
	}else if(num=="1"){
		select_to_do(className,gradeID);
	}
	
}
//已录成绩查询（可导出excel数据）
function select_done(className,gradeID){
	var sortway = $("input[name='sort_way']:checked").attr("id");
	var args = {"className":className,"gradeID":gradeID,"sortway":sortway,"currpage":currpage,"pagesize":pagesize};
	var url = "selectScore.action";
	$.post(url,args,function(data){
		var state = data.state;
		var mag = data.message; 
		if(state=="1"){
			var currtotalpage = mag[0].totalpage;
			if(currtotalpage!=0){
				if(currtotalpage != totalpage){
					totalpage = currtotalpage;
				}
				$("#query_classname").text(className);
				var html = "";
				var num = (currpage-1)*pagesize;
				
				for(var i=1;i<mag.length-1;i++){
					var jsondata = mag[i];
					html += "<tr><td>"+i+"</td><td>"+gradeID+"班</td>";
					html += "<td>"+jsondata.stuName+"</td>";
					html += "<td>"+jsondata.stuID+"</td>";
					html += "<td>"+jsondata.score+"</td>";
					html += "<td><span id='"+jsondata.stuID+"' onclick='$(\"#mengc\").show(stu_score_list(\""+jsondata.stuID+"\",\""+jsondata.stuName+"\"))'>其他课程成绩</span></td>";
					html += "</tr>";
				}
				$("#query_score_list").html(html);
				$("#pagefoot2").text(currpage+"/"+totalpage);
			}else{
				msg("查询数据为空！");
			}
			
		}
	});
}


//录入修改查询
function select_to_do(className,gradeID){
	var args = {"className":className,"gradeID":gradeID,"currpage":currpage,"pagesize":pagesize};
	var url = "selectScore.action";
	$.post(url,args,function(data){
		var state = data.state;
		var mag = data.message; 
		if(state=="1"){
			var currtotalpage = mag[0].totalpage;
			if(currtotalpage!=0){
				$("#score_classname").text(className);
				var htmlstr = "";
				var num = (currpage-1)*pagesize;
				if(currtotalpage != totalpage){
					totalpage = currtotalpage;
				}
				for(var i=1;i<mag.length-1;i++){
					var colorstr = "red";
					var statestr = "未录入";
					var jsondata = mag[i];
					htmlstr += "<tr><td>"+gradeID+"班</td>" ;
					htmlstr += "<td>"+jsondata.stuName+"</td>";
					htmlstr += "<td>"+jsondata.stuID+"</td>";
					htmlstr += "<td><input id=\""+jsondata.stuID+"\"name=\"scoreval\"type=\"text\"style=\"font-size:22px;text-align: center;\"";
					if(jsondata.score!=""&&jsondata.score!=null){
						colorstr = "green";
						statestr = "已录入";
						htmlstr += " value=\""+jsondata.score+"\"title=\"双击修改成绩\" readonly";
					}
					htmlstr += "></td>";
					htmlstr += "<td><font id=\"state"+jsondata.stuID+"\"style=\"color: "+colorstr+";\">"+statestr+"</font></td></tr>";
					htmlstr += "<tr class=\"empty_tr\"></tr>";
				}
				$("#update_score_list").html(htmlstr);
				$("#pagefoot1").text(currpage+"/"+totalpage);
				db_update_score();
				update_stuScore();
			}else{
				msg("查询数据为空！");
			}
		}
	});
}
//修改，登记成绩
//双击可修改成绩（设置input可修改）
function db_update_score(){
	$('input[name=\"scoreval\"]').dblclick(function(){
		var idnum = $(this).attr("id");
		$("#"+idnum).removeAttr("readonly");
		$("#"+idnum).css("background-color","#ffff00");
		$("#state"+idnum).css("color","red");
		$("#state"+idnum).text("未录入");
		var old_score = $("#"+idnum).val();
		$("#"+idnum).focusout(function(){
			var new_score = $("#"+idnum).val();
			if(old_score==new_score){
				$("#state"+idnum).css("color","green");
				$("#state"+idnum).text("已录入");
				$("#"+idnum).css("background-color","azure");
				$("#"+idnum).attr("readonly","readonly");
			}
		});
	});
}
//成绩修改
function update_stuScore(){
	$("input[name='scoreval']").change(function(){
		var stuID = $(this).attr("id");
		var score = $(this).val();
		var className = $("#score_classname").text();
		var url = "updateScore.action";
		var args = {"IDstr":stuID,"scorestr":score,"className":className};
		$.post(url,args,function(data){
			var state = data.state;
			var mag = data.message; 
			if(state=="1"){
				$("#state"+stuID).css("color","green");
				$("#state"+stuID).text(mag);
				$("#"+stuID).css("background-color","azure");
				$("#"+stuID).attr("readonly","readonly");
				$("#"+stuID).attr("title","双击修改成绩");
			}else{
				$("#state"+stuID).css("color","red");
				$("#state"+stuID).text(mag);
				$("#"+stuID).css("background-color","red");
			}
		});
	});
}
/**
 * 管理员页面操作
 */
//新增人员类型选择
function addblockshow(type){
	$("#reset").click();
	$("#typeval").attr("value",type);
}
//查询方式选择
function updatefindway(way){
	$("#findway").attr("name",way);
	showblock('selectInfoBlock');
	$("#selIn").val(null);
	if(way=="userway"){
		$("#findway").html("<font>用户名/ID：</font>");
		$("#selIn").attr("placeholder","请输入用户名/ID");
		$('#selIn').removeAttr("readonly");
	}else if(way=="gradeway"){
		$("#findway").html("<font>班&#160;级：</font>");
		$("#selIn").attr("placeholder","请选择  班 级");
		$.fn.zTree.init($("#querytree"), stusetting, treedata_grade);
		$('#selIn').attr("readonly","readonly");
	}else if(way=="classway"){
		$("#findway").html("<font>课&#160;程：</font>");
		$("#selIn").attr("placeholder","请选择 课 程");
		$.fn.zTree.init($("#querytree"), stusetting, treedata_Tea);
		$('#selIn').attr("readonly","readonly");
	}
}
//查询处理（列表）
function deal_search(){
	var type = $('input:radio:checked').val();
	var search_in = $("#selIn").val();
	var findway = $("#findway").attr("name");
	var args = {"type":type,"findway":findway,"val":search_in,"currpage":currpage,"pagesize":pagesize};
	var url = "selectInfo.action";
	$.post(url,args,function(data){
		var state = data.state;
		var mag = data.message;
		var html = "";
		if(state == "1"){
			var num = (currpage-1)*pagesize;
			var currtotalpage = mag[0].totalpage;
			if(currtotalpage != totalpage){
				totalpage = currtotalpage;
			}
			for(var i=1;i<mag.length;i++){
				var jsonobj = mag[i];
				var phone = jsonobj.phone;
				var sex = jsonobj.sex;
				var ID = jsonobj.userID;
				if(phone==null){
					phone = "";
				}
				if(sex=="1"){
					sex = "男";
				}else if(sex=="0"){
					sex = "女";
				}
				html += "<tr><td>"+(num+i)+"</td><td>";
				html += jsonobj.gradeID+" </td><td>";
				html += ID+"</td><td>";
//				html += jsonobj.password+"</td><td>";
				html += jsonobj.name+"</td><td>";
				html += sex+"</td><td>";
//				html += jsonobj.birth+"</td><td>";
//				html += phone+"</td><td>";
				html += "<span id='"+ID+"'name=\"search\"onclick=\"$('#mengc').show(detailInfo('"+ID+"','"+jsonobj.type+"'))\" >查看详情</span><span id='"+(num+i)+"'name=\"del\"onclick=\"delete_user('"+ID+"','"+jsonobj.type+"')\">删除人员</span></td></tr>"	
			}
			$("#querylist").html(html);
			$("#pagefoot").text(currpage+"/"+totalpage);
		}else{
			msg(mag);
		}
	});
}
//新增处理
function deal_add(){
	if(check_img_state()){
		var type = $("#typeval").attr("value");
		var name = $("#nameval").val();
		var sex = $('input[name="sexval"]:checked').val();
		var birth = $("#birthval").val();//YYYY-MM-DD
		var userID = $("#userIDval").val();
		var password = $("#passwordval").val();
		var phone = $("#phoneval").val();
		//获取课程、班级选中信息
		var grade = acquire_tree_checked("gradetree");
		var classes = acquire_tree_checked("classtree");
		var args = {"type":type,"name":name,"sex":sex,"birth":birth,"userID":userID,"password":password,"phone":phone,"grade":grade,"classes":classes};
		var url = "addInfo.action";
		$.post(url,args,function(data){
			var state = data.state;
			if(state == "1"){
				$("#reset").click();
				reset_img();
			}
			var mag = data.message;
			msg(mag);
		});
	}
}
//删除操作
function delete_user(userID,type){
	var url = "deleteInfo.action";
	var args = {"userID":userID,"type":type};
	$.post(url,args,function(data){
		var state = data.state;
		if(state == "1"){
			//更新页面
			deal_search();
		}
		msg(data.message);
	});
}
function info_hide(){
	$('#mengc').hide();
}
//------------------------------------------------------------------------//
//显示树div
function treeblockshow(treeName){
	if(treeName=="class"){
		var state = $("#ztreeblock").attr("value");
		if(state!="show"){
			$("#ztreeblock").show();
			$("#ztreeblock").attr("value","show");
		}
	}else if(treeName=="grade"){
		var state = $("#ztreeblock0").attr("value");
		if(state!="show"){
			$("#ztreeblock0").show();
			$("#ztreeblock0").attr("value","show");
		}
	}else if(treeName=="query"){
		var findway = $("#findway").attr("name");
		if(findway != "userway"){
			var state = $("#querytreeblock").attr("value");
			if(state!="show"){
				$("#querytreeblock").show();
				$("#querytreeblock").attr("value","show");
			}
		}
	}
	stopPropagation();
}
//--------------------------------------------------------------------------//
//隐藏树div
function treeblockhide(val){
	var querystate = $("#querytreeblock").attr("value");
		if("show"==querystate){
		$("#querytreeblock").hide();
		$("#querytreeblock").attr("value","hide");
		$("#selIn").val(val);
	}
	stopPropagation();
}
//点击节点文字input显示对应值并隐藏
function blocktreehide(event, treeId, treeNode){
	var treeObj = $.fn.zTree.getZTreeObj("querytree");
		treeNode.checked = true;
    treeObj.updateNode(treeNode);
    if(treeNode.chkDisabled==false){
    	treeblockhide(treeNode.name);
    }
}
//新增数据格式验证
function checkdata(){
	
	$("#nameval").focusout(function(){
	 var name = $(this).val();
	 var id = $(this).attr("id");
	 if(patrn.test(name)){
			msg("姓名存在 非法字符！");
			$("#state"+id).attr("src","../images/f.png");
			return false;
	}else{
		if(!chinesechar.test(name)){
			msg("请输入中文字符！");
			$("#state"+id).attr("src","../images/f.png");
			return false;
		}
		$("#state"+id).attr("src","../images/t.png");
	}
	 return true;
	});
	
	$("#birthval").focusout(function(){
	 var birth = $(this).val();
	 if(birth==""||birth==null){
		 $("#state"+id).attr("src","../images/f.png");
		 return false;
	 }
	 var id = $(this).attr("id");
	 var d=new Date(Date.parse(birth.replace(/\-/g, "\/")));
	 var curDate=new Date();
	 if(d >curDate){
		 msg("出生日期应小于今日！");
		 $("#state"+id).attr("src","../images/f.png");
	     return false;
	 }
	 $("#state"+id).attr("src","../images/t.png");
	 return true; 
	});
	$("#userIDval").focusout(function(){
		 var userID = $(this).val();
		 var id = $(this).attr("id");
		 if(!retnum.test(userID)){
			 $("#state"+id).attr("src","../images/f.png");
				msg("用户ID为10位数字！");
				return false;
			}
		 $("#state"+id).attr("src","../images/t.png");
		 return true;
		});
	$("#passwordval").focusout(function(){
		 var password = $(this).val();
		 var id = $(this).attr("id");
		 if(!retpassword.test(password)){
				msg("密码格式错误！");
				$("#state"+id).attr("src","../images/f.png");
				return false;
		}
		 $("#state"+id).attr("src","../images/t.png");
		 return true;
	});
	$("#phoneval").focusout(function(){
		 var phone = $(this).val();
		 var id = $(this).attr("id");
		 if(!retphone.test(phone)){
			 $("#state"+id).attr("src","../images/f.png");
				msg("电话号码位数错误！");
				return false;
			}
		 $("#state"+id).attr("src","../images/t.png");
		 return true;
		});
}
//新增验证信息是否填写完全、正确
function check_img_state(){
	var password = $("#statepasswordval").attr("src");
	var name = $("#statenameval").attr("src");
	var birth = $("#statebirthval").attr("src");
	var userID = $("#stateuserIDval").attr("src");
	if(name.indexOf("f.png")>=0){
		msg("姓名 未输入或格式错误！");
		return false;
	}
	if(birth.indexOf("f.png")>=0){
		msg("出生日期 未输入或格式错误！");
		return false;
	}
	if(userID.indexOf("f.png")>=0){
		msg("用户ID 未输入或格式错误！");
		return false;
	}
	if(password.indexOf("f.png")>=0){
		msg("密码 未输入或格式错误！");
		return false;
	}
	return true;
}
/**
 * 初始化数据
 */
//--------------------------------------------------------------//
//验证标志img初始化
function reset_img(){
	$("#statenameval").attr("src","../images/f.png");
	$("#statebirthval").attr("src","../images/f.png");
	$("#stateuserIDval").attr("src","../images/f.png");
	$("#statepasswordval").attr("src","../images/f.png");
	$("#statephoneval").attr("src","../images/f.png");
}

//加载新增div中选择框数据
function load_addtree(){
//获取班级
	$.post("gradeTree.action",function(data){
		var state = data.state;
		if(state ==1){
			treedata_grade = data.message;
		}else {
			msg(treedata_grade);
		}
	});
//获取教师课程
	$.post("classTree.action",{"type":"2"},function(data){
		var state = data.state;
		if(state ==1){
			treedata_Tea = data.message;
		}else {
			msg(treedata_Tea);
		}
	});
//获取学生课程
	$.post("classTree.action",{"type":"1"},function(data){
		var state = data.state;
		if(state ==1){
			treedata_Stu = data.message;
		}else {
			msg(treedata_Stu);
		}
	});
}
//--------------------------------------------------------------//
//初始化页码设置
function page_reset(){
	var currpage = 1;
	var pagesize = 10;
}
//初始化新增div中的树
function resetaddtree(){
	var type = $("#typeval").attr("value");
	var settingtree = null;
	var classtreedata = null;
	if(type=='1'){
		settingtree = stusetting;
		classtreedata = treedata_Stu;
		$("#typeval").text("学生");
	}else if(type=='2'){
		settingtree = teasetting;
		classtreedata = treedata_Tea;
		$("#typeval").text("教师");
	}
	//初始化班级列表
	$.fn.zTree.init($("#gradetree"), settingtree, treedata_grade);
	//初始化课程树
	$.fn.zTree.init($("#classtree"), settingtree, classtreedata);
}
//--------------------------------------------------------------//
/**
 * 公共调用方法
 */
//显示div
function showblock(id){
	hideblock();
	if(id=="addPerson_Block"){
		$('#'+id).show(checkdata());
	}else{
		$('#'+id).show();
	}
}
//隐藏div
function hideblock(){
	$("#reset").click();
	resetaddtree();
	page_reset();
	$("#ztreeblock0").attr("value","hide");
	$("#ztreeblock").attr("value","hide");
	$(".tcontent").hide();
	$("#mengc").hide();
	$("#select_Info_Block").hide();
	$("#addPerson_Block").hide();
	$("#inert_Score_Block").hide();
	$("#select_Score_Block").hide();
}
//查询处理（个人详细信息）
function detailInfo(userID,type){
	var url = "personInfo.action";
	var args = {"userID":userID,"type":type};
	$.post(url,args,function(data){
		var state = data.state;
		var mag = data.message;
		if(state == "1"){
			setval2html(mag);
		}else{
			msg(mag);
		}
	});
}
//数据赋值到页面（详细个人信息）
function setval2html(jsondata){
	if(jsondata!=null){
		var phone = jsondata.phone;
		var sex = jsondata.sex;
		var type = jsondata.type;
		if(type=="1"){
			type = "学生";
		}else if(type=="2"){
			type = "教师";
		}else if(type=="0"){
			type = "管理员";
		}
		if(phone==null||phone==""){
			phone = "无";
		}
		if(loginer_ID == jsondata.userID){
			if(sex=="1"){
				sex = "男";
				$("#user_jpg").attr("src","../images/tx1.jpg");
			}else if(sex=="0"){
				sex = "女";
				$("#user_jpg").attr("src","../images/tx0.jpg");
			}
			$("#loginer_typeval").text(type);
			$("#loginer_nameval").text(jsondata.name);
			$("#loginer").text(jsondata.name);
			$("#loginer_sexval").text(sex);
			$("#loginer_birthval").text(jsondata.birth);//YYYY-MM-DD
			$("#loginer_userIDval").text(jsondata.userID);
			$("#loginer_passwordval").text(jsondata.password);
			$("#loginer_phoneval").text(phone);
			$("#loginer_gradeval").text(deal_a_str(jsondata.gradeID,"班;"));
			$("#loginer_classval").text(jsondata.classes);
			if(type = "教师"){
				deal_classtr(jsondata.classes);
				deal_gradestr(jsondata.gradeID);
			}
			if(loginer_type=="1"){
				stu_score_list(loginer_ID,jsondata.name);
			}
		}else{
			if(sex=="1"){
				sex = "男";
				$("#tx_jpg").attr("src","../images/tx1.jpg");
			}else if(sex=="0"){
				sex = "女";
				$("#tx_jpg").attr("src","../images/tx0.jpg");
			}
			$("#personal_typeval").text(type);
			$("#personal_nameval").text(jsondata.name);
			$("#personal_sexval").text(sex);
			$("#personal_birthval").text(jsondata.birth);//YYYY-MM-DD
			$("#personal_userIDval").text(jsondata.userID);
			$("#personal_passwordval").text(jsondata.password);
			$("#personal_phoneval").text(phone);
			$("#personal_gradeval").text(deal_a_str(jsondata.gradeID,"班;"));
			$("#personal_classval").text(jsondata.classes);
		}
	}
}
//处理班级字符串
function deal_gradestr(str){
//	var gradestr = null;
//	if(str.indexOf(";")>0){
//		gradestr = str.split(";");
//	}else if(str.indexOf("班")>0){
//		gradestr = str.split("班");
//	}
	$("select[name='grade_option']").empty();
	$("select[name='grade_option']").append("<option disabled selected value=\"0\">请选择班级</option>");
//	for(var i=0;i<gradestr.length;i++){
//		if(gradestr[i]!=""){
//			$("select[name='grade_option']").append("<option value='"+gradestr[i]+"'>"+gradestr[i]+" 班</option>");
//		}
//	}
	for(var i = 1;i<4;i++){
		$("select[name='grade_option']").append("<option value='"+i+"'>"+i+" 班</option>");
	}
}
function deal_a_str(str,addstr){
	var strarray = null;
	var newstr ="";
	if(str!=""){
		if(str.indexOf(";")>0){
			strarray = str.split(";");
		}else if(str.indexOf("班")>0){
			strarray = str.split("班");
		}
		for(var i=0;i<strarray.length;i++){
			if(strarray[i]!=""){
				newstr += strarray[i]+addstr;
			}
		}
	}
	return newstr;
}
//处理课程字符串
function deal_classtr(str){
	var classtr = str.split(";");
	$("select[name='class_option']").empty();
	$("select[name='class_option']").append("<option disabled selected value=\"0\">请选择课程</option>");
	for(var i=0;i<classtr.length;i++){
		if(classtr[i]!=""){
			$("select[name='class_option']").append("<option value='"+classtr[i]+"'>"+classtr[i]+"</option>");
		}
	}
	
}
//获取树的选中信息，并转换为字符串（若为学生，则偶数为课程id、奇数为教师id）
function acquire_tree_checked(treename){
	var treeObj = $.fn.zTree.getZTreeObj(treename);
	var nodes=treeObj.getCheckedNodes(true);
	if(nodes!=null){
		var info="";
		for(var i=0;i<nodes.length;i++){
			//如果为父节点不处理
			 if (!nodes[i].isParent){
				 if(nodes[i].getParentNode()!=null){
					 info += nodes[i].getParentNode().id+"@";
				 }else{
					 
				 }
				 info += nodes[i].id+"@";
          	}
		}
		return info;
	}
	return null;
}
//消息框
var timer_0 = null;
function msg(mag){
	if(timer_0 != null){
		clearTimeout(timer_0);
	}
	jQuery("#msg").find("span").html(mag);
	jQuery("#msg").css("display","block");
	jQuery("#msg").animate({"display":"block","bottom":"7%"},1500);
	timer_0=setTimeout(function(){
		jQuery("#msg").animate({"bottom":"-40%","display":"none"},1500);
	},3000);
}
//阻止事件冒泡
function stopPropagation(e) {  
    e = e || window.event;  
    if(e.stopPropagation) { //W3C阻止冒泡方法  
        e.stopPropagation();  
    } else {  
        e.cancelBubble = true; //IE阻止冒泡方法  
    }  
}
var click_name = null;
$(document).click(function(e) { // 在页面任意位置点击而触发此事件
	click_name = $(e.target).attr("name");       // e.target表示被点击的目标
});
//翻页操作
function pageturn(ptname){
	if(ptname=="firstpage"){
		if(currpage>1){
			currpage=1;
		}
	}
	else if(ptname=="lastpage"){
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
	}
	//刷新查询页面
	if(loginer_type=="2"){
		deal_search_score(click_name);
	}else if(loginer_type=="0"){
		flush_search();
	}
}
//刷新查询页面
function flush_search(){
	//管理员查询
	deal_search();
}
//导出excel文件
function exportExcel(){
	var url = "exportExcel.action";
	var args = null;
	if(loginer_type=="1"){
		args = {"login_type":loginer_type,"loginer_ID":loginer_ID};
	}else if(loginer_type=="2"){
		var className = $("#c2").val();
		var gradeID = $("#g2").val();
		var sortway = $("input[name='sort_way']:checked").attr("id");
		args = {"login_type":loginer_type,"className":className,"gradeID":gradeID,"sortway":sortway};
		
	}else if(loginer_type=="0"){
		var type = $('input:radio:checked').val();
		var search_in = $("#selIn").val();
		var findway = $("#findway").attr("name");
		args = {"login_type":loginer_type,"type":type,"findway":findway,"val":search_in};
	}
	$.post(url,args,function(data){
		if(data!=""){
			window.location.href=data;
		}
	});
	stopPropagation();
}