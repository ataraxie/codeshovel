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

	public boolean equalsIgnoreMetadata(Object obj) {
		boolean ret = false;
		if (obj instanceof Yparameter) {
			Yparameter otherParameter = (Yparameter) obj;
			ret = this.name.equals(otherParameter.getName())
					&& this.type.equals(otherParameter.getType());
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		return this.equalsIgnoreMetadata(obj) && this.metadataString.equals(((Yparameter) obj).getMetadataString());
	}
}
