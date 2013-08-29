package com.dynamease.test.ads;

import java.util.Scanner;

public class EmbDynDirTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		EmbDynDirImpl eds = new EmbDynDirImpl();
		
		// play with starting and stopping the server
		System.out.println("Salut les aminches!");
		Scanner sc = new Scanner(System.in);
		String commande;

		do {
			System.out.print("Commande(start, stop, inject, load ou quit)    ?   ");
			commande = sc.nextLine();

			switch (commande) {
			case "quit":
				break;
			case "start":
				try {
	
					eds.start(10391);
					System.out.println("Serveur Demarré");
				} catch (Exception e) {
					System.out.println("Le serveur ne peut pas demarrer");
					e.printStackTrace();
				}
				break;
				
			case "stop":
				try {
					eds.stop();
					System.out.println("Serveur arrete");
				} catch (Exception e) {
					System.out.println("Le serveur ne peut pas s'arreter");
					e.printStackTrace();
				}
				break;
			case "load":
				System.out.println("Fichier Ldif a charger");
				String file = sc.nextLine();
				try {
					eds.ldifLoad(file);
					System.out.println("Fichier charge");
				} catch (Exception e) {
					System.out.println("ca merde");
					e.printStackTrace();
				}
				break;
	
			default:
				break;
			}

		} while (!(commande.equals("quit")));

		sc.close();
		System.out.println("Salut on se casse!");

	}
	

}
