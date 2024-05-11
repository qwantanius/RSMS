package RSMS.common.exceptions;


import RSMS.Locomotive.Locomotive;

public class LocomotiveException extends Exception {
    public LocomotiveException(Locomotive locomotive, String message) {
        super(String.format("LOCOMOTIVE %s EXCEPTION: ", locomotive.getName(), message));
    }
}
