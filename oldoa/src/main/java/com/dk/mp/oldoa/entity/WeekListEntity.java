package com.dk.mp.oldoa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WeekListEntity implements Serializable {
	private List<WeekEntity> weekL = new ArrayList<WeekEntity>();

	public List<WeekEntity> getWeekL() {
		return weekL;
	}

	public void setWeekL(List<WeekEntity> weekL) {
		this.weekL = weekL;
	}

}
