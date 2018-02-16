package com.rabbitforever.generateJavaMVC.bundles;

public abstract class PropertiesBase {
	public static final String LANG_EN = "EN";
	public static final String LANG_TCHI = "TCHI";
	protected String lang;
	
	public PropertiesBase(String lang) {
		this.lang = lang;
	}

	public PropertiesBase() {
		lang =LANG_TCHI;
	}
}
