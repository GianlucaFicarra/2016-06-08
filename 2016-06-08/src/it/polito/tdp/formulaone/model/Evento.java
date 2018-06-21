package it.polito.tdp.formulaone.model;

public class Evento implements Comparable<Evento> {

	private FantaPilota fp;  //puntatore al fantapilota
	int lap;                 //giro di riferimento
	int time;                //eventi ordianti in base al tempo 
	
	public Evento(FantaPilota fp, int lap, int time) {
		this.fp = fp;
		this.lap = lap;
		this.time = time;
	}
	
	public FantaPilota getFantaPilota() {
		return this.fp;
	}

	public int getLap() {
		return lap;
	}



	public void setLap(int lap) {
		this.lap = lap;
	}



	public int getTime() {
		return time;
	}



	public void setTime(int time) {
		this.time = time;
	}



	@Override //eventi ordinati in base al tempo
	public int compareTo(Evento o) {
		return Integer.compare(this.time, o.time);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Evento [fp=");
		builder.append(fp.getYear());
		builder.append(", lap=");
		builder.append(lap);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}
}
