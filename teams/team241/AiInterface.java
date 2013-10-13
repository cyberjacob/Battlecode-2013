package team241;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * Created with IntelliJ IDEA.
 * User: ccuk
 * Date: 11/01/2013
 * Time: 8:47PM
 * To change this template use File | Settings | File Templates.
 */
public interface AiInterface {

    public void run(RobotController rc) throws GameActionException;

    public void setup(RobotController rc) throws GameActionException;

}
