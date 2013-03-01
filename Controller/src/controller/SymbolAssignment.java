package controller;

import java.util.List;
import java.util.PriorityQueue;
import com.google.common.collect.BiMap;


public class SymbolAssignment {

	protected PriorityQueue<uPIFrequency> piFrequencyList; //List of UPIs and frequency
	protected BiMap<Integer, String> piTranslate; //List of UPIs and mac addresses
	protected List<Integer> piSymbolHashTable; //Array of UPIs with direct addressing
	protected int gUPI;
	
	public SymbolAssignment(){
		gUPI = 0;
	}
	
	protected int hashFunction(int input){
		return (input % 97);
	}
	
	public String addSymbol(int symbol){ //returns the mac address of a pi
		Integer newPi = piSymbolHashTable.get(hashFunction(symbol));
		if (newPi != null){ //If the symbol has been seen before, returns the pi associated with that symbol
			return piTranslate.get(newPi);
		}
		else { //Assigns the symbol to the pi with the lowest number of symbols associated with it and returns the pi
			uPIFrequency minUsePi = piFrequencyList.remove();
			newPi = minUsePi.uPI;
			minUsePi.frequency += 1;
			piFrequencyList.add(minUsePi);
			return piTranslate.get(newPi);
		}
	}
	
	public void add(String pi) { //Adds a pi to the pool available to have symbols assigned to them
		piTranslate.put(gUPI, pi); //Gives it a unique integer value.
		piFrequencyList.add(new uPIFrequency(gUPI)); //Adds the pi to the frequency list with frequency 0.
		gUPI += 1; //Ensure the next pi will be given a new number.
	}

	public String removePi(String pi) {
		Integer oldPiUPI = piTranslate.inverse().get(pi);
		uPIFrequency newPi = piFrequencyList.remove(); // get pi with lowest utilisation
		for (int i = 0;  i < piSymbolHashTable.size(); i++) { // for every symbol
			if (piSymbolHashTable.get(i) == oldPiUPI) { //if that symbol corresponds to the dead pi
				piSymbolHashTable.set(i, newPi.uPI); //set that symbol to correspond to the designated fail over pi
				newPi.frequency ++;
			}
		}
		return piTranslate.get(newPi.uPI);
	}

	protected class uPIFrequency { //used to store the amount of symbols associated to each pi
		
		uPIFrequency(int uPI){
			this.uPI = uPI;
			frequency = 0;
		}
		public final Integer uPI;
		public Integer frequency;
	}

}