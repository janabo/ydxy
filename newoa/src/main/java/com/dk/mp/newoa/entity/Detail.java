package com.dk.mp.newoa.entity;


import java.util.List;
import java.util.Map;
/**
 * 组装oa详细信息
 * @author dake
 *
 */
public class Detail {
	 private List<Jbxx> jbxxs;//基本信息
	 private Fjxx fjxx;//附件信息
	 private List<NewOpinion> opinionTypes;//审批意见分类列表
	 private Map<String,String> params;//下一步传递的参数
	 private Jbxx content;//内容
	 private Map<String,Boolean> operation;//控制菜单是否显示
	 private String html;//返回页面
	 private OASubmit submit;//提交界面的用于界面显示效果的参数
	 
	public OASubmit getSubmit() {
		return submit;
	}
	public void setSubmit(OASubmit submit) {
		this.submit = submit;
	}
	public List<Jbxx> getJbxxs() {
		return jbxxs;
	}
	public void setJbxxs(List<Jbxx> jbxxs) {
		this.jbxxs = jbxxs;
	}
	public Fjxx getFjxx() {
		return fjxx;
	}
	public void setFjxx(Fjxx fjxx) {
		this.fjxx = fjxx;
	}
	
	public List<NewOpinion> getOpinionTypes() {
		return opinionTypes;
	}
	public void setOpinionTypes(List<NewOpinion> opinionTypes) {
		this.opinionTypes = opinionTypes;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public Jbxx getContent() {
		return content;
	}
	public void setContent(Jbxx content) {
		this.content = content;
	}
	public Map<String, Boolean> getOperation() {
		return operation;
	}
	public void setOperation(Map<String, Boolean> operation) {
		this.operation = operation;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
	
}
