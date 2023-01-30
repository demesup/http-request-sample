package httprequests.sender;

public interface Sender {
    Runnable workToDo();

    default void finish(){}
}
