$(function() {
	var show_count = 20; // 要显示的条数
	var count = 1; // 递增的开始值，这里是你的ID
	$("#btn_addtr").click(function() {
		var length = $("#dynamicTable tbody tr").length;
		if (length < show_count) // 点击时候，如果当前的数字小于递增结束的条件
		{
			$("#tab11 tbody tr").clone().appendTo("#dynamicTable tbody"); // 在表格后面添加一行
			changeIndex();// 更新行号
		}
	});

});

function test() {
	var columns = new Array();
	for (var i = 0; i < 10; i++) {
		var columnObj = {
			name : "col" + i,
			type : "typ" + i
		};
		columns[i] = columnObj;
	}

	var family = {
		name : "family_1",
		column : columns
	};
	var table = {
		name : "table_1"
	};
	var obj = {
		family : family,
		table : table
	};
	var jsonStr = JSON.stringify(obj);
	$("#json_show").html(format(jsonStr));

}

function changeIndex() {
	var i = 1;
	$("#dynamicTable tbody tr").each(function() { // 循环tab tbody下的tr
		$(this).find("input[name='NO']").val(i++);// 更新行号
	});
}
function deltr(opp) {
	var length = $("#dynamicTable tbody tr").length;
	// alert(length);
	if (length <= 1) {
		alert("至少保留一行");
	} else {
		$(opp).parent().parent().remove();// 移除当前行
		changeIndex();
	}

	showJsonData();
}

function showJsonData() {
	
	$("#config_div").find("input[type='text']").each(function(i) {
		
		$(this).click(function() {
			$(this).attr("class", "form-control");
		});

	});
	$("#config_div").find("input[type='text']").each(function(i) {
		if ($(this).val() == null || $(this).val() == "") {
			$(this).attr("class", "form-control-invalid");
		}

	});
	var columnObjs = new Array();
	var familyNames = new Array();
	var index = 0;
	$("#dynamicTable tbody tr").each(function() { // 循环tab tbody下的tr
		var column_name = $(this).find("input[name='column_name']").val();
		var column_type = $(this).find("select[name='column_type']").val();
		var family_name = $(this).find("input[name='family_name']").val();
		var columnObj = {
			name : column_name,
			type : column_type,
			familyName : family_name
		};
		columnObjs[index] = columnObj;
		familyNames[index] = family_name;
		index++;
		// alert(column_name + " " + column_type + " " + family_name);
	});
	// delete repeated familyName
	familyNames = familyNames.delRepeat();

	var families = new Array();

	$.each(familyNames, function(i, familyName) {
		// alert(i+" "+familyName);
		var columns = new Array();
		$.each(columnObjs, function(j, columnObj) {
			if ($.trim(columnObj.familyName) == $.trim(familyName)) {
				// alert("#"+columnObj.familyName +" "+familyName);
				var column = {
					name : columnObj.name,
					type : columnObj.type
				};

				columns[j] = column;
			} else {
				columns[j] = null;
			}
		});

		// delete null column
		for (var k = 0; k < columns.length; k++) {
			if (columns[k] == null)
				columns.splice(k, 1);
		}
		var familyObj = {
			name : familyName,
			columns : columns
		};

		families[i] = familyObj;

	});

	var jsonObj = {
		connInfo : {
			ip : $("#server_ip").val(),
			port : $("#server_port").val(),
			tableName : $("#table_name").val()
		},
		families : families
	};
	var jsonStr = JSON.stringify(jsonObj);
	$("#json_show").html(format(jsonStr));

}

/**
 * delete repeat elements
 */
Array.prototype.delRepeat = function() {
	// hasOwnProperty用来判断一个对象是否有你给出名称的属性或对象
	var temp = {}, len = this.length;
	for (var i = 0; i < len; i++) {
		var tmp = this[i];
		if (!temp.hasOwnProperty(tmp)) {
			temp[this[i]] = "yes";
		}
	}

	len = 0;
	var tempArr = [];
	for ( var i in temp) {
		tempArr[len++] = i;
	}
	return tempArr;
}

function format(txt, compress/* 是否为压缩模式 */) {/* 格式化JSON源码(对象转换为JSON文本) */
	var indentChar = '    ';
	if (/^\s*$/.test(txt)) {
		alert('数据为空,无法格式化! ');
		return;
	}
	try {
		var data = eval('(' + txt + ')');
	} catch (e) {
		alert('数据源语法错误,格式化失败! 错误信息: ' + e.description, 'err');
		return;
	}
	;
	var draw = [], last = false, This = this, line = compress ? '' : '\n', nodeCount = 0, maxDepth = 0;

	var notify = function(name, value, isLast, indent/* 缩进 */, formObj) {
		nodeCount++;/* 节点计数 */
		for (var i = 0, tab = ''; i < indent; i++)
			tab += indentChar;/* 缩进HTML */
		tab = compress ? '' : tab;/* 压缩模式忽略缩进 */
		maxDepth = ++indent;/* 缩进递增并记录 */
		if (value && value.constructor == Array) {/* 处理数组 */
			draw.push(tab + (formObj ? ('"' + name + '":') : '') + '[' + line);/*
			 * 缩进'['
			 * 然后换行
			 */
			for (var i = 0; i < value.length; i++)
				notify(i, value[i], i == value.length - 1, indent, false);
			draw.push(tab + ']' + (isLast ? line : (',' + line)));/* 缩进']'换行,若非尾元素则添加逗号 */
		} else if (value && typeof value == 'object') {/* 处理对象 */
			draw.push(tab + (formObj ? ('"' + name + '":') : '') + '{' + line);/*
			 * 缩进'{'
			 * 然后换行
			 */
			var len = 0, i = 0;
			for ( var key in value)
				len++;
			for ( var key in value)
				notify(key, value[key], ++i == len, indent, true);
			draw.push(tab + '}' + (isLast ? line : (',' + line)));/* 缩进'}'换行,若非尾元素则添加逗号 */
		} else {
			if (typeof value == 'string')
				value = '"' + value + '"';
			draw.push(tab + (formObj ? ('"' + name + '":') : '') + value
					+ (isLast ? '' : ',') + line);
		}
		;
	};
	var isLast = true, indent = 0;
	notify('', data, isLast, indent, false);
	return draw.join('');
}