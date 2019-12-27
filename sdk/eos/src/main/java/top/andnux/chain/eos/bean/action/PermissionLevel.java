package top.andnux.chain.eos.bean.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import top.andnux.chain.eos.client.pack.Pack;
import top.andnux.chain.eos.client.pack.PackType;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissionLevel {
	
	@Pack(PackType.name)
	@JsonProperty("actor")
	private String actor;
	
	@Pack(PackType.name)
	@JsonProperty("permission")
	private String permission;

	public PermissionLevel() {
		
	}

	public PermissionLevel(String actor, String permission) {
		this.actor = actor;
		this.permission = permission;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
