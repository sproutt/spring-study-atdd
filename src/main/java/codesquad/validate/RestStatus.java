package codesquad.validate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestStatus {

    private boolean status;

    public boolean isStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "RestResponse [status=" + status + "]";
    }
}
