package com.tma.ttc.androidK13.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ListResult implements Serializable {
	private List<SearchResult> listResult = new ArrayList<SearchResult>();

	// Default constructor
	public ListResult() {
		
	}
	
	// Constructor with parameters
	public ListResult(List<SearchResult> searchResultList) {
		this.listResult = searchResultList;
	}

	// Return the list
	public List<SearchResult> getListSearchResult() {
		return listResult;
	}

	// Set list
	public void setListSearchResult(List<SearchResult> listResult) {
		this.listResult = listResult;
	}

}
