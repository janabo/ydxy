package com.dk.mp.oldoa.entity;

import java.util.List;
import java.util.Map;

import android.widget.TextView;

public class Doc {

	private String title;// 标题
	private String time;// 时间
	private String department;// 拟稿部门
	private String ngr;//拟稿人
	private String type;// 文件类型（1：收文:2：发文:3：请示报告）
	private List<Attachment> fujian;// 附件列表
	private List<Opinion> opinions;// 意见列表
	private List<Opinion> boss;// 领导意见

	private String dqbz;// 当前步骤
	private String dealState;// 当前状态的处理情况
	private String alreadyDeal;// 处理人
	private String documentState;// 公文状态
	private String id;//业务id
	private String operateId;//操作id
	private String activityId;//流程节点id
	private String flowDetailId;//flowDetailId主键
	private String yclry;//已处理人员
	private String dclry;//待处理人员
	private String url;//业务地址
	private String next;//下一步
	private String flowend;//下一步
	private String needOptiona;//是否需要参数
	private String retrieveNumber;

	public String getRetrieveNumber() {
		return retrieveNumber;
	}

	public void setRetrieveNumber(String retrieveNumber) {
		this.retrieveNumber = retrieveNumber;
	}

	Map<String, String> map;
	
	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getNeedOptiona() {
		return needOptiona;
	}

	public void setNeedOptiona(String needOptiona) {
		this.needOptiona = needOptiona;
	}

	public String getYclry() {
		return yclry;
	}

	public void setYclry(String yclry) {
		this.yclry = yclry;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDclry() {
		return dclry;
	}

	public void setDclry(String dclry) {
		this.dclry = dclry;
	}

	public List<Opinion> getBoss() {
		return boss;
	}

	public void setBoss(List<Opinion> boss) {
		this.boss = boss;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getFlowDetailId() {
		return flowDetailId;
	}

	public void setFlowDetailId(String flowDetailId) {
		this.flowDetailId = flowDetailId;
	}

	public String getOperateId() {
		return operateId;
	}

	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocumentState() {
		return documentState;
	}

	public void setDocumentState(String documentState) {
		this.documentState = documentState;
	}

	public String getDealState() {
		return dealState;
	}

	public void setDealState(String dealState) {
		this.dealState = dealState;
	}

	public String getAlreadyDeal() {
		return alreadyDeal;
	}

	public void setAlreadyDeal(String alreadyDeal) {
		this.alreadyDeal = alreadyDeal;
	}

	private String imageUri;// 图标

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getType() {
		return type;
	}
	
	

	public String getFlowend() {
		return flowend;
	}

	public void setFlowend(String flowend) {
		this.flowend = flowend;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Attachment> getFujian() {
		return fujian;
	}

	public void setFujian(List<Attachment> fujian) {
		this.fujian = fujian;
	}

	public List<Opinion> getOpinions() {
		return opinions;
	}

	public void setOpinions(List<Opinion> opinions) {
		this.opinions = opinions;
	}

	public String getDqbz() {
		return dqbz;
	}

	public void setDqbz(String dqbz) {
		this.dqbz = dqbz;
	}

	public String getNgr() {
		return ngr;
	}

	public void setNgr(String ngr) {
		this.ngr = ngr;
	}

}
