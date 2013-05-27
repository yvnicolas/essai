import java.io.FileReader;
import java.util.Scanner;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.unboundid.ldap.sdk.SearchScope;


public class DynCsvBacASable {

	/**
	 * @param args
	 */
	
private static CellProcessor[] getProcessors() {
        
       
        final CellProcessor[] processors = new CellProcessor[] { 
                new NotNull(), // firstName
                new NotNull(), // lastName
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional()
                
        };
        
        return processors;
	}
	public static void main(String[] args) throws Exception {
		DynDirectory d = new DynDirectory();
		Scanner sc = new Scanner(System.in);
		String nomRecherche ="";
		String encore = "";
		do {
			System.out.print("Nom a chercher");
			nomRecherche = sc.nextLine();
			if (d.ExisteNom(nomRecherche)){
				System.out.println("Je le connais!");
			}
			else {
				System.out.println("Je ne le connais pas...");
			}
			System.out.print("Autre recherche (o/n)?");
			encore = sc.nextLine();
		} while (encore.equals("o"));
		
		System.out.println("Bye!");
		sc.close();
		
	}

	public static void TestBeanReader(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		String nomFichier = "";
  
        ICsvBeanReader beanReader = null;
        System.out.println("Salut les aminches, Bienvenue dans la lecture CSV");
		System.out.print("Nom du fichier");
		nomFichier = sc.nextLine();
        try {
                beanReader = new CsvBeanReader(new FileReader(nomFichier), CsvPreference.EXCEL_PREFERENCE);
                
                // the header elements are used to map the values to the bean (names must match)
                final String[] header = beanReader.getHeader(true);
                final CellProcessor[] processors = getProcessors();
                
                DynPersonBean customer;
                while( (customer = beanReader.read(DynPersonBean.class, header, processors)) != null ) {
                        System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(),
                                beanReader.getRowNumber(), customer));
                }
                
        }
        finally {
                if( beanReader != null ) {
                        beanReader.close();
                }
        }
        sc.close();
        
}
	}


