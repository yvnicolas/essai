package com.dynamease.services.impl;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.junit.Test;

public class DynDirectoryTest {

	static DynDirectory d = null;

	@Test
	public void testDynDirectory() {

		System.out.println(">>>>>>>>  Initiatisation répertoire");
		try {
			d = new DynDirectory();
		} catch (Exception e) {

			fail(e.getMessage());
		}
		assertNotNull("Null Directory", d);

	}

	/**
	 * Boucle sur les noms presents dans nomFichier pour verifier s'ils sont
	 * dans le repertoire
	 * 
	 * @param nomFichier
	 * @param attendu
	 *            : pour dire si on s'attend a trouver les noms ou pas
	 * @return true si la presence des noms dans le répertoire est conforme à
	 *         attendu
	 */
	private boolean nameLookup(String nomFichier, boolean attendu) {

		Scanner sc = null;
		String nomRecherche = "";
		boolean result = true;

		URL url = this.getClass().getResource("/" + nomFichier);
		try {
			sc = new Scanner(new FileInputStream(url.getFile()));
		} catch (FileNotFoundException e) {
			System.out.println(">>>> pb ouverture fichier" + nomFichier);
			return false;
		}

		while (sc.hasNext()) {

			nomRecherche = sc.nextLine();
			if (d.nameLookup(nomRecherche) == attendu) {
				System.out.println(">>> Nom " + nomRecherche + ": OK");
			} else {
				System.out.println(">>> Nom " + nomRecherche + ": NOK");
				result = false;
			}

		}
		sc.close();
		return result;
	}

	@Test
	public void testExistingnameLookup() {

		String nomFichierExistants = "presents.txt";
		System.out.println(">>>>>>>>  Test de presence des Nom existants");
		assertTrue(this.nameLookup(nomFichierExistants, true));
	}

	@Test
	public void testNonExistingnameLookup() {

		String nomFichierNonExistants = "absents.txt";
		System.out.println(">>>>>>>>  Test d'absence des Nom non existants");
		assertTrue(this.nameLookup(nomFichierNonExistants, false));
	}

}
