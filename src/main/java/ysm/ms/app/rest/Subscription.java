package ysm.ms.app.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subscription {

	private long id;
	private List<MessageType> types = new ArrayList<MessageType>();
	private String description;

	private Map<String, Integer> countOfPostPerType = new HashMap<String, Integer>();
	
	public Map<String, Integer> getCountOfPostPerType() {
		return countOfPostPerType;
	}

	public void setCountOfPostPerType(Map<String, Integer> countOfPostPerType) {
		this.countOfPostPerType = countOfPostPerType;
	}

	public Subscription() {
	}

	public Subscription(long id, List<MessageType> types, String description) {
		this.id = id;
		this.types = types;
		this.description = description;
		if(types != null)
		{
			for (MessageType messageType : types) {
				this.countOfPostPerType.put(messageType.name(), 0);
			}
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<MessageType> getTypes() {
		return types;
	}

	public void setTypes(List<MessageType> types) {
		this.types = types;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean containsMessageType(MessageType type)
	{
		for (MessageType messageType : types) {
			if(type == messageType)
			{
				return true;
			}
		}
		return false;
	}
	
}