package com.android;

public class MyStructuredName {
	private String displayName;
	private String namePrefix;
	private String givenName;
	private String middleName;
	private String familyName;
	private String nameSuffix;
	private String phoneticGivenName;
	private String phoneticMiddleName;
	private String phoneticFamilyName;
	
	public MyStructuredName() {
		super();
	}

	public MyStructuredName(String displayName, String namePrefix, String givenName, String middleName,
			String familyName, String nameSuffix, String phoneticGivenName,
			String phoneticMiddleName, String phoneticFamilyName) {
		super();
		this.displayName = displayName;
		this.namePrefix = namePrefix;
		this.givenName = givenName;
		this.middleName = middleName;
		this.familyName = familyName;
		this.nameSuffix = nameSuffix;
		this.phoneticGivenName = phoneticGivenName;
		this.phoneticMiddleName = phoneticMiddleName;
		this.phoneticFamilyName = phoneticFamilyName;
	}

	
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getNameSuffix() {
		return nameSuffix;
	}

	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}

	public String getPhoneticGivenName() {
		return phoneticGivenName;
	}

	public void setPhoneticGivenName(String phoneticGivenName) {
		this.phoneticGivenName = phoneticGivenName;
	}

	public String getPhoneticMiddleName() {
		return phoneticMiddleName;
	}

	public void setPhoneticMiddleName(String phoneticMiddleName) {
		this.phoneticMiddleName = phoneticMiddleName;
	}

	public String getPhoneticFamilyName() {
		return phoneticFamilyName;
	}

	public void setPhoneticFamilyName(String phoneticFamilyName) {
		this.phoneticFamilyName = phoneticFamilyName;
	}
	
	
}
