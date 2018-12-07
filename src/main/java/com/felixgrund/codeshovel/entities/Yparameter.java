package com.felixgrund.codeshovel.entities;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Yparameter {

	public static final String TYPE_NONE = "";
	public static final Map<String, String> METADATA_NONE = new HashMap<>();

	private String name;
	private String type;
	private Map<String, String> metadata;
	private String metadataString;

	public Yparameter(String name, String type) {
		this.name = name;
		this.type = type;
		this.metadata = METADATA_NONE;
		this.metadataString = "";
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
		this.metadataString = "";
		if (!metadata.isEmpty()) {
			List<String> pairs = new ArrayList<>();
			for (String metadataKey : metadata.keySet()) {
				pairs.add(metadataKey + "-" + metadata.get(metadataKey));
			}
			this.metadataString = StringUtils.join(pairs, "__");
		}
	}

	public String getMetadataString() {
		return metadataString;
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if (obj instanceof Yparameter) {
			Yparameter otherParameter = (Yparameter) obj;
			ret = this.name.equals(otherParameter.getName())
					&& this.type.equals(otherParameter.getType());
		}
		return ret;
	}

	public String getNameTypeString() {
		String string = this.name;
		if (StringUtils.isNotBlank(this.type)) {
			string += "-" + this.type;
		}
		return string;
	}

	@Override
	public String toString() {
		String string = getNameTypeString();
		if (StringUtils.isNotEmpty(this.metadataString)) {
			string += "("+this.metadataString+")";
		}
		return string;
	}
}
