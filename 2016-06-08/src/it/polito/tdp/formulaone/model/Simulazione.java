package it.polito.tdp.formulaone.model;

import java.util.PriorityQueue;

public class Simulazione {

	private Model model;
	private PriorityQueue<Evento> pq;

	public Simulazione(Model model) {
		this.model = model;
		pq = new PriorityQueue<Evento>();
	}

	/*metodo dove realmente effettuo la simulazione
	schedulo eventi=passaggio dal via a partire dall'inizio della gara per il fantapilota considerato
	ogni volta che giro termina schedulo nuovo evento per processare questo giro, 
	e per il giro seguente per avere dei ordinamento dei tempi assoluto, vado a sommare il tempo del giro al tempo del giro precedente
	mi viene più semplice ordinare i passaggi dal via nella priorityque, che simula andamento gara, e processa un evento alla volta come se ci fosse
	un osservatore che guardasse la linea di passaggio dal via, quindi simula passaggio delle macchine una alla volta, indipendentemente dal giro*/
	//--->creo classe evento
	public void simula() {

		// Inizializzazione(inserisco eventi relativi al primo giro di ciascun fantapilota)
		for (FantaPilota fp : model.getFantaPiloti()) {
			pq.add(new Evento(fp, 0, fp.getLapTimes().get(0)));
			// metto fantapilota, giro, e tempo
			//assumo di far partire giro e posizione di ogni pilota da 0
		}
		

		// Processo gli eventi
		while (!pq.isEmpty()) {  //finche coda non è vuota
			Evento e = pq.poll();    //rendo primo eveto
			
			/* primo evento che processero è quello relativo al primo pilota
			 * che taglia il traguardo per il primo giro, e visto che i lap sono inizzializzati a -1
			 * nessun pilota avrà lap a 0 nel primo giro, e quini il primo considerato taglierà il traguardo */
		
			//--assegnare dei punti al pilota(se in pos migliore rispetto al precednete)
			//vedo quanti piloti hanno salvato nella varianile lap un numero paggiore o uguale del lap corrente, e vedo la mia posizione
			int count = 0;
			for (FantaPilota ifp : model.getFantaPiloti()) {
				if (!ifp.getEliminato()) {
					if (ifp.getLap() >= e.getLap()) {
						count++;
					}
				}
			}
			// Se la posizione del pilota è migliore di quella del giro precedente
			// Aggiorno il punteggio
			if (count < e.getFantaPilota().getRank()) {
				int points = (e.getFantaPilota().getPoints()) + 1;
				e.getFantaPilota().setPoints(points);   //aggiorno punteggio
			}
			e.getFantaPilota().setLap(e.getLap());    //aggiorno giro
			e.getFantaPilota().setRank(count);       //aggiorno posizione

			
			
			//--capire se il polita è  stato doppiato
			boolean doppiato = false;
			for (FantaPilota ifp : model.getFantaPiloti()) {
				//se qualcuno dei fantapiloti ha un lap superiore del nostro di 2, allora sono stato doppiato
				if (!ifp.getEliminato()) {
					if (ifp.getLap() > e.getLap() + 1) {
						doppiato = true;
					}
				}
			}
			if (doppiato) { //segnalo che sia eliminato e non creo più eventi per lui
				e.getFantaPilota().setEliminato();
				System.out.println("Pilota eliminato " + e.getFantaPilota().getYear());

			} else {
				
				//se non è stato doppiato
				//--inserire un nuovo evento relativo al giro successivo se la lista laptimes contiene altri eventi
				if (e.getFantaPilota().getLapTimes().size() > e.getLap() + 1) {
					/* se ci sono ancora eventi da aggiungere
					 * aggiungo nuovo evento per lo stesso pilota con tempo corrente+ quello successivo relativo al giro succcessivo*/
					int t = e.getFantaPilota().getLapTimes().get(e.getLap() + 1);  //tempo del giro successivo
					pq.add(new Evento(e.getFantaPilota(), e.getLap() + 1, e.getTime() + t));
				}
			}
		}
	}
}
