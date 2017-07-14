package ggalantsev.Entity;

/**
 * Created by AlonE_Angel on 05.03.2017.
 */
public class IssueAutocompleteResponse {

    String value;
    String label;
    String url;

    public IssueAutocompleteResponse(String value, String label, String url) {
        this.value = value;
        this.label = label;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IssueAutocompleteResponse() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
