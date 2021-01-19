import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor  {
        public static void main(String[] args) {		
            MarcoServidor mimarco = new MarcoServidor();		
            mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
        }	
}


class MarcoServidor extends JFrame implements Runnable {	    
        private JTextArea areatexto;
    
    
        public MarcoServidor(){		
		setBounds(800,300,400,350);				
			
		JPanel milamina = new JPanel();		
		milamina.setLayout(new BorderLayout());		
		areatexto = new JTextArea();		
		milamina.add(areatexto,BorderLayout.CENTER);		
		add(milamina);
		
		setVisible(true);
                
                //creación del hilo para mantenerlo a la escucha
                Thread mihilo = new Thread(this);
                mihilo.start();
		}	


        @Override
        public void run() {
                try {
                    ServerSocket serverE = new ServerSocket(4000); 
                    
                    String nick, ip, mensaje;
                    PaqueteDatos paqueteEntrante;
                    
                    while(true){
                            Socket miSocketE = serverE.accept();            
                            ObjectInputStream flujoEntrada = new ObjectInputStream(miSocketE.getInputStream());
                            
                            paqueteEntrante = (PaqueteDatos) flujoEntrada.readObject();
                            nick = paqueteEntrante.getNick();
                            ip = paqueteEntrante.getIp();
                            mensaje = paqueteEntrante.getMensaje();
                            
                            areatexto.append("\n" + nick + ": " + mensaje + ". Para: " + ip);
                                                        
                                Socket miSocketS = new Socket(ip, 3500);
                                ObjectOutputStream flujoSalida = new ObjectOutputStream(miSocketS.getOutputStream());
                                flujoSalida.writeObject(paqueteEntrante);
                                flujoSalida.close();
                                
                                miSocketS.close();
                            
                            miSocketE.close();                            
                            
                    }
                    
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                } 
        }
}