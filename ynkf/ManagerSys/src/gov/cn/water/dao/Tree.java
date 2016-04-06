package gov.cn.water.dao;

import java.util.List;

public class Tree {
	private int id;						//�ڵ��ID
	private String text;		//�ڵ���ʾ������
	private String state = "open";		//Ĭ��open,��Ϊ��closed��ʱ˵���˽ڵ������ӽڵ㣬����˽ڵ�ΪҶ�ӽڵ�
	private boolean checked = false;	//ָʾ�ڵ��Ƿ�ѡ��
	private Object attributes;			//��һ���ڵ�׷�ӵ��Զ�������
	private List<Tree> children;		//������һЩ�ӽڵ�Ľڵ�����
	private String iconCls;				//����ýڵ����ʽ	
	private int pid;					//����ýڵ�ĸ��ڵ�
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Object getAttributes() {
		return attributes;
	}
	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}

}
