package schaatstijden;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PersoonMulti extends Persoon {

	private int afstand2;
	private String tijd2;
	private int honderdsten;
	private int honderdsten2;
	private String punten;
	
	public PersoonMulti(int geslacht, String voornaam, String achternaam, String geboortedatum, int afstand,
			String begindatum, String einddatum, int afstand2) {
		super(geslacht, voornaam, achternaam, geboortedatum, afstand, begindatum, einddatum);
		this.afstand2 = afstand2;
		this.punten = "000,000";
		this.tijd2 = "00.00,00";
	}
	
	public String getTijd2(){
		return tijd2;
	}
	
	public String getPunten(){
		return punten;
	}
	
	public void setTijd2(String tijd2){
		int l = tijd2.length();
		char[] tijdChars = this.tijd2.toCharArray();
		for(int i = l; i > 0; i--){
			tijdChars[Constants.TIJD_LENGTH - 1 - l + i] = tijd2.charAt(i - 1);
		}
		this.tijd2 = new String(tijdChars);
	}
	
	public void importTijd(){
		super.importTijd();
		int id = getID();
		String begindatum = getBegindatum();
		String einddatum = getEinddatum();
		if(id == -1){
			tijd2 = Constants.NIET_GEVONDEN;
		} else {
			try {
				URL url  = new URL("http://speedskatingresults.com/api/xml/seed_times?skater=" + id + "&start=" + begindatum + "&end=" + einddatum + "&distance=" + afstand2);
				URLConnection conn;
				conn = url.openConnection();
	
	
	            BufferedReader br = new BufferedReader(
	                               new InputStreamReader(conn.getInputStream()));
	            String inputLine;
	            br.readLine();
	            inputLine = br.readLine();
	            if(inputLine.contains("<time>")){
	            	int beginIndex = inputLine.indexOf("<time>") + "<time>".length();
	            	int endIndex = inputLine.indexOf("</time>");
	            	setTijd2(inputLine.substring(beginIndex, endIndex));
	            } else {
	            	tijd2 = Constants.GEENTIJD;
	            }
	            
	            br.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void berekenPunten(){
		String tijd = getTijd();
		if(tijd != Constants.GEENTIJD && tijd2 != Constants.GEENTIJD && tijd != Constants.NIET_GEVONDEN){
			honderdsten = Integer.parseInt(tijd.substring(0, 2))*60*100 + Integer.parseInt(tijd.substring(3,5))*100 + Integer.parseInt(tijd.substring(6,8));
			honderdsten2 = Integer.parseInt(tijd2.substring(0, 2))*60*100 + Integer.parseInt(tijd2.substring(3,5))*100 + Integer.parseInt(tijd2.substring(6,8));
			int afstand = getAfstand();
			String puntenDuizendsten = Integer.toString((int)((((double)honderdsten / (double)afstand) * 500 + ((double)honderdsten2 / (double)afstand2) * 500) * 10));
			setPunten(puntenDuizendsten);
		} else if (tijd == Constants.NIET_GEVONDEN){
			punten = Constants.NIET_GEVONDEN;
		} else {
			punten = Constants.GEENTIJD;
		}
	}
	
	public void setPunten(String puntenDuizendsten){
		int l = puntenDuizendsten.length();
		char[] puntenChars = this.punten.toCharArray();
		for(int i = 0; i < l; i++){
			if(i < l - 3){
				puntenChars[i + 6 - l] = puntenDuizendsten.charAt(i);
			} else {
				puntenChars[i + 6 - l + 1] = puntenDuizendsten.charAt(i);
			}
		}
		this.punten = new String(puntenChars);
	}
	@Override
	public String toString(){
		String result = super.toString() + "\t" + tijd2 + "\t" + punten;
		return result;
	}
	public static void main(String args[]){
		PersoonMulti p = new PersoonMulti(Constants.VROUW, "Jip", "Spel", "1993-12-31", 500, "2015-07-01", "2016-06-30", 3000);
		p.importID();
		p.importTijd();
		p.berekenPunten();
		System.out.println(p);
	}
}
