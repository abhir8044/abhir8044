import java.io.Serializable;

public class Message implements MessageTypes, Serializable {
    private int type;
    private Object cont;

    public Message() {}
    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setContent(Object content) {
        this.cont = content;
    }

    public Object getContent() {
        return cont;
    }
}