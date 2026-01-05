package Server;

import Posta.PostaElettronica;
import Posta.Request;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ThreadConnection extends Thread {
    Socket socket;

    public ThreadConnection(Socket s) {
        this.socket = s;
    }

    ModelServer m = new ModelServer();

    @Override
    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            PostaElettronica posta = null;
            while (true) {
                try {
                    Request r = (Request) inputStream.readObject();
                    switch (r.getRequest()) {
                        case "Login":
                            posta = m.identificatore((String) r.getObj());
                            outputStream.writeObject(posta);
                            break;
                        case "LeggiEmail":
                            ArrayList<PostaElettronica.Email> p = m.aggiorna((PostaElettronica) r.getObj());
                            outputStream.writeObject(p);
                            outputStream.flush();
                            break;
                        case "CLOSE":
                            outputStream.close();
                            inputStream.close();
                            socket.close();
                            break;
                        case "InviaEmail":
                            PostaElettronica.Email postaInviata = (PostaElettronica.Email) r.getObj();
                            m.inviaEmail(postaInviata);
                            break;
                        case "Cancellazione":
                            PostaElettronica.Email emailRicevuta = (PostaElettronica.Email) r.getObj();
                            m.cancellaEmail(emailRicevuta);

                    }


                } catch (SocketException e) {
                    if (posta == null) {
                        m.setLog(m.getLog() + "\n[" + m.getDataLog() + "] Un client si è disconnesso prima di accedere");
                    } else {
                        m.setPostaOnline(posta.getNomeP());
                        m.logProperty().set(m.logProperty().getValue() + "\n[" + m.getDataLog() + "] Il client: " + posta.getNomeP() + " si è disconnesso");
                        socket.close();

                    }
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
