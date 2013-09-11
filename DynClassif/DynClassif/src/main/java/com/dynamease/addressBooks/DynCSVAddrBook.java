package com.dynamease.addressBooks;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.dynamease.entity.DynCSVContact;
import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;

public class DynCSVAddrBook extends DynExternalAddressBookImpl {

	private static final String CATSEP = "[:;*]";

	private Logger logger = LoggerFactory.getLogger(DynCSVAddrBook.class);

	private List<DynCSVContact> dynCSVContacts;

	private int currentIndex;

	private Set<String> categories;

	/**
	 * Creates a new Dynamease Address Book from a CSV file Normalizes the
	 * header
	 * 
	 * @param linkToFile
	 */
	public DynCSVAddrBook(File linkToFile) {
		super();
		initCsvAddrBook(linkToFile);
	}

	// Attention : this code does not work with InputStream Reader (SuperCSV
	// problem)
	private void initCsvAddrBook(File linkToFile) {

		categories = new HashSet<String>();

		String[] csvHeader = null;
		DynHeaderNormalizer headerDico = new DynHeaderNormalizer();

		// Reading the Header. A CsvListReader object is used here as it can
		// read a variable number of columns in the first line (see
		// http://supercsv.sourceforge.net/readers.html)
		CsvListReader listReader = null;
		InputStreamReader b = null;
		try {
			b = new InputStreamReader(new BufferedInputStream(new FileInputStream(linkToFile)));
			listReader = new CsvListReader(b, CsvPreference.STANDARD_PREFERENCE);
			csvHeader = listReader.getHeader(true);
		} catch (IOException e) {
			logger.info("Did not manage to get the Csv Header", e);
			try {
				listReader.close();
			} catch (IOException e1) {
				logger.info("Problem trying to close the readers", e1);
				return;
			}

		}

		// Header Normalization
		for (int i = 0; i < csvHeader.length; i++) {
			logger.info(String.format("Element %d du header :  %s", i,
					csvHeader[i]));
			csvHeader[i] = headerDico.lookup(csvHeader[i]);
		}

		// Trace the header after Normalization
		if (logger.isDebugEnabled()) {
			logger.info("Header apres normalisation");
			for (int i = 0; i < csvHeader.length; i++) {
				if (csvHeader[i] != null) {
					logger.info(String.format("Element %d du header :  %s", i,
							csvHeader[i]));
				}
			}
		}

		// Using the CSV bean reader to read the file. Now we know the number of
		// columns
		// A CsvBeanReader object is the choice to extract easier to POJO
		// structure
		CsvBeanReader beanReader = null;

		try {
			b = new InputStreamReader(new BufferedInputStream(new FileInputStream(linkToFile)));
			beanReader = new CsvBeanReader(b, CsvPreference.STANDARD_PREFERENCE);
			// beanReader starts reading from line 2 (see above)
			// it is as if we would be reading a file without a header
			beanReader.getHeader(false);

			dynCSVContacts = extractCSVContacts(beanReader, csvHeader);

		} catch (IOException e) {
			logger.info("Did not manage to get a working CsvBeanReader.", e);
			try {
				beanReader.close();
			} catch (IOException e1) {
				logger.info("Problem trying to close the readers", e1);
			}
			return;
		}

		currentIndex = 0;

		try {
			beanReader.close();

		} catch (IOException e) {
			logger.info("Problem trying to close the readers", e);
		}

	}

	@Override
	public boolean hasNext() {
		return (currentIndex < dynCSVContacts.size());
	}

	@Override
	public Set<String> retreiveCategories() {
		return categories;
	}

	@Override
	public DynPerson getNextEntry() {

		DynPerson toReturn = (DynPerson) dynCSVContacts.get(currentIndex);
		currentIndex++;
		return toReturn;
	}

	@Override
	public int rateCurrent(DynCategories cat, DynSubscriber sub) {
		DynCSVContact current = dynCSVContacts.get(currentIndex);
		StringBuilder sb = new StringBuilder(".*");
		sb.append(cat.toString());
		sb.append(".*");
		if (current.getCategories().toUpperCase().matches(sb.toString()))
			return 100;
		else
			return 0;
	}

	private CellProcessor[] constructProcessors(String[] csvHeader) {

		// In this implementation, no mandatory field are taken into account
		// This method just returns the right cell processors array with the
		// right number

		ArrayList<CellProcessor> processorsAsList = new ArrayList<CellProcessor>(
				20);
		for (int i = 0; i < csvHeader.length; i++) {
			processorsAsList.add(new Optional());
		}

		CellProcessor result[] = new CellProcessor[processorsAsList.size()];
		return processorsAsList.toArray(result);

	}

	/**
	 * Extracts DynCSVContacts from the beanReader. Retrieves the list of
	 * categories for this address book from the categories field.
	 * 
	 * @param beanReader
	 * @return A list of DynCSVContact
	 */
	private List<DynCSVContact> extractCSVContacts(ICsvBeanReader beanReader,
			String[] csvHeader) {

		List<DynCSVContact> contacts = new ArrayList<>();
		DynCSVContact contact;
		CellProcessor[] processors = constructProcessors(csvHeader);
		try {
			while ((contact = beanReader.read(DynCSVContact.class, csvHeader,
					processors)) != null) {
				extractCategories(contact.getCategories());
				logger.info(String.format("Contact : %s %s", contact.getFirstName(), contact.getLastName()));
				contacts.add(contact);
			}
		} catch (IOException e) {
			logger.info("There seems to be a trouble with the file.", e);
		}
		return contacts;
	}

	private void extractCategories(String catStringList) {

		// Categories might be found in a single column separated
		// Extract the individual categorie namses
		String[] catSepares = catStringList.split(CATSEP);
		for (String cat : catSepares) {
			// Remove Starting and ending white spaces
			cat = cat.replaceAll("^\\s", "");
			cat = cat.replaceAll("\\s$", "");
			if (!cat.equals("")) {
				categories.add(cat);
			}
		}

	}

}
