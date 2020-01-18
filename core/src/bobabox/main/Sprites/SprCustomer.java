package bobabox.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.sql.SQLOutput;

import bobabox.main.Objects.ObjTables;

/* EXPLANATION OF nSTATUS:
        0 = Is entering the restaurant
        1 = Is waiting for a table
        2 = is at a table, deciding what to order
        3 = is ready to order
        4 = order is taken, is waiting for their beverage
        5 = gets their order, is eating
        6 = done eating, wants to pay
        7 = pays
        8 = leaves happy
        9 = leaves due to lack of service
         */
public class SprCustomer extends Sprite {
    private SpriteBatch batch;
    private float fX, fY, fH, fW, fMove, fGoal = 30; //Guest
    private float fHx, fHy, fHw, fHh; //Hearts
    private boolean bCanDrag = false, isGone = false, bCustSat = false;
    //booleans related to server
    private boolean bServerHasOrder, bSvrHasBoba, bHasPaid;
    private Texture txtarHearts[] = new Texture[4], txtExclaim, txtDrink;
    private ObjTables objTable;
    private int nTimer = 1, nHearts = 0, nStatus, nDir;

    public SprCustomer(String sFile, SpriteBatch _batch) {
        super(new Texture(Gdx.files.internal(sFile)));

        //Customer specs
        fX = 80;
        fY = 330;
        fW = 80;
        fH = 100;
        fMove = 3.0f;
        setPosition(fX, fY);
        setSize(fW, fH);
        setFlip(false, false);

        //Heart specs
        fHw = 100;
        fHh = 30;
        txtarHearts[0] = new Texture("data/Hearts-01.png");
        txtarHearts[1] = new Texture("data/Hearts-02.png");
        txtarHearts[2] = new Texture("data/Hearts-03.png");
        txtarHearts[3] = new Texture("data/Hearts-04.png");
        txtDrink = new Texture("data/BubbleTea_img.png");
        txtExclaim = new Texture("data/Exclamation_img.png");

        this.batch = _batch;

        //nStatus
        nStatus = 0;
    }

    //takes booleans between the server and guest
    //there's definitely a more convenient way to go about with this, however we're on a time crunch and I 
    //wish to get the concept to work
    public void serviceValues(boolean bServerHasOrder, boolean bSvrHasBoba, boolean bHasPaid) {
        this.bServerHasOrder = bServerHasOrder;
        this.bSvrHasBoba = bSvrHasBoba;
        this.bHasPaid = bHasPaid;
    }

    public int getStatus() { return nStatus; }

    //is only called once seated
    public int updateStatus() {
        directions();
        bCanDrag = false;

        nTimer++;


        if (nStatus == 0) {
//            System.out.println("STATUS: Deciding order");
            if (nTimer >= 240) {
                nStatus = 1;
            }

        } else if (nStatus == 1) {
//            System.out.println("STATUS: Ready to order");
            batch.draw(txtExclaim, fX + fW / 2, fY + fH, 50, 60);
            if (bServerHasOrder) {
                nStatus = 2;
            }

        } else if (nStatus == 2) {
//            System.out.println("STATUS: Waiting for food");
            if (!bSvrHasBoba && !bServerHasOrder) {
                nStatus = 3;
            }

        } else if (nStatus == 3) {
            System.out.println("STATUS: Eating, nom nom nom");
            batch.draw(txtDrink, objTable.getX() / 2, objTable.getY() / 3, 50, 50);

            if (nTimer >= 360) {
                bHasPaid = false;
                nStatus = 4;
            }

        } else if (nStatus == 4) {
//            System.out.println("STATUS: Wants to pay");
            batch.draw(txtExclaim, fX + fW / 2, fY + fH, 50, 60);
            if (bHasPaid) {
                nStatus = 5;
            }

        } else if (nStatus == 5) {
//            System.out.println("paying");
            if (nTimer <= 120) {
                nStatus = 6;
                bHasPaid = true;
            }
        } else if (nStatus == 6) {
//            System.out.println("STATUS:leaving");
            leave();

        } else if (nStatus == 7) {
//            System.out.println("STATUS: This is horrible service!");
            leave();
            isLeaving();
        }
        return nStatus;
    }

    //Assures guest is walking down at the start
    public void entering(int nGst, int n, boolean bCustSat) {
        directions();
        if (!bCanDrag) {
            if (!bCustSat) {
                nDir = 2; //coming down

                //Update Goal
                if (nGst > 1) {
                    fGoal = 30 + ((fH + fHh + 10) * (nGst - 1));
                }

                //Customer move down
                if (fY <= fGoal) {
                    nStatus = 0;
                    nDir = 4;
                    bCanDrag = true;
//                    System.out.println("REACHED GOAL");
                }
            }
        }
        if (bCustSat) {
            updateQueue(n);
            nStatus = 0;
            bCanDrag = false;
        }
    }

    //Updates the Guest line once one customer is removed
    private void updateQueue(int nGst) {
        nDir = 2;
        if (nGst == 0) {
            fGoal = 30;
        } else if (nGst > 0) {
            fGoal = 30 + ((fH + fHh + 10) * (nGst));
        }
//        System.out.println("Goal for: " + nGst + " is " + fGoal);
//        System.out.println("The Y for: " + nGst + " is " + fY);

        if (fY <= fGoal) {
            nDir = 4;
            nStatus = 1;
        }


    }

    //Active when the guest is dragged
    public void drag(Vector2 vTouch, StretchViewport viewport) {
        nHearts = 0;
//        System.out.println("DRAGGING AFJDIAOJFHWFDAFCFHGJBKJA");
        viewport.unproject(vTouch.set(Gdx.input.getX(), Gdx.input.getY()));
        fX = vTouch.x - 50;
        fY = vTouch.y - 60;
        setX(fX);
        setY(fY);
    }

    //Update hearts
    public void hearts(ObjTables _objTable) {
        //Importing
        objTable = _objTable;
        fHx = fX - 10;
        fHy = fY + 110;

        //Level of hearts
        if (!isGone) {
            batch.draw(txtarHearts[nHearts], fHx, fHy, fHw, fHh);
            if (nStatus <= 5) {
                fHx = objTable.getX() + objTable.getWidth() / 2 + 20;
                fHy = objTable.getY();
            }
        }
        if ((nTimer % 360 == 0) && nStatus != 0 && nStatus != 3) {
            if (nHearts < 3) {
                nHearts++;
            }
        }

        if (nHearts == 3) {
            nStatus = 7;
        }
    }

    //Allows guests to exit appropriately from table to door
    private void directions() {
        if (nDir == 0) { //North
            fY += fMove;
            setY(fY);
        }
        if (nDir == 1) { //East
            fX += fMove;
            setX(fX);
        }
        if (nDir == 2) { //south
            fY -= fMove;
            setY(fY);
        } else if (nDir == 3) { //West
            fX -= fMove;
            setX(fX);
        } else if (nDir == 4) { // stopped
            setX(fX);
            setY(fY);
        }
    }

    //instructs the customer to leave through the door
    private void leave() {
        directions();
        nDir = 3;
        if (getX() > 75 && getX() < 85) {
            nDir = 0;
            if (getY() < 335 && getY() > 325) {
                nDir = 4;
                isGone = true;
            }
        }
    }

    //check if guest is leaving after sitting
    public boolean isLeaving() {
        if (nHearts == 3) {
            return true;
        }
        return false;
    }
}
