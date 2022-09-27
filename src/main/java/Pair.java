import java.util.*;

public class Pair {
	private int pair_id;  // sentence id (offset at 1)
	private Sentence esent;
	private Sentence csent;
	Vector  alignment = new Vector();
	Vector  user1 = new Vector();
	Vector  user2 = new Vector();
	Vector  user3 = new Vector();
	String  enc;

	public Pair(Sentence e, Sentence c, int i, String enc) {
		this.enc   = enc;
		esent = e;
		csent = c;
		pair_id = i;
	}
	
	public Sentence getEngSent() {
		return esent;
	}

	public Sentence getChSent() {
		return csent;
	}

	public int getID() {
		return pair_id;
	}

	private int find_word_in_fls(Word w, Vector v) {
		for (int i=0; i<v.size(); i++) {
			FauxLS fls = (FauxLS)v.elementAt(i);
			if ((fls.eConnect.indexOf(w) != -1) ||
				(fls.cConnect.indexOf(w) != -1))
				return i;
		}
		return -1;
	}

	private int find_fls(FauxLS fls1, Vector v) {
		for (int i=0; i<v.size(); i++) {
			FauxLS fls2 = (FauxLS)v.elementAt(i);
			if (fls1.is_same(fls2))
				return i;
		}
		return -1;
	}

	public void addLinkSet (LinkSet ls) {
		// if ls isn't already in alignment, add it to the list
		if ((!ls.is_empty()) && (alignment.indexOf(ls) == -1))
			alignment.addElement(ls);

		// need to maintain the user lists:
		// if the words in ls appears in either user1 or user2, remove that element
		for (Iterator i=ls.getLinks().iterator(); i.hasNext();) {
			Link  l = (Link)i.next();
			int id1 = find_word_in_fls(l.eng, user1);
			if (id1 != -1)
				user1.removeElementAt(id1);
			int id2 = find_word_in_fls(l.eng, user2);
			if (id2 != -1)
				user2.removeElementAt(id2);
		}
	}

	public void addFauxLS (FauxLS l, Vector v) {
		// if l isn't already in v: {alignment, user1, user2}
		// add it to the list
		if ((!l.is_empty()) && (v.indexOf(l) == -1))
			v.addElement(l);
	}
}