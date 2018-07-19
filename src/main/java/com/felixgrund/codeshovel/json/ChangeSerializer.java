package com.felixgrund.codeshovel.json;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.changes.Ycomparefunctionchange;
import com.felixgrund.codeshovel.changes.Ymultichange;
import com.felixgrund.codeshovel.wrappers.CommitWrap;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChangeSerializer implements JsonSerializer<Ychange> {

	@Override
	public JsonElement serialize(Ychange change,
			 Type typeOfSrc, JsonSerializationContext context) {

		JsonObject obj = new JsonObject();
		CommitWrap commitWrap = change.getCommitWrap();
		obj.addProperty("type", change.getTypeAsString());
		obj.addProperty("commitMessage", commitWrap.getCommitMessage());
		obj.addProperty("commitDate", commitWrap.getCommitDate().getTime());
		obj.addProperty("commitName", commitWrap.getCommitName());
		obj.addProperty("commitAuthor", commitWrap.getAuthorName());
		if (change instanceof Ymultichange) {
			JsonArray subchanges = new JsonArray();
			for (Ychange subchange : ((Ymultichange) change).getChanges()) {
				subchanges.add(subchange.getClass().getSimpleName());
			}
			obj.add("subchanges", subchanges);
		}
		if (change instanceof Ycomparefunctionchange) {
			Ycomparefunctionchange ycomparefunctionchange = (Ycomparefunctionchange) change;
			CommitWrap oldCommitWrap = ycomparefunctionchange.getOldCommitWrap();
			obj.addProperty("commitDateOld", oldCommitWrap.getCommitDate().getTime());
			obj.addProperty("commitNameOld", oldCommitWrap.getCommitName());
			obj.addProperty("commitAuthorOld", oldCommitWrap.getAuthorName());
			obj.addProperty("daysBetweenCommits", ycomparefunctionchange.getDaysBetweenCommits());
			obj.addProperty("commitsBetweenForRepo", ycomparefunctionchange.getCommitsBetweenForRepo().size());
			obj.addProperty("commitsBetweenForFile", ycomparefunctionchange.getCommitsBetweenForFile().size());
			obj.addProperty("diff", ycomparefunctionchange.getDiffAsString());
		}

		return obj;
	};
}
