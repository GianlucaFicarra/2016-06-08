package it.polito.tdp.formulaone.model;

import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	private FormulaOneDAO fonedao;
	private SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge> grafo;
	private DriverIdMap driverIdMap;
	private List<Driver> drivers;
	private List<FantaPilota> fantaPiloti;
	
	public Model() {
		fonedao = new FormulaOneDAO();
		driverIdMap = new DriverIdMap();
		drivers = fonedao.getAllDrivers(driverIdMap);
		System.out.println(driverIdMap);
	}

	public List<Season> getAllSeasons() {
		return fonedao.getAllSeasons();
	}
	
	public List<Driver> getAllDrivers() {
		return this.drivers;
	}
	
	public List<FantaPilota> getFantaPiloti() {
		return fantaPiloti;
	}
	
	//dal model avvio la simulazione
	public void simula(int circuitId, Driver driver) {
		fantaPiloti = fonedao.getFantaPiloti(circuitId, driver); //fp sono quelli scelti quando terminiamo circuito ed anno, 
		          //seleziono quindi circuito e pilota ed ottengo la lista dei fp relativi a tutti gli anni 
		Simulazione sim = new Simulazione(this); //passo alla simulazione il model stesso per poter accedere ai metodi del model
		sim.simula();
	}
}
