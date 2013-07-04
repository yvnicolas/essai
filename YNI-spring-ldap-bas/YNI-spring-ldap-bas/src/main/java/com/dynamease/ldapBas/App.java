package com.dynamease.ldapBas;

import java.util.List;
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

	void getNamePerson(Scanner sc, DyniPerson person) {
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
			System.out.println("i : recherche id");
			System.out.println("p : presence nom");
			System.out.println("+ : ajout subscriber");
			System.out.println("- : suppression subscriber");
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
			
			case "i" : {
				DyniSubscriber trouve;
				System.out.println("Subscriber ID?");
				trouve = dyniAnnuaireService.getSubscriber(Integer.parseInt(sc.nextLine()));
				if (trouve == null) {
					System.out.println("Id non existant");
				}
				else {
					System.out.println(trouve.toString());
				}
				break;

			}
			case "r": {
				List<DyniSubscriber> trouve = dyniAnnuaireService.getHomonyms(inputPerson(sc));
				if (trouve != null) {
					System.out.println("Present(s) dans l'annuaire : ");
					for (DyniSubscriber person : trouve) {
						System.out.println(person.toString());
					}
				} else {
					System.out.println("Personne de ce nom la dans l'annuaire");
				}
				break;
			}

			case "+": {
				DyniSubscriber aCreer = new DyniSubscriber();
				getNamePerson(sc, aCreer);
				System.out.println("Subscriber ID?");
				aCreer.setSubscriberid(Integer.parseInt(sc.nextLine()));
				try {
					dyniAnnuaireService.create(aCreer);
				} catch (DynInvalidSubIdException e) {
					System.out.println("Subscriber Id invalid");
				}
				break;
			}

			
			case "-": {
				DyniSubscriber aSuppr = new DyniSubscriber();
				getNamePerson(sc, aSuppr);
				System.out.println("Subscriber ID?");
				aSuppr.setSubscriberid(Integer.parseInt(sc.nextLine()));
				try {
					dyniAnnuaireService.delete(aSuppr);
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
