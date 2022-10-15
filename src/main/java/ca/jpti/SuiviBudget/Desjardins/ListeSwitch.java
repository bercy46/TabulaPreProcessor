
package ca.jpti.SuiviBudget.Desjardins;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nom",
    "indicateurFonctionActive"
})
@Generated("jsonschema2pojo")
public class ListeSwitch {

    @JsonProperty("nom")
    private String nom;
    @JsonProperty("indicateurFonctionActive")
    private Boolean indicateurFonctionActive;

    @JsonProperty("nom")
    public String getNom() {
        return nom;
    }

    @JsonProperty("nom")
    public void setNom(String nom) {
        this.nom = nom;
    }

    @JsonProperty("indicateurFonctionActive")
    public Boolean getIndicateurFonctionActive() {
        return indicateurFonctionActive;
    }

    @JsonProperty("indicateurFonctionActive")
    public void setIndicateurFonctionActive(Boolean indicateurFonctionActive) {
        this.indicateurFonctionActive = indicateurFonctionActive;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ListeSwitch.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("nom");
        sb.append('=');
        sb.append(((this.nom == null)?"<null>":this.nom));
        sb.append(',');
        sb.append("indicateurFonctionActive");
        sb.append('=');
        sb.append(((this.indicateurFonctionActive == null)?"<null>":this.indicateurFonctionActive));
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
        result = ((result* 31)+((this.nom == null)? 0 :this.nom.hashCode()));
        result = ((result* 31)+((this.indicateurFonctionActive == null)? 0 :this.indicateurFonctionActive.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ListeSwitch) == false) {
            return false;
        }
        ListeSwitch rhs = ((ListeSwitch) other);
        return (((this.nom == rhs.nom)||((this.nom!= null)&&this.nom.equals(rhs.nom)))&&((this.indicateurFonctionActive == rhs.indicateurFonctionActive)||((this.indicateurFonctionActive!= null)&&this.indicateurFonctionActive.equals(rhs.indicateurFonctionActive))));
    }

}
