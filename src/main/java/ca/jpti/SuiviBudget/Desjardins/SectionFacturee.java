
package ca.jpti.SuiviBudget.Desjardins;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nbrMoisRecherches",
    "indicateurLimiteListeAtteinte",
    "dateDebutPeriode",
    "dateFinPeriode",
    "transactionListe",
    "messages"
})
@Generated("jsonschema2pojo")
public class SectionFacturee {

    @JsonProperty("nbrMoisRecherches")
    private Integer nbrMoisRecherches;
    @JsonProperty("indicateurLimiteListeAtteinte")
    private Boolean indicateurLimiteListeAtteinte;
    @JsonProperty("dateDebutPeriode")
    private String dateDebutPeriode;
    @JsonProperty("dateFinPeriode")
    private String dateFinPeriode;
    @JsonProperty("transactionListe")
    private List<TransactionFacturee> transactionListe = null;
    @JsonProperty("messages")
    private List<Object> messages = null;

    @JsonProperty("nbrMoisRecherches")
    public Integer getNbrMoisRecherches() {
        return nbrMoisRecherches;
    }

    @JsonProperty("nbrMoisRecherches")
    public void setNbrMoisRecherches(Integer nbrMoisRecherches) {
        this.nbrMoisRecherches = nbrMoisRecherches;
    }

    @JsonProperty("indicateurLimiteListeAtteinte")
    public Boolean getIndicateurLimiteListeAtteinte() {
        return indicateurLimiteListeAtteinte;
    }

    @JsonProperty("indicateurLimiteListeAtteinte")
    public void setIndicateurLimiteListeAtteinte(Boolean indicateurLimiteListeAtteinte) {
        this.indicateurLimiteListeAtteinte = indicateurLimiteListeAtteinte;
    }

    @JsonProperty("dateDebutPeriode")
    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    @JsonProperty("dateDebutPeriode")
    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    @JsonProperty("dateFinPeriode")
    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    @JsonProperty("dateFinPeriode")
    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    @JsonProperty("transactionListe")
    public List<TransactionFacturee> getTransactionListe() {
        return transactionListe;
    }

    @JsonProperty("transactionListe")
    public void setTransactionListe(List<TransactionFacturee> transactionListe) {
        this.transactionListe = transactionListe;
    }

    @JsonProperty("messages")
    public List<Object> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SectionFacturee.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("nbrMoisRecherches");
        sb.append('=');
        sb.append(((this.nbrMoisRecherches == null)?"<null>":this.nbrMoisRecherches));
        sb.append(',');
        sb.append("indicateurLimiteListeAtteinte");
        sb.append('=');
        sb.append(((this.indicateurLimiteListeAtteinte == null)?"<null>":this.indicateurLimiteListeAtteinte));
        sb.append(',');
        sb.append("dateDebutPeriode");
        sb.append('=');
        sb.append(((this.dateDebutPeriode == null)?"<null>":this.dateDebutPeriode));
        sb.append(',');
        sb.append("dateFinPeriode");
        sb.append('=');
        sb.append(((this.dateFinPeriode == null)?"<null>":this.dateFinPeriode));
        sb.append(',');
        sb.append("transactionListe");
        sb.append('=');
        sb.append(((this.transactionListe == null)?"<null>":this.transactionListe));
        sb.append(',');
        sb.append("messages");
        sb.append('=');
        sb.append(((this.messages == null)?"<null>":this.messages));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.indicateurLimiteListeAtteinte == null)? 0 :this.indicateurLimiteListeAtteinte.hashCode()));
        result = ((result* 31)+((this.messages == null)? 0 :this.messages.hashCode()));
        result = ((result* 31)+((this.dateFinPeriode == null)? 0 :this.dateFinPeriode.hashCode()));
        result = ((result* 31)+((this.nbrMoisRecherches == null)? 0 :this.nbrMoisRecherches.hashCode()));
        result = ((result* 31)+((this.dateDebutPeriode == null)? 0 :this.dateDebutPeriode.hashCode()));
        result = ((result* 31)+((this.transactionListe == null)? 0 :this.transactionListe.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SectionFacturee) == false) {
            return false;
        }
        SectionFacturee rhs = ((SectionFacturee) other);
        return (((((((this.indicateurLimiteListeAtteinte == rhs.indicateurLimiteListeAtteinte)||((this.indicateurLimiteListeAtteinte!= null)&&this.indicateurLimiteListeAtteinte.equals(rhs.indicateurLimiteListeAtteinte)))&&((this.messages == rhs.messages)||((this.messages!= null)&&this.messages.equals(rhs.messages))))&&((this.dateFinPeriode == rhs.dateFinPeriode)||((this.dateFinPeriode!= null)&&this.dateFinPeriode.equals(rhs.dateFinPeriode))))&&((this.nbrMoisRecherches == rhs.nbrMoisRecherches)||((this.nbrMoisRecherches!= null)&&this.nbrMoisRecherches.equals(rhs.nbrMoisRecherches))))&&((this.dateDebutPeriode == rhs.dateDebutPeriode)||((this.dateDebutPeriode!= null)&&this.dateDebutPeriode.equals(rhs.dateDebutPeriode))))&&((this.transactionListe == rhs.transactionListe)||((this.transactionListe!= null)&&this.transactionListe.equals(rhs.transactionListe))));
    }

}
