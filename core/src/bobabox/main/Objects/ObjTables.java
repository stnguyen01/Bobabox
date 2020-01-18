package bobabox.main.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import bobabox.main.Sprites.SprCustomer;
import bobabox.main.Sprites.SprGuest;


public class ObjTables extends Sprite {

    private float fX, fY, fW, fH; //table
    private float fGY, fGX; //guest
    private Vector3 vTouch;
    private StretchViewport viewport;
    private Texture nTxt1, nTxt2;
    private int nCustIndex ;
    private boolean bSitting = false;

    public ObjTables(float _fX, float _fY, String _sOpenT, String _sSittingT, StretchViewport _viewport) {
        super(new Texture(Gdx.files.internal(_sOpenT)));

        //Importing Info
        nTxt1 = new Texture(_sOpenT);
        nTxt2 = new Texture(_sSittingT);
        viewport = _viewport;
        fW = 187;
        fH = 110;
        fX = _fX;
        fY = _fY;
        setPosition(fX, fY);
        setSize(fW, fH);
        setFlip(false, false);

        setTexture(nTxt1);
    }

    public boolean isAvb(boolean _isSitting, int _nCustIndex) {
        bSitting = _isSitting;
        nCustIndex = _nCustIndex;
        if (bSitting) {
            setTexture(nTxt2);
            return false;
        } else {
            setTexture(nTxt1);
        }
        return true;
    }


    //this is used in scratches, not in ScrGame
    public boolean isAvb2(SprGuest sprGuest) { // Checks if the spr is over the table

        fGY = sprGuest.getY();
        fGX = sprGuest.getX();

        if (fGX > fX && fGX < fX + fW) {
            if (fGY > fY && fGY < fY + fH) {
                super.setBounds(fX, fY, fW, fH);
                if (Gdx.input.isTouched() && !bSitting) {
                    super.setBounds(fX - 10, fY - 10, fW + 20, fH + 20);
                    return true;

                }
                return false;
            }
        }
        super.setBounds(fX, fY, fW, fH);
        return true;
    }

    //gives the customer's index withing the array to the server
    public int giveCstI(){
        return nCustIndex;
    }
}


