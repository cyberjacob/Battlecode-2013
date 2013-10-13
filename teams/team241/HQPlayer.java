package team241;

import battlecode.common.*;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ccuk
 * Date: 11/01/2013
 * Time: 8:36PM
 * To change this template use File | Settings | File Templates.
 */

public class HQPlayer implements AiInterface {

    private int units=0;
    private Upgrade upgrade;
    private Random rndGen = new Random();
    private Boolean doResearch = true;

    private void getUpgrade(RobotController rc) {

            int rndNum = (int)(rndGen.nextFloat() * 6);
            switch (rndNum) {
                case 0:
                    System.out.println("Research zero");
                    this.upgrade = Upgrade.NUKE;
                    break;
                case 1:
                    System.out.println("Research one");
                    this.upgrade = Upgrade.NUKE;
                    break;
                case 3:
                    System.out.println("Research two");
                    this.upgrade = Upgrade.FUSION;
                    break;
                case 4:
                    System.out.println("Research three");
                    this.upgrade = Upgrade.PICKAXE;
                    break;
                case 5:
                    System.out.println("Research four");
                    this.upgrade = Upgrade.VISION;
                    break;
                default:
                    System.out.println("Research five");
                    this.upgrade = Upgrade.DEFUSION;
                    break;
            }
        if(rc.hasUpgrade(this.upgrade)) {
            this.getUpgrade(rc);
        }
    }

    public void run(RobotController rc) throws GameActionException {
            if (rc.isActive()) {
                // Spawn a soldier
                Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());

                if (rc.canMove(dir)&&(rc.getTeamPower()>130)) {
                    if(doResearch) {
                        if(this.upgrade==null){
                            this.getUpgrade(rc);
                        } else if (rc.hasUpgrade(this.upgrade)) {
                            this.getUpgrade(rc);
                        }
                        doResearch = false;
                        rc.researchUpgrade(this.upgrade);
                        return;
                    } else {
                        doResearch = true;
                        rc.spawn(dir);
                        this.units = this.units + 1;
                        if (this.units>9) {
                            rc.broadcast(0,2);
                            this.units = 0;
                        } else {
                            rc.broadcast(0, 1);
                        }
                    }
                    return;
                } else {
                    if(this.upgrade==null){
                        this.getUpgrade(rc);
                    } else if (rc.hasUpgrade(this.upgrade)) {
                        this.getUpgrade(rc);
                    }
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
    }
}
