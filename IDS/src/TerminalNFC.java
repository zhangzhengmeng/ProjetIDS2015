import javax.smartcardio.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by grangea
 */
public class TerminalNFC {
	
    /**
     * *************GESTION TERMINAL + TAG*****************
     */
    private CardTerminal cardTerminal = null;
    private Card cardFound = null;
    private CardChannel cardChannel = null;
    private String contentTag = "";
    private String firstname = "";
    private String lastname = "";

	/**
     * ************INTERACTION LECTEUR********************
     */
    //Message commande APDU
    //CLA   INS     P1      P2      Le
    //0xFF  0xB0    0x00    Block#  0xXX
    //P2 = numero du bloc a  lire
    private static CommandAPDU command;
    private static byte CLA = (byte) 0xFF;
    private static byte INS = (byte) 0xB0;
    private static byte P1 = (byte) 0x00;
    private static byte P2 = (byte) 0x00;
    private static byte Le = (byte) 0x00;

    //Reponse commande ADPU
    //Data + SW1 + SW2
    //SW1 = 0x90 (No error) | 0x69 (Security status not satisfied) | 0x64 (state of non volatile memory unchanged)
    //SW2 = 0x00 (No error) | 0x82 (Security status not satisfied) | 0x00 (state of non volatile memory unchanged)
    private static ResponseAPDU response;
    private static byte[] data;
    private static int SW1;
    private static int SW2;
    private static int SW1Error = 0x69;
    private static int SW2Error = 0x82;

    /**
     * Constructeur par defaut
     */
    public TerminalNFC() {
        this.cardTerminal = getTerminalConnected("SCM Microsystems Inc. SCL3711 reader & NFC device 0"); //!!!
    }

