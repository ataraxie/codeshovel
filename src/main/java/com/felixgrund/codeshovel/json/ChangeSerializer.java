package com.felixgrund.codeshovel.json;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.changes.Ycomparefunctionchange;
import com.felixgrund.codeshovel.changes.Ymultichange;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChangeSerializer implements JsonSerializer<Ychange> {

	@Override
	public JsonElement serialize(Ychange change,
			 Type typeOfSrc, JsonSerializationContext context) {

		return change.toJsonObject();
	};

}
