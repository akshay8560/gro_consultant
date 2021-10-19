package com.clinicapp.providers;

public class AsyncResponse<T,E> {
    public static final int STATUS_NOT_STARTED = 0;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_ERROR = 3;


    public final T value;
    public final E error;
    public final int status;
    private boolean isFresh;

    public final String message;


    public static <T,E> AsyncResponse<T,E> success(T t){
        return new AsyncResponse(null, t, STATUS_SUCCESS, null);
    }

    public static <T,E> AsyncResponse<T,E> error(E e, String message){
        return new AsyncResponse(e, null, STATUS_ERROR, message);
    }

    public static <T,E> AsyncResponse<T,E> errorWithObj(T t, String message){
        return new AsyncResponse(null, t, STATUS_ERROR, message);
    }

    public static <T,E> AsyncResponse<T,E> error(E e){
        return new AsyncResponse(e, null, STATUS_ERROR, null);
    }

    public static <T,E> AsyncResponse<T,E> loading(){
        return new AsyncResponse(null, null, STATUS_LOADING, null);
    }

    public static <T,E> AsyncResponse<T,E> error(String msg){
        return new AsyncResponse(new Exception(msg), null, STATUS_ERROR, msg);
    }

    public static <T,E> AsyncResponse<T,E> notStarted(){
        return new AsyncResponse(null, null, STATUS_NOT_STARTED, null);
    }

    public static <T,E> AsyncResponse<T,E> loading(T t){
        return new AsyncResponse(null, t, STATUS_LOADING, null);
    }

    public static <T,E> AsyncResponse<T,E> withStatus(int status){
        return new AsyncResponse(null, null, status, null);
    }

    public AsyncResponse(E error, T t, int status, String message){
        this.value = t;
        this.error = error;
        this.status = status;
        this.message = message;
        isFresh = true;
    }

    public boolean isError(){
        return this.status == STATUS_ERROR;
    }
    public boolean isLoading() {return this.status == STATUS_LOADING;}
    public boolean isSuccess() {return this.status == STATUS_SUCCESS;}
    public boolean isNotStarted() {return this.status == STATUS_NOT_STARTED;}
    public T pop(){
        isFresh = false;
        return value;
    }

    public boolean isFresh(){
        return this.isFresh;
    }
}
