package schaatstijden;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class Persoon implements CharSequence{

	private int geslacht;
	private String voornaam;
	private String achternaam;
	private int afstand;
	private String tijd;
	private int id;
	private String geboortedatum;
	private String begindatum;
	private String einddatum;
	
	public Persoon(int geslacht, String voornaam, String achternaam, String geboortedatum, int afstand, String begindatum, String einddatum){
		this.geslacht = geslacht;
		this.voornaam = voornaam;
		this.achternaam = achternaam;
		this.afstand = afstand;
		this.geboortedatum = geboortedatum;
		this.begindatum = begindatum;
		this.einddatum = einddatum;
		this.id = -1;
		this.tijd = "00.00,00";
	}
	
	public String getTijd(){
		return tijd;
	}
	
	public void setTijd(String tijd){
		int l = tijd.length();
		char[] tijdChars = this.tijd.toCharArray();
		for(int i = l; i > 0; i--){
			tijdChars[Constants.TIJD_LENGTH - 1 - l + i] = tijd.charAt(i - 1);
		}
		this.tijd = new String(tijdChars);
	}
	
	public void importTijd(){
		if(id == -1){
			tijd = Constants.GEENTIJD;
		} else {
			try {
				URL url  = new URL("http://speedskatingresults.com/api/xml/seed_times?skater=" + id + "&start=" + begindatum + "&end=" + einddatum + "&distance=" + afstand);
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
	            	setTijd(inputLine.substring(beginIndex, endIndex));
	            } else {
	            	tijd = Constants.GEENTIJD;
	            }
	            
	            br.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void importID(){
		try {
			String urlText = ("http://speedskatingresults.com/api/xml/skater_search.php?familyname=" + achternaam + "&givenname=" + voornaam + "&gender=" + geslacht);
			urlText = urlText.replace(" ", "%20");
			URL url  = new URL(urlText);
			URLConnection conn;
			conn = url.openConnection();

            BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
            String inputLine;
            br.readLine();
            inputLine = br.readLine();
            while (inputLine.contains("<skater>")){
            	if(inputLine.contains("<dob>")){//Persoon heeft een geboortedatum
            		int beginIndex = inputLine.indexOf("<dob>") + "<dob>".length();
            		int endIndex = inputLine.indexOf("</dob>");
            		String dob = inputLine.substring(beginIndex, endIndex);
            		if(geboortedatum.equals(dob)){//Geboortedatum is correct
            			beginIndex = inputLine.indexOf("<id>") + "<id>".length();
            			endIndex = inputLine.indexOf("</id>");
            			id = Integer.parseInt(inputLine.substring(beginIndex, endIndex));
            			break;
            		} else {
            			int cutIndex = inputLine.indexOf("</skater>");
            			inputLine = inputLine.substring(cutIndex);
            		}
            	} else { //Neem aan dat dit de goede persoon is
            		int beginIndex = inputLine.indexOf("<id>") + "<id>".length();
        			int endIndex = inputLine.indexOf("</id>");
        			id = Integer.parseInt(inputLine.substring(beginIndex, endIndex));
        			break;
            	}
            }            
            br.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Persoon p = new Persoon(Constants.VROUW, "Jip", "Spel", "1993-12-31", 500, "2015-07-01", "2016-06-30");
		p.importID();
		p.importTijd();
		System.out.println(p.getTijd());
	}
	
	public String toString(){
		String result = voornaam + "\t" + achternaam + "\t" + tijd;
		return result;
	}

	@Override
	public char charAt(int index) {
		// TODO Auto-generated method stub
		return this.toString().charAt(index);
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return Constants.TIJD_LENGTH;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		// TODO Auto-generated method stub
		return this.toString().subSequence(start, end);
	}
}
