
package ca.jpti.SuiviBudget.Desjardins;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "identifiant",
    "numeroSequence",
    "dateInscription",
    "montantTransaction",
    "typeTransaction",
    "descriptionCourte",
    "descriptionSimplifiee",
    "dateTransaction",
    "devise",
    "nomEmbosse",
    "numeroCarteMasque",
    "codeRelation"
})
@Generated("jsonschema2pojo")
public class TransactionAutorisee {

    @JsonProperty("identifiant")
    private String identifiant;
    @JsonProperty("numeroSequence")
    private String numeroSequence;
    @JsonProperty("dateInscription")
    private String dateInscription;
    @JsonProperty("montantTransaction")
    private String montantTransaction;
    @JsonProperty("typeTransaction")
    private String typeTransaction;
    @JsonProperty("descriptionCourte")
    private String descriptionCourte;
    @JsonProperty("descriptionSimplifiee")
    private String descriptionSimplifiee;
    @JsonProperty("dateTransaction")
    private String dateTransaction;
    @JsonProperty("devise")
    private String devise;
    @JsonProperty("nomEmbosse")
    private String nomEmbosse;
    @JsonProperty("numeroCarteMasque")
    private String numeroCarteMasque;
    @JsonProperty("codeRelation")
    private String codeRelation;

    @JsonProperty("identifiant")
    public String getIdentifiant() {
        return identifiant;
    }

    @JsonProperty("identifiant")
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    @JsonProperty("numeroSequence")
    public String getNumeroSequence() {
        return numeroSequence;
    }

    @JsonProperty("numeroSequence")
    public void setNumeroSequence(String numeroSequence) {
        this.numeroSequence = numeroSequence;
    }

    @JsonProperty("dateInscription")
    public String getDateInscription() {
        return dateInscription;
    }

    @JsonProperty("dateInscription")
    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    @JsonProperty("montantTransaction")
    public String getMontantTransaction() {
        return montantTransaction;
    }

    @JsonProperty("montantTransaction")
    public void setMontantTransaction(String montantTransaction) {
        this.montantTransaction = montantTransaction;
    }

    @JsonProperty("typeTransaction")
    public String getTypeTransaction() {
        return typeTransaction;
    }

    @JsonProperty("typeTransaction")
    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    @JsonProperty("descriptionCourte")
    public String getDescriptionCourte() {
        return descriptionCourte;
    }

    @JsonProperty("descriptionCourte")
    public void setDescriptionCourte(String descriptionCourte) {
        this.descriptionCourte = descriptionCourte;
    }

    @JsonProperty("descriptionSimplifiee")
    public String getDescriptionSimplifiee() {
        return descriptionSimplifiee;
    }

    @JsonProperty("descriptionSimplifiee")
    public void setDescriptionSimplifiee(String descriptionSimplifiee) {
        this.descriptionSimplifiee = descriptionSimplifiee;
    }

    @JsonProperty("dateTransaction")
    public String getDateTransaction() {
        return dateTransaction;
    }

