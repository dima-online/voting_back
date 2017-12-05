package kz.bsbnb.common.util;

import java.util.Random;

/**
 * Created by Olzhas.Pazyldayev on 28.09.2017.
 */

public class BruteUtil {
    public static final Integer MIN_WAIT = 1;
    public static final Integer MAX_WAIT = 4;
    public static final Integer MAX_WAIT_MEDIUM = 7;
    public static final Integer MAX_WAIT_LONG = 10;

    public static Integer waitRandom() {
        return new Random().nextInt(MAX_WAIT - MIN_WAIT) + MIN_WAIT;
    }

    public static Integer waitRandomMedium() {
        return new Random().nextInt(MAX_WAIT_MEDIUM - MAX_WAIT) + MAX_WAIT;
    }

    public static Integer waitRandomLong() {
        return new Random().nextInt(MAX_WAIT_LONG - MAX_WAIT_MEDIUM) + MAX_WAIT_MEDIUM;
    }



}
