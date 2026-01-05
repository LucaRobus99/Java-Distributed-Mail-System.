package Posta.Property;

import Posta.PostaElettronica;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PostaElettronicaProperty implements Serializable {

    //NomePosta
    private final StringProperty nomeP = new SimpleStringProperty();

    public final StringProperty nomePProperty() {
        return this.nomeP;
    }

    public final String getnomeP() {
        return this.nomePProperty().get();
    }

    public final void setnomeP(final String nomeP) {
        this.nomePProperty().set(nomeP);
    }

    //ArrayDiEmail
    private final ObservableList<PostaElettronica.Email> email = new SimpleListProperty<>();

    public final List<PostaElettronica.Email> emailProperty() { return this.email; }

    public final List<PostaElettronica.Email> getEmail(int i) { return (List<PostaElettronica.Email>) this.emailProperty().get(i); }


    public final List<PostaElettronica.Email> setEmail(PostaElettronica.Email e, int i) { return (List<PostaElettronica.Email>) this.emailProperty().set(i, e); }



    public static class EmailProperty implements Serializable{

    //id
    private final StringProperty id = new SimpleStringProperty();

    public final StringProperty idProperty() {
        return this.id;
    }

    public final String getId() {
        return this.idProperty().get();
    }

    public final void setId(final String id) {
        this.idProperty().set(id);
    }
    //mittente

    private final StringProperty mittente = new SimpleStringProperty();

    public final StringProperty mittenteProperty() {
        return this.mittente;
    }

    public final String getMittente() {
        return this.mittenteProperty().get();
    }

    public final void setMittente(final String mittente) {
        this.mittenteProperty().set(mittente);
    }

    //destinatari
    private final ListProperty<SimpleStringProperty> destintatri = new SimpleListProperty<>();


    public final ListProperty<SimpleStringProperty> destinatariProperty() {
        return this.destintatri;
    }

    public final ListProperty<SimpleStringProperty> getDestinatari() {
        return  this.destinatariProperty();
    }


    public final void setDestintatri(final ArrayList<String> destinatarip) {
        ObservableList<SimpleStringProperty> destOB = FXCollections.observableArrayList();
        for(int i=0;i<destinatarip.size();i++){
            SimpleStringProperty d=new SimpleStringProperty();
            d.set(destinatarip.get(i));
            destOB.add(d);
            this.destinatariProperty().set(destOB);
        }
    }
       /* private final SimpleStringProperty destintatri1 = new SimpleStringProperty();


        public final SimpleStringProperty destinatariProperty1() {
            return this.destintatri1;
        }

        public final SimpleStringProperty getDestinatari1() {
            return  this.destinatariProperty1();
        }


        public final void setDestintatri1 (final String destinatari) {
            this.destinatariProperty1().set(destinatari);
      }*/


    //argomento
    private final StringProperty argomento = new SimpleStringProperty();

    public final StringProperty argomentoProperty() {
        return this.argomento;
    }

    public final String getArgomento() {
        return this.argomentoProperty().get();
    }

    public final void setArgomento(final String argomento) {
        this.argomentoProperty().set(argomento);
    }
    //testo

    private final StringProperty testo = new SimpleStringProperty();

    public final StringProperty testoProperty() {
        return this.testo;
    }

    public final String getTesto() {
        return this.testoProperty().get();
    }

    public final void setTesto(final String testo) {
        this.testoProperty().set(testo);
    }

    //data

    private final SimpleStringProperty data = new SimpleStringProperty();

    public final SimpleStringProperty dataProperty() {

        return this.data;
    }

    public final String getData() { ;

        return this.dataProperty().get();
    }

    public final void setData(final Date data) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strDate = dateFormat.format(data);
        this.dataProperty().set(strDate);
    }


    public EmailProperty(String id, String mittente, ArrayList destinatari, String argomento, String testo, Date data) {
        setId(id);
        setMittente(mittente);
        setDestintatri(destinatari);
        setArgomento(argomento);
        setTesto(testo);
        setData(data);
    }
    public EmailProperty(PostaElettronica.Email e) {
        setId(e.getId());
        setMittente(e.getMittente());
        setDestintatri(e.getDestinatari());
        setArgomento(e.getArgomento());
        setTesto(e.getTesto());
        setData(e.getData());
    }
}

}

