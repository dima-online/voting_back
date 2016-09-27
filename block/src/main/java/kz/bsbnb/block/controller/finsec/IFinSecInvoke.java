package kz.bsbnb.block.controller.finsec;

/**
 * Created by kanattulbassiyev on 8/15/16.
 */
public interface IFinSecInvoke {
    // todo: returns objects, must return architectural concept
    Object buyFinSec(final Long userId, final Long amount);

    Object sellFinSec(final Long userId, final Long amount);
}
