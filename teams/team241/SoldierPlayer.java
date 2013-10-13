package team241;

import battlecode.common.*;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ccuk
 * Date: 11/01/2013
 * Time: 8:40PM
 * To change this template use File | Settings | File Templates.
 */
public class SoldierPlayer implements AiInterface {

    private Boolean go=false;
    private Random rndGen = new Random();
    private MapLocation destination = null;
    private Boolean goingToEncampment = false;
    private MapLocation enemyHQ = null;

    private boolean smartMove(RobotController rc, Boolean takeTwo) throws GameActionException {
        Direction dir;
        if(takeTwo) {
            dir = Direction.values()[(int)(rndGen.nextFloat() * 8)];
        } else {
            if(this.destination==null){
                this.destination = this.enemyHQ;
            }
            dir = rc.getLocation().directionTo(this.destination);
        }
        MapLocation moveTo = rc.getLocation().add(dir);
        Team isMine = rc.senseMine(moveTo);
        if(isMine != null) {
            // There's a mine here!
            if(isMine != rc.getTeam()) {
                // Enemy mine detected!
                rc.defuseMine(moveTo);
                return true;
            } else if(rc.canMove(dir)) {
                rc.move(dir);
                return true;
            } else{
                if(takeTwo) {
                    return false;
                } else {
                    return smartMove(rc, true);
                }
            }
        } else if(rc.canMove(dir)) {
            if (rndGen.nextFloat()*10<2.0) {
                // Lay a mine
                rc.layMine();
                return true;
            } else{
                rc.move(dir);
                return true;
            }
        } else {
            if(takeTwo) {
                return false;
            } else {
                return smartMove(rc, true);
            }
        }
    }

    public void setSeed(RobotController rc) {
        this.rndGen.setSeed(rc.getRobot().getID());
    }

    public void run(RobotController rc) throws GameActionException {
        if(rc.isActive()){
            if(rc.senseEncampmentSquare(rc.getLocation())) {
                    double rnd = rndGen.nextFloat()*10;
                    int EncampmentTypeId = (int)(rnd);
                    RobotType EncampmentType = null;
                    switch (EncampmentTypeId) {
                        case 0:
                            EncampmentType = RobotType.ARTILLERY;
                            break;
                        case 2:
                            EncampmentType = RobotType.GENERATOR;
                            break;
                        case 3:
                            EncampmentType = RobotType.ARTILLERY;
                            break;
                        case 4:
                            EncampmentType = RobotType.MEDBAY;
                            break;
                        case 5:
                            EncampmentType = RobotType.SHIELDS;
                            break;
                        case 6:
                            EncampmentType = RobotType.SUPPLIER;
                            break;
                        case 7:
                            EncampmentType = RobotType.SUPPLIER;
                            break;
                        case 8:
                            EncampmentType = RobotType.GENERATOR;
                            break;
                        case 9:
                            EncampmentType = RobotType.ARTILLERY;
                            break;
                        default:
                    }
                    this.goingToEncampment = false;
                    System.out.println("Spawning "+EncampmentType);
                    rc.captureEncampment(EncampmentType);
                return;
            }
            //||(Clock.getRoundNum()>499)
            if(((rc.readBroadcast(0)==2))&&!this.go){
                this.go = true;
            }
            if(go){
                if((this.destination!=this.enemyHQ)||(this.destination==null)){
                    this.destination = this.enemyHQ;
                }
                smartMove(rc, false);
            } else if(this.goingToEncampment) {
                if(rc.canSenseSquare(this.destination)){
                    GameObject DestinationEncampment = rc.senseObjectAtLocation(this.destination);
                    if(DestinationEncampment==null) {
                        smartMove(rc, true);
                        return;
                    } else if(DestinationEncampment.getTeam()!=Team.NEUTRAL) {
                        this.goingToEncampment = false;
                        smartMove(rc, true);
                        return;
                    }
                }
                smartMove(rc, false);
            } else {
                MapLocation[] encampments = rc.senseEncampmentSquares(rc.getLocation(), 50, Team.NEUTRAL);
                for(MapLocation EncampmentLocation : encampments){
                    this.destination = EncampmentLocation;
                    this.goingToEncampment = true;
                    smartMove(rc, false);
                    return;
                }
                smartMove(rc, true);
                return;
            }
            // End turn
            return;
        } else {
        return;
        }
    }

    public void setup(RobotController rc) throws GameActionException {
        this.enemyHQ = rc.senseEnemyHQLocation();
        this.destination = this.enemyHQ;
        this.setSeed(rc);
    }
}
