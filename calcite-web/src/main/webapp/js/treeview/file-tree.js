var jsonData;
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : '/calcite-web/search/getConfigFileTree.do',
		dataType : 'json',
		success : function(json) {
			jsonData = json;
			showTree();
		}
	});

});

function showTree() {
	var o = {
		showcheck : true,
		//url: "http://jscs.cloudapp.net/ControlsSample/GetChildData" 
		theme : "bbit-tree-lines", //bbit-tree-lines ,bbit-tree-no-lines,bbit-tree-arrows
		showcheck : true,
		theme : "bbit-tree-arrows", //bbit-tree-lines ,bbit-tree-no-lines,bbit-tree-arrows
		onnodeclick : function(item) {
			alert(item.value);
			showFileContent(item.value);
			
			
		}
	};
	o.data = jsonData;
	$("#tree").treeview(o);
	$("#showchecked").click(function(e) {
		var s = $("#tree").getTSVs();
		if (s != null)
			alert(s.join(","));
		else
			alert("NULL");
	});
	$("#showcurrent").click(function(e) {
		var s = $("#tree").getTCT();
		if (s != null)
			alert(s.text);
		else
			alert("NULL");
	});
}

function showFileContent(filePath){
	$.ajax({
		type : "POST",
		url : '/calcite-web/search/getConfigFileContent.do',
		data:{
			filePath:filePath
		},
		dataType : 'json',
		success : function(data) {
			
			$("#file_content").html(data.content);
		}
	});
}