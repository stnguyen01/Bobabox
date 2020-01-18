package bobabox.main.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import bobabox.main.GamMenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import bobabox.main.Objects.ObjButton;

//Menu Screen
public class ScrMenu implements Screen, InputProcessor {
    GamMenu gamMenu;

    //Values
    int nW, nH;
    private Vector3 vTouch;
    private boolean isMute = false;
    //Logic
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private SpriteBatch batch;
    private Music music;
    //Assets
    Texture txtBack;
    ObjButton btnStart, btnTut, btnScratch, btnMusic;

    public ScrMenu(GamMenu _gamMenu, StretchViewport _viewport, OrthographicCamera _camera) {
        gamMenu = _gamMenu;

        nW = gamMenu.WORLD_WIDTH;
        nH = gamMenu.WORLD_HEIGHT;
        camera = _camera;
        camera.setToOrtho(false);
        viewport = _viewport;
        viewport.setCamera(camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        resize(nW, nH);
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        music = Gdx.audio.newMusic(Gdx.files.internal("data/Track_snd.mp3"));
        txtBack = new Texture("data/Main_bg.png");
        btnStart = new ObjButton(nW / 2, nH / 3 + 50, 260, 70, "data/START1_btn.png", "data/START2_btn.png", viewport);
        btnTut = new ObjButton(nW / 2, nH / 4 + 10, 260, 70, "data/TUTORIAL1_btn.png", "data/TUTORIAL2_btn.png", viewport);
        btnScratch = new ObjButton(nW / 2 - 55, 50, 110, 70, "data/SCRATCH1_btn.png", "data/SCRATCH2_btn.png", viewport);
        btnMusic = new ObjButton(nW / 2 + 55, 50, 110, 70, "data/MUSIC1_btn.png", "data/MUSIC2_btn.png", viewport);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void show() {
        return;
    }

    @Override
    public void render(float delta) {
        //Set-up
        Gdx.input.setInputProcessor(this);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.begin();
        if (!isMute) {
            music.play();
        }
        //Drawing
        batch.draw(txtBack, 0, 0, nW, nH);
        btnStart.update(batch);
        btnTut.update(batch);
        btnScratch.update(batch);
        btnMusic.update(batch);

        batch.end();
        //Buttons
        checkButtons();
    }

    private void checkButtons() { // Checks if Buttons are pressed
        if (btnStart.justClicked()) {
            gamMenu.updateScreen(0);
        }
        if (btnTut.justClicked()) {
            gamMenu.updateScreen(3);
        }
        if (btnScratch.justClicked()) {
            gamMenu.updateScreen(4);
        }
        if (btnMusic.justClicked()) {
            if (isMute) {
                music.play();
                isMute = false;
            } else if (!isMute) {
                //System.out.println("MUTE");
                music.pause();
                isMute = true;
            }
        }
    }

    @Override
    public void pause() {
        return;
    }

    @Override
    public void resume() {
        return;
    }

    @Override
    public void hide() {
        return;
    }

    @Override
    public void dispose() {
        batch.dispose();
        txtBack.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        vTouch = new Vector3(screenX, screenY, 0);
        //Readjusts input coordinates (vTouch.x and vTouch.y are our new input coordinates)
        //Gdx.input.getX/Y >> vTouch.x/y
        viewport.unproject(vTouch);

        return true;
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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
