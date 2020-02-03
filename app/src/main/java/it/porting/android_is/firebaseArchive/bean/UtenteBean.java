package it.porting.android_is.firebaseArchive.bean;

public class UtenteBean {

    public UtenteBean(){

    }

    private String nome;
    private String cognome;
    private String sesso;
    private String ruolo;
    private String password;
    private int cfu;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public void setRuolo(String ruolo) { this.ruolo = ruolo; }

    public void setCfu(int cfu) { this.cfu = cfu; }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getSesso() {
        return sesso;
    }

    public String getRuolo() { return ruolo; }

    public int getCfu() { return cfu; }
}
