package ysm.ms.app;

import java.util.HashMap;
import java.util.Map;

import ysm.ms.app.rest.Subscription;

public class ReadSubscription {
	
	private Subscription subscrition;
	private Map<String, Integer> countByType = new HashMap<String, Integer>();
	public Subscription getSubscrition() {
		return subscrition;
	}
	public void setSubscrition(Subscription subscrition) {
		this.subscrition = subscrition;
	}
	public Map<String, Integer> getCountByType() {
		return countByType;
	}
	public void setCountByType(Map<String, Integer> countByType) {
		this.countByType = countByType;
	}

}
