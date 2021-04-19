/**
 *
 */
package com.biu.wifi.campus.exception;

/**
 * ClassName: NeedLoginException 
 * @Description: TODO
 * @author King
 * @date 2016年10月8日
 */
public class NeedLoginException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 5237549537950011490L;


    public NeedLoginException(String message) {
        super(message);
    }
}
