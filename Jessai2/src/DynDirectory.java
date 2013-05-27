import java.util.Scanner;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;

/**
 * Réalisée pour se former aux méthodes d'accès à ldap.
 * Volonté de l'utiliser comme prototype d'un accès annuaire Dynamease.
 * Contient les méthodes de tests sur un annuaire.
 * <p>
 * S'appuie sur le sdk ldap de Unboundid
 * @author Yves Nicolas
 *
 */
public class DynDirectory implements DynAdressBook {

	/**
	 * Connexion annuaire sous-jacente...
	 */
	private LDAPConnection annuaire = null;
	private String baseDN = "dc=yves,dc=byoe";
	private String adresseIP = "192.168.1.14";
	private String bindDN = "cn=admin";
	private String pwd = "yves";
			
	private boolean initialise = false;

	
	public DynDirectory() {
		InitConnexion();
	}

	private void connectIfNeeded(){
		while (!initialise) {
			InitConnexion();
		}
		if (!annuaire.isConnected())
			try {
				annuaire.reconnect();
			} catch (LDAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

/**
 * 
 * Ouvre une connexion annuaire sur la base des valeurs de constantes du package
 * @return rien du tout	
 */
	private void InitConnexion() {
	
	
		try {
			annuaire = new LDAPConnection(adresseIP, 389, bindDN+","+baseDN, pwd);
			initialise = true;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage() );
		}
	
			}
/**
 * Recherche dans l'annuaire un nom de famille (attribut sn)
 * Suppose que l'annuaire a été initialisée
 * autorise recherches multiples	
 * @throws Exception : remontée de par la méthode search de unboundid
 * @see LDAPConnection.search 
 */
	public void TestFiltre () throws Exception {
		Scanner sc = new Scanner(System.in);
		String nomRecherche ="";
		SearchResult resultatRecherche = null;
		
		System.out.println("Tests de recherche dans l'annuaire");
		
		if (annuaire == null) {
			System.out.println("Annuaire non ouvert");
		}
		else {
			String encore = "";
			do {
				System.out.print("Nom a chercher");
				nomRecherche = sc.nextLine();
				resultatRecherche = annuaire.search(baseDN,SearchScope.SUB,"sn="+nomRecherche);
				System.out.println(String.format("Resultat recherche : %s",resultatRecherche));
				System.out.print("Autre recherche (o/n)?");
				encore = sc.nextLine();
			} while (encore.equals("o"));
		}
		sc.close();
	}
	
	public boolean ExisteNom (String nomCherche) {
		SearchResult resultatRecherche = null;
		
		connectIfNeeded();
		try {
			resultatRecherche = annuaire.search(baseDN,SearchScope.SUB,"sn="+nomCherche);
		} catch (LDAPSearchException e) {
			return false;
		}
		return (resultatRecherche.getEntryCount() != 0);
	}
}
