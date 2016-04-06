$(function(){
	$('#dataGrid').datagrid({
		title:'菜单信息',
			pagination:false,
        	toolbar:[{
				text:'添加', 
				iconCls:"icon-add",
				handler:addItem
			},{
				text:'删除',  
            	iconCls:'icon-remove',
				handler:deleteItem
			}],
			fit:true,
			url:'./getMenuList1.do',
			singleSelect:true,
			rownumbers:true,
			fitColumns:true,
			columns:[[
			{field:'id',title:'节点ID',width:150,align:'center'},
			{field:'name',title:'菜单名称',sortable:true,width:150,align:'center'},
			{field:'parentid',title:'是否有父节点',width:150,align:'center',
				formatter:function(value,row,index){
					if(value==0){
						return "否";
					}else{
						return "是";
					}
				}},
			
			{field:'url',title:'访问地址',width:150,align:'center',editor:'text'},
			{field:'enable',title:'是否隐藏',width:150,align:'center',editor:'text',
				formatter:function(value,row,index){
					if(value==1){
						return "否";
					}else{
						return "是";
					}
				}
			},
			{field:'parent',title:'父节点名称',width:150,align:'center'},
			{field:'cz',title:'操作',width:150,align:'center',
				formatter:function(value,row,index){
					if(row.parentid!=0){
						return '<a href="javascript:void(0)" onclick="editMenu('+index+')">编辑</a>';
					}
				}},
			]],
	});
});
function addItem(){
	$('#addlg').dialog('open').dialog('setTitle','新增菜单');
}
function deleteItem(){
	var index=$('#dataGrid').datagrid('getSelected');
	if(index==null){
		alert("请选择需要删除的行。。。。");
	}
	$.ajax({
		type:'POST',
		url:'./delMenu.do?id='+index.id,
		dataType:'html',
		success:function(data){
			if(data!=0){
				alert("删除菜单："+index.name+" 成功！");
				window.location.href="./getMenuList.do";
			}
		}
		});
}
function editMenu(index){
	$('#dataGrid').datagrid('selectRow',index);
	var index=$('#dataGrid').datagrid('getSelected');
	$('#dlg').dialog('open').dialog('setTitle','修改菜单');
	$('#fm').form('load',index);
}
function onSave(arg){
	var url=$('#url').val();
	var text=$('#text').val();
	//var parentMenu=$('#parentMenu').combobox("getValue");
	
	var enable=$('#rd input[name="enable"]:checked').val();
	//alert(url+":"+text+":"+parentMenu+":"+enable);
	
}
function onSaveAdd(){
	var url=$('#url1').val();
	var name=$('#text1').val();
	var Parentid=$('#parentMenu').combobox("getValue");
	
	var enable=$('#rd1 input[name="enable1"]:checked').val();
	$.ajax({
		type:'POST',
		url:'./addMenu.do?url='+url+'&name='+name+'&Parentid='+Parentid+'&enable='+enable,
		//contentType: "application/json",
		dataType:'html',
		//data:{'url':url,'name':name,'Parentid':Parentid,'enable':enable},
		success:function(data){
			if(data!=0){
				alert("增加菜单 ："+text+" 成功！");
				window.location.href="./getMenuList.do";
			}else{
				alert("增加菜单 ："+text+" 失败！");
				return false;
			}
		}
	});
}
