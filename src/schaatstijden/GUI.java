package schaatstijden;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import javax.swing.JToggleButton;
import java.awt.Choice;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.regex.Pattern;
import java.awt.TextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class GUI {

	private JFrame frame;
	private JTextField begindatum;
	private JTextField einddatum;
	private Choice afstand;
	private JButton btnImporteerTijden;
	private JCheckBox chckbxTussenvoegsel;
	private JLabel lblError;
	private TextArea textArea;
	private Schaatstijden schaatstijden;
	private JTextField txtMan;
	private JTextField txtVrouw;
	private JCheckBox chckbxSorteerOpTijd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		schaatstijden = new Schaatstijden(this);
		frame = new JFrame();
		frame.setBounds(100, 100, 526, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new TextArea();
		textArea.setBounds(33, 182, 443, 369);
		frame.getContentPane().add(textArea);
		
		afstand = new Choice();
		afstand.setBounds(139, 10, 156, 20);
		afstand.addItem("500 meter");
		afstand.addItem("1000 meter");
		afstand.addItem("1500 meter");
		afstand.addItem("3000 meter");
		afstand.addItem("5000 meter");
		afstand.addItem("10000 meter");	
		frame.getContentPane().add(afstand);
		
		begindatum = new JTextField();
		begindatum.setText("jjjj-mm-dd");
		begindatum.setBounds(139, 42, 156, 20);
		frame.getContentPane().add(begindatum);
		begindatum.setColumns(10);
		
		einddatum = new JTextField();
		einddatum.setText("jjjj-mm-dd");
		einddatum.setBounds(139, 73, 156, 20);
		frame.getContentPane().add(einddatum);
		einddatum.setColumns(10);
		
		btnImporteerTijden = new JButton("Importeer Tijden");
		btnImporteerTijden.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				importeerTijden();
			}
		});
		btnImporteerTijden.setBounds(139, 128, 156, 23);
		frame.getContentPane().add(btnImporteerTijden);
		
		JLabel lblAfstand = new JLabel("Afstand");
		lblAfstand.setBounds(48, 16, 46, 14);
		frame.getContentPane().add(lblAfstand);
		
		JLabel lblBegindatum = new JLabel("Begindatum");
		lblBegindatum.setBounds(48, 45, 106, 14);
		frame.getContentPane().add(lblBegindatum);
		
		JLabel lblEinddatum = new JLabel("Einddatum");
		lblEinddatum.setBounds(48, 76, 106, 14);
		frame.getContentPane().add(lblEinddatum);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setBounds(27, 162, 380, 14);
		frame.getContentPane().add(lblError);
		
		chckbxTussenvoegsel = new JCheckBox("Tussenvoegsel");
		chckbxTussenvoegsel.setBounds(139, 98, 156, 23);
		frame.getContentPane().add(chckbxTussenvoegsel);
		
		JLabel lblMan = new JLabel("Man");
		lblMan.setBounds(328, 16, 46, 14);
		frame.getContentPane().add(lblMan);
		
		JLabel lblVrouw = new JLabel("Vrouw");
		lblVrouw.setBounds(328, 45, 46, 14);
		frame.getContentPane().add(lblVrouw);
		
		chckbxSorteerOpTijd = new JCheckBox("Sorteer op tijd");
		chckbxSorteerOpTijd.setBounds(328, 72, 142, 23);
		frame.getContentPane().add(chckbxSorteerOpTijd);
		
		txtMan = new JTextField();
		txtMan.setText("Man");
		txtMan.setBounds(384, 13, 86, 20);
		frame.getContentPane().add(txtMan);
		txtMan.setColumns(10);
		
		txtVrouw = new JTextField();
		txtVrouw.setText("Vrouw");
		txtVrouw.setBounds(384, 42, 86, 20);
		frame.getContentPane().add(txtVrouw);
		txtVrouw.setColumns(10);
	}
	
	public void setFieldsEnabled(boolean set){
		afstand.setEnabled(set);
		begindatum.setEnabled(set);
		einddatum.setEnabled(set);
		chckbxTussenvoegsel.setEnabled(set);
		btnImporteerTijden.setEnabled(set);
		textArea.setEnabled(set);
		txtMan.setEnabled(set);
		txtVrouw.setEnabled(set);
		chckbxSorteerOpTijd.setEnabled(set);
	}
	
	public void showMessage(String message){
		lblError.setText(message);
		lblError.repaint();
	}

	public void importeerTijden(){
		frame.repaint();
		lblError.setText("");
		System.out.println("importing");
		setFieldsEnabled(false);
		if(!Pattern.matches(Constants.DATUM_FORMAT, begindatum.getText())){
			lblError.setText("Begindatum moet format jjjj-mm-dd hebben");
			setFieldsEnabled(true);
		} else if(!Pattern.matches(Constants.DATUM_FORMAT, einddatum.getText())){
			lblError.setText("Einddatum moet format jjjj-mm-dd hebben");
			setFieldsEnabled(true);
		} else if(txtMan.getText().equals("")){
			lblError.setText("Geef aanduiding voor man");
		} else if(txtVrouw.getText().equals("")){
			lblError.setText("Geef aanduiding voor vrouw");
		} else {
			schaatstijden.setAfstand(Integer.parseInt(afstand.getSelectedItem().substring(0, afstand.getSelectedItem().length()-6)));
			schaatstijden.setBegindatum(begindatum.getText());
			schaatstijden.setEinddatum(einddatum.getText());
			schaatstijden.setTussenvoegsel(chckbxTussenvoegsel.isSelected());
			schaatstijden.setSorteer(chckbxSorteerOpTijd.isSelected());
			Constants.MAN_TEKEN=txtMan.getText();
			Constants.VROUW_TEKEN=txtVrouw.getText();
			BufferedReader br = new BufferedReader(new StringReader(textArea.getText()));
			schaatstijden.setBr(br);
			schaatstijden.start();
		}
	}
	
	public void setResults(String results){
		textArea.setText(results);
		System.out.println(results);
		textArea.setEnabled(true);
		textArea.setCaretPosition(0);
		showMessage("");
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
	}
}
