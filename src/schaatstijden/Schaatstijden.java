package schaatstijden;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Schaatstijden extends Thread{

	private int afstand;
	private String begindatum;
	private String einddatum;
	private ArrayList<Persoon> personen;
	private boolean tussenvoegsel;
	private GUI gui;
	private BufferedReader br;
	private boolean sorteer;
	
	
	public Schaatstijden(){
		personen = new ArrayList<Persoon>();
	}
	
	public Schaatstijden(GUI gui){
		personen = new ArrayList<Persoon>();
		this.gui = gui;
	}
	
	public void setBr(BufferedReader br){
		this.br = br;
	}
	
	public void setSorteer(boolean sorteer){
		this.sorteer = sorteer;
	}
	
	public void addPersoon(int geslacht, String voornaam, String achternaam, String geboortedatum){
		Persoon p = new Persoon(geslacht, voornaam, achternaam, geboortedatum, afstand, begindatum, einddatum);
		personen.add(p);
	}
	
	public void setAfstand(int afstand){
		this.afstand = afstand;
	}
	
	public void setBegindatum(String begindatum){
		this.begindatum = begindatum;
	}
	
	public void setEinddatum(String einddatum){
		this.einddatum = einddatum;
	}
	
	public void setTussenvoegsel(boolean tv){
		this.tussenvoegsel = tv;
	}
	
	public ArrayList<Persoon> getPersonen(){
		return personen;
	}
	
	public int getAfstand(){
		return afstand;
	}
	
	public String getBegindatum(){
		return begindatum;
	}
	
	public String getEinddatum(){
		return einddatum;
	}
	
	public GUI getGUI(){
		return gui;
	}
	
	public boolean getSorteer(){
		return sorteer;
	}
	
	public void importAll(){
		for(int i = 0; i < personen.size(); i++){
			if(gui != null){
				gui.showMessage("Importeer persoon " + i);
			}
			Persoon p = personen.get(i);
			p.importID();
			p.importTijd();
		}
	}
	
	public void printAll(){
		for(int i = 0; i < personen.size(); i++){
			Persoon p = personen.get(i);
			System.out.println(p);
		}
	}
	
	public String getResultsString(){
		String result = new String();
		for(int i = 0; i < personen.size(); i++){
			result += personen.get(i) + "\n";
		}
		return result;
	}
	
	public void printToCSV(String fileName){
		Path file = Paths.get(fileName);
		try {
			Files.write(file, personen, Charset.forName("UTF-16"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sortPersonen(){
		personen.sort(new PersoonComparator());
	}
	
	public void importDeelnemers(){
		try {
			String line;
			while((line = br.readLine()) != null){
				if(!line.equals("")){
					String[] values = line.split("\t");
					int geslacht;
					if(values[0].equalsIgnoreCase(Constants.MAN_TEKEN)){
						geslacht = Constants.MAN;
					} else {
						geslacht = Constants.VROUW;
					}
					if(tussenvoegsel){
						if(!values[2].equals("")){
							addPersoon(geslacht, values[1], values[2] + " " + values[3], values[4]);
						} else {
							addPersoon(geslacht, values[1], values[3], values[4]);
						}
					} else {
						addPersoon(geslacht, values[1], values[2], values[3]);
					}
					if(gui != null){
						gui.showMessage("Persoon " + personen.size() + " toegevoegd");
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
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
