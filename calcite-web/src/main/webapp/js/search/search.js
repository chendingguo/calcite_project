function submitSelect() {

	try {
		$('#result_show').columns('destroy');
	} catch (e) {
		//alert(e.message);
		//first time load
	}

	var sql = $("#sql_input").val();
	var model = $("#model").val();
	$.ajax({
		type : "POST",
		url : '/calcite-web/search/select.do',
		dataType : 'json',
		data : {
			sql : sql,
			model : model
		},
		success : function(json) {
			result_show = $('#result_show').columns({
				data : json.data
			});
		}
	});
}

