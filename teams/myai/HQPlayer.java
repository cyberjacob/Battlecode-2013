package myai;

import battlecode.common.*;

import java.util.Random;

import static battlecode.common.Upgrade.*;

/**
 * Created with IntelliJ IDEA.
 * User: ccuk
 * Date: 11/01/2013
 * Time: 8:36PM
 * To change this template use File | Settings | File Templates.
 */

public class HQPlayer implements AiInterface {

    private int units=0;
    private Upgrade upgrade = PICKAXE;
    private Random rndGen = new Random();
    private Boolean doResearch = true;
    private Boolean upgradeTwo = false;
    private int leaveAt = 9;

    private void getUpgrade(RobotController rc) {
        if(upgradeTwo){
            if (this.upgrade==NUKE) {
                this.upgrade = VISION;
            } else {
                this.upgrade = NUKE;
            }
        } else {
            if(rc.hasUpgrade(PICKAXE)&&rc.hasUpgrade(DEFUSION)&&rc.hasUpgrade(FUSION))
                this.upgradeTwo = true;
            switch (this.upgrade) {
                case PICKAXE:
                    this.upgrade = DEFUSION;
                    break;
                case DEFUSION:
                    this.upgrade = FUSION;
                    break;
                case FUSION:
                    this.upgrade = PICKAXE;
                    break;
            }
        }
        if(rc.hasUpgrade(this.upgrade)) {
            this.getUpgrade(rc);
        }
    }

    public void run(RobotController rc) throws GameActionException {
            if (rc.isActive()) {
                // Spawn a soldier
                Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());

                if (rc.canMove(dir)&&(rc.getTeamPower()>30)) {
                    if(doResearch) {
                        this.getUpgrade(rc);
                        doResearch = false;
                        rc.researchUpgrade(this.upgrade);
                        return;
                    } else {
                        doResearch = true;
                        rc.spawn(dir);
                        this.units = this.units + 1;
                        if (this.units>leaveAt) {
                            rc.broadcast(0,2);
                            this.units = 0;
                        } else {
                            rc.broadcast(0, 1);
                        }
                    }
                    return;
                } else {
                    this.getUpgrade(rc);
                    rc.researchUpgrade(this.upgrade);
                    return;
                }
            }
        //rc.researchUpgrade(Upgrade.NUKE);
        // End turn
        return;
    }

    @Override
    public void setup(RobotController rc) throws GameActionException {
        this.rndGen.setSeed(rc.getRobot().getID());
        if(rc.getMapHeight()<45&&rc.getMapWidth()<45) {
            System.out.println("Small map detected");
            this.leaveAt = 0;
        }
    }
}
