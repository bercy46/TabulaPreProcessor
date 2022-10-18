
package ca.jpti.SuiviBudget.Desjardins;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.Optional;

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
    "montantRecompense",
    "tauxProgrammeRecompense",
    "categorieRecompense",
    "codeRelation",
    "categorieTransaction",
    "categorieParentTransaction",
    "idCategorieTransaction",
    "idCategorieParentTransaction",
    "montantDevise",
    "indicateurTransactionRecurrente",
    "codeSousTypeTransaction",
    "indicateurCategorieTransactionAutre"
})
@Generated("jsonschema2pojo")
public class TransactionFacturee {

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
    @JsonProperty("montantRecompense")
    private String montantRecompense;
    @JsonProperty("tauxProgrammeRecompense")
    private String tauxProgrammeRecompense;
    @JsonProperty("categorieRecompense")
    private String categorieRecompense;
    @JsonProperty("codeRelation")
    private String codeRelation;
    @JsonProperty("categorieTransaction")
    private String categorieTransaction;
    @JsonProperty("categorieParentTransaction")
    private String categorieParentTransaction;
    @JsonProperty("idCategorieTransaction")
    private Integer idCategorieTransaction;
    @JsonProperty("idCategorieParentTransaction")
    private Integer idCategorieParentTransaction;
    @JsonProperty("montantDevise")
    private String montantDevise;
    @JsonProperty("indicateurTransactionRecurrente")
    private Boolean indicateurTransactionRecurrente;
    @JsonProperty("codeSousTypeTransaction")
    private String codeSousTypeTransaction;
    @JsonProperty("indicateurCategorieTransactionAutre")
    private Boolean indicateurCategorieTransactionAutre;
    @JsonProperty("categorie")
    private Optional<String> categorie;
    @JsonProperty("posteDepense")
    private Optional<String> posteDepense;

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

    @JsonProperty("montantRecompense")
    public String getMontantRecompense() {
        return montantRecompense;
    }

    @JsonProperty("montantRecompense")
    public void setMontantRecompense(String montantRecompense) {
        this.montantRecompense = montantRecompense;
    }

    @JsonProperty("tauxProgrammeRecompense")
    public String getTauxProgrammeRecompense() {
        return tauxProgrammeRecompense;
    }

    @JsonProperty("tauxProgrammeRecompense")
    public void setTauxProgrammeRecompense(String tauxProgrammeRecompense) {
        this.tauxProgrammeRecompense = tauxProgrammeRecompense;
    }

    @JsonProperty("categorieRecompense")
    public String getCategorieRecompense() {
        return categorieRecompense;
    }

    @JsonProperty("categorieRecompense")
    public void setCategorieRecompense(String categorieRecompense) {
        this.categorieRecompense = categorieRecompense;
    }

    @JsonProperty("codeRelation")
    public String getCodeRelation() {
        return codeRelation;
    }

    @JsonProperty("codeRelation")
    public void setCodeRelation(String codeRelation) {
        this.codeRelation = codeRelation;
    }

    @JsonProperty("categorieTransaction")
    public String getCategorieTransaction() {
        return categorieTransaction;
    }

    @JsonProperty("categorieTransaction")
    public void setCategorieTransaction(String categorieTransaction) {
        this.categorieTransaction = categorieTransaction;
    }

    @JsonProperty("categorieParentTransaction")
    public String getCategorieParentTransaction() {
        return categorieParentTransaction;
    }

    @JsonProperty("categorieParentTransaction")
    public void setCategorieParentTransaction(String categorieParentTransaction) {
        this.categorieParentTransaction = categorieParentTransaction;
    }

    @JsonProperty("idCategorieTransaction")
    public Integer getIdCategorieTransaction() {
        return idCategorieTransaction;
    }

    @JsonProperty("idCategorieTransaction")
    public void setIdCategorieTransaction(Integer idCategorieTransaction) {
        this.idCategorieTransaction = idCategorieTransaction;
    }

