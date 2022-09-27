import java.io.*;

public class ReadInput {

    private final BufferedReader bReader;

    public ReadInput(String fname, String enc) throws IOException, UnsupportedEncodingException {
        bReader = new BufferedReader(new InputStreamReader(new FileInputStream(fname), enc));
    }

    public void closeFile() throws IOException {
        bReader.close();
    }

    public Sentence readSent(boolean bEng) throws IOException {
        Sentence s = null;
        String line;
        while ((line = bReader.readLine()) != null) {
            String[] words = line.trim().split("\\s+");
            s = new Sentence(bEng);
            for (String word : words) {
                s.addWord(word);
            }
            break;
        }
        if (s == null) {
            closeFile();
        }
        return s;
    }

    public void readLinkSets(Pair p, Bitext b) throws IOException {
        
        String line;
        while ((line = bReader.readLine()) != null) {
            String[] alignments = line.trim().split("\\s+");
            for( String alignment : alignments ){
                LinkSet ls = new LinkSet(b);
                String[] indices = alignment.split("-");
                if( indices.length > 1 ){
                    int ie = Integer.parseInt(indices[0]);
                    int ic = Integer.parseInt(indices[1]);
                    Word we = p.getEngSent().getWordAt(ie);
                    Word wc = p.getChSent().getWordAt(ic);
                    if (ls.eConnect.indexOf(we) == -1) {
                        ls.addWord(we);
                    }
                    if (ls.cConnect.indexOf(wc) == -1) {
                        ls.addWord(wc);
                    }
                    p.addLinkSet(ls);
                }
            }
            break;
        }
    }
}
