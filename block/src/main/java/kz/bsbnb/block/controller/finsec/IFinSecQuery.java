package kz.bsbnb.block.controller.finsec;


import kz.bsbnb.common.model.impl.user.User;

/**
 * Created by kanattulbassiyev on 8/15/16.
 */
public interface IFinSecQuery {
    // todo: make object stub
    Object getWallet(final long userId);

    // todo: make object stub
    Object getTransaction(final User user, final long transactionId);
}
