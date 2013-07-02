package com.dynamease.ldapBas;

import java.util.Scanner;

/**
 * Hello world!
 * 
 */
public class App {
	

	private static DyniAnnuaireService dyniAnnuaireService;
	
	
	public static void main( String[] args )
    {
	   	Scanner sc = new Scanner(System.in);
	   	String url ="ldap://192.168.1.10:";
		String prenom ="";
		String nom ="";
		String encore = "";

        System.out.println( "Salut les aminches!" );
        System.out.println("Port?");
        url = url + sc.nextLine();
        dyniAnnuaireService = new DyniAnnuaireService(url);
       
 		do {
			System.out.print("prenom?");
			prenom = sc.nextLine();
			System.out.print("Nom?");
			nom = sc.nextLine();
			if (dyniAnnuaireService.existsAsSubscriber(new DyniPerson(prenom, nom))) {
				System.out.println("Present dans l'annuaire");
			}
			else {
				System.out.println("Non present dans l'annuaire");
			}
			System.out.print("Autre recherche (o/n)?");
			encore = sc.nextLine();
		} while (encore.equals("o"));
		
		System.out.println("Bye!");
		sc.close();
		
    }
}
