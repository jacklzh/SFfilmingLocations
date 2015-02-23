
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name="listmovie", urlPatterns={"/listmovie"})
public class ListMovieServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try{
			resp.setContentType("application/json");
			String payload = "{\"id\":\"yitu-d5am\",\"name\":\"Film Locations in San Francisco\",\"attribution\":\"San Francisco Film Commission\",\"attributionLink\":\"http://www.filmsf.org/\",\"category\":\"Culture and Recreation\",\"description\":\"If you love movies, and you love San Francisco, you're bound to love this -- a listing of filming locations of movies shot in San Francisco from 1924 - 2010. You'll find the titles, locations, fun facts, names of the director, writer, actors, and studio for most of these films.\",\"displayType\":\"table\",\"publicationAppendEnabled\":false,\"columns\":[{\"id\":172767683,\"name\":\"Title\",\"fieldName\":\"title\",\"position\":1,\"width\":160,\"dataTypeName\":\"text\",\"tableColumnId\":1671601,\"format\":{\"align\":\"left\"},\"flags\":null,\"metadata\":{}},{\"id\":172767684,\"name\":\"Release Year\",\"fieldName\":\"release_year\",\"position\":2,\"width\":244,\"dataTypeName\":\"number\",\"tableColumnId\":1671602,\"format\":{\"precisionStyle\":\"standard\",\"align\":\"right\",\"noCommas\":\"true\"},\"flags\":null,\"metadata\":{}},{\"id\":172767685,\"name\":\"Locations\",\"fieldName\":\"locations\",\"position\":3,\"width\":208,\"dataTypeName\":\"text\",\"tableColumnId\":1671603,\"format\":{},\"flags\":null,\"metadata\":{}},{\"id\":172767686,\"name\":\"Fun Facts\",\"fieldName\":\"fun_facts\",\"position\":4,\"width\":208,\"dataTypeName\":\"text\",\"tableColumnId\":1671604,\"format\":{},\"flags\":null,\"metadata\":{}},{\"id\":172767687,\"name\":\"Production Company\",\"fieldName\":\"production_company\",\"position\":5,\"width\":316,\"dataTypeName\":\"text\",\"tableColumnId\":1671605,\"format\":{},\"flags\":null,\"metadata\":{}},{\"id\":172767688,\"name\":\"Distributor\",\"fieldName\":\"distributor\",\"position\":6,\"width\":232,\"dataTypeName\":\"text\",\"tableColumnId\":1671606,\"format\":{},\"flags\":null,\"metadata\":{}},{\"id\":172767689,\"name\":\"Director\",\"fieldName\":\"director\",\"position\":7,\"width\":196,\"dataTypeName\":\"text\",\"tableColumnId\":1671607,\"format\":{},\"flags\":null,\"metadata\":{}},{\"id\":172767690,\"name\":\"Writer\",\"fieldName\":\"writer\",\"position\":8,\"width\":172,\"dataTypeName\":\"text\",\"tableColumnId\":1671608,\"format\":{},\"flags\":null,\"metadata\":{}},{\"id\":172767691,\"name\":\"Actor 1\",\"fieldName\":\"actor_1\",\"position\":9,\"width\":184,\"dataTypeName\":\"text\",\"tableColumnId\":1671609,\"format\":{},\"flags\":null,\"metadata\":{}},{\"id\":172767692,\"name\":\"Actor 2\",\"fieldName\":\"actor_2\",\"position\":10,\"width\":184,\"dataTypeName\":\"text\",\"tableColumnId\":1671610,\"format\":{},\"flags\":null,\"metadata\":{}},{\"id\":172767693,\"name\":\"Actor 3\",\"fieldName\":\"actor_3\",\"position\":11,\"width\":184,\"dataTypeName\":\"text\",\"tableColumnId\":1671611,\"format\":{},\"flags\":null,\"metadata\":{}}],\"metadata\":{\"custom_fields\":{\"Department Metrics\":{\"Publishing Department\":\"Film Commission\",\"Frequency\":\"Annual\"}},\"renderTypeConfig\":{\"visible\":{\"table\":true}},\"availableDisplayTypes\":[\"table\",\"fatrow\",\"page\"],\"jsonQuery\":{\"order\":[{\"ascending\":true,\"columnFieldName\":\"title\"}]},\"rdfSubject\":\"0\"},\"query\":{\"orderBys\":[{\"ascending\":true,\"expression\":{\"columnId\":172767683,\"type\":\"column\"}}]},\"tags\":[\"film\",\"locations\",\"movies\",\"actors\",\"directors\"],\"flags\":[\"default\"],\"originalViewId\":\"yitu-d5am\",\"displayFormat\":{}}";
	
			URL url = new URL("https://data.sfgov.org/views/INLINE/rows.json?accessType=WEBSITE&method=getByIds&asHashes=true&start=0&length=50&meta=true");
	
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        writer.write(payload);
	        writer.close();
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line= null;
	        StringBuffer jsonString = new StringBuffer();
	        while ((line = br.readLine()) != null) {
	                jsonString.append(line);
	        }
	        br.close();
	        connection.disconnect();
			
	        JSONObject json = new JSONObject(jsonString.toString());
	        JSONObject result = new JSONObject();
	        JSONArray jarray = new JSONArray();
	        result.put("data",jarray);
	        JSONArray movielist = json.getJSONArray("data");
	        for (int i=0;i<movielist.length();i++) {
	        	JSONObject oneMovie = getMovieData(json.getJSONArray("data").getJSONObject(i));
	        	jarray.put(oneMovie);
	        }
			
			PrintWriter pwout = new PrintWriter(resp.getWriter());
			pwout.println(result.toString());
		}catch(IOException e){
			System.out.println(e);
		}
	}
	
	private JSONObject getMovieData(JSONObject json) {
		JSONObject jmovie = new JSONObject();
		jmovie.put("title", getField(json,"172767683"));
		jmovie.put("location", getField(json,"172767685"));
		jmovie.put("company", getField(json,"172767687"));
		jmovie.put("director", getField(json,"172767689"));
		jmovie.put("actors", getField(json,"172767690"));
		return jmovie;
	}
	
	private String getField(JSONObject json, String key) {
		try {
			return json.getString(key);
		}catch(Exception e){
		}
		return "";
	}
}
