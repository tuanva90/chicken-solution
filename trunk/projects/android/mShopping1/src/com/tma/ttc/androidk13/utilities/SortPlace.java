package com.tma.ttc.androidk13.utilities;

import java.util.Comparator;
import com.tma.ttc.androidk13.models.PlaceModel;

//Class to help sort place by distance
public class SortPlace implements Comparator<PlaceModel> {
	public int compare(PlaceModel place1, PlaceModel place2) {
		// TODO Auto-generated method stub
		double dis1 = place1.getDistance();
		double dis2 = place2.getDistance();
		
		if(dis1 > dis2)
			return 1;
		else {
			if(dis1 < dis2)
				return -1;
			else
				return 0;
		}		
	}	
}
