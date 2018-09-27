//http://2909c6fc.nat123.cc:17933/StuScoreSystem
$(function(){		
	$("body").keydown(function() {
		if (event.keyCode == "13") {//keyCode=13是回车键
			var id=$("input:focus").attr("id");
			if(id.indexOf("stu")!=-1){
				$("#stu").click();
			}else if(id.indexOf("tea")!=-1){
				$("#tea").click();
			}else if(id.indexOf("sec")!=-1){
				$("#sec").click();
			}
		}
	});
});
//用户登录
function login(typ){
//		var typ=$(this).attr("id");
		var userID=$("input[name='"+typ+"username']").val();
		var password=$("input[name='"+typ+"password']").val();
		if(userID==""||userID==null){
			alert("用户名不能为空！");
			$("input[name='"+typ+"username']").focus();
			return;
		}
		if(password==""||password==null){
			alert("密码不能为空！");
			return;
		}
		if(typ=="stu"){type="1";topage="scoreManagement/html/student.html";}
		else if(typ=="tea"){type="2";topage="scoreManagement/html/teacher.html";}
		else if(typ=="sec"){type="0";topage="scoreManagement/html/admin.html";}
		var url="login.action";
		var args={"userID":userID,"password":password,"type":type,"time":new Date()};
		$.post(url,args,function(data){
			var state=data.state;
			if(state=="1"){
				$.cookie('data',JSON.stringify({"userID":userID,"password":password,"type":type,"time":getFormatDate()}));
				location.href=topage;
			}else{
				alert(data.message);
			}
		});
}
 
//格式化时间
function getFormatDate(){  
    var nowDate = new Date();   
    var year = nowDate.getFullYear();  
    var month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;  
    var date = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate.getDate();  
    var hour = nowDate.getHours()< 10 ? "0" + nowDate.getHours() : nowDate.getHours();  
    var minute = nowDate.getMinutes()< 10 ? "0" + nowDate.getMinutes() : nowDate.getMinutes();  
    var second = nowDate.getSeconds()< 10 ? "0" + nowDate.getSeconds() : nowDate.getSeconds();  
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;  
}
//输入验证
function check_login(){
	//学生登录
	$("#stu_username_hide").focus(function(){
	 var username = $(this).val();
	 if(username=='输入学号'){
	 $(this).val('');
	 }
	});
	$("#stu_username_hide").focusout(function(){
	 var username = $(this).val();
	 if(username==''){
	 $(this).val('输入学号');
	 }
	});
	$("#stu_password_hide").focus(function(){
	 var username = $(this).val();
	 if(username=='输入密码'){
	 $(this).val('');
	 }
	});
	$("#stu_password_hide").focusout(function(){
	 var username = $(this).val();
	 if(username==''){
	 $(this).val('输入密码');
	 }
	});
}

