package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException{

    public NotEnoughStockException() {
        super();
    }

    //메세지 넘겨주고
    public NotEnoughStockException(String message) {
        super(message);
    }

    // 메세지와 이게 발생한 근원적인 exception을 넣어서 보여줌..?
    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
