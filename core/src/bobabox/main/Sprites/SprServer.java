package bobabox.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

import bobabox.main.Objects.ObjBar;
import bobabox.main.Objects.ObjTables;

public class SprServer extends Sprite {

    private float fX, fY, fW, fH, fXG, fYG;//Server X, Y, W, H
    private int nDir = 4, nTimer = 0, nStatCst;
    private Texture txtSheet, txtServer, txtDrink;
    private TextureRegion[] traniFrames;
    private TextureRegion[][] trTmpFrames;
    private Animation[] araniServer;
    private float fElapsedTime, fDx[], fDy[];
    private boolean bHasDrink = false, bHasOrder = false, bHasPaid = false;
    private SprCustomer sprCustomer;
    // nDir: 0 = North, 1 = East, 2 = South, 3 = West, 4 = stop

    public SprServer(float _fX, float _fY) {
        txtSheet = new Texture("data/SERVER1_sprsheet.png");
        txtServer = new Texture("data/SERVER1_spr.png");
        fX = _fX;
        fY = _fY;
        fW = 80;
        fH = 100;
        fDx = new float[]{0, 2, 0, -2, 0};
        fDy = new float[]{2, 0, -2, 0, 0};

        setFlip(false, false);

        txtDrink = new Texture("data/BubbleTea_img.png");
        araniServer = new Animation[4];

        trTmpFrames = TextureRegion.split(txtSheet, txtSheet.getWidth() / 5, txtSheet.getHeight() / 4);

        for (int i = 0; i < 4; i++) {
            traniFrames = new TextureRegion[5];
            for (int j = 0; j < 5; j++) {
                traniFrames[j] = trTmpFrames[i][j];
            }
            araniServer[i] = new Animation(1f / 6f, traniFrames);
        }
    }

    //under the condition that the
    private void directions(SpriteBatch batch, boolean isCstDragged) {

        if (!isCstDragged) {
            fX += fDx[nDir];
            fY += fDy[nDir];
            setX(fX);
            setY(fY);
        } else {
            nDir = 4;
        }
        if (nDir < 4) {
            batch.draw((TextureRegion) araniServer[nDir].getKeyFrame(fElapsedTime, true), fX, fY, fW - 10, fH);
        }


        if (nDir == 4) {
//            System.out.println("STOP");
            batch.draw(txtServer, fX, fY, fW, fH);
        }
    }


    // Makes server move to table coordinates
    public void update(float fXG, float fYG, SpriteBatch batch, boolean isCstDragged) {
        fElapsedTime += Gdx.graphics.getDeltaTime();//make sure to stop this timer when the game pauses
        directions(batch, isCstDragged);
        this.fXG = fXG; //Goal co-ordinates
        this.fYG = fYG - 1;

//        System.out.println("FXG: AND FYG: " + fXG + " + " + this.fYG);
//        System.out.println("FX: AND FY: " + fX + " + " + fY);
//        System.out.println("DIRECTION: " + nDir);

        //North
        if (fY <= fYG - 2) {
            nDir = 0;
        } //East
        else if (fX <= fXG - 2) {
            nDir = 1;
        } //South
        else if (fY >= fYG + 1) { //due to how the middle table is located, the South condition is offset by 1
            nDir = 2;
        } //West
        else if (fX >= fXG + 2) {
            nDir = 3;
        } else {
            nDir = 4;
        }
    }

    //server has arrived to table/bar
    public boolean arrived() {
        if (fY == fYG && fX == fXG && nDir == 4) {
//            System.out.println("HAS ARRIVED AT THE GOAL LOCATION");
            return true;
        } else {
            return false;
        }
    }

    public void service(SpriteBatch batch, int nClickedBar, SprCustomer _sprCustomer) {
        sprCustomer = _sprCustomer;
        nStatCst = sprCustomer.updateStatus();

        //server can take order from customer
        if (nStatCst == 1) {
            if (arrived() && nClickedBar == 0) {
                bHasOrder = true;
                sprCustomer.serviceValues(bHasOrder, bHasDrink, bHasPaid);
            }
        }

        if(nStatCst == 2) {
//            System.out.println("bHasOrder: " + bHasOrder);
//            System.out.println("nClicked b ar :" + nClickedBar);
            if (bHasOrder && nClickedBar == 1) {
                System.out.println("THE SERVER TIMER: " + nTimer);
                nTimer++;
            }
            if (nTimer >= 120) {
                batch.draw(txtDrink, 300 + fW, 350, 50, 50);
                if (nClickedBar >= 2) {
                    bHasDrink = true;
                }
            }
        }


        if (bHasDrink) {
//            System.out.println("HAS DRINK");
            nTimer = 0;
            batch.draw(txtDrink, fX, fY + 10, 50, 50);
            if(arrived()){
                bHasDrink = false;
            }
        }

        if (arrived() && bHasDrink) {
            bHasDrink = false;
        }
    }
}



