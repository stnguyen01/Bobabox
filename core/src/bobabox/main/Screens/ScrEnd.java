package bobabox.main.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import bobabox.main.Objects.ObjButton;
import bobabox.main.GamMenu;

//Game over screen
public class ScrEnd implements Screen, InputProcessor {

    GamMenu gamMenu;
    //Values
    int nW, nH;
    private Vector3 vTouch;
    //Logic
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private SpriteBatch batch;
    //Assets
    Texture txtBg;
    ObjButton btnHome;

    public ScrEnd(GamMenu _gamMenu, StretchViewport _viewport, OrthographicCamera _camera) {
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

        txtBg = new Texture("data/EndBG_img.png");
        btnHome = new ObjButton(nW / 2, nH/4, 260, 70,"data/HOME1_btn.png","data/HOME2_btn.png", viewport);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //Logic
        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Drawing
        batch.draw(txtBg, 0, 0, nW, nH);
        btnHome.update(batch);

        batch.end();

        //ObjButton
        checkButtons();
    }

    private void checkButtons(){ // Checks if Buttons are pressed
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
//        System.out.println("x: " + screenX);
//        System.out.println("y: " + (screenY * -1 + 500));

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
