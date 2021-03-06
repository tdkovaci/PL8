import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import pl8.api.JsonServlet;

@SuppressWarnings("serial")
public class QueryRecipe extends JsonServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// Get POST body as JSON
		StringBuilder sb = new StringBuilder();
	    BufferedReader reader = req.getReader();
	    try {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line).append('\n');
	        }
	    } finally {
	        reader.close();
	    }

	    // Convert JSON to QueryOptions object
		QueryOptions options = new Gson().fromJson(sb.toString(), QueryOptions.class);
		
		// Format and run query
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("Recipe");
		
		// Create Filters for options 
		
		// Set sort order
		String[] sortOptions = options.getSortOptions();

		if(sortOptions != null && sortOptions.length == 2) {
			if(sortOptions[1].equalsIgnoreCase("ascending")) {
				q.addSort(sortOptions[0], SortDirection.ASCENDING);
			} else if(sortOptions[1].equalsIgnoreCase("descending")) {
				q.addSort(sortOptions[0], SortDirection.DESCENDING);
			}
		}	
		
		PreparedQuery pq = datastore.prepare(q);
		
		List<Entity> recipes = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
		
		String json = new Gson().toJson(recipes);
		
		resp.setContentType("application/json");
		resp.getWriter().write(json);
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws IOException {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("Recipe");
		
		String id = request.getParameter("id");


		if (id != null) {
			long idLong = Long.valueOf(id);
			
			Key k = KeyFactory.createKey("Recipe", idLong);
			
			Filter uf = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, k);
			
			q.setFilter(uf);
			
			PreparedQuery pq = datastore.prepare(q);
			
			Entity u = pq.asSingleEntity();
			
			String json = new Gson().toJson(u);
			
			resp.setContentType("application/json");
			resp.getWriter().write(json);
			return;
		}
		
		List<Entity> recipes = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
		
		String query = request.getParameter("query");
		

		if(query == null || query.equals(""))
		{
			String json = new Gson().toJson(recipes);
			
			resp.setContentType("application/json");
			resp.getWriter().write(json);
			
			return;
		}
		
		
		/* Remove commas */
		query = query.replaceAll(",", "");
		
		/* Get String array of query terms, split by white space */
		String[] terms = query.split(" ");
		
		/* Remove stop words from array */
		ArrayList<String> queryTerms = new ArrayList<String>();
		for(String term : terms)
		{
			if(!queryTerms.contains(term) && (!term.equalsIgnoreCase("and") && !term.equalsIgnoreCase("the") 
					&& !term.equalsIgnoreCase("in")))
				queryTerms.add(term);
		}

		List<Result> results = new ArrayList<Result>();
		
		for(Entity e : recipes)
		{
			/* Create a new Result for this Entity */
			Result r = new Result(e);
			
			if(results.contains(r))
			{
				/* Get the existing version of this Entity */
				r = results.get(results.indexOf(r));
				results.remove(r);
			}
			for(String str : queryTerms)
			{
				/* Get properties for this entity */
				
				/* Get Map of Properties */
				Map<String, Object> name = e.getProperties();
				
				for(Map.Entry<String, Object> entry : name.entrySet())
				{
					/* Cast Value as a String */
					String value = "";

					value = entry.getValue().toString();
					
					/* If the property contains the query, increment the weight */
					if(value != null && value.contains(str))
						r.incrementWeight();
				}
			}	
			
			/* Add Result to the results list */
			results.add(r);
		
		}
		
		/* Sort results by weight */
		Collections.sort(results, new Comparator<Result>() {
			@Override
		    public int compare(Result r1, Result r2)
		    {
				/* Return -1, 0, -1 depending on weight */
		    	return r1.getWeight() - r2.getWeight();
		    }
		});
		
		/* Build Entity list to return */
		recipes = new ArrayList<Entity>();
		
		/* Iterate over Result list */
		for(Result r : results)
		{
			if(r.getWeight() > 0)
				recipes.add(r.getEntity());
		}
		
		/* Convert List to JSON and return */
		String json = new Gson().toJson(recipes);
		
		resp.setContentType("application/json");
		resp.getWriter().write(json);
	}	
}