    @JsonProperty("dateTransaction")
    public void setDateTransaction(String dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    @JsonProperty("devise")
    public String getDevise() {
        return devise;
    }

    @JsonProperty("devise")
    public void setDevise(String devise) {
        this.devise = devise;
    }

    @JsonProperty("nomEmbosse")
    public String getNomEmbosse() {
        return nomEmbosse;
    }

    @JsonProperty("nomEmbosse")
    public void setNomEmbosse(String nomEmbosse) {
        this.nomEmbosse = nomEmbosse;
    }

    @JsonProperty("numeroCarteMasque")
    public String getNumeroCarteMasque() {
        return numeroCarteMasque;
    }

    @JsonProperty("numeroCarteMasque")
    public void setNumeroCarteMasque(String numeroCarteMasque) {
        this.numeroCarteMasque = numeroCarteMasque;
    }

    @JsonProperty("codeRelation")
    public String getCodeRelation() {
        return codeRelation;
    }

    @JsonProperty("codeRelation")
    public void setCodeRelation(String codeRelation) {
        this.codeRelation = codeRelation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TransactionAutorisee.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("identifiant");
        sb.append('=');
        sb.append(((this.identifiant == null)?"<null>":this.identifiant));
        sb.append(',');
        sb.append("numeroSequence");
        sb.append('=');
        sb.append(((this.numeroSequence == null)?"<null>":this.numeroSequence));
        sb.append(',');
        sb.append("dateInscription");
        sb.append('=');
        sb.append(((this.dateInscription == null)?"<null>":this.dateInscription));
        sb.append(',');
        sb.append("montantTransaction");
        sb.append('=');
        sb.append(((this.montantTransaction == null)?"<null>":this.montantTransaction));
        sb.append(',');
        sb.append("typeTransaction");
        sb.append('=');
        sb.append(((this.typeTransaction == null)?"<null>":this.typeTransaction));
        sb.append(',');
        sb.append("descriptionCourte");
        sb.append('=');
        sb.append(((this.descriptionCourte == null)?"<null>":this.descriptionCourte));
        sb.append(',');
        sb.append("descriptionSimplifiee");
        sb.append('=');
        sb.append(((this.descriptionSimplifiee == null)?"<null>":this.descriptionSimplifiee));
        sb.append(',');
        sb.append("dateTransaction");
        sb.append('=');
        sb.append(((this.dateTransaction == null)?"<null>":this.dateTransaction));
        sb.append(',');
        sb.append("devise");
        sb.append('=');
        sb.append(((this.devise == null)?"<null>":this.devise));
        sb.append(',');
        sb.append("nomEmbosse");
        sb.append('=');
        sb.append(((this.nomEmbosse == null)?"<null>":this.nomEmbosse));
        sb.append(',');
        sb.append("numeroCarteMasque");
        sb.append('=');
        sb.append(((this.numeroCarteMasque == null)?"<null>":this.numeroCarteMasque));
        sb.append(',');
        sb.append("codeRelation");
        sb.append('=');
        sb.append(((this.codeRelation == null)?"<null>":this.codeRelation));
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
        result = ((result* 31)+((this.montantTransaction == null)? 0 :this.montantTransaction.hashCode()));
        result = ((result* 31)+((this.nomEmbosse == null)? 0 :this.nomEmbosse.hashCode()));
        result = ((result* 31)+((this.identifiant == null)? 0 :this.identifiant.hashCode()));
        result = ((result* 31)+((this.descriptionCourte == null)? 0 :this.descriptionCourte.hashCode()));
        result = ((result* 31)+((this.numeroCarteMasque == null)? 0 :this.numeroCarteMasque.hashCode()));
        result = ((result* 31)+((this.numeroSequence == null)? 0 :this.numeroSequence.hashCode()));
        result = ((result* 31)+((this.devise == null)? 0 :this.devise.hashCode()));
        result = ((result* 31)+((this.dateTransaction == null)? 0 :this.dateTransaction.hashCode()));
        result = ((result* 31)+((this.typeTransaction == null)? 0 :this.typeTransaction.hashCode()));
        result = ((result* 31)+((this.codeRelation == null)? 0 :this.codeRelation.hashCode()));
        result = ((result* 31)+((this.dateInscription == null)? 0 :this.dateInscription.hashCode()));
        result = ((result* 31)+((this.descriptionSimplifiee == null)? 0 :this.descriptionSimplifiee.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TransactionAutorisee) == false) {
            return false;
        }
        TransactionAutorisee rhs = ((TransactionAutorisee) other);
        return (((((((((((((this.montantTransaction == rhs.montantTransaction)||((this.montantTransaction!= null)&&this.montantTransaction.equals(rhs.montantTransaction)))&&((this.nomEmbosse == rhs.nomEmbosse)||((this.nomEmbosse!= null)&&this.nomEmbosse.equals(rhs.nomEmbosse))))&&((this.identifiant == rhs.identifiant)||((this.identifiant!= null)&&this.identifiant.equals(rhs.identifiant))))&&((this.descriptionCourte == rhs.descriptionCourte)||((this.descriptionCourte!= null)&&this.descriptionCourte.equals(rhs.descriptionCourte))))&&((this.numeroCarteMasque == rhs.numeroCarteMasque)||((this.numeroCarteMasque!= null)&&this.numeroCarteMasque.equals(rhs.numeroCarteMasque))))&&((this.numeroSequence == rhs.numeroSequence)||((this.numeroSequence!= null)&&this.numeroSequence.equals(rhs.numeroSequence))))&&((this.devise == rhs.devise)||((this.devise!= null)&&this.devise.equals(rhs.devise))))&&((this.dateTransaction == rhs.dateTransaction)||((this.dateTransaction!= null)&&this.dateTransaction.equals(rhs.dateTransaction))))&&((this.typeTransaction == rhs.typeTransaction)||((this.typeTransaction!= null)&&this.typeTransaction.equals(rhs.typeTransaction))))&&((this.codeRelation == rhs.codeRelation)||((this.codeRelation!= null)&&this.codeRelation.equals(rhs.codeRelation))))&&((this.dateInscription == rhs.dateInscription)||((this.dateInscription!= null)&&this.dateInscription.equals(rhs.dateInscription))))&&((this.descriptionSimplifiee == rhs.descriptionSimplifiee)||((this.descriptionSimplifiee!= null)&&this.descriptionSimplifiee.equals(rhs.descriptionSimplifiee))));
    }

}
