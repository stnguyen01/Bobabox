package bobabox.main.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import bobabox.main.GamMenu;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import bobabox.main.Objects.ObjButton;

//Tutorial Screen
public class ScrTut implements Screen, InputProcessor {

    GamMenu gamMenu;
    //Values
    int nW, nH;
    private Vector3 vTouch;
    //Logic
    private OrthographicCamera camera;
    private StretchViewport viewport; //how it's seen
    private SpriteBatch batch;
    //Assets
    private Texture txtBg;
    private ObjButton btnStart, btnHome;

    public ScrTut(GamMenu _gamMenu, StretchViewport _viewport, OrthographicCamera _camera) {
        gamMenu = _gamMenu;

        nW = gamMenu.WORLD_WIDTH;
        nH = gamMenu.WORLD_HEIGHT;
        Gdx.input.setInputProcessor(this);
        vTouch = new Vector3();
        viewport = _viewport;
        viewport.apply();
        camera = _camera;
        camera.setToOrtho(false);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); //camera looks at the center of the screen
        resize(nW, nH);
        batch = new SpriteBatch();


        txtBg = new Texture("data/Tut_bg.png");
        btnStart = new ObjButton(70, 70, 260 / 2, 70 / 2, "data/START1_btn.png", "data/START2_btn.png", viewport);
        btnHome = new ObjButton(70, 30, 260 / 2, 70 / 2, "data/HOME1_btn.png", "data/HOME2_btn.png", viewport);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Drawing
        batch.draw(txtBg, 0, 0, nW, nH);
       

        btnHome.update(batch);
        btnStart.update(batch);

        batch.end();

        //ObjButton
        checkButtons();
    }

    private void checkButtons() { // Checks if Buttons are pressed
        if (btnStart.justClicked()) {
            gamMenu.updateScreen(0);
        }
        if (btnHome.justClicked()) {
            gamMenu.updateScreen(2);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        txtBg.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        viewport.unproject(vTouch.set(screenX, (screenY * (-1) + nH), 0));
        System.out.println("x: " + screenX);
        System.out.println("y: " + (screenY * -1 + 500));

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