    @JsonProperty("idCategorieParentTransaction")
    public Integer getIdCategorieParentTransaction() {
        return idCategorieParentTransaction;
    }

    @JsonProperty("idCategorieParentTransaction")
    public void setIdCategorieParentTransaction(Integer idCategorieParentTransaction) {
        this.idCategorieParentTransaction = idCategorieParentTransaction;
    }

    @JsonProperty("montantDevise")
    public String getMontantDevise() {
        return montantDevise;
    }

    @JsonProperty("montantDevise")
    public void setMontantDevise(String montantDevise) {
        this.montantDevise = montantDevise;
    }

    @JsonProperty("indicateurTransactionRecurrente")
    public Boolean getIndicateurTransactionRecurrente() {
        return indicateurTransactionRecurrente;
    }

    @JsonProperty("indicateurTransactionRecurrente")
    public void setIndicateurTransactionRecurrente(Boolean indicateurTransactionRecurrente) {
        this.indicateurTransactionRecurrente = indicateurTransactionRecurrente;
    }

    @JsonProperty("codeSousTypeTransaction")
    public String getCodeSousTypeTransaction() {
        return codeSousTypeTransaction;
    }

    @JsonProperty("codeSousTypeTransaction")
    public void setCodeSousTypeTransaction(String codeSousTypeTransaction) {
        this.codeSousTypeTransaction = codeSousTypeTransaction;
    }

    @JsonProperty("indicateurCategorieTransactionAutre")
    public Boolean getIndicateurCategorieTransactionAutre() {
        return indicateurCategorieTransactionAutre;
    }

    @JsonProperty("indicateurCategorieTransactionAutre")
    public void setIndicateurCategorieTransactionAutre(Boolean indicateurCategorieTransactionAutre) {
        this.indicateurCategorieTransactionAutre = indicateurCategorieTransactionAutre;
    }

    public Optional<String> getCategorie() {
        return categorie;
    }

    public void setCategorie(Optional<String> categorie) {
        this.categorie = categorie;
    }

    public Optional<String> getPosteDepense() {
        return posteDepense;
    }

