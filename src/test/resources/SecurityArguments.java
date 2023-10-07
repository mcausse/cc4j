import java.util.Map;

public class SecurityArguments {

    private boolean isTLSEnable;
    private boolean isMutualEnable;
    private String crtPath; //selfsigned.crt
    private String keyPath; //selfsigned.key
    private String caCrtPath; //selfsigned-ca.crt
    private Map<String, Boolean> clientAuthHosts;
    private int port;

    public SecurityArguments() {
    }

    public SecurityArguments(boolean isTLSEnable, boolean isMutualEnable, String crtPath, String keyPath, String caCrtPath, Map<String, Boolean> clientAuthHosts, Integer port) {
        this.isTLSEnable = isTLSEnable;
        this.isMutualEnable = isMutualEnable;
        this.crtPath = crtPath;
        this.keyPath = keyPath;
        this.caCrtPath = caCrtPath;
        this.clientAuthHosts = clientAuthHosts;
        this.port = port;
    }

    public boolean getTLSEnable() {
        return isTLSEnable;
    }

    public void setTLSEnable(boolean tLSEnable) {
        isTLSEnable = tLSEnable;
    }

    public boolean getMutualEnable() {
        return isMutualEnable;
    }

    public void setMutualEnable(boolean mutualEnable) {
        isMutualEnable = mutualEnable;
    }

    public String getCrtPath() {
        if (crtPath != null) {
            return crtPath;
        }
        return "";
    }

    public void setCrtPath(String crtPath) {
        this.crtPath = crtPath;
    }

    public String getKeyPath() {
        if (keyPath != null) {
            return keyPath;
        }
        return "";
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getCaCrtPath() {
        if (caCrtPath != null) {
            return caCrtPath;
        }
        return "";
    }

    public void setCaCrtPath(String caCrtPath) {
        this.caCrtPath = caCrtPath;
    }

    public Map<String, Boolean> getClientAuthHosts() {
        return clientAuthHosts;
    }

    public void setClientAuthHosts(Map<String, Boolean> clientAuthHosts) {
        this.clientAuthHosts = clientAuthHosts;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    // TODO Cover with UT *before* refactor.
    // TODO The Cyclomatic Complexity of this method "isEmpty" is 11 which is greater than 10 authorized.
    // TODO Reduce the number of conditional operators (10) used in the expression (maximum allowed 3).
    public boolean isEmpty() {
        return !isTLSEnable && !isMutualEnable && (crtPath == null || crtPath.isEmpty())
                && (keyPath == null || keyPath.isEmpty()) && (caCrtPath == null || caCrtPath.isEmpty())
                && (clientAuthHosts == null || clientAuthHosts.isEmpty()) && port == 0;
    }

    @Override
    public String toString() {
        return "SecurityArguments{" +
                "isTLSEnable=" + isTLSEnable +
                ", isMutualEnable=" + isMutualEnable +
                ", certificateFile='" + crtPath + '\'' +
                ", certificateKeyFile='" + keyPath + '\'' +
                ", certificateCAFile='" + caCrtPath + '\'' +
                ", clientAuthHosts=" + clientAuthHosts +
                ", port=" + port +
                '}';
    }
}