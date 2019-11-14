package at.dalex.grape.entity;

import java.util.ArrayList;
import java.util.UUID;

public class EntityManager {

	private static ArrayList<UUID> entityIds = new ArrayList<UUID>();
	
	public static UUID genEntityId() {
		UUID id = null;
		//Search for free Id
		boolean done = false;
		while (!done) {
			id = UUID.randomUUID();
			if (!entityIds.contains(id)) {
				done = true;
			}
		}
		//Register Id
		entityIds.add(id);
		return id;
	}
}
