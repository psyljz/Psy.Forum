$(function(){
	$("#publishBtn").click(publish);
});

function publish() {


	$("#publishModal").modal("hide");

	//获取标题和内容
	var title =$("#recipient-name").val();
	var content=$("#message-text").val();
	//发送异步请求
	$.post(
		"/discuss/add",
		{"title":title,"content":content},
		function (data){
			data=$.parseJSON(data);
			//在提示中显示返回消息
			$("#hintBody").text(data.msg);
			//显示提示狂
			$("#hintModal").Modal("show");
			//2秒后自动义仓提示狂
			setTimeout(function (){
				$("#hintModal").modal("hide");
				//刷新页码
					window.location.reload();

			},2000)

		}
	)

	$("#hintModal").modal("show");
	setTimeout(function(){
		$("#hintModal").modal("hide");
	}, 2000);
}