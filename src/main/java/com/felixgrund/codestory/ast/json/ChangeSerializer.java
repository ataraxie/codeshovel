package com.felixgrund.codestory.ast.json;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.changes.Ymultichange;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChangeSerializer implements JsonSerializer<Ychange> {

	@Override
	public JsonElement serialize(Ychange change,
			 Type typeOfSrc, JsonSerializationContext context) {

		JsonObject obj = new JsonObject();
		obj.addProperty("type", change.getClass().getSimpleName());
		if (change instanceof Ymultichange) {
			JsonArray subchanges = new JsonArray();
			for (Ychange subchange : ((Ymultichange) change).getChanges()) {
				subchanges.add(subchange.getClass().getSimpleName());
			}
			obj.add("subchanges", subchanges);
		}

		return obj;
	};
}
