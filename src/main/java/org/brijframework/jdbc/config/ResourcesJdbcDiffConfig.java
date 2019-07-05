package org.brijframework.jdbc.config;

public class ResourcesJdbcDiffConfig implements JdbcConfig {

	private String catalogLeft;
	private String CatalogRight;
	private boolean enable;
	private String location;

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public String getCatalogLeft() {
		return catalogLeft;
	}

	public void setCatalogLeft(String catalog) {
		this.catalogLeft = catalog;
	}
	
	public String getCatalogRight() {
		return CatalogRight;
	}

	public void setCatalogRight(String catalog) {
		this.CatalogRight = catalog;
	}
}
