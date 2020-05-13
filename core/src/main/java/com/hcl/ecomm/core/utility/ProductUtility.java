package com.hcl.ecomm.core.utility;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ProductUtility {

	private static final Logger LOG = LoggerFactory.getLogger(ProductUtility.class);

	public static JsonArray fromStringToJsonArray(String responseStream) {

		JsonArray j_array;
		JsonObject jobj = new Gson().fromJson(responseStream, JsonObject.class);
		if (jobj.has("items")) {
			j_array = jobj.getAsJsonArray("items");
		} else {
			j_array = jobj.getAsJsonArray();
		}
		return j_array;
	}

	public static Map<String, String> fromJsonArrayToMap(JsonArray productJsonArray) {

		HashMap<String, String> productMap = new HashMap<>();

		try {

			JsonObject productObject = productJsonArray.get(0).getAsJsonObject();
			if (!productObject.isJsonNull()) {
				Set<String> keys = productObject.keySet();
				productMap.put("description", getDescription(productObject));
				productMap.put("categoryId", getProductCategoryId(productObject).toString());
				for (String key : keys) {
					productMap.put(key, productObject.get(key).getAsString());
				}
			}
		} catch (Exception e) {
			LOG.error("Caught exception : " + e.getMessage());
		}

		return productMap;
	}

	public static List<Map<String, String>> fromJsonArrayToList(JsonArray productJsonArray) {

		List<Map<String, String>> productList = new LinkedList<>();

		for (JsonElement jel : productJsonArray) {
			Map<String, String> productMap = fromJsonArrayToMap(jel.getAsJsonArray());
			productList.add(productMap);
		}
		return productList;
	}

	private static String getDescription(JsonObject productObject) {
		String description = null;

		JsonArray j_array = productObject.get("custom_attributes").getAsJsonArray();

		for (JsonElement el : j_array) {
			String attribute = el.getAsJsonObject().get("attribute_code").getAsString();
			if ("description".equalsIgnoreCase(attribute)) {
				description = el.getAsJsonObject().get("value").getAsString();
			}
		}
		return description;
	}

	private static List<Integer> getProductCategoryId(JsonObject productObject) {
		List<Integer> categoryList = new ArrayList<>();

		JsonArray j_array = productObject.get("extension_attributes").getAsJsonObject().get("category_links")
				.getAsJsonArray();

		for (int i = 0; i < j_array.size(); i++) {
			int id = j_array.get(i).getAsJsonObject().get("category_id").getAsInt();
			categoryList.add(id);
		}
		return categoryList;
	}
}
