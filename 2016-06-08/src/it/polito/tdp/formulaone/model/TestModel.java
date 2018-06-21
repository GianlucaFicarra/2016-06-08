package it.polito.tdp.formulaone.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		System.out.println(m.getAllDrivers());
		
		m.simula(1, m.getAllDrivers().get(0));
		List<FantaPilota> fps = m.getFantaPiloti();
		Collections.sort(fps, new Comparator<FantaPilota>() {

			@Override
			public int compare(FantaPilota o1, FantaPilota o2) {
				return Integer.compare(o1.getPoints(), o2.getPoints());
			}
			
		});
		
		for (FantaPilota fp : fps) {
			System.out.println("Year: " + fp.getYear() + " Points: " + fp.getPoints() + " Rank: " + fp.getRank() + " Eliminato: " + fp.getEliminato());
		}
	}
}
