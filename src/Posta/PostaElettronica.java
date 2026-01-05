package Posta;

import Posta.Property.PostaElettronicaProperty;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class PostaElettronica implements Serializable {
    private String nomeP;
    private ArrayList<Email> email;
    private boolean available = true;
    private static final long serialVersionUID = 5950169519310163575L;

    public PostaElettronica(String nomeP, ArrayList<Email> email) {
        this.nomeP = nomeP;
        this.email=email;
    }

    public ArrayList<Email> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<Email> email) {
        this.email = email;
    }

    public String getNomeP() {
        return nomeP;
    }

    public void setNomeP(String nomeP) {
        nomeP = nomeP;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }



    public static class Email implements Serializable {
        private static final long serialVersionUID = 5950169519310163575L;
        private String id;
        private String mittente;
        private ArrayList<String> destinatari;
        private String argomento;
        private String testo;
        private Date data;

        public Email(String id, String mittente, ArrayList<String> destinatari, String argomento, String testo, Date data) {
            this.id = id;
            this.mittente = mittente;
            this.destinatari = destinatari;
            this.argomento = argomento;
            this.testo = testo;
            this.data = data;
        }

        public Email(PostaElettronicaProperty.EmailProperty e) {
           setId(e.getId());
            setMittente(e.getMittente());
          //  this.destinatari.add(e.getDestinatari().get(0).getValue());
            setArgomento(getArgomento());
            setTesto(getTesto());

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMittente() {
            return mittente;
        }

        public void setMittente(String mittente) {
            this.mittente = mittente;
        }

        public ArrayList<String> getDestinatari() {
            return destinatari;
        }

        public void setDestinatari(ArrayList<String> destinatari) {
            this.destinatari = destinatari;
        }

        public String getArgomento() {
            return argomento;
        }

        public void setArgomento(String argomento) {
            this.argomento = argomento;
        }

        public String getTesto() {
            return testo;
        }

        public void setTesto(String testo) {
            this.testo = testo;
        }

        public Date getData() {
            return data;
        }

        public void setData(Date data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Email{" +
                    "id='" + id + '\'' +
                    ", mittente='" + mittente + '\'' +
                    ", destinatari=" + destinatari +
                    ", argomento='" + argomento + '\'' +
                    ", testo='" + testo + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
}
