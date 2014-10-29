package core.exceptions;

public class RedirectException extends Exception {
    private String url;
    public RedirectException(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
}
