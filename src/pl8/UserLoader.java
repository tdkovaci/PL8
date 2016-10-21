package pl8;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class UserLoader {
	public static Entity getUserByEmail(String email) {
		/* Init a datastore session to perform the check */
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		/* Create Filter for Email */
		Filter uf = new FilterPredicate("Email", FilterOperator.EQUAL, email);

		/* Apply Filter to a Query on the Datastore */
		Query query = null;
		Entity entity = null;

		/* Form Query for execution */
		query = new Query("User").setFilter(uf);

		/* Run Query on Datastore */
		PreparedQuery pq = datastore.prepare(query);

		/* There should only be one result since emails are unique */
		entity = pq.asSingleEntity();

		return entity;
	}

	public static Entity getUserByUsername(String username) {
		/* Init a datastore session to perform the check */
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		/* Create Filter for Username */
		Filter filter = new FilterPredicate("Username", FilterOperator.EQUAL, username);

		/* Apply Filter to a Query on the Datastore */
		Query query = null;
		Entity entity = null;

		/* Form Query for execution */
		query = new Query("User").setFilter(uf);

		/* Run Query on Datastore */
		PreparedQuery pq = datastore.prepare(query);

		/* There should only be one result since usernames are unique */
		entity = pq.asSingleEntity();

		return entity;
	}

	public static void deleteAll() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("User");

		Iterable<Entity> it = datastore.prepare(query).asIterable();

		List<Key> keys = new ArrayList<Key>();
		for (Entity entity : it) {
			keys.add(entity.getKey());
		}

		datastore.delete(keys);
	}

	public static Entity saveUser(String username, String password, String email)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		/* Encrypt password using PBKDF2 */
		String pw = Password.getHash(password);

		/* Set Entity properties */
		Entity entity = new Entity("User");
		entity.setProperty("Username", username);
		entity.setProperty("Password", pw);
		entity.setProperty("Email", email);

		/* Add new User to the datastore */
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(entity);

		return entity;
	}
}