    public void setPosteDepense(Optional<String> posteDepense) {
        this.posteDepense = posteDepense;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TransactionFacturee.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("montantRecompense");
        sb.append('=');
        sb.append(((this.montantRecompense == null)?"<null>":this.montantRecompense));
        sb.append(',');
        sb.append("tauxProgrammeRecompense");
        sb.append('=');
        sb.append(((this.tauxProgrammeRecompense == null)?"<null>":this.tauxProgrammeRecompense));
        sb.append(',');
        sb.append("categorieRecompense");
        sb.append('=');
        sb.append(((this.categorieRecompense == null)?"<null>":this.categorieRecompense));
        sb.append(',');
        sb.append("codeRelation");
        sb.append('=');
        sb.append(((this.codeRelation == null)?"<null>":this.codeRelation));
        sb.append(',');
        sb.append("categorieTransaction");
        sb.append('=');
        sb.append(((this.categorieTransaction == null)?"<null>":this.categorieTransaction));
        sb.append(',');
        sb.append("categorieParentTransaction");
        sb.append('=');
        sb.append(((this.categorieParentTransaction == null)?"<null>":this.categorieParentTransaction));
        sb.append(',');
        sb.append("idCategorieTransaction");
        sb.append('=');
        sb.append(((this.idCategorieTransaction == null)?"<null>":this.idCategorieTransaction));
        sb.append(',');
        sb.append("idCategorieParentTransaction");
        sb.append('=');
        sb.append(((this.idCategorieParentTransaction == null)?"<null>":this.idCategorieParentTransaction));
        sb.append(',');
        sb.append("montantDevise");
        sb.append('=');
        sb.append(((this.montantDevise == null)?"<null>":this.montantDevise));
        sb.append(',');
        sb.append("indicateurTransactionRecurrente");
        sb.append('=');
        sb.append(((this.indicateurTransactionRecurrente == null)?"<null>":this.indicateurTransactionRecurrente));
        sb.append(',');
        sb.append("codeSousTypeTransaction");
        sb.append('=');
        sb.append(((this.codeSousTypeTransaction == null)?"<null>":this.codeSousTypeTransaction));
        sb.append(',');
        sb.append("indicateurCategorieTransactionAutre");
        sb.append('=');
        sb.append(((this.indicateurCategorieTransactionAutre == null)?"<null>":this.indicateurCategorieTransactionAutre));
        sb.append(',');
        sb.append("categorie");
        sb.append('=');
        sb.append(((this.getCategorie() == null || this.getCategorie().isEmpty())?"":this.getCategorie().get()));
        sb.append(',');
        sb.append("posteDepense");
        sb.append('=');
        sb.append(((this.getPosteDepense() == null || this.getPosteDepense().isEmpty())?"":this.getPosteDepense().get()));
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
        result = ((result* 31)+((this.montantRecompense == null)? 0 :this.montantRecompense.hashCode()));
        result = ((result* 31)+((this.montantTransaction == null)? 0 :this.montantTransaction.hashCode()));
        result = ((result* 31)+((this.nomEmbosse == null)? 0 :this.nomEmbosse.hashCode()));
        result = ((result* 31)+((this.identifiant == null)? 0 :this.identifiant.hashCode()));
        result = ((result* 31)+((this.descriptionCourte == null)? 0 :this.descriptionCourte.hashCode()));
        result = ((result* 31)+((this.numeroCarteMasque == null)? 0 :this.numeroCarteMasque.hashCode()));
        result = ((result* 31)+((this.numeroSequence == null)? 0 :this.numeroSequence.hashCode()));
        result = ((result* 31)+((this.montantDevise == null)? 0 :this.montantDevise.hashCode()));
        result = ((result* 31)+((this.tauxProgrammeRecompense == null)? 0 :this.tauxProgrammeRecompense.hashCode()));
        result = ((result* 31)+((this.devise == null)? 0 :this.devise.hashCode()));
        result = ((result* 31)+((this.dateTransaction == null)? 0 :this.dateTransaction.hashCode()));
        result = ((result* 31)+((this.categorieRecompense == null)? 0 :this.categorieRecompense.hashCode()));
        result = ((result* 31)+((this.codeSousTypeTransaction == null)? 0 :this.codeSousTypeTransaction.hashCode()));
        result = ((result* 31)+((this.typeTransaction == null)? 0 :this.typeTransaction.hashCode()));
        result = ((result* 31)+((this.codeRelation == null)? 0 :this.codeRelation.hashCode()));
        result = ((result* 31)+((this.dateInscription == null)? 0 :this.dateInscription.hashCode()));
        result = ((result* 31)+((this.indicateurTransactionRecurrente == null)? 0 :this.indicateurTransactionRecurrente.hashCode()));
        result = ((result* 31)+((this.categorieParentTransaction == null)? 0 :this.categorieParentTransaction.hashCode()));
        result = ((result* 31)+((this.idCategorieTransaction == null)? 0 :this.idCategorieTransaction.hashCode()));
        result = ((result* 31)+((this.idCategorieParentTransaction == null)? 0 :this.idCategorieParentTransaction.hashCode()));
        result = ((result* 31)+((this.indicateurCategorieTransactionAutre == null)? 0 :this.indicateurCategorieTransactionAutre.hashCode()));
        result = ((result* 31)+((this.descriptionSimplifiee == null)? 0 :this.descriptionSimplifiee.hashCode()));
        result = ((result* 31)+((this.categorieTransaction == null)? 0 :this.categorieTransaction.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TransactionFacturee) == false) {
            return false;
        }
        TransactionFacturee rhs = ((TransactionFacturee) other);
        return ((((((((((((((((((((((((this.montantRecompense == rhs.montantRecompense)||((this.montantRecompense!= null)&&this.montantRecompense.equals(rhs.montantRecompense)))&&((this.montantTransaction == rhs.montantTransaction)||((this.montantTransaction!= null)&&this.montantTransaction.equals(rhs.montantTransaction))))&&((this.nomEmbosse == rhs.nomEmbosse)||((this.nomEmbosse!= null)&&this.nomEmbosse.equals(rhs.nomEmbosse))))&&((this.identifiant == rhs.identifiant)||((this.identifiant!= null)&&this.identifiant.equals(rhs.identifiant))))&&((this.descriptionCourte == rhs.descriptionCourte)||((this.descriptionCourte!= null)&&this.descriptionCourte.equals(rhs.descriptionCourte))))&&((this.numeroCarteMasque == rhs.numeroCarteMasque)||((this.numeroCarteMasque!= null)&&this.numeroCarteMasque.equals(rhs.numeroCarteMasque))))&&((this.numeroSequence == rhs.numeroSequence)||((this.numeroSequence!= null)&&this.numeroSequence.equals(rhs.numeroSequence))))&&((this.montantDevise == rhs.montantDevise)||((this.montantDevise!= null)&&this.montantDevise.equals(rhs.montantDevise))))&&((this.tauxProgrammeRecompense == rhs.tauxProgrammeRecompense)||((this.tauxProgrammeRecompense!= null)&&this.tauxProgrammeRecompense.equals(rhs.tauxProgrammeRecompense))))&&((this.devise == rhs.devise)||((this.devise!= null)&&this.devise.equals(rhs.devise))))&&((this.dateTransaction == rhs.dateTransaction)||((this.dateTransaction!= null)&&this.dateTransaction.equals(rhs.dateTransaction))))&&((this.categorieRecompense == rhs.categorieRecompense)||((this.categorieRecompense!= null)&&this.categorieRecompense.equals(rhs.categorieRecompense))))&&((this.codeSousTypeTransaction == rhs.codeSousTypeTransaction)||((this.codeSousTypeTransaction!= null)&&this.codeSousTypeTransaction.equals(rhs.codeSousTypeTransaction))))&&((this.typeTransaction == rhs.typeTransaction)||((this.typeTransaction!= null)&&this.typeTransaction.equals(rhs.typeTransaction))))&&((this.codeRelation == rhs.codeRelation)||((this.codeRelation!= null)&&this.codeRelation.equals(rhs.codeRelation))))&&((this.dateInscription == rhs.dateInscription)||((this.dateInscription!= null)&&this.dateInscription.equals(rhs.dateInscription))))&&((this.indicateurTransactionRecurrente == rhs.indicateurTransactionRecurrente)||((this.indicateurTransactionRecurrente!= null)&&this.indicateurTransactionRecurrente.equals(rhs.indicateurTransactionRecurrente))))&&((this.categorieParentTransaction == rhs.categorieParentTransaction)||((this.categorieParentTransaction!= null)&&this.categorieParentTransaction.equals(rhs.categorieParentTransaction))))&&((this.idCategorieTransaction == rhs.idCategorieTransaction)||((this.idCategorieTransaction!= null)&&this.idCategorieTransaction.equals(rhs.idCategorieTransaction))))&&((this.idCategorieParentTransaction == rhs.idCategorieParentTransaction)||((this.idCategorieParentTransaction!= null)&&this.idCategorieParentTransaction.equals(rhs.idCategorieParentTransaction))))&&((this.indicateurCategorieTransactionAutre == rhs.indicateurCategorieTransactionAutre)||((this.indicateurCategorieTransactionAutre!= null)&&this.indicateurCategorieTransactionAutre.equals(rhs.indicateurCategorieTransactionAutre))))&&((this.descriptionSimplifiee == rhs.descriptionSimplifiee)||((this.descriptionSimplifiee!= null)&&this.descriptionSimplifiee.equals(rhs.descriptionSimplifiee))))&&((this.categorieTransaction == rhs.categorieTransaction)||((this.categorieTransaction!= null)&&this.categorieTransaction.equals(rhs.categorieTransaction))));
    }

}
