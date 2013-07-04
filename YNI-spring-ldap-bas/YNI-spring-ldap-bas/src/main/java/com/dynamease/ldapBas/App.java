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

	DyniPerson inputPerson(Scanner sc) {

		DyniPerson toReturn = new DyniPerson();
		getNamePerson(sc, toReturn);
		return toReturn;
	}
	
	void getNamePerson(Scanner sc, DyniPerson person){
		String prenom = "";
		String nom = "";

		System.out.print("prenom?");
		prenom = sc.nextLine();
		System.out.print("Nom?");
		nom = sc.nextLine();
		person.setFirstName(prenom);
		person.setLastName(nom);
	}

	void start() {
		Scanner sc = new Scanner(System.in);
		String choix = "";

		System.out.println("Salut les aminches!");

		while (!"f".equalsIgnoreCase(choix)) {

			System.out.println("r : recherche nom");
			System.out.println("p : presence nom");
			System.out.println("+ : ajout subscriber");
			System.out.println("f : fin");
			System.out.println("Choix?");
			choix = sc.nextLine();
			switch (choix) {
			case "p": {
				if (dyniAnnuaireService.isPresent(inputPerson(sc))) {
					System.out.println("Present dans l'annuaire");
				} else {
					System.out.println("Non present dans l'annuaire");
				}
				break;
			}
			case "r": {
				DyniSubscriber trouve = null;
				if ((trouve = dyniAnnuaireService.getSubscriber(inputPerson(sc))) != null) {
					System.out.println("Present dans l'annuaire");
					System.out.println(trouve.toString());
				} else {
					System.out.println("Non present dans l'annuaire");
				}
				break;
			}

			case "+": {
				DyniSubscriber aCreer = new DyniSubscriber();
				getNamePerson(sc,aCreer);
				System.out.println("Subscriber ID?");
				aCreer.setSubscriberid(Integer.parseInt(sc.nextLine()));
				try {
					dyniAnnuaireService.create(aCreer);
				} catch (DynInvalidSubIdException e) {
					System.out.println("Subscriber Id invalid");
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
