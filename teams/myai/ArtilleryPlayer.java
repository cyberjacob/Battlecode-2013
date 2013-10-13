package myai;

import battlecode.common.*;

/**
 * Created with IntelliJ IDEA.
 * User: ccuk
 * Date: 11/01/2013
 * Time: 9:06PM
 * To change this template use File | Settings | File Templates.
 */
public class ArtilleryPlayer implements AiInterface {
    @Override
    public void run(RobotController rc) throws GameActionException {
        if(rc.isActive()){
            Robot[] objects = rc.senseNearbyGameObjects(Robot.class, 63, rc.getTeam().opponent());
            for (Robot object : objects) {
                MapLocation robotLocation = rc.senseLocationOf(object);
                if((rc.canAttackSquare(robotLocation))) {
                    rc.attackSquare(robotLocation);
                    System.out.println("Attacking "+robotLocation);
                    break;
                }
            }
        }
        rc.yield();
    }

    @Override
    public void setup(RobotController rc) throws GameActionException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
