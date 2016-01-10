package schaatstijden;

import java.util.ArrayList;

public class SchaatstijdenMulti extends Schaatstijden {

	private int afstand2;
	
	public SchaatstijdenMulti(GUIMulti gui){
		super(gui);
	}
	
	public void setAfstand2(int afstand2){
		this.afstand2 = afstand2;
	}
	
	public void addPersoon(int geslacht, String voornaam, String achternaam, String geboortedatum){
		int afstand = getAfstand();
		String begindatum = getBegindatum();
		String einddatum = getEinddatum();
		PersoonMulti p = new PersoonMulti(geslacht, voornaam, achternaam, geboortedatum, afstand, begindatum, einddatum, afstand2);
		getPersonen().add(p);
	}
	
	public void sortPersonen(){
		ArrayList<Persoon> personen = getPersonen();
		personen.sort(new PersoonComparatorMulti());
	}
	
	public void importAll(){
		ArrayList<Persoon>personen = getPersonen();
		GUI gui = getGUI();
		for(int i = 0; i < personen.size(); i++){
			if(gui != null){
				gui.showMessage("Importeer persoon " + i);
			}
			Persoon p = personen.get(i);
			p.importID();
			p.importTijd();
			((PersoonMulti) p).berekenPunten();
		}
	}
	
	public void run(){
		GUI gui = getGUI();
		boolean sorteer = getSorteer();
		importDeelnemers();
		importAll();
		if(sorteer){
			sortPersonen();
		}
		String results = getResultsString();
		if(gui != null){	
			gui.setResults(results);
		}
	}
}
