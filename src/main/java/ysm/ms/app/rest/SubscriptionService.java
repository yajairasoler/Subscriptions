package ysm.ms.app.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.web.client.RestTemplate;

import ysm.ms.app.ReadSubscription;

@Named
@Path("/")
public class SubscriptionService {

	@Inject
	private RestTemplate restTemplate;

	Subscription subscription1 = new Subscription(1L,
			Arrays.asList(MessageType.Type1), "Type 1 Subscription1");
	Subscription subscription2 = new Subscription(2L,
			Arrays.asList(MessageType.Type2), "Type 2 Subscription2");
	Subscription subscription3 = new Subscription(3L,
			Arrays.asList(MessageType.Type3), "Type 3 Subscription3");
	Subscription subscription4 = new Subscription(4L, Arrays.asList(
			MessageType.Type1, MessageType.Type2), "Type 1 Subscription4");
	Subscription subscription5 = new Subscription(5L, Arrays.asList(
			MessageType.Type1, MessageType.Type2, MessageType.Type3),
			"Type 2 Subscription5");

	List<Subscription> subscriptions = Arrays.asList(subscription1,
			subscription2, subscription3, subscription4, subscription5);
	long sequence = 5;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subscription> retrieveSubscriptionList() {

		return this.subscriptions;

	}

	// http://localhost:8080/subscriptionById?id=1
	@GET
	@Path("subscriptionById")
	@Produces(MediaType.APPLICATION_JSON)
	public ReadSubscription readSubscriptionById(@QueryParam("id") long id) {

		Subscription prod = null;
		for (Subscription p : subscriptions) {
			if (p.getId() == id)
				prod = p;
		}

		ReadSubscription response = new ReadSubscription();
		response.setSubscrition(prod);
		response.setCountByType(prod.getCountOfPostPerType());
		
		return response;

	}

	// http://localhost:8080/updateSubscription?id=1&messageTypeList=Type2&description=hola
	@GET
	@Path("updateSubscription")
	@Produces(MediaType.APPLICATION_JSON)
	public Subscription updateSubscription(@QueryParam("id") long id,
			@QueryParam("messageTypeList") String messageTypeList,
			@QueryParam("description") String description) {

		Subscription prod = null;
		if (id > 0) {
			for (Subscription p : subscriptions) {
				if (p.getId() == id)
					prod = p;
				break;
			}
		}
		if (prod != null) {

			List<MessageType> types = new ArrayList<MessageType>();
			if (messageTypeList != null && messageTypeList.contains("-")) {
				for (String string : messageTypeList.split("-")) {
					MessageType tp = MessageType.valueOf(string);
					if (tp != null) {
						types.add(tp);
					}
				}
			} else
				types.add(MessageType.valueOf(messageTypeList));

			prod.setTypes(types);
			prod.setDescription(description);

			if (prod.getTypes() != null) {
				for (MessageType messageType : prod.getTypes()) {
					if (prod.getCountOfPostPerType().get(messageType) == null) {
						prod.getCountOfPostPerType().put(messageType.name(), 0);
					}
				}
			}
		} else {
			// Here throw and error or sending a response
		}

		return prod;

	}

	// http://localhost:8080/createSubscription?messageTypeList=Type2-Type1-Type3&description=holaNew
	@GET
	@Path("createSubscription")
	@Produces(MediaType.APPLICATION_JSON)
	public Subscription createSubscription(
			@QueryParam("messageTypeList") String messageTypeList,
			@QueryParam("description") String description) {

		List<MessageType> types = new ArrayList<MessageType>();
		if (messageTypeList != null && messageTypeList.contains("-")) {
			for (String string : messageTypeList.split("-")) {
				MessageType tp = MessageType.valueOf(string);
				if (tp != null) {
					types.add(tp);
				}
			}

		} else {
			types.add(MessageType.valueOf(messageTypeList));
		}

		sequence++;
		Subscription prod = new Subscription(sequence, types, description);
		List<Subscription> newList = new ArrayList<Subscription>();
		newList.addAll(subscriptions);
		newList.add(prod);
		this.subscriptions = newList;
		return prod;

	}

	// http://localhost:8080/postMessage?messageType=Type1&message=message
	@GET
	@Path("postMessage")
	@Produces(MediaType.APPLICATION_JSON)
	public Message postMessage(@QueryParam("messageType") String type,
			@QueryParam("message") String message) {
		MessageType mtype = null;
		if (type != null) {
			mtype = MessageType.valueOf(type);
		}

		for (Subscription subscription : subscriptions) {
			if (subscription.containsMessageType(mtype)) {
				int counterpost = subscription.getCountOfPostPerType().get(
						mtype.name());
				// adding 1 to the counter already exist
				subscription.getCountOfPostPerType().put(mtype.name(),
						counterpost++);

				Map<String, String> map = new HashMap<String, String>();
				map.put("message", message);
				map.put("type", type);
				Message mes = restTemplate.getForObject(
						"http://localhost:8081/addMessage?message={message}&type={type}", Message.class, map);

				return mes;
			}
		}

		return null;
	}

}
