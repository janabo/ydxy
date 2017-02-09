package com.dk.mp.newoa.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewDoc implements Parcelable {
	public static final int STATE_HANDLE_IN = 0;
	public static final int STATE_HANDLE_OVER = 1;

	public static final String TYPE_SHOUWEN = "OA_SW";
	public static final String TYPE_FAWEN = "OA_FW";
	public static final String TYPE_HUIYI = "OA_MEETING_SUMMARY";
	public static final String TYPE_BAOGAO = "OA_QSBG";
	public static final String TYPE_HETONG = "OA_LXHTQS";
	public static final String TYPE_YONGYIN = "OA_SIGNET_APPLICATION";
	public static final String TYPE_JIEDAI = "OA_RECEPTION";
	public static final String TYPE_XINFANG = "OA_PETITION";
	public static final String TYPE_XINXI = "OA_SUBMISSION_INFO";
	public static final String TYPE_HUICHANG = "OA_HCGL";

	private String title;// 标题
	private String time;// 时间
	private String subTitle;// 拟稿部门
	private String type;// 文件类型（1：收文:2：发文:3：请示报告）
	private List<NewAttachment> fujian = new ArrayList<NewAttachment>();// 附件列表
	private List<NewOpinion> opinions = new ArrayList<NewOpinion>();// 意见列表
	private List<NewOpinion> boss = new ArrayList<NewOpinion>();// 领导意见
	private Map<String,String> param = new HashMap<String, String>();

	private String dqbz;// 当前步骤
	private String dealState;// 当前状态的处理情况
	private String alreadyDeal;// 处理人
	private String documentState;// 公文状态
	private String id;// 业务id
	private String operateId;// 操作id
	private String activityId;// 流程节点id
	private String flowInstanceId;
	private String flowDetailId;// flowDetailId主键
	private String yclry;// 已处理人员
	private String dclry;// 待处理人员
	private String url;// 业务地址
	private String workItemId;

	private String next;// 下一步
	private String flowend;// 办结
	private String needOptiona;// 是否需要参数
	private String retrieveNumber;
	private String proxy;// 是否代理
	private String proxyActorName;// 代理人姓名

	public NewDoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getProxyActorName() {
		return proxyActorName;
	}

	public void setProxyActorName(String proxyActorName) {
		this.proxyActorName = proxyActorName;
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

	public List<NewOpinion> getBoss() {
		return boss;
	}

	public void setBoss(List<NewOpinion> boss) {
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

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String department) {
		this.subTitle = department;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<NewAttachment> getFujian() {
		return fujian;
	}

	public void setFujian(List<NewAttachment> fujian) {
		this.fujian = fujian;
	}

	public List<NewOpinion> getOpinions() {
		return opinions;
	}

	public void setOpinions(List<NewOpinion> opinions) {
		this.opinions = opinions;
	}

	public String getDqbz() {
		return dqbz;
	}

	public void setDqbz(String dqbz) {
		this.dqbz = dqbz;
	}

	public String getFlowend() {
		return flowend;
	}

	public void setFlowend(String flowend) {
		this.flowend = flowend;
	}

	public String getRetrieveNumber() {
		return retrieveNumber;
	}

	public void setRetrieveNumber(String retrieveNumber) {
		this.retrieveNumber = retrieveNumber;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		// Return true if the objects are identical.
		// (This is just an optimization, not required for correctness.)
		if (this == o) {
			return true;
		}

		// Return false if the other object has the wrong type.
		// This type may be an interface depending on the interface's
		// specification.
		if (!(o instanceof NewDoc)) {
			return false;
		}

		// Cast to the appropriate type.
		// This will succeed because of the instanceof, and lets us access
		// private fields.
		NewDoc lhs = (NewDoc) o;

		// Check each field. Primitive fields, reference fields, and nullable
		// reference
		// fields are all treated differently.
		return id.equals(lhs.getId());
	}

	private NewDoc(Parcel source) {
		this.title = source.readString();
		this.time = source.readString();
		this.subTitle = source.readString();
		this.type = source.readString();
		this.id = source.readString();
		this.operateId = source.readString();
		this.activityId = source.readString();
		this.flowInstanceId = source.readString();
		this.flowDetailId = source.readString();
		this.workItemId = source.readString();
		this.proxy = source.readString();
		this.proxyActorName = source.readString();
	}

	public static final Creator<NewDoc> CREATOR = new Creator<NewDoc>() {

		@Override
		public NewDoc createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new NewDoc(source);
		}

		@Override
		public NewDoc[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NewDoc[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.title);
		dest.writeString(this.time);
		dest.writeString(this.subTitle);
		dest.writeString(this.type);
		dest.writeString(this.id);
		dest.writeString(this.operateId);
		dest.writeString(this.activityId);
		dest.writeString(this.flowInstanceId);
		dest.writeString(this.flowDetailId);
		dest.writeString(this.workItemId);
		dest.writeString(this.proxy);
		dest.writeString(this.proxyActorName);
	}

	public Map<String, String> getParam() {
		return param;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	
	
}
