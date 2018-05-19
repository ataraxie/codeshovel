package com.felixgrund.codestory.ast.json;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ChangeSerializer implements JsonSerializer<Ychange> {

	@Override
	public JsonElement serialize(Ychange change,
			 Type typeOfSrc, JsonSerializationContext context) {

		JsonObject obj = new JsonObject();
		obj.addProperty("type", change.getClass().getSimpleName());

		
		return obj;
	};
}
