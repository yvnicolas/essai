import java.util.Scanner;

import com.dynamease.repository.ldap.DynDirectoryRepositoryService;
import com.dynamease.repository.ldap.DynDirectoryRepositoryServiceImpl;

public class EmbeddedDSTest {

	/**
	 * Main class.
	 * 
	 * @param args
	 *            Not used.
	 */

	public static void main(String[] args) {
		try {

			// Create the server
			DynDirectoryRepositoryService ads = new DynDirectoryRepositoryServiceImpl();

			// play with starting and stopping the server
			System.out.println("Salut les aminches!");
			Scanner sc = new Scanner(System.in);
			String commande;

			do {
				System.out.print("Commande(start, stop, info ou quit)    ?   ");
				commande = sc.nextLine();

				switch (commande) {
				case "quit":
					break;
				case "start":
					try {
						ads.startFromScratch();
						System.out.println("Serveur demarre");
					} catch (Exception e) {
						System.out.println("Le serveur ne peut pas demarrer");
						e.printStackTrace();
					}
					break;
				case "stop":
					try {
						ads.stop();
						System.out.println("Serveur arrete");
					} catch (Exception e) {
						System.out.println("Le serveur ne peut pas s'arreter");
						e.printStackTrace();
					}
					break;
				case "info":

					// Prints the status of what is found
					System.out.println("Directory service status : ");
					System.out.println("---------------------------");
					System.out.println(ads.info());

					break;
				default:
					break;
				}

			} while (!(commande.equals("quit")));

			sc.close();
			System.out.println("Salut on se casse!");
		} catch (Exception e) {
			// Ok, we have something wrong going on ...
			e.printStackTrace();
		}
	}

}
