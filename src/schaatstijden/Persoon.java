package schaatstijden;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

//import sun.net.www.URLConnection;

public class Persoon {

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
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getTijd(){
		return tijd;
	}
	
	public void importTijd(){
		try {
			URL url  = new URL("http://speedskatingresults.com/api/xml/seed_times?skater=" + id + "&start=" + begindatum + "&end=" + einddatum + "&distance=" + afstand);
			URLConnection conn;
			conn = url.openConnection();


            BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
            String inputLine;
            br.readLine();
            inputLine = br.readLine();
            int beginIndex = inputLine.indexOf("<time>") + "<time>".length();
            int endIndex = inputLine.lastIndexOf("</time>");
            tijd = inputLine.substring(beginIndex, endIndex);
            
            

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
		Persoon p = new Persoon(Constants.MAN, "Sybe", "van Hijum", "1992-04-29", 3000, "2015-07-01", "2016-06-30");
		p.setId(42292);
		p.importTijd();
		System.out.println(p.getTijd());
	}
	
	
}
