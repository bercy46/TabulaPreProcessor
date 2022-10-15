
package ca.jpti.SuiviBudget.Desjardins;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "transactionListe",
    "messages"
})
@Generated("jsonschema2pojo")
public class SectionAutorisee {

    @JsonProperty("transactionListe")
    private List<TransactionAutorisee> transactionListeAutorisees = null;
    @JsonProperty("messages")
    private List<Object> messages = null;

    @JsonProperty("transactionListe")
    public List<TransactionAutorisee> getTransactionListe() {
        return transactionListeAutorisees;
    }

    @JsonProperty("transactionListe")
    public void setTransactionListe(List<TransactionAutorisee> transactionListeAutorisees) {
        this.transactionListeAutorisees = transactionListeAutorisees;
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
        sb.append(SectionAutorisee.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("transactionListe");
        sb.append('=');
        sb.append(((this.transactionListeAutorisees == null)?"<null>":this.transactionListeAutorisees));
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
        result = ((result* 31)+((this.messages == null)? 0 :this.messages.hashCode()));
        result = ((result* 31)+((this.transactionListeAutorisees == null)? 0 :this.transactionListeAutorisees.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SectionAutorisee) == false) {
            return false;
        }
        SectionAutorisee rhs = ((SectionAutorisee) other);
        return (((this.messages == rhs.messages)||((this.messages!= null)&&this.messages.equals(rhs.messages)))&&((this.transactionListeAutorisees == rhs.transactionListeAutorisees)||((this.transactionListeAutorisees != null)&&this.transactionListeAutorisees.equals(rhs.transactionListeAutorisees))));
    }

}
