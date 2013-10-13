package myai;

import battlecode.common.*;

/** The example funcs player is a player meant to demonstrate basic usage of the most common commands.
 * Robots will move around randomly, occasionally mining and writing useless messages.
 * The HQ will spawn soldiers continuously. 
 */
public class RobotPlayer {

    public static void run(RobotController rc) {
        AiInterface r = null;
        switch (rc.getType()) {
            case HQ:
                r = new HQPlayer();
                System.out.println("New HQ!");
                break;
            case SOLDIER:
                r = new SoldierPlayer();
                break;
            case MEDBAY:
                System.out.println("New MedBay!");
                while(true) {
                    rc.yield();
                }
            case SHIELDS:
                System.out.println("New Shield Generator!");
                while(true) {
                    rc.yield();
                }
            case ARTILLERY:
                r = new ArtilleryPlayer();
                System.out.println("New Artillery!");
                break;
            case GENERATOR:
                System.out.println("New Generator!");
                while(true) {
                    rc.yield();
                }
            case SUPPLIER:
                System.out.println("New Supplier!");
                while(true) {
                    rc.yield();
                }
            default:
                System.out.println("I don't know who I am!");
                System.out.println(rc.getType());
                while (true){
                    rc.yield();
                }
        }
        try{
        r.setup(rc);
        } catch (Exception e){
            e.printStackTrace();
        }
        while (true) {
            try {
                r.run(rc);
                rc.yield();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}