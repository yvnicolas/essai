package com.dynamease.ldapBas;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Hello world!
 * 
 */
@Service
public class App {

	@Autowired
	private static DyniAnnuaireService dyniAnnuaireService;

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("file:src/main/resources/ldapbascontext.xml");
		App App = context.getBean(App.class);
		App.start();
	
	}
	
		void start(){
		Scanner sc = new Scanner(System.in);
		String prenom = "";
		String nom = "";
		String encore = "";

		System.out.println("Salut les aminches!");

		do {
			System.out.print("prenom?");
			prenom = sc.nextLine();
			System.out.print("Nom?");
			nom = sc.nextLine();
			if (dyniAnnuaireService.existsAsSubscriber(new DyniPerson(prenom, nom))) {
				System.out.println("Present dans l'annuaire");
			} else {
				System.out.println("Non present dans l'annuaire");
			}
			System.out.print("Autre recherche (o/n)?");
			encore = sc.nextLine();
		} while (encore.equals("o"));

		System.out.println("Bye!");
		sc.close();

	}
}
