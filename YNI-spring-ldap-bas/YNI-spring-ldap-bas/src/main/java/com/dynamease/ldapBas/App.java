package com.dynamease.ldapBas;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Hello world!
 * 
 */
@Service
public class App {

	@Autowired
	private DyniAnnuaireService dyniAnnuaireService;

	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("file:src/main/resources/ldapbascontext.xml");
		App App = context.getBean(App.class);
		App.start();
		context.close();

	}

	void start() {
		Scanner sc = new Scanner(System.in);
		String prenom = "";
		String nom = "";
		String choix = "";

		System.out.println("Salut les aminches!");

		while (!"f".equalsIgnoreCase(choix)) {

			System.out.println("r : recherche nom");
			System.out.println("f : fin");
			System.out.println("Choix?");
			choix = sc.nextLine();
			switch (choix) {
			case "r": {
				System.out.print("prenom?");
				prenom = sc.nextLine();
				System.out.print("Nom?");
				nom = sc.nextLine();
				if (dyniAnnuaireService.existsAsSubscriber(new DyniPerson(prenom, nom))) {
					System.out.println("Present dans l'annuaire");
				} else {
					System.out.println("Non present dans l'annuaire");
				}
				break;
			}
	
			default:
				break;
			}
		}

		System.out.println("Bye!");
		sc.close();

	}
}
