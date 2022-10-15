
package ca.jpti.SuiviBudget.Desjardins;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "codeRelation",
    "nomProgrammeRecompense"
})
@Generated("jsonschema2pojo")
public class SectionCompte {

    @JsonProperty("codeRelation")
    private String codeRelation;
    @JsonProperty("nomProgrammeRecompense")
    private String nomProgrammeRecompense;

    @JsonProperty("codeRelation")
    public String getCodeRelation() {
        return codeRelation;
    }

    @JsonProperty("codeRelation")
    public void setCodeRelation(String codeRelation) {
        this.codeRelation = codeRelation;
    }

    @JsonProperty("nomProgrammeRecompense")
    public String getNomProgrammeRecompense() {
        return nomProgrammeRecompense;
    }

    @JsonProperty("nomProgrammeRecompense")
    public void setNomProgrammeRecompense(String nomProgrammeRecompense) {
        this.nomProgrammeRecompense = nomProgrammeRecompense;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SectionCompte.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("codeRelation");
        sb.append('=');
        sb.append(((this.codeRelation == null)?"<null>":this.codeRelation));
        sb.append(',');
        sb.append("nomProgrammeRecompense");
        sb.append('=');
        sb.append(((this.nomProgrammeRecompense == null)?"<null>":this.nomProgrammeRecompense));
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
        result = ((result* 31)+((this.nomProgrammeRecompense == null)? 0 :this.nomProgrammeRecompense.hashCode()));
        result = ((result* 31)+((this.codeRelation == null)? 0 :this.codeRelation.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SectionCompte) == false) {
            return false;
        }
        SectionCompte rhs = ((SectionCompte) other);
        return (((this.nomProgrammeRecompense == rhs.nomProgrammeRecompense)||((this.nomProgrammeRecompense!= null)&&this.nomProgrammeRecompense.equals(rhs.nomProgrammeRecompense)))&&((this.codeRelation == rhs.codeRelation)||((this.codeRelation!= null)&&this.codeRelation.equals(rhs.codeRelation))));
    }

}
