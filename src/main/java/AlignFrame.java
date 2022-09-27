import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.*;

public class AlignFrame extends JFrame{
    
	public AlignFrame(Bitext b, String enc, String alignmentPath) {
		super("Alignment UI");
		setSize(1000, 900);

		AlignMainContent main_pane = new AlignMainContent(b, enc);
		getContentPane().add(main_pane.createComponents());

		setDefaultCloseOperation(AlignFrame.DO_NOTHING_ON_CLOSE);
                this.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent event) {
                        if( b.getUpdated() ){
                            System.out.println("Bitext has changed, writing alignments...");
                            try {
                                b.writePairs(alignmentPath);
                            } catch (FileNotFoundException ex) {
                                System.out.println("ERROR writing alignments: "+ex);
                            }
                        }else{
                            System.out.println("Bitext has not changed.");
                        }
                        System.exit(0);
                    }
                });
                
		pack();
		setVisible(true);
	}

	public static void main(String argv[]) throws IOException, ClassNotFoundException {

		if (argv.length < 3 ) {
		    System.out.println("usage -- java -jar align_ui.jar <enfile> <frfile> <alignments> [encoding]");
		    System.exit(0);
		}

		// get the encoding if provided otherwise use UTF-8 as the default encoding
                String encoding;
		if (argv.length == 4) {
			encoding = argv[3];
		}
		else {
			encoding = "UTF-8";
		}

		// data structure for holding parallel sentences
		Bitext b = new Bitext(encoding);

		// read all the sentences into the Bitext (parallel vectors) structure
		ReadInput ef = new ReadInput(argv[0], encoding);
		ReadInput cf = new ReadInput(argv[1], encoding);
                ReadInput af;
                try{
                    af = new ReadInput(argv[2], encoding);
                }catch( FileNotFoundException ex){
                    af = null;
                }
                
                Sentence es, cs;
                int id = 1;
		while (((es = ef.readSent(true))!=null) &&
			   ((cs = cf.readSent(false))!=null)) {
                    Pair p = new Pair(es, cs, id++, b.enc);
                    if( af != null ){
                        af.readLinkSets(p, b);
                    }
                    b.add_sent_pair(p);
		}

                ef.closeFile();
                cf.closeFile();
                if( af != null ){
                    af.closeFile();
                }
                
                new AlignFrame(b, encoding, argv[2]);
	}
}


