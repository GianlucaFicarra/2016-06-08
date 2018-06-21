package it.polito.tdp.formulaone.model;

import java.util.List;

public class FantaPilota {

	// dato Driver e il circuito già selezionati
	//memorizzo per ogni istanza del pilota immaginario
	private int year;
	private List<Integer> lapTimes; //lista di tempi di percorrenza sul circuito
	
	private int points;  //punteggio del pilota
	private int lap;     //giro corrente
	private int rank;    //posizione
	private boolean eliminato;   //segna se sorpassato
	
	public FantaPilota(int year, List<Integer> lapTimes) { //passo anno e lista tempi su giro
		this.year = year;
		this.lapTimes = lapTimes;
		this.points = 0; 
		this.lap = -1;
		this.rank = Integer.MAX_VALUE;
		this.eliminato = false;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<Integer> getLapTimes() {
		return lapTimes;
	}

	public void setLapTimes(List<Integer> lapTimes) {
		this.lapTimes = lapTimes;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLap() {
		return lap;
	}

	public void setLap(int lap) {
		this.lap = lap;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FantaPilota [year=");
		builder.append(year);
		builder.append(", lapTimes=");
		builder.append(lapTimes);
		builder.append(", points=");
		builder.append(points);
		builder.append(", lap=");
		builder.append(lap);
		builder.append(", rank=");
		builder.append(rank);
		builder.append("]");
		return builder.toString();
	}

	
	//tiene conto se sia stato doppiato o meno
	public void setEliminato() {
		this.eliminato = true;
	}
	
	public boolean getEliminato() {
		return this.eliminato;
	}
}
