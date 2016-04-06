$(function() {
		$('#mainMenu').tree({
			
			url : path+'/getMenu.do',
			parentField : 'pid',
				onClick:function(node){
			//alert(node.attributes.url);
			if (node.attributes.url != '' && node.attributes.url != null)
            {
				//alert($('.easyui-tabs').tabs('exists', node.text));
                if ($('.easyui-tabs').tabs('exists', node.text))
                {
//					var tab = self.parent.$('.easyui-tabs').tabs('getSelected');
//					alert(tab.panel('options').content);
//   					 $(".easyui-tabs").tabs('update',{
//       				 tab: tab,
//        			options:{
//
//           				 content: '<iframe scrolling="auto" frameborder="0"  src="http://127.0.0.1:8080/ManagerSys/'+node.attributes.url+'" style="width:100%;height:100%;"></iframe>'
//        			}
//   				 });
                    $('.easyui-tabs').tabs('select', node.text);
                }
                else
                {
					var content = '<iframe scrolling="auto" frameborder="0"  src="http://127.0.0.1:8080/ManagerSys/'+node.attributes.url+'" style="width:100%;height:100%;"></iframe>';
                    $('.easyui-tabs').tabs('add',{   
                        title:node.text,
                        content:content,   
                        closable:true  
                    });
                }
            }
		}
		});
		$('#dataGrid').datagrid({
			title:'取水户信息',
			border:false,
        	//height:350,
			pagination:true,
        	toolbar:'#tool',
			fit:true,
			url:'./getUnnomal.do',
			singleSelect:true,
			rownumbers:true,
			fitColumns:true,
			columns:[[
			{field:'wiu_cd',title:'组织机构',sortable:true,width:150,align:'center'},
			{field:'wiu_nm',title:'户名',width:150,align:'center'},
			{field:'lr_nm',title:'法人',width:150,align:'center'},
			{field:'addr',title:'地址',width:150,align:'center',editor:'text'},
			{field:'wiu_tp',title:'取水类型',width:150,align:'center',
				formatter: function(value,row,index){
                		  if (value == "1") {
						  	value = '只取不用';
						  	return value;
						  }else if(value =='2')
						  	return "只用不取";
						  else 
						  	return value;	  
                      }},
			{field:'tel',title:'电话',width:150,align:'center'},
			{field:'ad_nm',title:'行政区',width:150,align:'center'}
			]],
			onLoadSuccess:function(data){
				
			}
		});
	});