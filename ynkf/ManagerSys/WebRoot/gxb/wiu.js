$(function() {
	var value=$('#search_name').textbox('getValue');
		$('#dataGrid').datagrid(
	
		{
//			onBeforeLoad:function(param){
//				value=$('#search_name').textbox('getValue');
//			},
			title:'取水户信息',
			//border:false,
        	//height:3500,
			pagination:true,
			pageSize:12,
			pageList:[12,20,30,40],
        	toolbar:'#tool',
			fit:true,
			url:'./getUnnomal.do',
			//queryParams:{search_name:$('#search_name').textbox('getValue')},
			singleSelect:true,
			//rownumbers:true,
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
				//alert("^^^^^^^^"+$('#search_name').val());
			}
		});
		$('#dataGrid').datagrid('reload',{
			search_name:$('#search_name').textbox('getValue')
		});
		var p=$('#dataGrid').datagrid("getPager");
		p.pagination({
			pageSize:12,
			pageList:[12,20,30,40],
			beforePageText:'第',
			afterPageText:'页       共{pages}页',
			displayMsg:'当前显示  {from}-{to} 条记录      共    {total} 条记录',
			onSelectPage:function(pageNumber,pageSize){
				changePage(pageNumber,pageSize);
			},
		});
	});
	function changePage(pi,ps){
		$.ajax({
					type:'POST',
					url:'./getUnnomal.do',
					data:{'search_name':$('#search_name').textbox('getValue'),'rows':ps,'page':pi},
					dataType:'json',
					success: function(data){
						$('#dataGrid').datagrid('loadData', data);
					}
				});
	}
	function FindData(){
		//alert("你好");
		var value=$('#search_name').textbox('getValue');
		//alert(value);
//		window.location.href="./getUnnomal.do?search_name="+value;
		$.ajax({
			type:'POST',
			url:'./getUnnomal.do',
			data:'search_name='+value,
			dataType:'json',
			success:function(data){
				$('#dataGrid').datagrid('loadData',data);
//				for(var aa in data){
//					alert(data[aa].wiu_nm);
//					var a=[{
//						'wiu_cd': data[aa].wiu_cd,
//						'wiu_nm': data[aa].wiu_nm,
//						'lr_nm': data[aa].lr_nm,
//						'addr': data[aa].addr,
//						'wiu_tp': data[aa].wiu_tp,
//						'tel': data[aa].tel,
//						'ad_nm': data[aa].ad_nm
//					}];
//					$('#dataGrid').datagrid('loadData',a);
//				}
			}
		});
	}