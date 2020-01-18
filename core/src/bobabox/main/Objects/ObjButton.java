package bobabox.main.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.omg.CORBA.VersionSpecHelper;

import bobabox.main.GamMenu;

//Thanks Joel https://github.com/brauj1894/Yoilith/blob/master/core/src/com/icsgame/objects/Button.java
//Creates all the buttons

public class ObjButton extends Sprite implements InputProcessor {

    private float fX, fY, fW, fH;
    private Vector3 vTouch;
    private StretchViewport viewport;
    private Texture txtButton, txtClicked;
    private int nTxt;
    private Sound sdClick;
    private Rectangle rect;

    public ObjButton(float _fX, float _fY, float _fW, float _fH, String _sButton, String _sClicked, StretchViewport _viewport) {
        super(new Texture(Gdx.files.internal(_sButton)));

        //Importing Info
        txtButton = new Texture(_sButton);
        txtClicked = new Texture(_sClicked);
        viewport = _viewport;
        fW = _fW;
        fH = _fH;
        fX = _fX - fW / 2;
        fY = _fY - fH / 2;
        rect = new Rectangle(fX, fY, fW, fH);

        //Setting Size
        setPosition(fX, fY);
        setSize(fW, fH);
        setFlip(false, false);

        //Setting Texture Info
        setTexture(txtButton);
        nTxt = 0;
        //Audio
        sdClick = Gdx.audio.newSound(Gdx.files.internal("data/Click_snd.mp3"));
        sdClick.setVolume(0, 1f);
    }

    // Will update the buttons image and draw the button
    public void update(SpriteBatch batch) {
        // Changes texture
        if (isMousedOver() && Gdx.input.isTouched()) {
            changeTexture(1);
        } else {
            // Default button image
            changeTexture(0);
        }

        this.draw(batch);
    }

    // Checks if the mouse is over the button, not whether the mouse was clicked
    public boolean isMousedOver() {
        vTouch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(vTouch);
        if (rect.contains(vTouch.x, vTouch.y)) {
            return true;
        }
        return false;
    }

    // Checks if the button was just clicked
    public boolean justClicked() {
        if (Gdx.input.justTouched()) {
            if (isMousedOver()) {
                sdClick.play();
                return true;
            }
        }
        return false;
    }

    private void changeTexture(int _nTxt) { // Changes the Texture of the button
        if (nTxt != _nTxt) {
            switch (_nTxt) {
                case 0:
                    // Regular Texture
                    setTexture(txtButton);
                    nTxt = 0;
                    break;
                case 1:
                    // Pressed Button
                    setTexture(txtClicked);
                    nTxt = 1;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!isMousedOver()) {
            setTexture(txtButton);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}