    /**
     * Verifier si le tableau de byte contient le caracta¨re terminant le contenu du tag
     *
     * @param data
     * @return vrai s'il s'agit du dernier tableau de byte contenant le message
     */
    public boolean containLastCharacter(byte[] data) {

        for (int i = 0; i < data.length; i++) {
            String bs = Integer.toHexString(data[i] & 0xFF);
            if ((data[i] & 0xFF) == 0xFE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convertir un tableau de byte en hexadecimal
     *
     * @param data
     * @return contenu en hexadecimal
     */
    public String arrayToHex(byte[] data) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < data.length; i++) {
            String bs = Integer.toHexString(data[i] & 0xFF);
            if (bs.length() == 1) {
                sb.append(0);
            }
            sb.append(bs);
        }

        return sb.toString();
    }

    /**
     * Convertir un tableau de byte en chaine de caracteres UTF8
     *
     * @param data
     * @return contenu en UTF-8
     */
    public String byteToUTF8(byte[] data) {
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Detecter la presence et reconnaitre le lecteur NFC
     *
     * @param nameTerminal
     * @return terminal NFC
     */
    public CardTerminal getTerminalConnected(String nameTerminal) {
        CardTerminals cardTerm = TerminalFactory.getDefault().terminals();
        List<CardTerminal> listTerm;
        CardTerminal terminalFound;

        try {
            listTerm = cardTerm.list();
        } catch (Exception e) {
            System.out.println("Erreur liste terminaux: " + e.toString());
            return null;
        }

        System.out.print("[Info : Liste des lecteurs PC/SC connectes disponibles :");
        ListIterator i = listTerm.listIterator();
        while (i.hasNext()) {
            System.out.print(((CardTerminal) i.next()).getName());
        }
        System.out.println("]");
        terminalFound = cardTerm.getTerminal(nameTerminal);
        return terminalFound;
    }

    /**
     * Envoyer une commande APDU, traiter la reponse et enregistrer le contenu
     *
     * @throws javax.smartcardio.CardException
     */
    public void handleContentTag() throws CardException {
        contentTag = "";
        firstname = "";
        lastname = "";
        int blockNb = 7;
        //Lecture 0x04 bytes par bloc

        do {
            //Envoi de la commande--------------------
            P2 = (byte) blockNb++;
            command = new CommandAPDU(new byte[]{CLA, INS, P1, P2, Le});
            //----------------------------------------

            //Reception de la reponse-----------------
            response = cardChannel.transmit(command);
            data = response.getData();
            SW1 = response.getSW1();
            SW2 = response.getSW2();
            //----------------------------------------

            //Enregistrement contenu tag--------------
            //System.out.println(arrayToHex(data) + " | " + byteToUTF8(data));
            this.contentTag += byteToUTF8(data);
            if (containLastCharacter(data)) {
                //dernier bloc lu
            	if(!contentTag.contains(",")){
            		//erreur contenu
            		firstname = "unknown";
            		lastname = "unknown";
            		break;
            	}
                contentTag = contentTag.substring(2, contentTag.length() - 1);
                firstname = contentTag.split(",")[0];
                lastname = contentTag.split(",")[1];
                System.out.println(firstname);
                System.out.println(lastname);
               break;
            }
            //----------------------------------------
        } while (SW1 != SW1Error && SW2 != SW2Error);
    }

    /**
     * Envoyer au serveur le contenu du tag (prenom/nom)
     */
    public void sendMsgServer() {
        //url => /attendance/put/prenom/NOM
        String USER_AGENT = "Mozilla/5.0";
        String address = "http://localhost:8082/attendance/put/" + firstname + "/" + lastname.toUpperCase();
        String inputLine;
        int response;
        StringBuffer responseBuffer = new StringBuffer();
        BufferedReader in;
        HttpURLConnection con;

        try {
            URL url = new URL(address);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            try {
                response = con.getResponseCode();
            } catch (UnknownHostException u) {
                System.out.println("[Info : Adresse URL introuvable]");
                response = 0;
            } catch (ConnectException ce) {
                response = 0;
                System.out.println("[Info : Connexion refusee]");
            }

            if (response == 200) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    responseBuffer.append(inputLine);
                }
                in.close();
                if (responseBuffer.toString().contains("ok")) {
                    System.out.println("=> Ok! " + firstname + " " + lastname + " est desormais enregistre!");
                } else {
                    System.out.println("=> Echec! " + firstname + " " + lastname + " est inconnu");
                }
            } else {
                System.out.println("[Info : Echec reponse serveur]");
                System.out.println("=> Echec! " + firstname + " " + lastname + " n'a pas ete enregistre");
            }
        } catch (IOException e) {
            System.out.println("[Info : Erreur lors de l'envoi au serveur]" + e.toString());
            System.out.println("=> Echec! " + firstname + " " + lastname + " n'a pas ete enregistre");
        }
    }

    public static void lecture() {
        TerminalNFC terminal;
        String protocol = "T=0"; //T=1
        CardTerminal card;


            System.out.println("Option 1 selectionnee");
            terminal = new TerminalNFC();
            if (terminal.cardTerminal == null) {
                System.out.println("[Info : Aucune cle NFC n'a ete trouvee]");
            } else {
                try {

                        System.out.println("Deposez votre tag NFC...");
                        do {
                            card = terminal.cardTerminal;
                        } while (!card.isCardPresent());
                        
                        while (card.isCardPresent()) {
                            try {
                                terminal.cardFound = card.connect(protocol);
                                System.out.println("[Info : Carte NFC trouve]");
                            } catch (Exception e) {
                                System.out.println("{Info : Aucune carte NFC trouve : " + e.toString() + "]");
                            }
                            terminal.cardChannel = terminal.cardFound.getBasicChannel();
                            //lire et traiter le contenu du tag a lire
                            terminal.handleContentTag();
                            System.out.println("---------------------------------------------------------");
                            System.out.println("=> Vous etes " + terminal.contentTag);
                            Main.code=terminal.contentTag;
                            Main.fini=true;

                            /*if (terminal.contentTag != "" && terminal.firstname != "" && terminal.lastname != "") {
                                //transmettre au serveur les infos du tag
                                terminal.sendMsgServer();
                            }*/
                            System.out.println("---------------------------------------------------------");
                            break;
                        }
                        System.out.println("Merci de patienter quelques secondes");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                } catch (CardException e) {
                    System.out.println("[Info : Erreur tag :" + e.toString() + "]");
                }
            }

    }
}
