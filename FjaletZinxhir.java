import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FjaletZinxhir {

	/*
	 * Fjalet Zinxhir, perdoret per te shkuar nga nje fjale te nje fjale tjeter,
	 * duke i nderruar vetem nga nje shkronje per secilen fjale, ku secila fjale
	 * duhet te ekzistoj ne fjalor, pra fjalet te mos jen pa kuptim apo qe nuk
	 * ekzistojn.
	 * 
	 * Si wordlist eshte perdorur lista nga linku:
	 * http://codekata.com/data/wordlist.txt
	 * 
	 */

	private List<String> fjalet;
	private List<String> kandidatet;
	private Map<String, String> shtuar;
	private int numerimi;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("First word: ");
		String firstWord = input.next();
		
		System.out.print("Second word: ");
		String secondWord = input.next();
		
		FjaletZinxhir cf = new FjaletZinxhir();
		try {
			System.out.print("Demo:  ");
			System.out.println(cf.gjejZinxhirin("cat", "dog"));
			System.out.print("Rezultati i kerkimit:  ");
			System.out.println(cf.gjejZinxhirin(firstWord, secondWord));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Lexojm skedarin dhe i shtojm fjalet, por jo te gjitha fjalet i shtojm vetem
	 * fjalet e gjatesis se njejt me fjalet qe i vendosim si parametra per te
	 * kerkuar
	 */
	private void lexoFjaletNeKeteGjatesi(int gjatesiaEFjales) throws IOException {
		// e riinicializojm vargun per cdo fjale te re
		fjalet = new ArrayList<>();

		// e lexojm skedarin dhe i shtojm fjalet e te njejtes gjatesi
		BufferedReader r = new BufferedReader(new FileReader("wordlist.txt"));
		try {
			String line;
			while ((line = r.readLine()) != null)
				if (line.length() == gjatesiaEFjales)
					fjalet.add(line);
		} finally {
			r.close();
		}
	}

	public String gjejZinxhirin(String start, String end) throws Exception {

		// kontrollojm nese fjalet nuk jane te te njejtes gjatesi
		if (start.length() != end.length())
			throw new RuntimeException("fjalet duhet te jene te te njejtes gjatesi");

		// nese fjala e pare dhe fjala destinuese jane te njejta nuk ka nevoj per kerkim
		if (start.equals(end))
			return start;

		// lexojm dhe i shtojm fjalet nga skedari ku gjinden te gjitha fjalet
		lexoFjaletNeKeteGjatesi(start.length());

		/*
		 * Kontrollojm se a e ekziston ajo fjale ne fjalor qe te mos behet kerkimi per
		 * dicka qe nuk ekziston
		 */
		if (!fjalet.contains(start)) {
			throw new Exception("Kjo fjale nuk ekziston ne fjalor");
		}
		if (!fjalet.contains(end)) {
			throw new Exception("Kjo fjale nuk ekziston ne fjalor");
		}

		// riinicializojm kandidatet e ri dhe fjalet e shtuara
		kandidatet = new LinkedList<>();
		shtuar = new HashMap<>();
		numerimi = 0;

		kandidatet.add(start);
		shtuar.put(start, null);

		/*
		 * fillojm kerkimin e fjaleve por i vendosim nje limit deri 15000 fjale, qe te
		 * mos mbetet programi duke kerkuar pafundesisht
		 */
		while (kandidatet.size() > 0) {
			if (numerimi++ > 15000)
				throw new RuntimeException("exceeded search limit");

			String c = (String) kandidatet.remove(0);

			Iterator<String> iter = fjalet.iterator();
			while (iter.hasNext()) {
				String w = (String) iter.next();
				if (!shtuar.containsKey(w) && adjacent(c, w)) {
					shtuar.put(w, c);

					if (end.equals(w)) {
						return rregulloParaqitjenEZinxhirit(w);
					}

					kandidatet.add(w);
				}
			}

		}
		return null;
	}

	/*
	 * Kontrollo dy fjale te ndryshme se a ndryshojn vetem nje shkronje ndermjet
	 * vete apo me shume
	 */
	public boolean adjacent(String a, String b) {
		int nDifferent = 0;
		int length = a.length();
		for (int i = 0; i < length; i++) {
			if (a.charAt(i) != b.charAt(i)) {
				nDifferent++;
			}
			if (nDifferent > 1)
				return false;
		}
		return true;
	}

	private String rregulloParaqitjenEZinxhirit(String word) {
		String result = word;
		word = (String) shtuar.get(word);
		while (word != null) {
			result = word + "," + result;
			word = (String) shtuar.get(word);
		}
		return result;
	}
}