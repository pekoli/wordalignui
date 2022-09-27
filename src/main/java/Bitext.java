import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Bitext extends Observable {

    private int num_sents;
    private int cur_sentID;
    private final List<Pair> pairs = new ArrayList<>();
    String enc;
    private boolean updated = false;
    LinkSet active_linkset;

    public Bitext(String e) {
        enc = e;
        num_sents = 0;
        cur_sentID = 1;
        active_linkset = new LinkSet(this);  // init 
    }

    public void add_sent_pair(Pair p) {
        pairs.add(p);
        num_sents++;
    }

    public int size() {
        return num_sents;
    }

    public Pair getPair() {
        return pairs.get(cur_sentID - 1);
    }
    
    public List<Pair> getPairs() {
        return pairs;
    }

    public Sentence getEngSent() {
        return getPair().getEngSent();
    }

    public Sentence getChSent() {
        return getPair().getChSent();
    }

    public String getEngStr() {
        return (getEngSent().getStr());
    }

    public String getChStr() {
        return (getChSent().getStr());
    }

    public int getSentID() {
        return cur_sentID;
    }

    public void setSentID(int i) {
        cur_sentID = i;
        changed();
    }

    public void newLinkSet() {
        active_linkset = new LinkSet(this);
        changed();
    }

    public void resetLinkSet() {
        active_linkset.reset();
        changed();
    }

    public void setLinkSet(LinkSet l) {
        active_linkset = l;
        changed();
    }

    public void changed() {
        setChanged();
        notifyObservers();
    }
    
    public boolean getUpdated(){
        return updated;
    }
    
    public void setUpdated(){
        updated = true;
    }

    public void writePairs(String alignmentPath) throws FileNotFoundException {
        FileOutputStream stream = new FileOutputStream(alignmentPath);
        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(stream, enc);
        } catch (UnsupportedEncodingException e) {
            System.out.println("encoding not supported");
            writer = new OutputStreamWriter(stream);
        }
        PrintWriter printer = new PrintWriter(writer);
            
        for (Pair p : pairs) {
            int e = 0;
            while (e < p.alignment.size()) {
                LinkSet linkset = (LinkSet) p.alignment.elementAt(e);
                if (linkset.is_empty()) {
                    p.alignment.removeElementAt(e);
                } else {
                    for (Iterator i = linkset.getLinks().iterator(); i.hasNext();) {
                        Link l = (Link) i.next();
                        if( e > 0 ){
                            printer.print(" ");
                        }
                        printer.print(l.eng.getPosition() + "-" + l.ch.getPosition());
                    }   
                    e++;
                }
            }
            printer.println();
        }
        printer.close();
    }
}
