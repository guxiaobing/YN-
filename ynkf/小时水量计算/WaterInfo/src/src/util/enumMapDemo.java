package src.util;

import java.util.EnumMap;
import java.util.Map;

enum Ident{
	WT,PH,COND,TURB,DOX,CODMN,CODCR,TN,NH3N,NO2,NO3,TP,TOC,VLPH,CHLA,F,ARS,HG,CR6,CU,PB,CD,ZN,SB ;
}
public class enumMapDemo {
	public String getValue(String str){
		Map<Ident,String> desc = null ;		// 定义Map对象，同时指定类型
		desc = new EnumMap<Ident,String>(Ident.class) ;	// 实例化EnumMap对象
		desc.put(Ident.WT,"Z63") ;
		desc.put(Ident.PH,"Z60") ;
		desc.put(Ident.COND,"Z62") ;
		desc.put(Ident.TURB,"Z71") ;
		desc.put(Ident.DOX,"Z64") ;
		desc.put(Ident.CODMN,"Z14") ;
		desc.put(Ident.CODCR,"Z02") ;
		desc.put(Ident.TN,"Z18") ;
		desc.put(Ident.NH3N,"Z05") ;
		desc.put(Ident.NO2,"Z19") ;
		desc.put(Ident.NO3,"Z20") ;
		desc.put(Ident.TP,"Z17") ;
		desc.put(Ident.TOC,"Z23") ;
		desc.put(Ident.VLPH,"Z04") ;
		desc.put(Ident.CHLA,"Z73") ;
		desc.put(Ident.F,"Z03") ;
		desc.put(Ident.ARS,"YN3") ;
		desc.put(Ident.HG,"Z06") ;
		desc.put(Ident.CR6,"Z12") ;
		desc.put(Ident.CU,"YN4") ;
		desc.put(Ident.PB,"Z07") ;
		desc.put(Ident.CD,"Z08") ;
		desc.put(Ident.ZN,"Z24") ;
		desc.put(Ident.SB,"Z51") ;
		String value="";
		for(Ident c:Ident.values()){
			if(str.equals(desc.get(c))){
				value=c.name();
				break;
			}
		}
		return value;
	}
}
