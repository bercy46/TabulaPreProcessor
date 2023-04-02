package ca.jpti.SuiviBudget.Desjardins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sectionCompte",
        "sectionAutorisee",
        "sectionFacturee",
        "listeSwitch"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("jsonschema2pojo")
public class RapportCC {

    @JsonProperty("sectionCompte")
    private SectionCompte sectionCompte;
    @JsonProperty("sectionAutorisee")
    private SectionAutorisee sectionAutorisee;
    @JsonProperty("sectionFacturee")
    private SectionFacturee sectionFacturee;
    @JsonProperty("listeSwitch")
    private List<ListeSwitch> listeSwitch = null;

    @JsonProperty("sectionCompte")
    public SectionCompte getSectionCompte() {
        return sectionCompte;
    }

    @JsonProperty("sectionCompte")
    public void setSectionCompte(SectionCompte sectionCompte) {
        this.sectionCompte = sectionCompte;
    }

    @JsonProperty("sectionAutorisee")
    public SectionAutorisee getSectionAutorisee() {
        return sectionAutorisee;
    }

    @JsonProperty("sectionAutorisee")
    public void setSectionAutorisee(SectionAutorisee sectionAutorisee) {
        this.sectionAutorisee = sectionAutorisee;
    }

    @JsonProperty("sectionFacturee")
    public SectionFacturee getSectionFacturee() {
        return sectionFacturee;
    }

    @JsonProperty("sectionFacturee")
    public void setSectionFacturee(SectionFacturee sectionFacturee) {
        this.sectionFacturee = sectionFacturee;
    }

    @JsonProperty("listeSwitch")
    public List<ListeSwitch> getListeSwitch() {
        return listeSwitch;
    }

    @JsonProperty("listeSwitch")
    public void setListeSwitch(List<ListeSwitch> listeSwitch) {
        this.listeSwitch = listeSwitch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RapportCC.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sectionCompte");
        sb.append('=');
        sb.append(((this.sectionCompte == null) ? "<null>" : this.sectionCompte));
        sb.append(',');
        sb.append("sectionAutorisee");
        sb.append('=');
        sb.append(((this.sectionAutorisee == null) ? "<null>" : this.sectionAutorisee));
        sb.append(',');
        sb.append("sectionFacturee");
        sb.append('=');
        sb.append(((this.sectionFacturee == null) ? "<null>" : this.sectionFacturee));
        sb.append(',');
        sb.append("listeSwitch");
        sb.append('=');
        sb.append(((this.listeSwitch == null) ? "<null>" : this.listeSwitch));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.listeSwitch == null) ? 0 : this.listeSwitch.hashCode()));
        result = ((result * 31) + ((this.sectionCompte == null) ? 0 : this.sectionCompte.hashCode()));
        result = ((result * 31) + ((this.sectionAutorisee == null) ? 0 : this.sectionAutorisee.hashCode()));
        result = ((result * 31) + ((this.sectionFacturee == null) ? 0 : this.sectionFacturee.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RapportCC) == false) {
            return false;
        }
        RapportCC rhs = ((RapportCC) other);
        return (((((this.listeSwitch == rhs.listeSwitch) || ((this.listeSwitch != null) && this.listeSwitch.equals(rhs.listeSwitch))) && ((this.sectionCompte == rhs.sectionCompte) || ((this.sectionCompte != null) && this.sectionCompte.equals(rhs.sectionCompte)))) && ((this.sectionAutorisee == rhs.sectionAutorisee) || ((this.sectionAutorisee != null) && this.sectionAutorisee.equals(rhs.sectionAutorisee)))) && ((this.sectionFacturee == rhs.sectionFacturee) || ((this.sectionFacturee != null) && this.sectionFacturee.equals(rhs.sectionFacturee))));
    }

}
